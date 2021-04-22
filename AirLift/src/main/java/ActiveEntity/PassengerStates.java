
package ActiveEntity;

/**
 * Definition of the internal states of the passenger during the life cycle.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class PassengerStates {
    
    /**
     * The passenger is going to the airport.
     */
    public static final int GTAP = 0;
    /**
     * The passenger is in the queue on the airport.
     */
    public static final int INQE = 1;
    /**
     * The passenger is in flight.
     */
    public static final int INFL = 2;
    /**
     * The passenger is at the destination airport.
     */
    public static final int ATDS = 3;

    /**
     * It can not be instantiated.
     */
    private PassengerStates() {}
}
