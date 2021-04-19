
package DepartureAirport;

import Common.STHostess;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IDepartureAirport_Hostess {
    public void checkDocuments();
    public STHostess waitForNextPassenger();
    public boolean waitForNextFlight();
    public void prepareForPassBoarding();
}
