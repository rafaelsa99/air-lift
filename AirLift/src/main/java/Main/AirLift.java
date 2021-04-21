
package Main;

import ActiveEntity.AEHostess;
import ActiveEntity.AEPassenger;
import ActiveEntity.AEPilot;
import Common.Parameters;
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

    /**
     * Number of passengers
     * @serialfield numPassenger
     */
    private int numPassenger;
    /**
     * Maximum mumber of passengers in a flight
     * @serialfield maxPassenger
     */
    private int maxPassenger;
    /**
     * Minimum number of passengers in a flight
     * @serialfield minPassenger
     */
    private int minPassenger;
    /**
     * @serialfield maxSleep
     */
    private int maxSleep;
    
    /**
     * SRDepartureAirport
     * @serialfield srDepartureAirport
     */
    private SRDepartureAirport srDepartureAirport;
    /**
     * srPlane
     * @serialfield srPlane
     */
    private SRPlane srPlane;
    /**
     * SRDestinationAirport
     * @serialfield srDestinationAirport
     */
    private SRDestinationAirport srDestinationAirport;
    
    /**
     * Pilot state
     * @serialfield aePilot
     */
    private AEPilot aePilot;
    /**
     * Hostess state
     * @serialfield aeHostess
     */
    private AEHostess aeHostess;
    /**
     * Passenger state
     * @serialfield aePassenger
     */
    private AEPassenger[] aePassenger;
    
    /**
     * Repository
     * @serialfield repository
     */
    private Repository repository;
    
    /**
     * Airlift Instantiation
     * @param args
     * @throws IOException 
     */
    public AirLift(String[] args) throws IOException {
        numPassenger = Parameters.NUM_PASSENGER; //or from args
        maxPassenger = Parameters.MAX_PASSENGER; //or from args
        minPassenger = Parameters.MIN_PASSENGER; //or from args
        maxSleep = Parameters.MAX_SLEEP; //or from args
        
        repository = new Repository(numPassenger);
        
        //Shared regions instatiation
        srDepartureAirport = new SRDepartureAirport(numPassenger, minPassenger, maxPassenger,
                                                    (IRepository_DepartureAirport) repository,
                                                    maxSleep);
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
                                             (IPlane_Passenger)srPlane, i);
        }
        
        //...
    }
    
    /**
     * Function to start the program
     */
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
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
        System.out.println("End of Simulation!");
        System.out.println("Logs written to the file \"" + Parameters.LOG_FILENAME + "\"");
    }
    
    /**
     * Main fuction
     * @param args 
     */
    public static void main(String[] args) {
        try {
            new AirLift(args).startSimulation();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
