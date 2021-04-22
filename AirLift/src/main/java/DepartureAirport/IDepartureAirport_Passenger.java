
package DepartureAirport;

/**
 * Defines the methods of the passenger in the departure airport.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IDepartureAirport_Passenger {
    /**
     * Operation for the passenger to wait in the queue.
     * If the passenger is the first on the queue, signals the hostess.
     * The passenger waits to be called by the hostess.
     * @param passengerID passenger id 
     */
    public void waitInQueue(int passengerID);
    /**
     * Operation for the passenger show the documents.
     * The passenger signals the hostess to show the documents.
     * Then, waits for the hostess to check the documents.
     * @param passengerID passenger id
     */
    public void showDocuments(int passengerID);
}
