
package DestinationAirport;

import Common.STPassenger;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRDestinationAirport implements IDestinationAirport_Passenger{

    @Override
    public STPassenger leaveThePlane(int passengerID) {
        return STPassenger.ATDS;
    }
    
}
