
package Main;

/**
 * Defines the simulation default parameters.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class Parameters {
    
    /**
     * Default number of passengers to transfer.
     */
    public static final int NUM_PASSENGER = 21;
    /**
     * Default maximum number of passengers on each flight.
     */
    public static final int MAX_PASSENGER = 10;
    /**
     * Default minimum number of passengers on each flight.
     */
    public static final int MIN_PASSENGER = 5;
    /**
     * Default name of the logging file.
     */
    public static final String LOG_FILENAME = "log.txt";
    /**
     * Default maximum sleeping time, in milliseconds.
     */
    public static final int MAX_SLEEP = 1000;
    /**
     * Default server host name.
     */
    public static final String SERVER_HOSTNAME = "localhost";
    /**
     * Departure Airport default server port.
     */
    public static final int DEPARTURE_AIRPORT_SERVER_PORT = 9001;
    /**
     * Destination Airport default server port.
     */
    public static final int DESTINATION_AIRPORT_SERVER_PORT = 9002;
    /**
     * Plane default server port.
     */
    public static final int PLANE_SERVER_PORT = 9003;
    /**
     * General Repository default server port.
     */
    public static final int REPOSITORY_SERVER_PORT = 9004;
      
    /**
     * It can not be instantiated.
     */
    private Parameters(){}
}
