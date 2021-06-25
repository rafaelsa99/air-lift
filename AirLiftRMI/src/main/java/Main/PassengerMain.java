
package Main;

import ActiveEntity.AEPassenger;
import DepartureAirport.IDepartureAirport_Passenger;
import DestinationAirport.IDestinationAirport_Passenger;
import Plane.IPlane_Passenger;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
                    + "\n\t-rh <REGISTRY_HOSTNAME>: Name of the platform where is located the RMI registering service (Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REGISTRY_PORT>: Port number where the registering service is listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of Passengers (Default = " + Parameters.NUM_PASSENGER + ")"
                    + "\n\t-s <MAX_SLEEP>: Maximum sleeping time in milliseconds (Default = " + Parameters.MAX_SLEEP + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        String registryHostname = Parameters.SERVER_HOSTNAME;
        int registryPort = Parameters.REGISTRY_PORT; 
        int numPassengers = Parameters.NUM_PASSENGER;
        int maxSleep = Parameters.MAX_SLEEP;
        try{
            for (int i = 0; i < args.length; i+=2) {
                switch(args[i].toLowerCase()){
                    case "-rh": registryHostname = args[i+1];
                               break;
                    case "-rp": registryPort = Integer.valueOf(args[i+1]);
                               if ((registryPort < 4000) || (registryPort >= 65536)){
                                   registryPort = Parameters.REGISTRY_PORT; 
                                   System.out.println("Argument " + args[i] + " is invalid. Default value will be used.");
                               }
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
                    + "\n\t-rh <REGISTRY_HOSTNAME>: Name of the platform where is located the RMI registering service (Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REGISTRY_PORT>: Port number where the registering service is listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of Passengers (Default = " + Parameters.NUM_PASSENGER + ")"
                    + "\n\t-s <MAX_SLEEP>: Maximum sleeping time in milliseconds (Default = " + Parameters.MAX_SLEEP + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        Registry registry; // remote reference for registration in the RMI registry service
        IDepartureAirport_Passenger srDepartureAirportStub; // remote reference to the object
        IPlane_Passenger srPlaneStub; // remote reference to the object
        IDestinationAirport_Passenger srDestinationAirportStub; // remote reference to the object
        try{
            registry = LocateRegistry.getRegistry (registryHostname, registryPort);
            srDepartureAirportStub = (IDepartureAirport_Passenger) registry.lookup (Parameters.DEPARTURE_AIRPORT_ENTRY_NAME);
            srPlaneStub = (IPlane_Passenger) registry.lookup (Parameters.PLANE_ENTRY_NAME);
            srDestinationAirportStub = (IDestinationAirport_Passenger) registry.lookup (Parameters.DESTINATION_AIRPORT_ENTRY_NAME);
            AEPassenger[] aePassenger = new AEPassenger[numPassengers];
            for (int i = 0; i < numPassengers; i++) {
                aePassenger[i] = new AEPassenger(srDepartureAirportStub, srDestinationAirportStub,
                                                 srPlaneStub, i, maxSleep);
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
            srDestinationAirportStub.shutdown();
            System.out.println("Passenger threads ended!");
        } catch (RemoteException | NotBoundException  ex) {
            System.out.println(ex.getMessage());
        }
    }
}
