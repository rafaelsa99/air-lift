
package Repository;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Defines the methods of the repository used on the plane.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IRepository_Plane extends Remote{
    /**
     * Sets the pilot state.
     * Also updates the flight number depending on the new state.
     * @param stPilot new pilot state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setPilotState(int stPilot) throws RemoteException;
    /**
     * Sets the hostess state.
     * @param stHostess new hostess state1
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setHostessState(int stHostess) throws RemoteException;
    /**
     * Sets the passenger state.
     * Also updates the passengers counters according to the new state.
     * @param stPassenger new passenger state
     * @param passengerID passenger id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setPassengerState(int stPassenger, int passengerID) throws RemoteException;
}
