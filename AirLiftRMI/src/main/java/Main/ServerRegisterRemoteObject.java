package Main;

import RMI.Register;
import RMI.RegisterRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This data type instantiates and registers a remote object that enables the
 * registration of other remote objects located in the same or other processing
 * nodes in the local registry service. Communication is based in Java RMI.
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class ServerRegisterRemoteObject {

    private static boolean serviceEnd = false;

    /**
     * Main method.
     * @param args program arguments
     */ 
    public static void main(String[] args) {
        if((args.length % 2) != 0){
            System.out.println("Optional arguments: "
                    + "\n\t-lp <PORT>: Port number for listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")"
                    + "\n\t-rh <REGISTRY_HOSTNAME>: Name of the platform where is located the RMI registering service (Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REGISTRY_PORT>: Port number where the registering service is listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        int port = Parameters.REGISTRY_PORT; 
        String registryHostname = Parameters.SERVER_HOSTNAME;
        int registryPort = Parameters.REGISTRY_PORT; 
        try{
            for (int i = 0; i < args.length; i+=2) {
                switch(args[i].toLowerCase()){
                    case "-lp": port = Integer.valueOf(args[i+1]);
                               if ((port < 4000) || (port >= 65536)){
                                   port = Parameters.REGISTRY_PORT; 
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
                    + "\n\t-lp <PORT>: Port number for listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")"
                    + "\n\t-rh <REGISTRY_HOSTNAME>: Name of the platform where is located the RMI registering service (Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REGISTRY_PORT>: Port number where the registering service is listening to service requests (Default = " + Parameters.REGISTRY_PORT + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        /* create and install the security manager */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        System.out.println("Security manager was installed!");
        
        RegisterRemoteObject regEngine;
        Register regEngineStub;
        Registry registry;
        try{
             /* instantiate a registration remote object and generate a stub for it */
            regEngine = new RegisterRemoteObject(registryHostname, registryPort);
            regEngineStub = (Register) UnicastRemoteObject.exportObject(regEngine, port);
            System.out.println("Stub was generated!");
            registry = LocateRegistry.getRegistry(registryHostname, registryPort);
            System.out.println("RMI registry was created!");
            registry.rebind(Parameters.REGISTRY_ENTRY_NAME, regEngineStub);
            System.out.println("RegisterRemoteObject object was registered!");
            /* wait for the end of operations */
            while(!serviceEnd){
                synchronized(Class.forName("Main.ServerRegisterRemoteObject")){
                    try {
                        Class.forName("Main.ServerRegisterRemoteObject").wait();
                    } catch(InterruptedException e){
                        System.out.println("Registry main thread was interrupted!");
                    }
                }
            }
            /* Register Remote Object shutdown */
            boolean shutdownDone;
            regEngineStub.unbind(Parameters.REGISTRY_ENTRY_NAME);
            System.out.println("RegisterRemoteObject was deregistered!");
            shutdownDone = UnicastRemoteObject.unexportObject (regEngine, true);
            if (shutdownDone)
                System.out.println("RegisterRemoteObject was shutdown!");
        } catch (RemoteException | NotBoundException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Close of operations.
     */
    public static void shutdown() {
        serviceEnd = true;
        try {
            synchronized (Class.forName("Main.ServerRegisterRemoteObject")) {
                Class.forName("Main.ServerRegisterRemoteObject").notify();
            }
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
