
package Main;

import Common.MemException;
import DepartureAirport.IDepartureAirport;
import DepartureAirport.SRDepartureAirport;
import RMI.Register;
import Repository.IRepository_DepartureAirport;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Simulation of the air lift problem - Departure Airport Main.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class DepartureAirportMain {

    /**
     * Flag signaling the end of operations.
     */
    private static boolean end = false;
    
    /**
     * Main method.
     * @param args program arguments
     */ 
    public static void main(String[] args) {
        if((args.length % 2) != 0){
            System.out.println("Optional arguments: "
                    + "\n\t-lp <PORT>: Port number for listening to service requests (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-rh <REGISTRY_HOSTNAME>: Name of the platform where is located the RMI registering service (Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REGISTRY_PORT>: Port number where the registering service is listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")"
                    + "\n\t-i <MIN_PASSENGER>: Minimum number of passengers on a flight (Default = " + Parameters.MIN_PASSENGER + ")"
                    + "\n\t-a <MAX_PASSENGER>: Maximum number of passengers on a flight (Default = " + Parameters.MAX_PASSENGER + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        int port = Parameters.DEPARTURE_AIRPORT_SERVER_PORT; 
        String registryHostname = Parameters.SERVER_HOSTNAME;
        int registryPort = Parameters.REGISTRY_PORT; 
        int numPassenger = Parameters.NUM_PASSENGER; 
        int minPassenger = Parameters.MIN_PASSENGER; 
        int maxPassenger = Parameters.MAX_PASSENGER; 
        try{
            for (int i = 0; i < args.length; i+=2) {
                switch(args[i].toLowerCase()){
                    case "-lp": port = Integer.valueOf(args[i+1]);
                               if ((port < 4000) || (port >= 65536)){
                                   port = Parameters.DEPARTURE_AIRPORT_SERVER_PORT; 
                                   System.out.println("Argument " + args[i] + " is invalid. Default value will be used.");
                               }
                               break;
                    case "-rh": registryHostname = args[i+1];
                               break;
                    case "-rp": registryPort = Integer.valueOf(args[i+1]);
                               if ((registryPort < 4000) || (registryPort >= 65536)){
                                   registryPort = Parameters.REGISTRY_PORT; 
                                   System.out.println("Argument " + args[i] + " is invalid. Default value will be used.");
                               }
                               break;
                    case "-p": numPassenger = Integer.valueOf(args[i+1]);
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
                    + "\n\t-lp <PORT>: Port number for listening to service requests (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-rh <REGISTRY_HOSTNAME>: Name of the platform where is located the RMI registering service (Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REGISTRY_PORT>: Port number where the registering service is listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")"
                    + "\n\t-i <MIN_PASSENGER>: Minimum number of passengers on a flight (Default = " + Parameters.MIN_PASSENGER + ")"
                    + "\n\t-a <MAX_PASSENGER>: Maximum number of passengers on a flight (Default = " + Parameters.MAX_PASSENGER + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        /* create and install the security manager */
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        System.out.println("Security manager was installed!");

        IRepository_DepartureAirport repositoryStub; // remote reference to the repository object
        Registry registry; // remote reference for registration in the RMI registry service
        Register reg; // remote reference to the object that enables the registration of other remote objects
        try {
            registry = LocateRegistry.getRegistry (registryHostname, registryPort);
            repositoryStub = (IRepository_DepartureAirport) registry.lookup (Parameters.REPOSITORY_ENTRY_NAME);
            reg = (Register) registry.lookup (Parameters.REGISTRY_ENTRY_NAME);
            SRDepartureAirport srDepartureAirport = new SRDepartureAirport(numPassenger, minPassenger, maxPassenger, repositoryStub);
            IDepartureAirport departureAirportStub = (IDepartureAirport) UnicastRemoteObject.exportObject (srDepartureAirport, port);
            System.out.println("Stub was generated!");
            reg.bind (Parameters.DEPARTURE_AIRPORT_ENTRY_NAME, departureAirportStub);
            System.out.println("Departure airport was registered!");
            /* wait for the end of operations */
            System.out.println("Departure airport is in operation!");
            while(!end){
                synchronized(Class.forName("Main.DepartureAirportMain")){
                    try {
                        Class.forName("Main.DepartureAirportMain").wait();
                    } catch(InterruptedException e){
                        System.out.println("Departure airport main thread was interrupted!");
                    }
                }
            }
            
            /* Departure Airport shutdown */
            boolean shutdownDone;
            reg.unbind(Parameters.DEPARTURE_AIRPORT_ENTRY_NAME);
            System.out.println("Departure airport was deregistered!");
            shutdownDone = UnicastRemoteObject.unexportObject (srDepartureAirport, true);
            repositoryStub.shutdown();
            if (shutdownDone)
                System.out.println("Departure airport was shutdown!");
        } catch (MemException | RemoteException | NotBoundException | AlreadyBoundException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Close of operations.
     */
    public static void shutdown(){
        end = true;
        try {
            synchronized(Class.forName("Main.DepartureAirportMain")){
                Class.forName("Main.DepartureAirportMain").notify();
            }
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
