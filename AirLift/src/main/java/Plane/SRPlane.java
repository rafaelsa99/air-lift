    
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

    private final IRepository_Plane iRepository;
    private final ReentrantLock rl;
    private final Condition takeOff;
    private final Condition deboarding;
    private final Condition[] flight;
    private boolean readyTakeOff;
    private MemList<Integer> passengersOnPlane;
    private boolean endOfFlight;
    private final int maxSleep;
    
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

    @Override
    public void flyToDestinationPoint() {
        iRepository.setPilotState(PilotStates.FLFW);
        try {
            Thread.sleep((long)(Math.random() * maxSleep));
	} catch (InterruptedException e) {}
    }

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

    @Override
    public void flyToDeparturePoint() {
        iRepository.setPilotState(PilotStates.FLBK);
        try {
            Thread.sleep((long)(Math.random() * maxSleep));
	} catch (InterruptedException e) {}
    }

    @Override
    public void parkAtTransferGate() {
        iRepository.setPilotState(PilotStates.ATRG);
    }

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
