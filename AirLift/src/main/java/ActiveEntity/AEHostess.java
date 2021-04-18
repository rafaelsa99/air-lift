
package ActiveEntity;

import Common.STHostess;
import DepartureAirport.IDepartureAirport_Hostess;
import Plane.IPlane_Hostess;
/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEHostess extends Thread{
    
    private final IDepartureAirport_Hostess iDepartureAirport;
    private final IPlane_Hostess iPlane;
    private STHostess stHostess;
    
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
    public void run(){
        while(true){
            stHostess = iDepartureAirport.waitForNextFlight();
            System.out.println("HOSTESS: " + stHostess);
            stHostess = iDepartureAirport.prepareForPassBoarding();
            System.out.println("HOSTESS: " + stHostess);
            while(stHostess != STHostess.RDTF){
                stHostess = iDepartureAirport.checkDocuments();
                System.out.println("HOSTESS: " + stHostess);
                stHostess = iDepartureAirport.waitForNextPassenger();
                System.out.println("HOSTESS: " + stHostess);
            }
            stHostess = iPlane.informPlaneReadyToTakeOff();
            System.out.println("HOSTESS: " + stHostess);
        }
    }
    
}
