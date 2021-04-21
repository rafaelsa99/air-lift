
package DestinationAirport;

import ActiveEntity.PassengerStates;
import Repository.IRepository_DestinationAirport;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRDestinationAirport implements IDestinationAirport_Passenger{

    private final IRepository_DestinationAirport iRepository;

    public SRDestinationAirport(IRepository_DestinationAirport iRepository) {
        this.iRepository = iRepository;
    }
    
    @Override
    public void leaveThePlane(int passengerID) {
        iRepository.setPassengerState(PassengerStates.ATDS, passengerID);
    }
    
}
