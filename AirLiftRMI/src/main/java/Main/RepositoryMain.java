
package Main;

import Common.MemException;
import RMI.Register;
import Repository.IRepository;
import Repository.Repository;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Simulation of the air lift problem - Repository Main.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class RepositoryMain {
    
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
                    + "\n\t-lp <PORT>: Port number for listening to service requests (Default = " + Parameters.REPOSITORY_SERVER_PORT + ")"
                    + "\n\t-rh <REGISTRY_HOSTNAME>: Name of the platform where is located the RMI registering service (Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REGISTRY_PORT>: Port number where the registering service is listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")"
                    + "\n\t-l <LOG_FILENAME>: Filename of the logging file (Default = \"" + Parameters.LOG_FILENAME + "\")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        int port = Parameters.REPOSITORY_SERVER_PORT; 
        String registryHostname = Parameters.SERVER_HOSTNAME;
        int registryPort = Parameters.REGISTRY_PORT; 
        String logFilename = Parameters.LOG_FILENAME;
        int numPassenger = Parameters.NUM_PASSENGER; 
        try{
            for (int i = 0; i < args.length; i+=2) {
                switch(args[i].toLowerCase()){
                    case "-lp": port = Integer.valueOf(args[i+1]);
                               if ((port < 4000) || (port >= 65536)){
                                   port = Parameters.REPOSITORY_SERVER_PORT; 
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
                    case "-l": logFilename = args[i+1];
                               break;
                    default: throw new IllegalArgumentException();
                }
            }
        } catch(IllegalArgumentException ex){
            System.out.println("Optional arguments: "
                    + "\n\t-lp <PORT>: Port number for listening to service requests (Default = " + Parameters.REPOSITORY_SERVER_PORT + ")"
                    + "\n\t-rh <REGISTRY_HOSTNAME>: Name of the platform where is located the RMI registering service (Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REGISTRY_PORT>: Port number where the registering service is listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")"
                    + "\n\t-l <LOG_FILENAME>: Filename of the logging file (Default = \"" + Parameters.LOG_FILENAME + "\")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        /* create and install the security manager */
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        System.out.println("Security manager was installed!");
        
        Registry registry; // remote reference for registration in the RMI registry service
        Register reg; // remote reference to the object that enables the registration of other remote objects
        try{
            registry = LocateRegistry.getRegistry (registryHostname, registryPort);
            reg = (Register) registry.lookup (Parameters.REGISTRY_ENTRY_NAME);
            Repository repository = new Repository(numPassenger, logFilename);
            IRepository repositoryStub = (IRepository) UnicastRemoteObject.exportObject (repository, port);
            System.out.println("Stub was generated!");
            reg.bind (Parameters.REPOSITORY_ENTRY_NAME, repositoryStub);
            System.out.println("General repository was registered!");
            /* wait for the end of operations */
            System.out.println("General repository is in operation!");
            while(!end){
                synchronized(Class.forName("Main.RepositoryMain")){
                    try {
                        Class.forName("Main.RepositoryMain").wait();
                    } catch(InterruptedException e){
                        System.out.println("General repository main thread was interrupted!");
                    }
                }
            }
            /* General repository shutdown */
            boolean shutdownDone;
            reg.unbind(Parameters.REPOSITORY_ENTRY_NAME);
            System.out.println("General repository was deregistered!");
            shutdownDone = UnicastRemoteObject.unexportObject (repository, true);
            if (shutdownDone)
                System.out.println("General repository was shutdown!");
        } catch (IOException | MemException | NotBoundException | AlreadyBoundException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Close of operations.
     */
    public static void shutdown(){
        end = true;
        try {
            synchronized(Class.forName("Main.RepositoryMain")){
                Class.forName("Main.RepositoryMain").notify();
            }
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
