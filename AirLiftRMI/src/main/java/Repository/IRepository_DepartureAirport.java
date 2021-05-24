
package Repository;

/**
 * Defines the methods of the repository used on the departure airport.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IRepository_DepartureAirport {
    /**
     * Sets the pilot state.
     * Also updates the flight number depending on the new state.
     * @param stPilot new pilot state
     */
    public void setPilotState(int stPilot);
    /**
     * Sets the hostess state.
     * @param stHostess new hostess state1
     */
    public void setHostessState(int stHostess);
    /**
     * Sets the hostess state.
     * Also updates the counter of passengers in the queue according to the new state.
     * @param stHostess new hostess state
     * @param passengerID passenger id being checked
     */
    public void setHostessState(int stHostess, int passengerID);
    /**
     * Sets the passenger state.
     * Also updates the passengers counters according to the new state.
     * @param stPassenger new passenger state
     * @param passengerID passenger id
     */
    public void setPassengerState(int stPassenger, int passengerID);
    /**
     * Prints the final sum up with all flights that took place and the number of passenger in each one.
     */
    public void printSumUp();
}
