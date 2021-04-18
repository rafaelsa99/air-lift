
package Repository;
import Common.Parameters;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 *  @author Rafael Sá (104552), José Brás (74029)
 */
public class Repository {
    // States abr
    private final String [] stateAbrev ={"PT", "HT", "P00", "P01", "P02", "P03", "P04", 
                                           "P05", "P06", "P07","P08", "P09", "P10", "P11", 
                                           "P12", "P13", "P14", "P15", "P16", "P17", "P18", 
                                           "P19", "P20", "InQ", "InF", "PTAL"};
    private File logFile;
    private FileWriter writer;
    
    // Passenger
    //private States[] passangerState;
    private int passCount;
    private int[] PassengerID;
    private boolean inQueue;
    // Pilot 
    // private States pilotState;
    // Hostess
    // private States hostessState;
    
    // Departure Airport
    private int customerQueue;
    private int waitingForBoarding;

    public Repository() throws IOException{
        logFile = new File(Parameters.LOG_FILENAME);
        writer = new FileWriter(logFile);
        for (int i = 0; i < stateAbrev.length; i++)
            writer.write(stateAbrev[i] + " ");
        writer.write("\n");
    }
}
