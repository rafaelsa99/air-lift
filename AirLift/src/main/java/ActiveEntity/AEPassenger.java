
package ActiveEntity;

import DepartureAirport.IDepartureAirport_Passenger;
import DestinationAirport.IDestinationAirport_Passenger;
import Plane.IPlane_Passenger;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEPassenger extends Thread{
    private final IDepartureAirport_Passenger iDepartureAirport;
    private final IDestinationAirport_Passenger iDestinationAirport;
    private final IPlane_Passenger iPlane;
    private final int maxSleep;
    
    /**
     * Passenger's id 
     * @serialField id
     */
    private final int id;
    /**
     * Passenger instantiation
     * 
     * @param id Passenger's Id
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
     * Returns this Passenger's id.
     * @return Passenger's id
     */
    public int getPassengerID() {
            return id;
    }

    public int getMaxSleep() {
        return maxSleep;
    }
    
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
    
    private void travelToAirport() {
        try {
            Thread.sleep((long)(Math.random() * maxSleep));
	} catch (InterruptedException e) {}
    }
}
