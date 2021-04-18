
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
    private final IDepartureAirport_Passenger iDepartureAirport;
    private final IDestinationAirport_Passenger iDestinationAirport;
    private final IPlane_Passenger iPlane;
    private STPassenger stPassenger;
    
    /**
     * Passenger's id 
     * @serialField id
     */
    private int id;
    /**
     * Passenger instantiation
     * 
     * @param id Passenger's Id
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
    
    public void run(){
        stPassenger = iDepartureAirport.travelToAirport(id);
        System.out.println("PASSENGER " + id + ": " + stPassenger);
        stPassenger = iDepartureAirport.waitInQueue(id);
        System.out.println("PASSENGER " + id + ": " + stPassenger);
        stPassenger = iDepartureAirport.showDocuments(id);
        System.out.println("PASSENGER " + id + ": " + stPassenger);
        stPassenger = iPlane.boardThePlane(id);
        System.out.println("PASSENGER " + id + ": " + stPassenger);
        stPassenger = iPlane.waitForEndOfFlight(id);
        System.out.println("PASSENGER " + id + ": " + stPassenger);
        stPassenger = iDestinationAirport.leaveThePlane(id);
        System.out.println("PASSENGER " + id + ": " + stPassenger);
    }
}
