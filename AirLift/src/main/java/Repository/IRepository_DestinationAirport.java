
package Repository;

/**
 * Defines the methods of the repository used on the destination airport.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IRepository_DestinationAirport {
    /**
     * Sets the passenger state.
     * Also updates the passengers counters according to the new state.
     * @param stPassenger new passenger state
     * @param passengerID passenger id
     */
    public void setPassengerState(int stPassenger, int passengerID);
}
