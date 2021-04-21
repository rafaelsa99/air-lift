
package Repository;
import Main.Parameters;
import ActiveEntity.HostessStates;
import ActiveEntity.PassengerStates;
import ActiveEntity.PilotStates;
import Common.MemException;
import Common.MemFIFO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 *  @author Rafael Sá (104552), José Brás (74029)
 */
public class Repository implements IRepository_DepartureAirport, 
                                   IRepository_Plane,
                                   IRepository_DestinationAirport{
    // States abr
    private static final String [] stateAbrev ={"PT", "HT", "P", "InQ", "InF", "PTAL"};
    private final ReentrantLock rl;
    private final File logFile;
    private final FileWriter writer;
    private String line;

    // States
    private final int[] passangerState;
    private int pilotState;
    private int hostessState;
    
    private int passengersInQueue;
    private int passengersInPlane;
    private int passengersAtDestination;
    
    private int flightNumber;
    private MemFIFO<Integer> flights;

    public Repository(int numPassengers) throws IOException{
        rl = new ReentrantLock(true);
        pilotState = PilotStates.ATRG;
        hostessState = HostessStates.WTFL;
        passangerState = new int[numPassengers];
        for (int i = 0; i < numPassengers; i++)
            passangerState[i] = PassengerStates.GTAP;
        passengersInQueue = 0;
        passengersInPlane = 0;
        passengersAtDestination = 0;
        flightNumber = 0;
        try {
            flights = new MemFIFO<>(numPassengers);
        } catch (MemException ex) {}
        logFile = new File(Parameters.LOG_FILENAME);
        writer = new FileWriter(logFile);
        initializeLogFile(numPassengers);
    }

    private void initializeLogFile(int numPassengers) throws IOException{
        line = "";
        for (int i = 0; i < stateAbrev.length; i++){
            switch(stateAbrev[i]){
                case "P":
                    for (int j = 0; j < numPassengers; j++)
                        line += " " + stateAbrev[i] + String.format("%02d", j) + " ";
                    break;
                case "PT":
                case "HT":
                    line += " " + stateAbrev[i] + "  ";
                    break;
                case "InQ":
                case "InF":
                case "PTAL":
                    line += stateAbrev[i] + " ";
                    break;
            }
        }
        line += "\n";
        writer.write(line);
        printStates();
    }
    
    @Override
    public void printSumUp(){
        try {
            rl.lock();
            line = "";
            int count = 0;
            line += "\nAirlift sum up:";
            while(!flights.empty()){
                try{
                    line += "\nFlight " + (++count) + 
                                " transported " + flights.read() + " passengers";
                } catch(MemException ex) {}
            }
            line += ".";
            writer.write(line);
            writer.close();
        } catch (IOException ex) {}
        finally {
            rl.unlock();
        }
    }
    
    private void printStates() throws IOException{
        line = "";
        printPilotState();
        printHostessState();
        for (int i = 0; i < passangerState.length; i++)
            printPassengerState(i);
        int[] counters = {passengersInQueue, passengersInPlane, passengersAtDestination};
        int counterLength;
        String spacesFormat;
        for (int i = 0; i < counters.length; i++) {
            counterLength = String.valueOf(counters[i]).length();
            spacesFormat = "   ";
            if(counterLength == 2) 
                spacesFormat = "  ";
            line += spacesFormat + counters[i];
        }
        line += "\n";
        writer.write(line);
    }
    
    private void printPilotState(){
        switch(pilotState){
            case PilotStates.RDFB: line += "RDFB ";
                                   break;
            case PilotStates.WTFB: line += "WTFB ";
                                   break;    
            case PilotStates.FLFW: line += "FLFW ";
                                   break;
            case PilotStates.DRPP: line += "DRPP ";
                                   break;
            case PilotStates.FLBK: line += "FLBK ";
                                   break;
            case PilotStates.ATRG: line += "ATRG ";
                                   break;                       
        }
    }
    
    private void printHostessState(){
        switch(hostessState){
            case HostessStates.CKPS: line += "CKPS";
                                   break;
            case HostessStates.WTFL: line += "WTFL";
                                   break;    
            case HostessStates.RDTF: line += "RDTF";
                                   break;
            case HostessStates.WTPS: line += "WTPS";
                                   break;                     
        }
    }
    
    private void printPassengerState(int i){
        switch(passangerState[i]){
            case PassengerStates.ATDS: line += " ATDS";
                                   break;
            case PassengerStates.GTAP: line += " GTAP";
                                   break;    
            case PassengerStates.INFL: line += " INFL";
                                   break;
            case PassengerStates.INQE: line += " INQE";
                                   break;                     
        }
    }
    
    @Override
    public void setPilotState(int stPilot) {
        try {
            rl.lock();
            pilotState = stPilot;
            switch(stPilot){
                case PilotStates.RDFB:
                    flightNumber += 1;
                    writer.write("\nFlight " + flightNumber + ": boarding started.\n");
                    break;
                case PilotStates.FLBK:
                    writer.write("\nFlight " + flightNumber + ": returning.\n");
                    break;
                case PilotStates.DRPP:
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
    public void setHostessState(int stHostess) {
        try {
            rl.lock();
            if(stHostess != hostessState){
                hostessState = stHostess;
                if(stHostess == HostessStates.RDTF){
                    try{
                        flights.write(passengersInPlane);
                    } catch(MemException ex) {}
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
    public void setHostessState(int stHostess, int passengerID) {
        try {
            rl.lock();
            hostessState = stHostess;
            if(stHostess == HostessStates.CKPS){
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
    public void setPassengerState(int stPassenger, int passengerID) {
        try {
            rl.lock();
            passangerState[passengerID] = stPassenger;
            switch(stPassenger){
                case PassengerStates.INQE:
                    passengersInQueue += 1;
                    break;
                case PassengerStates.INFL:
                    passengersInPlane += 1;
                    break;
                case PassengerStates.ATDS:
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
