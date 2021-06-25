
package Main;

import DestinationAirport.IDestinationAirport;
import DestinationAirport.SRDestinationAirport;
import RMI.Register;
import Repository.IRepository_DestinationAirport;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Simulation of the air lift problem - Destination Airport Main.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class DestinationAirportMain {
    
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
                    + "\n\t-lp <PORT>: Port number for listening to service requests (Default = " + Parameters.DESTINATION_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-rh <REGISTRY_HOSTNAME>: Name of the platform where is located the RMI registering service (Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REGISTRY_PORT>: Port number where the registering service is listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        int port = Parameters.DESTINATION_AIRPORT_SERVER_PORT; 
        String registryHostname = Parameters.SERVER_HOSTNAME;
        int registryPort = Parameters.REGISTRY_PORT; 
        try{
            for (int i = 0; i < args.length; i+=2) {
                switch(args[i].toLowerCase()){
                    case "-lp": port = Integer.valueOf(args[i+1]);
                               if ((port < 4000) || (port >= 65536)){
                                   port = Parameters.DESTINATION_AIRPORT_SERVER_PORT; 
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
                    default: throw new IllegalArgumentException();
                }
            }
        } catch(IllegalArgumentException ex){
            System.out.println("Optional arguments: "
                    + "\n\t-lp <PORT>: Port number for listening to service requests (Default = " + Parameters.DESTINATION_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-rh <REGISTRY_HOSTNAME>: Name of the platform where is located the RMI registering service (Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REGISTRY_PORT>: Port number where the registering service is listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        /* create and install the security manager */
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        System.out.println("Security manager was installed!");

        IRepository_DestinationAirport repositoryStub; // remote reference to the repository object
        Registry registry; // remote reference for registration in the RMI registry service
        Register reg; // remote reference to the object that enables the registration of other remote objects
        try {
            registry = LocateRegistry.getRegistry (registryHostname, registryPort);
            repositoryStub = (IRepository_DestinationAirport) registry.lookup (Parameters.REPOSITORY_ENTRY_NAME);
            reg = (Register) registry.lookup (Parameters.REGISTRY_ENTRY_NAME);
            SRDestinationAirport srDestinationAirport = new SRDestinationAirport(repositoryStub);
            IDestinationAirport destinationAirportStub = (IDestinationAirport) UnicastRemoteObject.exportObject (srDestinationAirport, port);
            System.out.println("Stub was generated!");
            reg.bind (Parameters.DESTINATION_AIRPORT_ENTRY_NAME, destinationAirportStub);
            System.out.println("Destination airport was registered!");
            /* wait for the end of operations */
            System.out.println("Destination airport is in operation!");
            while(!end){
                synchronized(Class.forName("Main.DestinationAirportMain")){
                    try {
                        Class.forName("Main.DestinationAirportMain").wait();
                    } catch(InterruptedException e){
                        System.out.println("Destination airport main thread was interrupted!");
                    }
                }
            }
            /* Destination Airport shutdown */
            boolean shutdownDone;
            reg.unbind(Parameters.DESTINATION_AIRPORT_ENTRY_NAME);
            System.out.println("Destination airport was deregistered!");
            shutdownDone = UnicastRemoteObject.unexportObject (srDestinationAirport, true);
            if (shutdownDone)
                System.out.println("Destination airport was shutdown!");
        } catch (RemoteException | NotBoundException | AlreadyBoundException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Close of operations.
     */
    public static void shutdown(){
        end = true;
        try {
            synchronized(Class.forName("Main.DestinationAirportMain")){
                Class.forName("Main.DestinationAirportMain").notify();
            }
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
