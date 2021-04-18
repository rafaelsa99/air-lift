
package DepartureAirport;

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
public class SRDepartureAirport implements IDepartureAirport_Hostess, 
                                           IDepartureAirport_Passenger, 
                                           IDepartureAirport_Pilot{

    private final ReentrantLock rl;
    private final Condition boarding;
    private final Condition[] queue;
    private final Condition passenger;
    private final Condition check;
    private boolean readyForBoarding;
    private boolean documentsShown;
    private boolean canEnterPlane;
    private int nextPassenger;
    private final LinkedList<Integer> passengersQueue;
    private final int minPassengers;
    private final int maxPassengers;
    private int numPassengersOnPlane;
    private int numPassengersLeftToTransport;
    
    public SRDepartureAirport(int numPassengers, int minPassengers, int maxPassengers) {
        rl = new ReentrantLock(true);
        boarding = rl.newCondition();
        queue = new Condition[numPassengers];
        for (int i = 0; i < numPassengers; i++)
            queue[i] = rl.newCondition();
        passenger = rl.newCondition();
        check = rl.newCondition();
        readyForBoarding = false;
        documentsShown = false;
        canEnterPlane = false;
        passengersQueue = new LinkedList<>();
        nextPassenger = -1;
        this.maxPassengers = maxPassengers;
        this.minPassengers = minPassengers;
        numPassengersOnPlane = 0;
        numPassengersLeftToTransport = numPassengers;
    }
    
    @Override
    public STPilot informPlaneReadyForBoarding() {
        try{
            rl.lock();
            readyForBoarding = true;
            boarding.signal();
        } catch(Exception ex){}
        finally{
            rl.unlock();
        }
        return STPilot.WTFB;
    }

    @Override
    public boolean allPassengersTransported() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public STHostess checkDocuments() {
        try{
            rl.lock();
            nextPassenger = passengersQueue.removeFirst();
            queue[nextPassenger].signal();
            while(!documentsShown)
                check.await();
            documentsShown = false;
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
        return STHostess.WTPS;
    }

    @Override
    public STHostess waitForNextPassenger() {
        try{
            rl.lock();
            canEnterPlane = true;
            queue[nextPassenger].signal();
            numPassengersOnPlane += 1;
            numPassengersLeftToTransport -= 1;
            if((passengersQueue.isEmpty() && numPassengersOnPlane >= minPassengers) ||
                numPassengersOnPlane == maxPassengers || numPassengersLeftToTransport == 0)
                return STHostess.RDTF;
            while(passengersQueue.isEmpty())
                passenger.await();
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
        return STHostess.CKPS;
    }

    @Override
    public STHostess waitForNextFlight() {
        try{
            rl.lock();
            while(!readyForBoarding)
                boarding.await();
            numPassengersOnPlane = 0;
            readyForBoarding = false;
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
        return STHostess.WTPS;
    }

    @Override
    public STHostess prepareForPassBoarding() {
        try{
            rl.lock();
            while(passengersQueue.isEmpty())
                passenger.await();
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
        return STHostess.CKPS;
    }

    @Override
    public STPassenger travelToAirport(int passengerID) {
        try {
            sleep((long) (new Random().nextInt(100))); //VERIFICAR TEMPO DO SLEEP
	} catch (InterruptedException e) {}
        return STPassenger.INQE;
    }

    @Override
    public STPassenger waitInQueue(int passengerID) {
        try{
            rl.lock();
            passengersQueue.add(passengerID);
            if(passengersQueue.size() == 1) //If it was empty
                passenger.signal();
            while(nextPassenger != passengerID)
                queue[passengerID].await();
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
        return STPassenger.INQE;
    }

    @Override
    public STPassenger showDocuments(int passengerID) {
        try{
            rl.lock();
            documentsShown = true;
            check.signal();
            while(!canEnterPlane)
                queue[passengerID].await();
            canEnterPlane = false;
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
        return STPassenger.INFL;
    }

}
