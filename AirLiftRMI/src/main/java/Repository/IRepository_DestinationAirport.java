
package Repository;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Defines the methods of the repository used on the destination airport.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IRepository_DestinationAirport extends IRepository{
    /**
     * Sets the passenger state.
     * Also updates the passengers counters according to the new state.
     * @param stPassenger new passenger state
     * @param passengerID passenger id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setPassengerState(int stPassenger, int passengerID) throws RemoteException;
}
