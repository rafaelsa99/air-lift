
package Main;

import ActiveEntity.AEHostess;
import ActiveEntity.AEPassenger;
import ActiveEntity.AEPilot;
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

    private final int numPassenger;
    private final int maxPassenger;
    private final int minPassenger;
    private final int maxSleep;
    
    private final SRDepartureAirport srDepartureAirport;
    private final SRPlane srPlane;
    private final SRDestinationAirport srDestinationAirport;
    
    private final AEPilot aePilot;
    private final AEHostess aeHostess;
    private final AEPassenger[] aePassenger;
    
    private final Repository repository;
    
    public AirLift(String[] args) throws IOException {
        numPassenger = Parameters.NUM_PASSENGER; //or from args
        maxPassenger = Parameters.MAX_PASSENGER; //or from args
        minPassenger = Parameters.MIN_PASSENGER; //or from args
        maxSleep = Parameters.MAX_SLEEP; //or from args
        
        repository = new Repository(numPassenger);
        
        //Shared regions instatiation
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
        
        //...
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
        System.out.println("Logs written to the file \"" + Parameters.LOG_FILENAME + "\"");
    }
    
    public static void main(String[] args) {
        try {
            new AirLift(args).startSimulation();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
