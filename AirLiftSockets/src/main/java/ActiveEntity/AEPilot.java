
package ActiveEntity;

import DepartureAirport.IDepartureAirport_Pilot;
import Plane.IPlane_Pilot;

/**
 * Pilot thread, which simulates the pilot life cycle.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEPilot extends Thread{
    
    /**
     * Interface of the pilot to the reference of the Departure Airport.
     */
    private final IDepartureAirport_Pilot iDepartureAirport;
    /**
     * Interface of the pilot to the reference of the Plane.
     */
    private final IPlane_Pilot iPlane;

    /**
     * Instantiation of a Passenger thread.
     * @param iDepartureAirport interface of the pilot to the reference of the Departure Airport
     * @param iPlane interface of the pilot to the reference of the Plane
     */
    public AEPilot(IDepartureAirport_Pilot iDepartureAirport, IPlane_Pilot iPlane) {
        super("Pilot");
        this.iDepartureAirport = iDepartureAirport;
        this.iPlane = iPlane;
    }

    /**
     * Life cycle of the pilot.
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
