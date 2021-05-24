
package Main;

import Common.MemException;
import Plane.PlaneProxy;
import Plane.SRPlane;
import Plane.SRPlaneInterface;
import Repository.IRepository_Plane;
import Repository.RepositoryStub;

/**
 * Simulation of the air lift problem - Plane Main.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class PlaneMain {
    /**
     * Main method.
     * @param args program arguments
     */
    public static void main(String[] args) {
        if((args.length % 2) != 0){
            System.out.println("Optional arguments: "
                    + "\n\t-sp <SERVER_PROXY_PORT>: Server Proxy Agent Port (Default = " + Parameters.PLANE_SERVER_PORT + ")"
                    + "\n\t-rh <REPOSITORY_SERVER_HOSTNAME>: General Repository Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REPOSITORY_SERVER_PORT>: General Repository Server Port (Default = " + Parameters.REPOSITORY_SERVER_PORT + ")"
                    + "\n\t-s <MAX_SLEEP>: Maximum sleeping time in milliseconds (Default = " + Parameters.MAX_SLEEP + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        int proxyPort = Parameters.PLANE_SERVER_PORT; 
        String repositoryHostname = Parameters.SERVER_HOSTNAME;
        int repositoryPort = Parameters.REPOSITORY_SERVER_PORT; 
        int numPassenger = Parameters.NUM_PASSENGER; 
        int maxSleep = Parameters.MAX_SLEEP; 
        try{
            for (int i = 0; i < args.length; i+=2) {
                switch(args[i].toLowerCase()){
                    case "-sp": proxyPort = Integer.valueOf(args[i+1]);
                               break;
                    case "-rh": repositoryHostname = args[i+1];
                               break;
                    case "-rp": repositoryPort = Integer.valueOf(args[i+1]);
                               break;
                    case "-p": numPassenger = Integer.valueOf(args[i+1]);
                               break;
                    case "-s": maxSleep = Integer.valueOf(args[i+1]);
                               break;
                    default: throw new IllegalArgumentException();
                }
            }
        } catch(IllegalArgumentException ex){
            System.out.println("Optional arguments: "
                    + "\n\t-sp <SERVER_PROXY_PORT>: Server Proxy Agent Port (Default = " + Parameters.PLANE_SERVER_PORT + ")"
                    + "\n\t-rh <REPOSITORY_SERVER_HOSTNAME>: General Repository Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REPOSITORY_SERVER_PORT>: General Repository Server Port (Default = " + Parameters.REPOSITORY_SERVER_PORT + ")"
                    + "\n\t-s <MAX_SLEEP>: Maximum sleeping time in milliseconds (Default = " + Parameters.MAX_SLEEP + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        RepositoryStub repositoryStub = new RepositoryStub(repositoryHostname, repositoryPort);
        try {
            SRPlaneInterface srPlane = new SRPlaneInterface(new SRPlane(numPassenger,
                                            (IRepository_Plane) repositoryStub, maxSleep));
            PlaneProxy planeProxy = new PlaneProxy(srPlane, proxyPort);
            System.out.println("Plane server proxy agent started!");
            planeProxy.start();
            try{
                planeProxy.join();
            }catch(InterruptedException ex){
                System.out.println(ex.getMessage());
            }
            System.out.println("Plane server proxy agent ended!");
        } catch (MemException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
