
package DepartureAirport;

import java.rmi.RemoteException;

/**
 * Defines the methods of the passenger in the departure airport.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IDepartureAirport_Passenger extends IDepartureAirport{
    /**
     * Operation for the passenger to wait in the queue.
     * If the passenger is the first on the queue, signals the hostess.
     * The passenger waits to be called by the hostess.
     * @param passengerID passenger id 
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void waitInQueue(int passengerID) throws RemoteException;
    /**
     * Operation for the passenger show the documents.
     * The passenger signals the hostess to show the documents.
     * Then, waits for the hostess to check the documents.
     * @param passengerID passenger id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void showDocuments(int passengerID) throws RemoteException;
}
