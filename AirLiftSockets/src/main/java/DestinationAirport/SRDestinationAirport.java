
package DestinationAirport;

import ActiveEntity.PassengerStates;
import Repository.IRepository_DestinationAirport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Destination Airport.
 * It is responsible for the passengers arriving at the destination airport.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRDestinationAirport implements IDestinationAirport_Passenger{

    /**
     * Reentrant Lock.
     */
    private final ReentrantLock rl;
    /**
     * Interface of the destination airport to the reference of the repository.
     */
    private final IRepository_DestinationAirport iRepository;

    /**
     * Instantiation of the destination airport
     * @param iRepository interface of the destination airport to the reference of the repository
     */
    public SRDestinationAirport(IRepository_DestinationAirport iRepository) {
        this.iRepository = iRepository;
        this.rl = new ReentrantLock(true);
    }
    
    /**
     * Operation to indicate that the passenger left the plane and arrived at the destination airport.
     * @param passengerID passenger id
     */
    @Override
    public void leaveThePlane(int passengerID) {
        try{
            rl.lock();
            iRepository.setPassengerState(PassengerStates.ATDS, passengerID);
        } finally {
            rl.unlock();
        }
    }
    
}
