
package Plane;

import Common.STPassenger;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IPlane_Passenger {
    public void boardThePlane(int passengerID);
    public void waitForEndOfFlight(int passengerID);
    public void leaveThePlane(int passengerID);
}
