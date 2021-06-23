
package Main;

import ActiveEntity.AEPassenger;
import DepartureAirport.IDepartureAirport_Passenger;
import DepartureAirport.SRDepartureAirportStub;
import DestinationAirport.IDestinationAirport_Passenger;
import DestinationAirport.SRDestinationAirportStub;
import Plane.IPlane_Passenger;
import Plane.SRPlaneStub;

/**
 * Simulation of the air lift problem - Passengers Main.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class PassengerMain {
     /**
     * Main method.
     * @param args program arguments
     */   
     public static void main(String[] args) {
        if((args.length % 2) != 0){
            System.out.println("Optional arguments: "
                    + "\n\t-dh <DEPARTURE_AIRPORT_SERVER_HOSTNAME>: Shared Region Departure Airport Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-dp <DEPARTURE_AIRPORT_SERVER_PORT>: Shared Region Departure Airport Server Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-dsh <DESTINATION_AIRPORT_SERVER_HOSTNAME>: Shared Region Destination Airport Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-dsp <DESTINATION_AIRPORT_SERVER_PORT>: Shared Region Destination Airport Server Port (Default = " + Parameters.DESTINATION_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-ph <PLANE_SERVER_HOSTNAME>: Shared Region Plane Server Hostname (Default = \"" + Parameters.SERVER_HOSTNAME + "\")"
                    + "\n\t-pp <PLANE_SERVER_PORT>: Shared Region Plane Server Port (Default = " + Parameters.PLANE_SERVER_PORT + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of Passengers (Default = " + Parameters.NUM_PASSENGER + ")"
                    + "\n\t-s <MAX_SLEEP>: Maximum sleeping time in milliseconds (Default = " + Parameters.MAX_SLEEP + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        String depAirHostname = Parameters.SERVER_HOSTNAME;
        String planeHostname = Parameters.SERVER_HOSTNAME;
        String destAirHostname = Parameters.SERVER_HOSTNAME;
        int depAirPort = Parameters.DEPARTURE_AIRPORT_SERVER_PORT; 
        int planePort = Parameters.PLANE_SERVER_PORT;
        int destAirPort = Parameters.DESTINATION_AIRPORT_SERVER_PORT;
        int numPassengers = Parameters.NUM_PASSENGER;
        int maxSleep = Parameters.MAX_SLEEP;
        try{
            for (int i = 0; i < args.length; i+=2) {
                switch(args[i].toLowerCase()){
                    case "-dh": depAirHostname = args[i+1];
                               break;
                    case "-dp": depAirPort = Integer.valueOf(args[i+1]);
                               break;
                    case "-ph": planeHostname = args[i+1];
                               break;
                    case "-pp": planePort = Integer.valueOf(args[i+1]);
                               break;
                    case "-dsh": destAirHostname = args[i+1];
                               break;
                    case "-dsp": destAirPort = Integer.valueOf(args[i+1]);
                               break;
                    case "-p": numPassengers = Integer.valueOf(args[i+1]);
                               break;
                    case "-s": maxSleep = Integer.valueOf(args[i+1]);
                               break;
                    default: throw new IllegalArgumentException();
                }
            }
        } catch(IllegalArgumentException ex){
            System.out.println("Optional arguments: "
                    + "\n\t-dh <DEPARTURE_AIRPORT_SERVER_HOSTNAME>: Shared Region Departure Airport Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-dp <DEPARTURE_AIRPORT_SERVER_PORT>: Shared Region Departure Airport Server Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-dsh <DESTINATION_AIRPORT_SERVER_HOSTNAME>: Shared Region Destination Airport Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-dsp <DESTINATION_AIRPORT_SERVER_PORT>: Shared Region Destination Airport Server Port (Default = " + Parameters.DESTINATION_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-ph <PLANE_SERVER_HOSTNAME>: Shared Region Plane Server Hostname (Default = \"" + Parameters.SERVER_HOSTNAME + "\")"
                    + "\n\t-pp <PLANE_SERVER_PORT>: Shared Region Plane Server Port (Default = " + Parameters.PLANE_SERVER_PORT + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of Passengers (Default = " + Parameters.NUM_PASSENGER + ")"
                    + "\n\t-s <MAX_SLEEP>: Maximum sleeping time in milliseconds (Default = " + Parameters.MAX_SLEEP + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        SRDepartureAirportStub srDepartureAirportStub = new SRDepartureAirportStub(depAirHostname, depAirPort);
        SRDestinationAirportStub srDestinationAirportStub = new SRDestinationAirportStub(destAirHostname, destAirPort);
        SRPlaneStub srPlaneStub = new SRPlaneStub(planeHostname, planePort);
        AEPassenger[] aePassenger = new AEPassenger[numPassengers];
        for (int i = 0; i < numPassengers; i++) {
            aePassenger[i] = new AEPassenger((IDepartureAirport_Passenger)srDepartureAirportStub,
                                             (IDestinationAirport_Passenger)srDestinationAirportStub,
                                             (IPlane_Passenger)srPlaneStub, i, maxSleep);
        }
        System.out.println("Passenger threads started!");
        for (int i = 0; i < numPassengers; i++) 
            aePassenger[i].start();
        try{
            for (int i = 0; i < numPassengers; i++) 
                aePassenger[i].join();
        }catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }
        srDestinationAirportStub.end();
        System.out.println("Passenger threads ended!");
    }
}
