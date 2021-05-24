
package ActiveEntity;

import DepartureAirport.IDepartureAirport_Hostess;
import Plane.IPlane_Hostess;

/**
 * Hostess thread, which simulates the hostess life cycle.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEHostess extends Thread{
    
    /**
     * Interface of the Hostess to the reference of the Departure Airport.
     */
    private final IDepartureAirport_Hostess iDepartureAirport;
    /**
     * Interface of the Hostess to the reference of the Plane.
     */
    private final IPlane_Hostess iPlane;
    
    /**
     * Instantiation of a Hostess thread.
     * @param iDepartureAirport_Hostess interface of the Hostess to the reference of the Departure Airport 
     * @param iPlane_Hostess interface of the Hostess to the reference of the Plane
     */
    public AEHostess(IDepartureAirport_Hostess iDepartureAirport_Hostess,
                     IPlane_Hostess iPlane_Hostess ) {
        super("Hostess");
        iDepartureAirport = iDepartureAirport_Hostess;
        iPlane = iPlane_Hostess;
    }
    /**
     * Life cycle of the Hostess.
     */
    @Override
    public void run(){
        int passengersOnFlight;
        while(true){
            if(!iDepartureAirport.waitForNextFlight())
                break;
            iDepartureAirport.prepareForPassBoarding();
            do{
                iDepartureAirport.checkDocuments();
                passengersOnFlight = iDepartureAirport.waitForNextPassenger();
            }while(passengersOnFlight == -1);
            iPlane.informPlaneReadyToTakeOff(passengersOnFlight);
        }
    }   
}
