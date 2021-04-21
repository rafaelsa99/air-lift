
package ActiveEntity;

import Common.STPassenger;
import DepartureAirport.IDepartureAirport_Passenger;
import DestinationAirport.IDestinationAirport_Passenger;
import Plane.IPlane_Passenger;
import java.util.*;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEPassenger extends Thread{
    
    /**
     * iDepartureAirport
     * @serialField IDepartureAirport_Passenger
     */
    private final IDepartureAirport_Passenger iDepartureAirport;
    /**
     * iDestinationAirport
     * @serialField IDestinationAirport_Passenger
     */
    private final IDestinationAirport_Passenger iDestinationAirport;
    /**
     * Plane
     * @serialField IPlane_Passenger
     */
    private final IPlane_Passenger iPlane;
    /**
     * Passenger states
     * @serialField STPassenger
     */
    private STPassenger stPassenger;
    
    /**
     * Passenger's id 
     * @serialField id
     */
    private int id;
    /**
     * Passenger instantiation
     * 
     * @param pDepartureAirport_passenger   Departure Airport shared region  
     * @param pDestinationAirport_passenger Destination Airport shared region  
     * @param pPlane_passenger              Plane shared region              
     * @param id                            Passenger's Id 
     */
    public AEPassenger(IDepartureAirport_Passenger pDepartureAirport_passenger,
                       IDestinationAirport_Passenger pDestinationAirport_passenger,
                       IPlane_Passenger pPlane_passenger,
                       int id) {
        super("Passenger " + id);
        this.id = id;
        iDepartureAirport   = pDepartureAirport_passenger;
        iDestinationAirport = pDestinationAirport_passenger;
        iPlane = pPlane_passenger;
    }
    /**
     * Returns this Passenger's id.
     * @return Passenger's id
     */
    public int getPassengerID() {
            return id;
    }
    /**
     * Passenger's lifecycle
     */
    public void run(){
        iDepartureAirport.travelToAirport(id);
        iDepartureAirport.waitInQueue(id);
        iDepartureAirport.showDocuments(id);
        iPlane.boardThePlane(id);
        iPlane.waitForEndOfFlight(id);
        iDestinationAirport.leaveThePlane(id);
        iPlane.leaveThePlane(id);
    }
}
