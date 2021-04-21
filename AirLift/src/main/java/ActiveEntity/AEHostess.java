
package ActiveEntity;

import DepartureAirport.IDepartureAirport_Hostess;
import Plane.IPlane_Hostess;
/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEHostess extends Thread{
    
    private final IDepartureAirport_Hostess iDepartureAirport;
    private final IPlane_Hostess iPlane;
    
    /**
     * Hostess instantiation
     * 
     */
    public AEHostess(IDepartureAirport_Hostess iDepartureAirport_Hostess,
                     IPlane_Hostess iPlane_Hostess ) {
        super("Hostess");
        iDepartureAirport = iDepartureAirport_Hostess;
        iPlane = iPlane_Hostess;
    }
    
    @Override
    public void run(){
        while(true){
            if(!iDepartureAirport.waitForNextFlight())
                break;
            iDepartureAirport.prepareForPassBoarding();
            do{
                iDepartureAirport.checkDocuments();
            }while(iDepartureAirport.waitForNextPassenger());
            iPlane.informPlaneReadyToTakeOff();
        }
    }   
}
