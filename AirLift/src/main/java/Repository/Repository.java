
package Repository;
import Common.Parameters;
import Common.STHostess;
import Common.STPassenger;
import Common.STPilot;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *  @author Rafael Sá (104552), José Brás (74029)
 */
public class Repository implements IRepository_DepartureAirport, 
                                   IRepository_Plane,
                                   IRepository_DestinationAirport{
    // States abr
    private final String [] stateAbrev ={"PT", "HT", "P", "InQ", "InF", "PTAL"};
    private final ReentrantLock rl;
    private final File logFile;
    private final FileWriter writer;
    
    // States
    private final STPassenger[] passangerState;
    private STPilot pilotState;
    private STHostess hostessState;
    
    private int passengersInQueue;
    private int passengersInPlane;
    private int passengersAtDestination;
    
    private int flightNumber;
    private final LinkedHashMap<Integer, Integer> flights;

    public Repository(int numPassengers) throws IOException{
        rl = new ReentrantLock(true);
        pilotState = STPilot.ATRG;
        hostessState = STHostess.WTFL;
        passangerState = new STPassenger[numPassengers];
        for (int i = 0; i < numPassengers; i++)
            passangerState[i] = STPassenger.GTAP;
        passengersInQueue = 0;
        passengersInPlane = 0;
        passengersAtDestination = 0;
        flightNumber = 0;
        flights = new LinkedHashMap<>();
        logFile = new File(Parameters.LOG_FILENAME);
        writer = new FileWriter(logFile);
        initializeLogFile(numPassengers);
    }

    private void initializeLogFile(int numPassengers) throws IOException{
        for (int i = 0; i < stateAbrev.length; i++){
            switch(stateAbrev[i]){
                case "P":
                    for (int j = 0; j < numPassengers; j++)
                        writer.write(" " + stateAbrev[i] + String.format("%02d", j) + " ");
                    break;
                case "PT":
                case "HT":
                    writer.write(" " + stateAbrev[i] + "  ");
                    break;
                case "InQ":
                case "InF":
                case "PTAL":
                    writer.write(stateAbrev[i] + " ");
                    break;
            }
        }
        writer.write("\n");
        printStates();
    }
    
    @Override
    public void printSumUp(){
        try {
            rl.lock();
            writer.write("\nAirlift sum up:");
            for (Map.Entry<Integer,Integer> flight : flights.entrySet())
                writer.write("\nFlight " + flight.getKey() + 
                        " transported " + flight.getValue() + " passengers");
            writer.write(".");
            writer.close();
        } catch (IOException ex) {}
        finally {
            rl.unlock();
        }
    }
    
    private void printStates() throws IOException{
        writer.write(pilotState + " " + hostessState);
        for (int i = 0; i < passangerState.length; i++)
            writer.write(" " + passangerState[i]);
        int[] counters = {passengersInQueue, passengersInPlane, passengersAtDestination};
        int counterLength;
        String spacesFormat;
        for (int i = 0; i < counters.length; i++) {
            counterLength = String.valueOf(counters[i]).length();
            spacesFormat = "   ";
            if(counterLength == 2) 
                spacesFormat = "  ";
            writer.write(spacesFormat + counters[i]);
        }
        writer.write("\n");
    }
    
    @Override
    public void setPilotState(STPilot stPilot) {
        try {
            rl.lock();
            pilotState = stPilot;
            switch(stPilot){
                case RDFB:
                    flightNumber += 1;
                    writer.write("\nFlight " + flightNumber + ": boarding started.\n");
                    break;
                case FLBK:
                    writer.write("\nFlight " + flightNumber + ": returning.\n");
                    break;
                case DRPP:
                    writer.write("\nFlight " + flightNumber + ": arrived.\n");
                    break;
            }
            printStates();
        } catch (IOException ex) {}
        finally {
            rl.unlock();
        }
    }

    @Override
    public void setHostessState(STHostess stHostess) {
        try {
            rl.lock();
            if(stHostess != hostessState){
                hostessState = stHostess;
                if(stHostess == STHostess.RDTF){
                    flights.put(flightNumber, passengersInPlane);
                    writer.write("\nFlight " + flightNumber + ": departed with "
                                 + passengersInPlane + " passengers.\n");
                }
                printStates();
            }
        } catch (IOException ex) {}
        finally {
            rl.unlock();
        }
    }

    @Override
    public void setHostessState(STHostess stHostess, int passengerID) {
        try {
            rl.lock();
            hostessState = stHostess;
            if(stHostess == STHostess.CKPS){
                passengersInQueue -= 1;
                writer.write("\nFlight " + flightNumber + ": passenger "
                             + passengerID + " checked.\n");
            }
            printStates();
        } catch (IOException ex) {}
        finally {
            rl.unlock();
        }
    }
    
    @Override
    public void setPassengerState(STPassenger stPassenger, int passengerID) {
        try {
            rl.lock();
            passangerState[passengerID] = stPassenger;
            switch(stPassenger){
                case INQE:
                    passengersInQueue += 1;
                    break;
                case INFL:
                    passengersInPlane += 1;
                    break;
                case ATDS:
                    passengersInPlane -= 1;
                    passengersAtDestination += 1;
                    break;
            }
            printStates();
        } catch (IOException ex) {}
        finally {
            rl.unlock();
        }
    }
}
