    
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
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRPlane implements IPlane_Pilot, 
                                IPlane_Hostess, 
                                IPlane_Passenger{

    /**
     * Repository
     * @serialfield iRepository
     */
    private final IRepository_Plane iRepository;
    /**
     * Lock instantiation
     * @serialfield rl
     */
    private final ReentrantLock rl;
    /**
     * Contition variable
     * sender: Pilot
     * receiver: 
     * @serialfield takeOff
     */
    private final Condition takeOff;
    /**
     * Condition variable
     * Sender: Pilot signals 
     * Receiver: 
     * @serialfield deboarding
     */
    private final Condition deboarding;
    /**
     * Condition variable
     * Sender: 
     * Receiver: 
     * @serialfield flight
     */
    private final Condition[] flight;
    /**
     * bolean variable
     * true if plane is ready to takeoff
     * @serialfield readyTakeOff
     */
    private boolean readyTakeOff;
    /**
     * Memory list
     * Number of passengers in the plane
     * @serialfield passengersOnPlane
     */
    private MemList<Integer> passengersOnPlane;
    /**
     * bolean variable
     * True if flight has ended (arrived at departue airport)
     * @serialfield endOfFlight
     */
    private boolean endOfFlight;
    /**
     * 
     * @serialfield maxSleep
     */
    private final int maxSleep;
    
    /**
     * SR Plane instatiation
     * @param numPassengers
     * @param iRepository
     * @param maxSleep 
     */
    public SRPlane(int numPassengers, IRepository_Plane iRepository, int maxSleep) {
        this.iRepository = iRepository;
        rl = new ReentrantLock(true);
        takeOff = rl.newCondition();
        deboarding = rl.newCondition();
        flight = new Condition[numPassengers];
        for (int i = 0; i < numPassengers; i++)
            flight[i] = rl.newCondition();
        readyTakeOff = false;
        try {
            passengersOnPlane = new MemList<>(numPassengers);
        } catch (MemException ex) {}
        endOfFlight = false;
        this.maxSleep = maxSleep;
    }
    
    /**
     * waits for passengers to get on board
     */
    @Override
    public void waitForAllInBoard() {
        try{
            rl.lock();
            iRepository.setPilotState(PilotStates.WTFB);
            while(!readyTakeOff)
                takeOff.await();
            readyTakeOff = false;
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
    }

    /**
     * Random value to simualte passenger going to airport
     */
    @Override
    public void flyToDestinationPoint() {
        iRepository.setPilotState(PilotStates.FLFW);
        try {
            Thread.sleep((long)(Math.random() * maxSleep));
	} catch (InterruptedException e) {}
    }

    /**
     * Pilot anounces the flight has arrived
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
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
    }

    /**
     * Random time simulating the plane traveling to departure airport
     */
    @Override
    public void flyToDeparturePoint() {
        iRepository.setPilotState(PilotStates.FLBK);
        try {
            Thread.sleep((long)(Math.random() * maxSleep));
	} catch (InterruptedException e) {}
    }

    /**
     * Sets state of pilot when the plane arrives at transfer gate
     */
    @Override
    public void parkAtTransferGate() {
        iRepository.setPilotState(PilotStates.ATRG);
    }

    /**
     * Hostess anounces the plane is ready to takeoff
     */
    @Override
    public void informPlaneReadyToTakeOff() {
        try{
            rl.lock();
            iRepository.setHostessState(HostessStates.RDTF);
            readyTakeOff = true;
            takeOff.signal();
        } catch(Exception ex){}
        finally{
            rl.unlock();
        }
    }

    /**
     * Passenger with id passengerID boards the plane and its added to the list of
     * passengers in the plane
     * @param passengerID 
     */
    @Override
    public void boardThePlane(int passengerID) {
        try{
            rl.lock();
            iRepository.setPassengerState(PassengerStates.INFL, passengerID);
            passengersOnPlane.write(passengerID);
        } catch(MemException ex){}
        finally{
            rl.unlock();
        }
    }

    /**
     * Passenger waits until the flight has arrived at the destination airport
     * @param passengerID 
     */
    @Override
    public void waitForEndOfFlight(int passengerID) {
        try{
            rl.lock();
            while(!endOfFlight)
                flight[passengerID].await();
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
            
        }
    }

    /**
     * Passengers leave the plane
     * @param passengerID 
     */
    @Override
    public void leaveThePlane(int passengerID) {
        try{
            rl.lock();
            passengersOnPlane.read(passengerID);
            if(passengersOnPlane.empty())
                deboarding.signal();
        } catch(MemException ex){}
        finally{
            rl.unlock();
        }
    }
}
