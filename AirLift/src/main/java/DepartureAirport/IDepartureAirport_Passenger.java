
package DepartureAirport;

import Common.STPassenger;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IDepartureAirport_Passenger {
    public STPassenger travelToAirport(int passengerID);
    public STPassenger waitInQueue(int passengerID);
    public STPassenger showDocuments(int passengerID);
}
