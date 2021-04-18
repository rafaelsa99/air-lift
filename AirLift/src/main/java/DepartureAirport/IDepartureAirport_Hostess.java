
package DepartureAirport;

import Common.STHostess;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IDepartureAirport_Hostess {
    public STHostess checkDocuments();
    public STHostess waitForNextPassenger();
    public STHostess waitForNextFlight();
    public STHostess prepareForPassBoarding();
}
