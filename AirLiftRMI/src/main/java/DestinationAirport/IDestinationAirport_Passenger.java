
package DestinationAirport;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Defines the methods of the passenger in the destination airport.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IDestinationAirport_Passenger extends Remote{
    /**
     * Operation to indicate that the passenger left the plane and arrived at the destination airport.
     * @param passengerID passenger id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void leaveThePlane(int passengerID) throws RemoteException;
    /**
     * Operation server shutdown.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void shutdown() throws RemoteException;
}
