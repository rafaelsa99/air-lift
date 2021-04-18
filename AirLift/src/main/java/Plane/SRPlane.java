    
package Plane;

import Common.STHostess;
import Common.STPassenger;
import Common.STPilot;
import static java.lang.Thread.sleep;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRPlane implements IPlane_Pilot, 
                                IPlane_Hostess, 
                                IPlane_Passenger{

    private final ReentrantLock rl;
    private final Condition takeOff;
    private final Condition passengerLeaving;
    private final Condition[] flight;
    private boolean readyTakeOff;
    private final LinkedList<Integer> passengersOnPlane;
    private int passengerToLeave;
    
    public SRPlane(int numPassengers) {
        rl = new ReentrantLock(true);
        takeOff = rl.newCondition();
        passengerLeaving = rl.newCondition();
        flight = new Condition[numPassengers];
        for (int i = 0; i < numPassengers; i++)
            flight[i] = rl.newCondition();
        readyTakeOff = false;
        passengersOnPlane = new LinkedList<>();
        passengerToLeave = -1;
    }
    
    @Override
    public STPilot waitForAllInBoard() {
        try{
            rl.lock();
            while(!readyTakeOff)
                takeOff.await();
            readyTakeOff = false;
        } catch(Exception ex){}
        finally{
            rl.unlock();
        }
        return STPilot.FLFW;
    }

    @Override
    public STPilot flyToDestinationPoint() {
        try {
            sleep((long) (new Random().nextInt(100))); //VERIFICAR TEMPO DO SLEEP
	} catch (InterruptedException e) {}
        return STPilot.DRPP;
    }

    @Override
    public STPilot announceArrival() {
        try{
            rl.lock();
            while(!passengersOnPlane.isEmpty()){
                passengerToLeave = passengersOnPlane.removeLast();
                flight[passengerToLeave].signal();
                while(passengerToLeave != -1)
                    passengerLeaving.await();
            }
        } catch(Exception ex){}
        finally{
            rl.unlock();
        }
        return STPilot.FLBK;
    }

    @Override
    public STPilot flyToDeparturePoint() {
        try {
            sleep((long) (new Random().nextInt(100))); //VERIFICAR TEMPO DO SLEEP
	} catch (InterruptedException e) {}
        return STPilot.ATRG;
    }

    @Override
    public STPilot parkAtTransferGate() {
        return STPilot.RDFB;
    }

    @Override
    public STHostess informPlaneReadyToTakeOff() {
        try{
            rl.lock();
            readyTakeOff = true;
            takeOff.signal();
        } catch(Exception ex){}
        finally{
            rl.unlock();
        }
        return STHostess.WTFL;
    }

    @Override
    public STPassenger boardThePlane(int passengerID) {
        try{
            rl.lock();
            passengersOnPlane.add(passengerID);
        } catch(Exception ex){}
        finally{
            rl.unlock();
        }
        return STPassenger.INFL;
    }

    @Override
    public STPassenger waitForEndOfFlight(int passengerID) {
        try{
            rl.lock();
            while(passengerToLeave != passengerID)
                flight[passengerID].await();
            passengerToLeave = -1;
            passengerLeaving.signal();
        } catch(Exception ex){}
        finally{
            rl.unlock();
        }
        return STPassenger.ATDS;
    }
}
