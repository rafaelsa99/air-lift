
package Repository;

import java.rmi.RemoteException;

/**
 * Defines the methods of the repository used on the departure airport.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IRepository_DepartureAirport extends IRepository{
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
     * Sets the hostess state.
     * Also updates the counter of passengers in the queue according to the new state.
     * @param stHostess new hostess state
     * @param passengerID passenger id being checked
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setHostessState(int stHostess, int passengerID) throws RemoteException;
    /**
     * Sets the passenger state.
     * Also updates the passengers counters according to the new state.
     * @param stPassenger new passenger state
     * @param passengerID passenger id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setPassengerState(int stPassenger, int passengerID) throws RemoteException;
    /**
     * Prints the final sum up with all flights that took place and the number of passenger in each one.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void printSumUp() throws RemoteException;
    /**
     * Operation server shutdown.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void shutdown() throws RemoteException;
}
