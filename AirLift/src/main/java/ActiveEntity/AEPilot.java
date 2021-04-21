
package ActiveEntity;

import DepartureAirport.IDepartureAirport_Pilot;
import Plane.IPlane_Pilot;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEPilot extends Thread{
    
    //Shared Regions
    /**
     * iDepartureAirport shared region for Pilot
     * @serialField iDepartureAirport
     */
    private final IDepartureAirport_Pilot iDepartureAirport;
    /**
     * iPlane shared region for Pilot
     * @serialField iPlane
     */
    private final IPlane_Pilot iPlane;
<<<<<<< HEAD
    /**
     * 
     */
    private STPilot stPilot;
=======
>>>>>>> 4b6575849ebaca5ec8aad99646ce091ce7dfd2bf
    
    /**
     * Pilot Instantiation
     * @param iDepartureAirport
     * @param iPlane 
     */
    public AEPilot(IDepartureAirport_Pilot iDepartureAirport, IPlane_Pilot iPlane) {
        super("Pilot");
        this.iDepartureAirport = iDepartureAirport;
        this.iPlane = iPlane;
    }

    /**
     * Pilot's lifecycle
     * 
     */
    @Override
    public void run() {   
        while(true){
            if(!iDepartureAirport.informPlaneReadyForBoarding())
                break;
            iPlane.waitForAllInBoard();
            iPlane.flyToDestinationPoint();
            iPlane.announceArrival();
            iPlane.flyToDeparturePoint();
            iPlane.parkAtTransferGate();
        }
    }
}
