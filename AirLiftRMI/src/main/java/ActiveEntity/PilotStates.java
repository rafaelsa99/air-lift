
package ActiveEntity;

/**
 * Definition of the internal states of the pilot during the life cycle.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class PilotStates {
    /**
     * The pilot is ready for boarding.
     */
    public static final int RDFB = 0;
    /**
     * The pilot is waiting for boarding.
     */
    public static final int WTFB = 1;
    /**
     * The pilot is flying forward to the destination airport.
     */
    public static final int FLFW = 2;
    /**
     * The pilot is deboarding the passengers.
     */
    public static final int DRPP = 3;
    /**
     * The pilot is flying back to the departure airport.
     */
    public static final int FLBK = 4;
    /**
     * The pilot is at the transfer gate.
     */
    public static final int ATRG = 5;

    /**
     * It can not be instantiated.
     */
    private PilotStates() {}
}
