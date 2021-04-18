
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
import Repository.Repository;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AirLift {

    private int numPassenger;
    private int maxPassenger;
    private int minPassenger;
    
    private SRDepartureAirport srDepartureAirport;
    private SRPlane srPlane;
    private SRDestinationAirport srDestinationAirport;
    
    private AEPilot aePilot;
    private AEHostess aeHostess;
    private AEPassenger[] aePassenger;
    
    private Repository repository;
    
    public AirLift(String[] args) throws IOException {
        numPassenger = Parameters.NUM_PASSENGER; //or from args
        maxPassenger = Parameters.MAX_PASSENGER; //or from args
        minPassenger = Parameters.MIN_PASSENGER; //or from args
        
        repository = new Repository();
        
        //Shared regions instatiation
        srDepartureAirport = new SRDepartureAirport(numPassenger, minPassenger, maxPassenger);
        srPlane = new SRPlane(numPassenger);
        srDestinationAirport = new SRDestinationAirport();
        
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
            //...
        }
        
        System.out.println("End of Simulation!");
    }
    
    public static void main(String[] args) {
        try {
            new AirLift(args).startSimulation();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
