
package DepartureAirport;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Defines the methods of the hostess in the departure airport.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IDepartureAirport_Hostess extends Remote{
    /**
     * Operation to check the documents of the next passenger in queue.
     * The hostess signals the next passenger and awaits for him to show the documents.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void checkDocuments() throws RemoteException;
    /**
     * Operation for the hostess to wait for the next passenger.
     * The hostess signals the passenger she was checking that he can proceed to the plane and waits for him to leave.
     * If there are no more customers in the queue and the plane is not ready to fly, the hostess waits for the next passenger.
     * @return the number of passengers on the plane.
     *         -1, if the boarding procedure is still ongoing.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int waitForNextPassenger() throws RemoteException;
    /**
     * Operation for the hostess to wait for the next flight.
     * The hostess waits for the plane to arrive so that the boarding process can begin.
     * @return true, if there are still passengers to transport and the  simulation continues. 
     *         false, if there are no more passengers left to transport and the simulation ends.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public boolean waitForNextFlight() throws RemoteException;
    /**
     * Operation for the Hostess to prepare for passenger boarding.
     * If there are no customers in the queue, the hostess waits for the next passenger.
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void prepareForPassBoarding() throws RemoteException;
}
