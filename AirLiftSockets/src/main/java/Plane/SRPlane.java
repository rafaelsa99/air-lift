    
package Plane;

import ActiveEntity.HostessStates;
import ActiveEntity.PassengerStates;
import ActiveEntity.PilotStates;
import Common.MemException;
import Common.MemList;
import Repository.IRepository_Plane;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Plane.
 * It is responsible for keeping a constantly updated account of the passengers inside the plane.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRPlane implements IPlane_Pilot, 
                                IPlane_Hostess, 
                                IPlane_Passenger{

    /**
     * Interface of the plane to the reference of the repository.
     */
    private final IRepository_Plane iRepository;
    /**
     * Reentrant Lock.
     */
    private final ReentrantLock rl;
    /**
     * Condition for the pilot to start the flight.
     * Sender: Hostess signals
     * Receiver: Pilot is signaled
     */
    private final Condition takeOff;
    /**
     * Condition for while passenger deboarding is in progress.
     * Sender: Last passenger to leave the plane signals
     * Receiver: Pilot is signaled
     */
    private final Condition deboarding;
    /**
     * Condition for the passenger to leave the plane at the end of the flight (one for each passenger).
     * Sender: Pilot signals
     * Receiver: Passenger is signaled
     */
    private final Condition[] flight;
    /**
     * Flag indicating whether the plane is ready to take off.
     * True if the plane is ready to take off.. 
     * False otherwise.
     */
    private boolean readyTakeOff;
    /**
     * Memory FIFO with the passengers on the plane.
     */
    private final MemList<Integer> passengersOnPlane;
    /**
     * Flag indicating whether the flight is over.
     * True if flight is over.
     * False otherwise.
     */
    private boolean endOfFlight;
    /**
     * Maximum sleeping time, in milliseconds.
     */
    private final int maxSleep;
    
    /**
     * Instantiation of the plane.
     * @param numPassengers number of passengers to transport
     * @param iRepository interface of the plane to the reference of the repository
     * @param maxSleep maximum sleeping time (ms)
     * @throws Common.MemException if it was not possible to create the List
     */
    public SRPlane(int numPassengers, IRepository_Plane iRepository, int maxSleep) throws MemException {
        this.iRepository = iRepository;
        rl = new ReentrantLock(true);
        takeOff = rl.newCondition();
        deboarding = rl.newCondition();
        flight = new Condition[numPassengers];
        for (int i = 0; i < numPassengers; i++)
            flight[i] = rl.newCondition();
        readyTakeOff = false;
        passengersOnPlane = new MemList<>(numPassengers);
        endOfFlight = false;
        this.maxSleep = maxSleep;
    }
    
    /**
     * Operation for the pilot to wait for the boarding process to end.
     * The pilot waits for the signal of the hostess indicating that the plane is ready to take off.
     */
    @Override
    public void waitForAllInBoard() {
        try{
            rl.lock();
            iRepository.setPilotState(PilotStates.WTFB);
            while(!readyTakeOff)
                takeOff.await();
            readyTakeOff = false;
        } catch(InterruptedException ex){System.err.println("Exception: " + ex.getMessage());}
        finally{
            rl.unlock();
        }
    }

    /**
     * Operation for the pilot to fly the plane to the Destination Point.
     * The thread sleeps for a random period of time, for a maximum o maxSleep time.
     */
    @Override
    public void flyToDestinationPoint() {
        iRepository.setPilotState(PilotStates.FLFW);
        try {
            Thread.sleep((long)(Math.random() * maxSleep));
	} catch (InterruptedException e) {}
    }

    /**
     * Operation for the pilot to announce that the plane has arrived to the destination airport.
     * The pilot signals all passengers to leave the plane, and waits for the last passenger to leave.
     */
    @Override
    public void announceArrival() {
        try{
            rl.lock();
            iRepository.setPilotState(PilotStates.DRPP);
            endOfFlight = true;
            for (Condition cFlight : flight)
                cFlight.signal();
            while(!passengersOnPlane.empty())
                deboarding.await();
            endOfFlight = false;
        } catch(InterruptedException ex){System.err.println("Exception: " + ex.getMessage());}
        finally{
            rl.unlock();
        }
    }

    /**
     * Operation for the pilot to fly the plane to the Departure Point.
     * The thread sleeps for a random period of time, for a maximum o maxSleep time.
     */
    @Override
    public void flyToDeparturePoint() {
        iRepository.setPilotState(PilotStates.FLBK);
        try {
            Thread.sleep((long)(Math.random() * maxSleep));
	} catch (InterruptedException e) {}
    }

    /**
     * Operation for the pilot to park the plane at the transfer gate.
     */
    @Override
    public void parkAtTransferGate() {
        iRepository.setPilotState(PilotStates.ATRG);
    }

    /**
     * Operation for the hostess to inform that the plane is ready to take off.
     * The hostess signals the pilot to start the flight.
     */
    @Override
    public void informPlaneReadyToTakeOff() {
        try{
            rl.lock();
            iRepository.setHostessState(HostessStates.RDTF);
            readyTakeOff = true;
            takeOff.signal();
        } finally{
            rl.unlock();
        }
    }

    /**
     * Operation for the passengers to board the plane.
     * The passenger is added to the list of passengers on the plane.
     * @param passengerID passenger id
     */
    @Override
    public void boardThePlane(int passengerID) {
        try{
            rl.lock();
            iRepository.setPassengerState(PassengerStates.INFL, passengerID);
            passengersOnPlane.write(passengerID);
        } catch(MemException ex){System.err.println("Exception: " + ex.getMessage());}
        finally{
            rl.unlock();
        }
    }

    /**
     * Operation for the passenger to wait for the end of the flight.
     * The passenger waits to be signaled by the pilot that he can leave the plane.
     * @param passengerID passenger id
     */
    @Override
    public void waitForEndOfFlight(int passengerID) {
        try{
            rl.lock();
            while(!endOfFlight)
                flight[passengerID].await();
        } catch(InterruptedException ex){System.err.println("Exception: " + ex.getMessage());}
        finally{
            rl.unlock();
            
        }
    }

    /**
     * Operation for the passenger leave the plane.
     * If it is the last passenger to leave the plane, the passenger signals the pilot.
     * @param passengerID passenger id
     */
    @Override
    public void leaveThePlane(int passengerID) {
        try{
            rl.lock();
            passengersOnPlane.read(passengerID);
            if(passengersOnPlane.empty())
                deboarding.signal();
        } catch(MemException ex){System.err.println("Exception: " + ex.getMessage());}
        finally{
            rl.unlock();
        }
    }
}
