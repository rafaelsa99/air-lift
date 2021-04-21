
package DepartureAirport;

import ActiveEntity.HostessStates;
import ActiveEntity.PassengerStates;
import ActiveEntity.PilotStates;
import Common.MemException;
import Common.MemFIFO;
import Repository.IRepository_DepartureAirport;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRDepartureAirport implements IDepartureAirport_Hostess, 
                                           IDepartureAirport_Passenger, 
                                           IDepartureAirport_Pilot{

    private final IRepository_DepartureAirport iRepository;
    private final ReentrantLock rl;
    private final Condition boarding;
    private final Condition[] queue;
    private final Condition passenger;
    private final Condition check;
    private final Condition pLeaving;
    private boolean readyForBoarding;
    private boolean documentsShown;
    private boolean canEnterPlane;
    private int nextPassenger;
    private MemFIFO<Integer> passengersQueue;
    private final int minPassengers;
    private final int maxPassengers;
    private int numPassengersOnPlane;
    private int numPassengersLeftToTransport;
    
    public SRDepartureAirport(int numPassengers, int minPassengers, int maxPassengers,
                              IRepository_DepartureAirport iRepository) {
        this.iRepository = iRepository;
        rl = new ReentrantLock(true);
        boarding = rl.newCondition();
        pLeaving = rl.newCondition();
        queue = new Condition[numPassengers];
        for (int i = 0; i < numPassengers; i++)
            queue[i] = rl.newCondition();
        passenger = rl.newCondition();
        check = rl.newCondition();
        readyForBoarding = false;
        documentsShown = false;
        canEnterPlane = false;
        try {
            passengersQueue = new MemFIFO<>(numPassengers);
        } catch (MemException ex) {}
        nextPassenger = -1;
        this.maxPassengers = maxPassengers;
        this.minPassengers = minPassengers;
        numPassengersOnPlane = 0;
        numPassengersLeftToTransport = numPassengers;
    }
    
    @Override
    public boolean informPlaneReadyForBoarding() {
        try{
            rl.lock();
            if(numPassengersLeftToTransport == 0){
                iRepository.printSumUp();
                return false;
            }
            iRepository.setPilotState(PilotStates.RDFB);
            readyForBoarding = true;
            boarding.signal();
        } catch(Exception ex){}
        finally{
            rl.unlock();
        }
        return true;
    }
    
    @Override
    public void checkDocuments() {
        try{
            rl.lock();
            try {
                nextPassenger = passengersQueue.read();
            } catch (MemException ex) {}
            iRepository.setHostessState(HostessStates.CKPS, nextPassenger);
            queue[nextPassenger].signal();
            while(!documentsShown)
                check.await();
            documentsShown = false;
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
    }

    @Override
    public boolean waitForNextPassenger() {
        try{
            rl.lock();
            iRepository.setHostessState(HostessStates.WTPS);
            canEnterPlane = true;
            queue[nextPassenger].signal();
            numPassengersOnPlane += 1;
            numPassengersLeftToTransport -= 1;
            while(canEnterPlane)
                pLeaving.await();
            if((passengersQueue.empty()&& numPassengersOnPlane >= minPassengers) ||
                numPassengersOnPlane == maxPassengers || numPassengersLeftToTransport == 0)
                return false;
            while(passengersQueue.empty())
                passenger.await();
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
        return true;
    }

    @Override
    public boolean waitForNextFlight() {
        try{
            rl.lock();
            iRepository.setHostessState(HostessStates.WTFL);
            if(numPassengersLeftToTransport == 0)
                return false;
            while(!readyForBoarding)
                boarding.await();
            numPassengersOnPlane = 0;
            readyForBoarding = false;
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
        return true;
    }

    @Override
    public void prepareForPassBoarding() {
        try{
            rl.lock();
            iRepository.setHostessState(HostessStates.WTPS);
            while(passengersQueue.empty())
                passenger.await();
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
    }
    
    @Override
    public void waitInQueue(int passengerID) {
        try{
            rl.lock();
            iRepository.setPassengerState(PassengerStates.INQE, passengerID);
            try {
                passengersQueue.write(passengerID);
            } catch (MemException ex) {}
            if(passengersQueue.getCounter()== 1) //If it was empty
                passenger.signal();
            while(nextPassenger != passengerID)
                queue[passengerID].await();
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
    }

    @Override
    public void showDocuments(int passengerID) {
        try{
            rl.lock();
            documentsShown = true;
            check.signal();
            while(!canEnterPlane)
                queue[passengerID].await();
            canEnterPlane = false;
            pLeaving.signal();
        } catch(InterruptedException ex){}
        finally{
            rl.unlock();
        }
    }
}
