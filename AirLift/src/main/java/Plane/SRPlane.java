    
package Plane;

import Common.STHostess;
import Common.STPassenger;
import Common.STPilot;
import Repository.IRepository_Plane;
import java.util.ArrayList;
import java.util.List;
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
    private final List<Integer> passengersOnPlane;
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
        passengersOnPlane = new ArrayList<>();
        endOfFlight = false;
        this.maxSleep = maxSleep;
    }
    
    @Override
    public void waitForAllInBoard() {
        try{
            rl.lock();
            iRepository.setPilotState(STPilot.WTFB);
            while(!readyTakeOff)
                takeOff.await();
            readyTakeOff = false;
        } catch(Exception ex){}
        finally{
            rl.unlock();
        }
    }

    @Override
    public void flyToDestinationPoint() {
        iRepository.setPilotState(STPilot.FLFW);
        try {
            Thread.sleep((long)(Math.random() * maxSleep));
	} catch (InterruptedException e) {}
    }

    @Override
    public void announceArrival() {
        try{
            rl.lock();
            iRepository.setPilotState(STPilot.DRPP);
            endOfFlight = true;
            for (int i = 0; i < flight.length; i++) 
                flight[i].signal();
            while(!passengersOnPlane.isEmpty())
                deboarding.await();
            endOfFlight = false;
        } catch(Exception ex){}
        finally{
            rl.unlock();
        }
    }

    @Override
    public void flyToDeparturePoint() {
        iRepository.setPilotState(STPilot.FLBK);
        try {
            Thread.sleep((long)(Math.random() * maxSleep));
	} catch (InterruptedException e) {}
    }

    @Override
    public void parkAtTransferGate() {
        iRepository.setPilotState(STPilot.ATRG);
    }

    @Override
    public void informPlaneReadyToTakeOff() {
        try{
            rl.lock();
            iRepository.setHostessState(STHostess.RDTF);
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
            iRepository.setPassengerState(STPassenger.INFL, passengerID);
            passengersOnPlane.add(passengerID);
        } catch(Exception ex){}
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
        } catch(Exception ex){}
        finally{
            rl.unlock();
            
        }
    }

    @Override
    public void leaveThePlane(int passengerID) {
        try{
            rl.lock();
            passengersOnPlane.remove(Integer.valueOf(passengerID));
            if(passengersOnPlane.isEmpty())
                deboarding.signal();
        } catch(Exception ex){}
        finally{
            rl.unlock();
        }
    }
}
