
package Plane;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Defines the methods of the passenger in the plane.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IPlane_Passenger extends Remote{
    /**
     * Operation for the passengers to board the plane.
     * The passenger is added to the list of passengers on the plane.
     * @param passengerID passenger id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void boardThePlane(int passengerID) throws RemoteException;
    /**
     * Operation for the passenger to wait for the end of the flight.
     * The passenger waits to be signaled by the pilot that he can leave the plane.
     * @param passengerID passenger id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void waitForEndOfFlight(int passengerID) throws RemoteException;
    /**
     * Operation for the passenger leave the plane.
     * If it is the last passenger to leave the plane, the passenger signals the pilot.
     * @param passengerID passenger id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void leaveThePlane(int passengerID) throws RemoteException;
}
