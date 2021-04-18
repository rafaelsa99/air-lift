
package ActiveEntity;

import Common.STPilot;
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
    private STPilot stPilot;
    
    public AEPilot(IDepartureAirport_Pilot iDepartureAirport, IPlane_Pilot iPlane) {
        super("Pilot");
        this.iDepartureAirport = iDepartureAirport;
        this.iPlane = iPlane;
    }

    @Override
    public void run() {   
        while(true){
            stPilot = iDepartureAirport.informPlaneReadyForBoarding();
            System.out.println("PILOT: " + stPilot);
            stPilot = iPlane.waitForAllInBoard();
            System.out.println("PILOT: " + stPilot);
            stPilot = iPlane.flyToDestinationPoint();
            System.out.println("PILOT: " + stPilot);
            stPilot = iPlane.announceArrival();
            System.out.println("PILOT: " + stPilot);
            stPilot = iPlane.flyToDeparturePoint();
            System.out.println("PILOT: " + stPilot);
            stPilot = iPlane.parkAtTransferGate();
            System.out.println("PILOT: " + stPilot);
            //if(iDepartureAirport.allPassengersTransported())
                //return;
        }
    }
}
