
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

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AirLift {

    private int NUM_PASSENGER;
    private int MAX_PASSENGER;
    private int MIN_PASSENGER;
    
    private SRDepartureAirport srDepartureAirport;
    private SRPlane srPlane;
    private SRDestinationAirport srDestinationAirport;
    
    private AEPilot aePilot;
    private AEHostess aeHostess;
    private AEPassenger[] aePassenger;
    
    public AirLift(String[] args) {
        NUM_PASSENGER = 21; //or from args
        MAX_PASSENGER = 10; //or from args
        MIN_PASSENGER = 5; //or from args
        
        //Shared regions instatiation
        srDepartureAirport = new SRDepartureAirport();
        srPlane = new SRPlane();
        srDestinationAirport = new SRDestinationAirport();
        
        //Active entities instatiation (threads)
        aePilot = new AEPilot((IDepartureAirport_Pilot)srDepartureAirport,
                              (IPlane_Pilot)srPlane);
        aeHostess = new AEHostess((IDepartureAirport_Hostess)srDepartureAirport,
                                  (IPlane_Hostess)srPlane);
        aePassenger = new AEPassenger[NUM_PASSENGER];
        
        for (int i = 0; i < NUM_PASSENGER; i++) {
            aePassenger[i] = new AEPassenger((IDepartureAirport_Passenger)srDepartureAirport,
                                             (IPlane_Passenger)srPlane,
                                             (IDestinationAirport_Passenger)srDestinationAirport);
        }
        
        //...
    }
    
    private void startSimulation(){
        System.out.println("Simulation Started!");
        
        //Start active entities (threads)
        aePilot.start();
        aeHostess.start();
        for (int i = 0; i < MAX_PASSENGER; i++) 
            aePassenger[i].start();
        
        //Wait active entities to die
        try{
            aePilot.join();
            aeHostess.join();
            for (int i = 0; i < MAX_PASSENGER; i++) 
                aePassenger[i].join();
        }catch(Exception ex){
            //...
        }
        
        System.out.println("End of Simulation!");
    }
    
    public static void main(String[] args) {
        new AirLift(args).startSimulation();
    }
}
