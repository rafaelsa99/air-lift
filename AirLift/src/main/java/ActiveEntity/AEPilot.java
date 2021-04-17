
package ActiveEntity;

import DepartureAirport.IDepartureAirport_Pilot;
import DestinationAirport.IDestinationAirport_Pilot;
import Plane.IPlane_Pilot;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEPilot extends Thread{
    private final IDepartureAirport_Pilot pDepartureAirport;
    private final IDestinationAirport_Pilot pDestinationAirport;
    private final IPlane_Pilot pPlane;
    /**
     * Pilot instantiation
     * 
     */
    public AEPilot(IDepartureAirport_Pilot pDepartureAirport_pilot,
                   IDestinationAirport_Pilot pDestinationAirport_pilot,
                   IPlane_Pilot pPlane_Pilot) {
		super("Pilot ");
                pDepartureAirport   = pDepartureAirport_pilot;
                pDestinationAirport = pDestinationAirport_pilot;
                pPlane              = pPlane_Pilot;
    }
    public void run(){
        while(true){
            if(pPlane_Pilot.parkAtTransferGate()==0){
                break;
            }
            pDepartureAirport_pilot.pDepartureAirport();
            pPlane_Pilot.waitForAllInBoard();
            pPlane_Pilot.flyToDestinationPoint();
            pPlane_Pilot.announceArrival();
            pPlane_Pilot.flyToDepartureAirport();
        }
        
       
    }
}
