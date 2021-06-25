
package Main;

/**
 * Defines the simulation default parameters.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class Parameters {
    
    /**
     * Number of passengers to transfer.
     */
    public static final int NUM_PASSENGER = 21;
    /**
     * Maximum number of passengers on each flight.
     */
    public static final int MAX_PASSENGER = 10;
    /**
     * Minimum number of passengers on each flight.
     */
    public static final int MIN_PASSENGER = 5;
    /**
     * Name of the logging file.
     */
    public static final String LOG_FILENAME = "log.txt";
    /**
     * Maximum sleeping time, in milliseconds.
     */
    public static final int MAX_SLEEP = 1000;
    /**
     * Default host name.
     */
    public static final String SERVER_HOSTNAME = "localhost";
    /**
     * Registry default port.
     */
    public static final int REGISTRY_PORT = 9000;
    /**
     * Departure Airport default port.
     */
    public static final int DEPARTURE_AIRPORT_SERVER_PORT = 9001;
    /**
     * Destination Airport default port.
     */
    public static final int DESTINATION_AIRPORT_SERVER_PORT = 9002;
    /**
     * Plane default port.
     */
    public static final int PLANE_SERVER_PORT = 9003;
    /**
     * General Repository default port.
     */
    public static final int REPOSITORY_SERVER_PORT = 9004;
    /**
     * Registry entry name.
     */
    public final static String REGISTRY_ENTRY_NAME = "RegisterHandler";
    /**
     * Departure Airport entry name.
     */
    public final static String DEPARTURE_AIRPORT_ENTRY_NAME = "DepartureAirport";
    /**
     * Destination Airport entry name.
     */
    public final static String DESTINATION_AIRPORT_ENTRY_NAME = "DestinationAirport";
    /**
     * Plane entry name.
     */
    public final static String PLANE_ENTRY_NAME = "Plane";
    /**
     * General Repository entry name.
     */
    public final static String REPOSITORY_ENTRY_NAME = "GeneralRepository";
    
    /**
     * It can not be instantiated.
     */
    private Parameters(){}
}
