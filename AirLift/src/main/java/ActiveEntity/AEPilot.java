
package ActiveEntity;

import DepartureAirport.IDepartureAirport_Pilot;
import Plane.IPlane_Pilot;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEPilot extends Thread{
    
    //Shared Regions
    private final IDepartureAirport_Pilot iDepartureAirport;
    private final IPlane_Pilot iPlane;

    public AEPilot(IDepartureAirport_Pilot iDepartureAirport, IPlane_Pilot iPlane) {
        this.iDepartureAirport = iDepartureAirport;
        this.iPlane = iPlane;
    }

    @Override
    public void run() {
        Boolean notEnd = true;
        
        while(notEnd){
            iDepartureAirport.informPlaneReadyForBoarding();
            iPlane.waitForAllInBoard();
            iPlane.flyToDestinationPoint();
            iPlane.announceArrival();
            iPlane.flyToDeparturePoint();
            iPlane.parkAtTransferGate();
            if(iDepartureAirport.allPassengersTransported())
                notEnd = false;
        }
    }
}
