
package Plane;

/**
 * Defines the methods of the passenger in the plane.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IPlane_Passenger {
    /**
     * Operation for the passengers to board the plane.
     * The passenger is added to the list of passengers on the plane.
     * @param passengerID passenger id
     */
    public void boardThePlane(int passengerID);
    /**
     * Operation for the passenger to wait for the end of the flight.
     * The passenger waits to be signaled by the pilot that he can leave the plane.
     * @param passengerID passenger id
     */
    public void waitForEndOfFlight(int passengerID);
    /**
     * Operation for the passenger leave the plane.
     * If it is the last passenger to leave the plane, the passenger signals the pilot.
     * @param passengerID passenger id
     */
    public void leaveThePlane(int passengerID);
}
