
package Main;

import ActiveEntity.AEHostess;
import ActiveEntity.AEPassenger;
import ActiveEntity.AEPilot;
import Common.MemException;
import DepartureAirport.IDepartureAirport_Hostess;
import DepartureAirport.IDepartureAirport_Passenger;
import DepartureAirport.IDepartureAirport_Pilot;
import DepartureAirport.SRDepartureAirport;
import DestinationAirport.IDestinationAirport_Passenger;
import DestinationAirport.SRDestinationAirport;
import Plane.IPlane_Hostess;
import Plane.IPlane_Passenger;
import Plane.IPlane_Pilot;
import Plane.SRPlane;
import Repository.IRepository_DepartureAirport;
import Repository.IRepository_DestinationAirport;
import Repository.IRepository_Plane;
import Repository.Repository;
import java.io.IOException;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AirLift {

    private final SRDepartureAirport srDepartureAirport;
    private final SRPlane srPlane;
    private final SRDestinationAirport srDestinationAirport;    
    private final AEPilot aePilot;
    private final AEHostess aeHostess;
    private final AEPassenger[] aePassenger;    
    private final Repository repository;
    
    private int numPassenger;
    private String logFilename;
    
    public AirLift(String[] args) throws IOException, MemException {
        if((args.length % 2) != 0){
            System.out.println("Optional arguments: "
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")"
                    + "\n\t-s <MAX_SLEEP>: Maximum sleeping time in miliseconds (Default = " + Parameters.MAX_SLEEP + ")"
                    + "\n\t-l <LOG_FILENAME>: Filename of the logging file (Default = \"" + Parameters.LOG_FILENAME + "\")"
                    + "\n\t-i <MIN_PASSENGER>: Minimum number of passengers on a flight (Default = " + Parameters.MIN_PASSENGER + ")"
                    + "\n\t-a <MAX_PASSENGER>: Maximum number of passengers on a flight (Default = " + Parameters.MAX_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
               
        numPassenger = Parameters.NUM_PASSENGER; 
        int maxPassenger = Parameters.MAX_PASSENGER; 
        int minPassenger = Parameters.MIN_PASSENGER; 
        int maxSleep = Parameters.MAX_SLEEP;
        logFilename = Parameters.LOG_FILENAME;
        try{
            for (int i = 0; i < args.length; i+=2) {
                switch(args[i].toLowerCase()){
                    case "-p": numPassenger = Integer.valueOf(args[i+1]);
                               break;
                    case "-s": maxSleep = Integer.valueOf(args[i+1]);
                               break;
                    case "-l": logFilename = args[i+1];
                               break;
                    case "-i": minPassenger = Integer.valueOf(args[i+1]);
                               break;
                    case "-a": maxPassenger = Integer.valueOf(args[i+1]);
                               break;  
                    default: throw new IllegalArgumentException();
                }
            }
        } catch(IllegalArgumentException ex){
            System.out.println("Optional arguments: "
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")"
                    + "\n\t-s <MAX_SLEEP>: Maximum sleeping time in miliseconds (Default = " + Parameters.MAX_SLEEP + ")"
                    + "\n\t-l <LOG_FILENAME>: Filename of the logging file (Default = \"" + Parameters.LOG_FILENAME + "\")"
                    + "\n\t-i <MIN_PASSENGER>: Minimum number of passengers on a flight (Default = " + Parameters.MIN_PASSENGER + ")"
                    + "\n\t-a <MAX_PASSENGER>: Maximum number of passengers on a flight (Default = " + Parameters.MAX_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
                
        //Shared regions instatiation
        repository = new Repository(numPassenger, logFilename);
        srDepartureAirport = new SRDepartureAirport(numPassenger, minPassenger, maxPassenger,
                                                    (IRepository_DepartureAirport) repository);
        srPlane = new SRPlane(numPassenger, (IRepository_Plane) repository, maxSleep);
        srDestinationAirport = new SRDestinationAirport((IRepository_DestinationAirport) repository);
        
        //Active entities instatiation (threads)
        aePilot = new AEPilot((IDepartureAirport_Pilot)srDepartureAirport,
                              (IPlane_Pilot)srPlane);
        aeHostess = new AEHostess((IDepartureAirport_Hostess)srDepartureAirport,
                                  (IPlane_Hostess)srPlane);
        aePassenger = new AEPassenger[numPassenger];
        
        for (int i = 0; i < numPassenger; i++) {
            aePassenger[i] = new AEPassenger((IDepartureAirport_Passenger)srDepartureAirport,
                                             (IDestinationAirport_Passenger)srDestinationAirport,
                                             (IPlane_Passenger)srPlane, i, maxSleep);
        }
    }
    
    private void startSimulation(){
        System.out.println("Simulation Started!");
        
        //Start active entities (threads)
        aePilot.start();
        aeHostess.start();
        for (int i = 0; i < numPassenger; i++) 
            aePassenger[i].start();
        
        //Wait active entities to die
        try{
            aePilot.join();
            aeHostess.join();
            for (int i = 0; i < numPassenger; i++) 
                aePassenger[i].join();
        }catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }
        
        System.out.println("End of Simulation!");
        System.out.println("Logs written to the file \"" + logFilename + "\"");
    }
    
    public static void main(String[] args) {
        try {
            new AirLift(args).startSimulation();
        } catch (MemException | IOException | IllegalArgumentException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }
}
