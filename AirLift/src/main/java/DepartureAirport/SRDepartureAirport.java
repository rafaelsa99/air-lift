
package DepartureAirport;

import Common.STHostess;
import Common.STPassenger;
import Common.STPilot;
import Repository.IRepository_DepartureAirport;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRDepartureAirport implements IDepartureAirport_Hostess, 
                                           IDepartureAirport_Passenger, 
                                           IDepartureAirport_Pilot{

    /**
     * iRepository shared region
     * @serialField IRepository_DepartureAirport
     */
    private final IRepository_DepartureAirport iRepository;
    /**
     * Lock instantiation
     * @serialField ReentrantLock
     */
    private final ReentrantLock rl;
    /**
     * Condition variable
     * Sender: Pilot signals
     * Receiver: Hostess is signaled
     */
    private final Condition boarding;
    /**
     * Condition variable
     * Sender: Hostess signals
     * Receiver: Passenger is signaled
     */
    private final Condition[] queue;
    /**
     * Condition variable
     * Sender: Hostess signals
     * Receiver: Passenger is signaled
     */
    private final Condition passenger;
     /**
     * Condition variable
     * Sender: Passenger signals
     * Receiver: Hostess is signaled
     */
    private final Condition check;
    /**
     * Condition variable
     * Sender: Passenger signals
     * Receiver: Hostess is signaled
     */
    private final Condition pLeaving;
    /**
     * boolean
     * True if plane is ready for boarding (Arrived at departure airport)
     * @serialField readyForBoarding
     */
    private boolean readyForBoarding;
    /**
     * boolean
     * True if the passenger as shown his documents
     * @serialField documentsShown
     */
    private boolean documentsShown;
    /**
     * Boolean variable
     * True if the passenger is permited to enter the plane
     * @serialField canEnterPlane
     */
    private boolean canEnterPlane;
    /**
     * Next passenger in queue
     * @serialField nextPassenger
     */
    private int nextPassenger;
    /**
     * Array with the List of passengers in queue
     * @serialField passengersQueue
     */
    private final LinkedList<Integer> passengersQueue;
    /**
     * Minimum value for passenger inside a plane
     * @serialField minPassengers
     */
    private final int minPassengers;
    /**
     * Maximum value for passenger inside a plane
     * @serialField maxPassengers
     */
    private final int maxPassengers;
    /**
     * Current number of passengers on Plane
     * @serialField numPassengersOnPlane
     */
    private int numPassengersOnPlane;
    /**
     * Number of passengers remaining to fly do departureAirport
     * @serialField numPassengersLeftToTransport
     */
    private int numPassengersLeftToTransport;
    /**
     * @serialfield maxSleep
     */
    private final int maxSleep;
    
    /**
     * Instantiation of SRDepartureAirport
     * @param numPassengers
     * @param minPassengers
     * @param maxPassengers
     * @param iRepository
     * @param maxSleep 
     */
    public SRDepartureAirport(int numPassengers, int minPassengers, int maxPassengers,
                              IRepository_DepartureAirport iRepository, int maxSleep) {
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
        passengersQueue = new LinkedList<>();
        nextPassenger = -1;
        this.maxPassengers = maxPassengers;
        this.minPassengers = minPassengers;
        numPassengersOnPlane = 0;
        numPassengersLeftToTransport = numPassengers;
        this.maxSleep = maxSleep;
    }
    
    /**
     * Function used to inform the hostess that the plane is ready for boarding
     * Hostess is signaled by the pilot
     * @return boolean
     * returns true when the plane is ready for boarding
     */
    @Override
    public boolean informPlaneReadyForBoarding() {
        try{
            rl.lock();
            if(numPassengersLeftToTransport == 0){
                iRepository.printSumUp();
                return false;
            }
            iRepository.setPilotState(STPilot.RDFB);
            readyForBoarding = true;
            boarding.signal();
        } catch(Exception ex){System.err.println("Exception occured"+ex);}
        finally{
            rl.unlock();
        }
        return true;
    }
    
    /**
     * Function used to check documents of passenger by the hostess
     * The hostess sends a signal to the next passenger in queue
     */
    @Override
    public void checkDocuments() {
        try{
            rl.lock();
            nextPassenger = passengersQueue.removeFirst();
            iRepository.setHostessState(STHostess.CKPS, nextPassenger);
            queue[nextPassenger].signal();
            while(!documentsShown)
                check.await();
            documentsShown = false;
        } catch(InterruptedException ex){System.err.println("Exception occured"+ex.getMessage());}
        finally{
            rl.unlock();
        }
    }

    /**
     * Hostess waits for passenger to arrive at DepartureAirport
     * @return STHostess
     * returns the State of Hostess after checking Documents
     */
    @Override
    public STHostess waitForNextPassenger() {
        try{
            rl.lock();
            iRepository.setHostessState(STHostess.WTPS);
            canEnterPlane = true;
            queue[nextPassenger].signal();
            numPassengersOnPlane += 1;
            numPassengersLeftToTransport -= 1;
            while(canEnterPlane)
                pLeaving.await();
            if((passengersQueue.isEmpty() && numPassengersOnPlane >= minPassengers) ||
                numPassengersOnPlane == maxPassengers || numPassengersLeftToTransport == 0)
                return STHostess.RDTF;
            while(passengersQueue.isEmpty())
                passenger.await();
        } catch(InterruptedException ex){System.err.println("Exception occured"+ex.getMessage());}
        finally{
            rl.unlock();
        }
        return STHostess.CKPS;
    }
    /**
     * The hostess waits for the plane to return from departureAirport
     * @return true if there are still passengers left to transport
     * false if otherwise 
     */
    @Override
    public boolean waitForNextFlight() {
        try{
            rl.lock();
            iRepository.setHostessState(STHostess.WTFL);
            if(numPassengersLeftToTransport == 0)
                return false;
            while(!readyForBoarding)
                boarding.await();
            numPassengersOnPlane = 0;
            readyForBoarding = false;
        } catch(InterruptedException ex){System.err.println("Exception occured"+ex.getMessage());}
        finally{
            rl.unlock();
        }
        return true;
    }
    
    /**
     * Hostess prepares the arrival of passengers and the boarding of the plane
     */
    @Override
    public void prepareForPassBoarding() {
        try{
            rl.lock();
            iRepository.setHostessState(STHostess.WTPS);
            while(passengersQueue.isEmpty())
                passenger.await();
        } catch(InterruptedException ex){System.err.println("Exception occured"+ex.getMessage());}
        finally{
            rl.unlock();
        }
    }

    /**
     * Random time for passengers to get to airport
     * @param passengerID 
     */
    @Override
    public void travelToAirport(int passengerID) {
        try {
            Thread.sleep((long)(Math.random() * maxSleep));
	} catch (InterruptedException ex) {System.err.println("Exception occured"+ex.getMessage());}
    }
    /**
     * Passengers wait in queue while they are not called by the Hostess
     * @param passengerID 
     */
    @Override
    public void waitInQueue(int passengerID) {
        try{
            rl.lock();
            iRepository.setPassengerState(STPassenger.INQE, passengerID);
            passengersQueue.add(passengerID);
            if(passengersQueue.size() == 1) //If it was empty
                passenger.signal();
            while(nextPassenger != passengerID)
                queue[passengerID].await();
        } catch(InterruptedException ex){System.err.println("Exception occured"+ex.getMessage());}
        finally{
            rl.unlock();
        }
    }

    /**
     * Passenger shows documents to Hostess
     * @param passengerID 
     */
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
        } catch(InterruptedException ex){System.err.println("Exception occured"+ex.getMessage());}
        finally{
            rl.unlock();
        }
    }
}
