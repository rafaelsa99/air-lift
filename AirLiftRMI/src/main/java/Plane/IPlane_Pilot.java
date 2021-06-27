
package Plane;

import java.rmi.RemoteException;

/**
 * Defines the methods of the pilot in the plane.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IPlane_Pilot extends IPlane{
    /**
     * Operation for the pilot to wait for the boarding process to end.
     * The pilot waits for the signal of the hostess indicating that the plane is ready to take off.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void waitForAllInBoard() throws RemoteException;
    /**
     * Operation for the pilot to fly the plane to the Destination Point.
     * The thread sleeps for a random period of time, for a maximum o maxSleep time.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void flyToDestinationPoint() throws RemoteException;
    /**
     * Operation for the pilot to announce that the plane has arrived to the destination airport.
     * The pilot signals all passengers to leave the plane, and waits for the last passenger to leave.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void announceArrival() throws RemoteException;
    /**
     * Operation for the pilot to fly the plane to the Departure Point.
     * The thread sleeps for a random period of time, for a maximum o maxSleep time.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void flyToDeparturePoint() throws RemoteException;
    /**
     * Operation for the pilot to park the plane at the transfer gate.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void parkAtTransferGate() throws RemoteException;
    /**
     * Operation server shutdown.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void shutdown() throws RemoteException;
}
