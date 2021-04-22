
package ActiveEntity;

/**
 * Definition of the internal states of the hostess during the life cycle.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class HostessStates {

    /**
     * The hostess is waiting for the next flight.
     */
    public static final int WTFL = 0;
    /**
     * The hostess is checking a passenger.
     */
    public static final int CKPS = 1;
    /**
     * The hostess is waiting for a passenger.
     */
    public static final int WTPS = 2;
    /**
     * The hostess is ready to fly.
     */
    public static final int RDTF = 3;

    /**
     * It can not be instantiated.
     */
    private HostessStates() {} 
}
