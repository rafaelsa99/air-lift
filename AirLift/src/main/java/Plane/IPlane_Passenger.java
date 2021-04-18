
package Plane;

import Common.STPassenger;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IPlane_Passenger {
    public STPassenger boardThePlane(int passengerID);
    public STPassenger waitForEndOfFlight(int passengerID);
}
