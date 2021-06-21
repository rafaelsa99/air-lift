/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import Main.Parameters;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */

/**
 * This data type instantiates and registers a remote object that enables the
 * registration of other remote objects located in the same or other processing
 * nodes in the local registry service. Communication is based in Java RMI.
 */
public class ServerRegisterRemoteObject {
    public static boolean serviceEnd = false;
    /**
        * Main task.
        * @param args Unused arguments
        */
       public static void main(String[] args) {
            /* get location of the registry service */

               String rmiRegHostName = Parameters.serverHostname;
               int rmiRegPortNumb = Parameters.severPort;

               /* create and install the security manager */

               if (System.getSecurityManager() == null)
                       System.setSecurityManager(new SecurityManager());
               System.out.println("Security manager was installed!");

               /* instantiate a registration remote object and generate a stub for it */

               RegisterRemoteObject regEngine = new RegisterRemoteObject(rmiRegHostName, rmiRegPortNumb);
               Register regEngineStub = null;
               int listeningPort = Parameters.severPort; /* it should be set accordingly in each case */

               try {
                       regEngineStub = (Register) UnicastRemoteObject.exportObject(regEngine, listeningPort);
               } catch (RemoteException e) {
                       System.out.println("RegisterRemoteObject stub generation exception: " + e.getMessage());
                       System.exit(1);
               }
               System.out.println("Stub was generated!");

               /* register it with the local registry service */

               String nameEntry = Parameters.registryEntryName;
               Registry registry = null;

               try {
                       registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
               } catch (RemoteException e) {
                       System.out.println("RMI registry creation exception: " + e.getMessage());
                       System.exit(1);
               }
               System.out.println("RMI registry was created!");

               try {
                       registry.rebind(nameEntry, regEngineStub);
               } catch (RemoteException e) {
                       e.printStackTrace();
                       System.out.println("RegisterRemoteObject remote exception on registration: " + e.getMessage());
                       System.exit(1);
               }
               System.out.println("RegisterRemoteObject object was registered!");

               /* Wait for registry engine */
               while (!serviceEnd) {
                       try {
                               synchronized(regEngine) {
                                       regEngine.wait();
                               }
                       } catch (InterruptedException e) {
               System.out.println("Main thread of registry was interrupted.");
               System.exit(1);
           }
               }

               System.out.println("Registry finished execution.");

               /* Unregister register */
               try {
                       registry.unbind(nameEntry);
               }
       catch (RemoteException e) { 
                       System.out.println("RegisterRemoteObject unregistration exception: " + e.getMessage ());
                       e.printStackTrace ();
                       System.exit (1);
       } catch (NotBoundException e) {
                       System.out.println("RegisterRemoteObject unregistration exception: " + e.getMessage ());
                       e.printStackTrace ();
                       System.exit (1);
               }

               /* Unexport register */

       try {
                       UnicastRemoteObject.unexportObject (regEngine, false);
       } catch (RemoteException e) { 
                       System.out.println("RegisterRemoteObject unexport exception: " + e.getMessage ());
                       e.printStackTrace ();
                       System.exit (1);
       }

       System.out.println("RegisterRemoteObject object was unexported successfully!");

       }

}
