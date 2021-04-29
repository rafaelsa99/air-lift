
package Plane;

/**
 * Defines the methods of the pilot in the plane.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IPlane_Pilot {
    /**
     * Operation for the pilot to wait for the boarding process to end.
     * The pilot waits for the signal of the hostess indicating that the plane is ready to take off.
     */
    public void waitForAllInBoard();
    /**
     * Operation for the pilot to fly the plane to the Destination Point.
     * The thread sleeps for a random period of time, for a maximum o maxSleep time.
     */
    public void flyToDestinationPoint();
    /**
     * Operation for the pilot to announce that the plane has arrived to the destination airport.
     * The pilot signals all passengers to leave the plane, and waits for the last passenger to leave.
     */
    public void announceArrival();
    /**
     * Operation for the pilot to fly the plane to the Departure Point.
     * The thread sleeps for a random period of time, for a maximum o maxSleep time.
     */
    public void flyToDeparturePoint();
    /**
     * Operation for the pilot to park the plane at the transfer gate.
     */
    public void parkAtTransferGate();
}
