
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
     * Server host name.
     */
    public static final String SERVER_HOSTNAME = "localhost";
    /**
     * Departure Airport server port.
     */
    public static final int DEPARTURE_AIRPORT_SERVER_PORT = 9001;
    /**
     * Destination Airport server port.
     */
    public static final int DESTINATION_AIRPORT_SERVER_PORT = 9002;
    /**
     * Plane server port.
     */
    public static final int PLANE_SERVER_PORT = 9003;
    /**
     * General Repository server port.
     */
    public static final int REPOSITORY_SERVER_PORT = 9004;
      
    /**
     * It can not be instantiated.
     */
    private Parameters(){}
}
