
package DepartureAirport;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Defines the methods of the pilot in the departure airport.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IDepartureAirport_Pilot extends Remote{
    /**
     * Operation to inform that the plane is ready for boarding.
     * Pilot signals the Hostess to start the boarding process.
     * @return true, if there are still passengers to transport and the  simulation continues. 
     *         false, if there are no more passengers left to transport and the simulation ends.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public boolean informPlaneReadyForBoarding() throws RemoteException;
    /**
     * Operation server shutdown.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void shutdown() throws RemoteException;
}
