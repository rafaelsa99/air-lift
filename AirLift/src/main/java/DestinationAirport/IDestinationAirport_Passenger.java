
package DestinationAirport;

/**
 * Defines the methods of the passenger in the destination airport.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public interface IDestinationAirport_Passenger {
    /**
     * Operation to indicate that the passenger left the plane and arrived at the destination airport.
     * @param passengerID passenger id
     */
    public void leaveThePlane(int passengerID);
}
