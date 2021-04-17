
package ActiveEntity;

import DepartureAirport.IDepartureAirport_Hostess;
import Plane.IPlane_Hostess;
import DestinationAirport.IDestinationAiport_Hostess;
/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEHostess extends Thread{
    
    private final IDepartureAirport_Hostess hDepartureAirport;
    private final IDestinationAiport_Hostess hDestinationAirport;
    private final IPlane_Hostess hPlane;
    
    /**
     * Hostess instantiation
     * 
     */
    public AEHostess(IDepartureAirport_Hostess hDepartureAirport_hostess,
                    IDestinationAiport_Hostess hDestinationAirport_hostess,
                    IPlane_Hostess hPlane_Hostess ) {
		super("Hostess ");
                hDepartureAirport = hDepartureAirport_hostess;
                hDestinationAirport = hDestinationAirport_hostess;
                hPlane = hPlane_Hostess;
    }
    public void run(){
        while(hPlane_Hostess.waitForAllInBoard()!= 0){
            if(hDepartureAirport_hostess.waitForNextFlight() == 0){
                break;
            }
            hDepartureAirport_hostess.prepareForPassBoarding()();
            while(passangersInqueue && spaceAvailableOnPlane){
                hDepartureAirport_hostess.checkDocuments();
                hDepartureAirport_hostess.waitForNextPassenger();
            }
            hPlane_Hostess.informPlaneReadyToTakeOff();
            
            
        }
    }
    
}
