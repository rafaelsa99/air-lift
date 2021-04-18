
package Plane;

import Common.STPilot;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IPlane_Pilot {
    public STPilot waitForAllInBoard();
    public STPilot flyToDestinationPoint();
    public STPilot announceArrival();
    public STPilot flyToDeparturePoint();
    public STPilot parkAtTransferGate();
}
