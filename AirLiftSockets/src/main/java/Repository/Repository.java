
package Repository;
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
 * General Repository.
 * It is responsible to keep the visible internal state of the problem and to provide means for it to be printed in the logging file.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class Repository implements IRepository_DepartureAirport, 
                                   IRepository_Plane,
                                   IRepository_DestinationAirport{

    /**
     * Abbreviations to write in the logging file.
     */
    private static final String [] abbrv ={"PT", "HT", "P", "InQ", "InF", "PTAL"};
    /**
     * Reentrant Lock.
     */
    private final ReentrantLock rl;
    /**
     * Logging file.
     */
    private final File logFile;
    /**
     * File writer for the logging file.
     */
    private final FileWriter writer;
    /**
     * Current line to write in the file.
     */
    private String line;
    /**
     * State of the passengers.
     */
    private final int[] passangerState;
    /**
     * State of the pilot.
     */
    private int pilotState;
    /**
     * State of the hostess.
     */
    private int hostessState;    
    /**
     * Number of passengers in the queue.
     */
    private int passengersInQueue;
    /**
     * Number of passengers inside the plane.
     */
    private int passengersInPlane;
    /**
     * Number of passengers that arrived at the destination.
     */
    private int passengersAtDestination;
    /**
     * Number of the current flight.
     */
    private int flightNumber;
    /**
     * Memory FIFO with all the occupation of flights completed
     */
    private final MemFIFO<Integer> flights;

    /**
     * Instantiation of the repository.
     * @param numPassengers number of passengers to transport
     * @param logFilename name of the logging file
     * @throws IOException if an error occurred with the file
     * @throws Common.MemException if it was not possible to create the FIFO
     */
    public Repository(int numPassengers, String logFilename) throws IOException, MemException{
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
        flights = new MemFIFO<>(numPassengers);
        logFile = new File(logFilename);
        writer = new FileWriter(logFile);
        initializeLogFile(numPassengers);
    }

    /**
     * Initializes the logging file.
     * Writes the header and the initial states to the logging file.
     * @param numPassengers number of passengers to transport
     * @throws IOException if an error occurred with the file
     */
    private void initializeLogFile(int numPassengers) throws IOException{
        line = "";
        for (String abbr : abbrv) {
            switch (abbr) {
                case "P":
                    for (int j = 0; j < numPassengers; j++) {
                        line += " " + abbr + String.format("%02d", j) + " ";
                    }
                    break;
                case "PT":
                case "HT":
                    line += " " + abbr + "  ";
                    break;
                case "InQ":
                case "InF":
                case "PTAL":
                    line += abbr + " ";
                    break;
            }
        }
        line += "\n";
        writer.write(line);
        printStates();
    }
    
    /**
     * Prints the final sum up with all flights that took place and the number of passenger in each one.
     */
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

        } catch (IOException ex) {}
        finally {
            rl.unlock();
        }
    }
    
    /**
     * Prints the the visible internal state to the logging file.
     * @throws IOException if an error occurred with the file
     */
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
    
    /**
     * Print to the current line the name of the pilot state.
     */
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
    
    /**
     * Print to the current line the name of the hostess state.
     */
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
    
    /**
     * Print to the current line the name of the passenger state.
     * @param i passenger id
     */
    private void printPassengerState(int id){
        switch(passangerState[id]){
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
    
    /**
     * Sets the pilot state.
     * Also updates the flight number depending on the new state.
     * @param stPilot new pilot state
     */
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
        } catch (IOException ex) {System.err.println("Exception: " + ex.getMessage());}
        finally {
            rl.unlock();
        }
    }

    /**
     * Sets the hostess state.
     * @param stHostess new hostess state1
     */
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
        } catch (IOException ex) {System.err.println("Exception: " + ex.getMessage());}
        finally {
            rl.unlock();
        }
    }

    /**
     * Sets the hostess state.
     * Also updates the counter of passengers in the queue according to the new state.
     * @param stHostess new hostess state
     * @param passengerID passenger id being checked
     */
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
        } catch (IOException ex) {System.err.println("Exception: " + ex.getMessage());}
        finally {
            rl.unlock();
        }
    }
    
    /**
     * Sets the passenger state.
     * Also updates the passengers counters according to the new state.
     * @param stPassenger new passenger state
     * @param passengerID passenger id
     */
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
        } catch (IOException ex) {System.err.println("Exception: " + ex.getMessage());}
        finally {
            rl.unlock();
        }
    }

}
