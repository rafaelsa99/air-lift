
package ActiveEntity;

import DepartureAirport.IDepartureAirport_Passenger;
import DestinationAirport.IDestinationAirport_Passenger;
import Plane.IPlane_Passenger;

/**
 * Passenger thread, which simulates the Passenger life cycle.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEPassenger extends Thread{
    
    /**
     * Interface of the Passenger to the reference of the Departure Airport.
     */
    private final IDepartureAirport_Passenger iDepartureAirport;
    /**
     * Interface of the Passenger to the reference of the Destination Airport.
     */
    private final IDestinationAirport_Passenger iDestinationAirport;
    /**
     * Interface of the Passenger to the reference of the Plane.
     */
    private final IPlane_Passenger iPlane;
    /**
     * Maximum sleeping time, in milliseconds.
     */
    private final int maxSleep;
    /**
     * Passenger identification.
     */
    private final int id;
    
    /**
     * Instantiation of a Passenger thread.
     * 
     * @param pDepartureAirport_passenger interface of the Passenger to the reference of the Departure Airport  
     * @param pDestinationAirport_passenger interface of the Passenger to the reference of the Destination Airport 
     * @param pPlane_passenger interface of the Passenger to the reference of the Plane            
     * @param id passenger Id
     * @param maxSleep maximum sleeping time (ms)
     */
    public AEPassenger(IDepartureAirport_Passenger pDepartureAirport_passenger,
                       IDestinationAirport_Passenger pDestinationAirport_passenger,
                       IPlane_Passenger pPlane_passenger,
                       int id, int maxSleep) {
        super("Passenger " + id);
        this.id = id;
        iDepartureAirport   = pDepartureAirport_passenger;
        iDestinationAirport = pDestinationAirport_passenger;
        iPlane = pPlane_passenger;
        this.maxSleep = maxSleep;
    }
    /**
     * Get passenger ID.
     * @return passenger id
     */
    public int getPassengerID() {
            return id;
    }
    
    /**
     * Get maximum sleeping time.
     * @return maximum sleeping time
     */
    public int getMaxSleep() {
        return maxSleep;
    }
    
    /**
     * Life cycle of the Passenger.
     */
    @Override
    public void run(){
        travelToAirport();
        iDepartureAirport.waitInQueue(id);
        iDepartureAirport.showDocuments(id);
        iPlane.boardThePlane(id);
        iPlane.waitForEndOfFlight(id);
        iDestinationAirport.leaveThePlane(id);
        iPlane.leaveThePlane(id);
    }
    
    /**
     * Passenger traveling to the airport.
     * The thread sleeps for a random period of time, for a maximum o maxSleep time.
     */
    private void travelToAirport() {
        try {
            Thread.sleep((long)(Math.random() * maxSleep));
	} catch (InterruptedException e) {}
    }
}
