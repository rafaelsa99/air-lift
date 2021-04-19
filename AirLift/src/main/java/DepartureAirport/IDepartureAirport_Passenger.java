
package DepartureAirport;

import Common.STPassenger;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IDepartureAirport_Passenger {
    public void travelToAirport(int passengerID);
    public void waitInQueue(int passengerID);
    public void showDocuments(int passengerID);
}
