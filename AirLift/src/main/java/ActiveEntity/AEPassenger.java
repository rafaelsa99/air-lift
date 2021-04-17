
package ActiveEntity;

import DepartureAirport.IDepartureAirport_Passenger;
import DestinationAirport.IDestinationAirport_Passenger;
import Plane.IPlane_Passenger;
import java.util.*;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEPassenger extends Thread{
    private final IDepartureAirport_Passenger pDepartureAirport;
    private final IDestinationAirport_Passenger pDestinationAirport;
    private final IPlane_Passenger pPlane;
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
		super("Passenger "+id);
		this.id = id;
                pDepartureAirport   = pDepartureAirport_passenger;
                pDestinationAirport = pDestinationAirport_passenger;
                pPlane              = pPlane_passenger;
    }
    /**
     * Returns this Passenger's id.
     * @return Passenger's id
     */
    public int getPassengerID() {
            return id;
    }
    
    public void run(){
        goingToAirport();
        pDepartureAirport.travelToAirport(id);
        pDepartureAirport.waitInQueue(id);
        if(pDepartureAirport.checkDocuments(id)==true){
            pDepartureAirport.showDocuments(id);      
        }
        pPlane.boardThePlane(id);
        pPlane.waitForEndOfFlight(id);
        pDestinationAirport.leaveThePlane(id);
    }
    public void goingToAirport(){
        try {
            sleep((long) (new Random().nextInt(100)));
	} catch (InterruptedException e) {}
    }
}
