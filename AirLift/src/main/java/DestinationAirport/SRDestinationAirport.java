
package DestinationAirport;

import Common.STPassenger;
import Repository.IRepository_DestinationAirport;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRDestinationAirport implements IDestinationAirport_Passenger{

    private final IRepository_DestinationAirport iRepository;

    /**
     * SRDestinationAirport instantiation
     * @param iRepository 
     */
    public SRDestinationAirport(IRepository_DestinationAirport iRepository) {
        this.iRepository = iRepository;
    }
    
    /**
     * Passenger state is set to ATDS
     * @param passengerID 
     */
    @Override
    public void leaveThePlane(int passengerID) {
        iRepository.setPassengerState(STPassenger.ATDS, passengerID);
    }
    
}
