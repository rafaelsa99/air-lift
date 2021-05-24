
package Plane;

/**
 * Defines the methods of the hostess in the plane.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IPlane_Hostess {
    /**
     * Operation for the hostess to inform that the plane is ready to take off.
     * The hostess signals the pilot to start the flight.
     * @param numPassengersOnPlane number of passengers that must be on plane before allow the take off
     */
    public void informPlaneReadyToTakeOff(int numPassengersOnPlane);
}
