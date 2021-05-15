
package DepartureAirport;

import ActiveEntity.HostessStates;
import ActiveEntity.PassengerStates;
import ActiveEntity.PilotStates;
import Common.MemException;
import Common.MemFIFO;
import Main.Parameters;
import Repository.IRepository_DepartureAirport;
import Repository.RepositoryStub;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Departure Airport.
 * It is responsible for keeping a constantly updated account of the passengers inside the airport, waiting to board the plane.
 * 
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRDepartureAirport implements IDepartureAirport_Hostess, 
                                           IDepartureAirport_Passenger, 
                                           IDepartureAirport_Pilot{

    /**
     * Interface of the departure airport to the reference of the repository.
     */
    private final IRepository_DepartureAirport iRepository;
    /**
     * Reentrant Lock.
     */
    private final ReentrantLock rl;
    /**
     * Condition for the boarding process to start.
     * Sender: Pilot signals
     * Receiver: Hostess is signaled
     */
    private final Condition boarding;
    /**
     * Condition for the passenger to leave the queue (one for each passenger).
     * Sender: Hostess signals
     * Receiver: Passenger is signaled
     */
    private final Condition[] queue;
    /**
     * Condition for when there are no passengers in the queue.
     * Sender: Passenger signals
     * Receiver: Hostess is signaled
     */
    private final Condition passenger;
     /**
     * Condition for checking the passenger's documents.
     * Sender: Passenger signals
     * Receiver: Hostess is signaled
     */
    private final Condition check;
    /**
     * Condition for while the passenger is leaving the queue.
     * Sender: Passenger signals
     * Receiver: Hostess is signaled
     */
    private final Condition pLeaving;
    /**
     * Flag indicating whether the boarding process can start.
     * True if the plane is ready for boarding. 
     * False otherwise.
     */
    private boolean readyForBoarding;
    /**
     * Flag indicating whether the passenger has shown their documents.
     * True if the passenger as shown his documents. 
     * False otherwise.
     */
    private boolean documentsShown;
    /**
     * Flag indicating whether the passenger may leave the queue to board the plane.
     * True if the passenger is permitted to enter the plane.
     * False otherwise.
     */
    private boolean canEnterPlane;
    /**
     * Id of the next passenger in the queue.
     */
    private int nextPassenger;
    /**
     * Memory FIFO with the passengers in queue.
     */
    private final MemFIFO<Integer> passengersQueue;
    /**
     * Minimum number of passengers in a flight.
     */
    private final int minPassengers;
    /**
     * Maximum number of passengers in a flight.
     */
    private final int maxPassengers;
    /**
     * Current number of passengers on the Plane.
     */
    private int numPassengersOnPlane;
    /**
     * Number of passengers remaining to fly.
     */
    private int numPassengersLeftToTransport;
    
    /**
     * Instantiation of the departure airport
     * @param numPassengers number of passengers to transport
     * @param minPassengers minimum number of passengers in a flight
     * @param maxPassengers maximum number of passengers in a flight
     * @param iRepository interface of the departure airport to the reference of the repository
     * @throws Common.MemException if it was not possible to create the FIFO
     */
    public SRDepartureAirport(int numPassengers, int minPassengers, int maxPassengers,
                              IRepository_DepartureAirport iRepository) throws MemException {
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
        passengersQueue = new MemFIFO<>(numPassengers);
        nextPassenger = -1;
        this.maxPassengers = maxPassengers;
        this.minPassengers = minPassengers;
        numPassengersOnPlane = 0;
        numPassengersLeftToTransport = numPassengers;
    }
    
    /**
     * Operation to inform that the plane is ready for boarding.
     * Pilot signals the Hostess to start the boarding process.
     * @return true, if there are still passengers to transport and the  simulation continues. 
     *         false, if there are no more passengers left to transport and the simulation ends.
     */
    @Override
    public boolean informPlaneReadyForBoarding() {
        try{
            rl.lock();
            if(numPassengersLeftToTransport == 0){ // Check if there are no more passengers left to transport
                iRepository.printSumUp();
                return false;
            }
            iRepository.setPilotState(PilotStates.RDFB);
            readyForBoarding = true;
            boarding.signal();
        } finally{
            rl.unlock();
        }
        return true;
    }
    
    /**
     * Operation to check the documents of the next passenger in queue.
     * The hostess signals the next passenger and awaits for him to show the documents.
     */
    @Override
    public void checkDocuments() {
        try{
            rl.lock();
            try {
                nextPassenger = passengersQueue.read();
            } catch (MemException ex) {System.err.println("Exception: " + ex.getMessage());}
            iRepository.setHostessState(HostessStates.CKPS, nextPassenger);
            queue[nextPassenger].signal();
            while(!documentsShown)
                check.await();
            documentsShown = false;
        } catch(InterruptedException ex){System.err.println("Exception: " + ex.getMessage());}
        finally{
            rl.unlock();
        }
    }

    /**
     * Operation for the hostess to wait for the next passenger.
     * The hostess signals the passenger she was checking that he can proceed to the plane and waits for him to leave.
     * If there are no more customers in the queue and the plane is not ready to fly, the hostess waits for the next passenger.
     * @return the number of passengers on the plane.
     *         -1, if the boarding procedure is still ongoing.
     */
    @Override
    public int waitForNextPassenger() {
        try{
            rl.lock();
            iRepository.setHostessState(HostessStates.WTPS);
            canEnterPlane = true;
            queue[nextPassenger].signal();
            numPassengersOnPlane += 1;
            numPassengersLeftToTransport -= 1;
            //Wait for the passenger to leave the queue.
            while(canEnterPlane)
                pLeaving.await();
            // Check if the plane is ready to fly
            if((passengersQueue.empty()&& numPassengersOnPlane >= minPassengers) ||
                numPassengersOnPlane == maxPassengers || numPassengersLeftToTransport == 0)
                return numPassengersOnPlane;
            // If there is no passengers in the queue, wait for the next passenger
            while(passengersQueue.empty())
                passenger.await();
        } catch(InterruptedException ex){System.err.println("Exception: " + ex.getMessage());}
        finally{
            rl.unlock();
        }
        return -1;
    }
    /**
     * Operation for the hostess to wait for the next flight.
     * The hostess waits for the plane to arrive so that the boarding process can begin.
     * @return true, if there are still passengers to transport and the  simulation continues. 
     *         false, if there are no more passengers left to transport and the simulation ends.
     */
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
        } catch(InterruptedException ex){System.err.println("Exception: " + ex.getMessage());}
        finally{
            rl.unlock();
        }
        return true;
    }
    
    /**
     * Operation for the Hostess to prepare for passenger boarding.
     * If there are no customers in the queue, the hostess waits for the next passenger.
     */
    @Override
    public void prepareForPassBoarding() {
        try{
            rl.lock();
            iRepository.setHostessState(HostessStates.WTPS);
            while(passengersQueue.empty())
                passenger.await();
        } catch(InterruptedException ex){System.err.println("Exception: " + ex.getMessage());}
        finally{
            rl.unlock();
        }
    }
   
    /**
     * Operation for the passenger to wait in the queue.
     * If the passenger is the first on the queue, signals the hostess.
     * The passenger waits to be called by the hostess.
     * @param passengerID passenger id 
     */
    @Override
    public void waitInQueue(int passengerID) {
        try{
            rl.lock();
            iRepository.setPassengerState(PassengerStates.INQE, passengerID);
            try {
                passengersQueue.write(passengerID);
            } catch (MemException ex) {System.err.println("Exception: " + ex.getMessage());}
            if(passengersQueue.getCounter()== 1) //If it is the first passenger on the queue, signals the hostess
                passenger.signal();
            while(nextPassenger != passengerID)
                queue[passengerID].await();
        } catch(InterruptedException ex){System.err.println("Exception: " + ex.getMessage());}
        finally{
            rl.unlock();
        }
    }

    /**
     * Operation for the passenger show the documents.
     * The passenger signals the hostess to show the documents.
     * Then, waits for the hostess to check the documents.
     * @param passengerID passenger id
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
        } catch(InterruptedException ex){System.err.println("Exception: " + ex.getMessage());}
        finally{
            rl.unlock();
        }
    }
    
    public static void main(String[] args) {
        if((args.length % 2) != 0){
            System.out.println("Optional arguments: "
                    + "\n\t-sh <SERVER_PROXY_HOSTNAME>: Server Proxy Agent Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-sp <SERVER_PROXY_PORT>: Server Proxy Agent Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-rh <REPOSITORY_SERVER_HOSTNAME>: General Repository Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REPOSITORY_SERVER_PORT>: General Repository Server Port (Default = " + Parameters.REPOSITORY_SERVER_PORT + ")"
                    + "\n\t-i <MIN_PASSENGER>: Minimum number of passengers on a flight (Default = " + Parameters.MIN_PASSENGER + ")"
                    + "\n\t-a <MAX_PASSENGER>: Maximum number of passengers on a flight (Default = " + Parameters.MAX_PASSENGER + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        String proxyHostname = Parameters.SERVER_HOSTNAME;
        int proxyPort = Parameters.DEPARTURE_AIRPORT_SERVER_PORT; 
        String repositoryHostname = Parameters.SERVER_HOSTNAME;
        int repositoryPort = Parameters.REPOSITORY_SERVER_PORT; 
        int numPassenger = Parameters.NUM_PASSENGER; 
        int minPassenger = Parameters.MIN_PASSENGER; 
        int maxPassenger = Parameters.MAX_PASSENGER; 
        try{
            for (int i = 0; i < args.length; i+=2) {
                switch(args[i].toLowerCase()){
                    case "-sh": proxyHostname = args[i+1];
                               break;
                    case "-sp": proxyPort = Integer.valueOf(args[i+1]);
                               break;
                    case "-rh": repositoryHostname = args[i+1];
                               break;
                    case "-rp": repositoryPort = Integer.valueOf(args[i+1]);
                               break;
                    case "-p": numPassenger = Integer.valueOf(args[i+1]);
                               break;
                    case "-i": minPassenger = Integer.valueOf(args[i+1]);
                               break;
                    case "-a": maxPassenger = Integer.valueOf(args[i+1]);
                               break; 
                    default: throw new IllegalArgumentException();
                }
            }
        } catch(IllegalArgumentException ex){
            System.out.println("Optional arguments: "
                    + "\n\t-sh <SERVER_PROXY_HOSTNAME>: Server Proxy Agent Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-sp <SERVER_PROXY_PORT>: Server Proxy Agent Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-rh <REPOSITORY_SERVER_HOSTNAME>: General Repository Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REPOSITORY_SERVER_PORT>: General Repository Server Port (Default = " + Parameters.REPOSITORY_SERVER_PORT + ")"
                    + "\n\t-i <MIN_PASSENGER>: Minimum number of passengers on a flight (Default = " + Parameters.MIN_PASSENGER + ")"
                    + "\n\t-a <MAX_PASSENGER>: Maximum number of passengers on a flight (Default = " + Parameters.MAX_PASSENGER + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        RepositoryStub repositoryStub = new RepositoryStub(repositoryHostname, repositoryPort);
        try {
            SRDepartureAirport srDepartureAirport = new SRDepartureAirport(numPassenger, minPassenger, maxPassenger,
                    (IRepository_DepartureAirport) repositoryStub);
            DepartureAirportProxy departureAirportProxy = new DepartureAirportProxy(srDepartureAirport);
            System.out.println("Departure airport server proxy agent started!");
            departureAirportProxy.start();
            try{
                departureAirportProxy.join();
            }catch(InterruptedException ex){
                System.out.println(ex.getMessage());
            }
            System.out.println("Departure airport server proxy agent ended!");
        } catch (MemException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
