
package Main;

import ActiveEntity.AEHostess;
import DepartureAirport.IDepartureAirport_Hostess;
import Plane.IPlane_Hostess;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Simulation of the air lift problem - Hostess Main.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class HostessMain {

    /**
     * Main method.
     * @param args program arguments
     */ 
    public static void main(String[] args) {
        if((args.length % 2) != 0){
            System.out.println("Optional arguments: "
                    + "\n\t-rh <REGISTRY_HOSTNAME>: Name of the platform where is located the RMI registering service (Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REGISTRY_PORT>: Port number where the registering service is listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        String registryHostname = Parameters.SERVER_HOSTNAME;
        int registryPort = Parameters.REGISTRY_PORT; 
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
                    default: throw new IllegalArgumentException();
                }
            }
        } catch(IllegalArgumentException ex){
            System.out.println("Optional arguments: "
                    + "\n\t-rh <REGISTRY_HOSTNAME>: Name of the platform where is located the RMI registering service (Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REGISTRY_PORT>: Port number where the registering service is listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        Registry registry; // remote reference for registration in the RMI registry service
        IDepartureAirport_Hostess srDepartureAirportStub; // remote reference to the object
        IPlane_Hostess srPlaneStub; // remote reference to the object
        try {
            registry = LocateRegistry.getRegistry (registryHostname, registryPort);
            srDepartureAirportStub = (IDepartureAirport_Hostess) registry.lookup (Parameters.DEPARTURE_AIRPORT_ENTRY_NAME);
            srPlaneStub = (IPlane_Hostess) registry.lookup (Parameters.PLANE_ENTRY_NAME);
            AEHostess aeHostess = new AEHostess(srDepartureAirportStub, srPlaneStub);
            System.out.println("Hostess thread started!");
            aeHostess.start();
            try{
                aeHostess.join();
            }catch(InterruptedException ex){
                System.out.println(ex.getMessage());
            }
            System.out.println("Hostess thread ended!");
        } catch (RemoteException | NotBoundException  ex) {
            System.out.println(ex.getMessage());
        }
    }
}
