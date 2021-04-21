
package ActiveEntity;

import DepartureAirport.IDepartureAirport_Hostess;
import Plane.IPlane_Hostess;
/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEHostess extends Thread{
    /**
     * IDepartureAirport_Hostess shared region for Hostess
     * @serialField IDepartureAirport_Hostess
     */
    private final IDepartureAirport_Hostess iDepartureAirport;
    /**
     * IPlane_Hostess shared region for Hostess
     * @serialField IPlane_Hostess
     */
    private final IPlane_Hostess iPlane;
    /**
     * States for Hostess
     * @serialField STHostess
     */
    //private STHostess stHostess;
    
    /**
     * AEHostess instantiation
     * @param IDepartureAirport_Hostess iDepartureAirport_Hostess
     * @param IPlane_Hostess iPlane_Hostess
     */
    public AEHostess(IDepartureAirport_Hostess iDepartureAirport_Hostess,
                     IPlane_Hostess iPlane_Hostess ) {
        super("Hostess");
        iDepartureAirport = iDepartureAirport_Hostess;
        iPlane = iPlane_Hostess;
    }
    /**
     * Hostess's lifecycle
     */
    
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
