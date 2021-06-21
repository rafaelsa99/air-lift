
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
      
    
    public static final String serverHostname = "localhost";
             
    public static final int severPort = 9000;
    
    public final static String registryEntryName = "registry";
    /**
     * It can not be instantiated.
     */
    private Parameters(){}
}
