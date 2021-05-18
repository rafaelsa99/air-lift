
package Main;

import Common.MemException;
import DepartureAirport.DepartureAirportProxy;
import DepartureAirport.SRDepartureAirport;
import DepartureAirport.SRDepartureAirportInterface;
import Repository.IRepository_DepartureAirport;
import Repository.RepositoryStub;

/**
 * Simulation of the air lift problem - Departure Airport Main.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class DepartureAirportMain {
    
    /**
     * Main method.
     * @param args program arguments
     */    
    public static void main(String[] args) {
        if((args.length % 2) != 0){
            System.out.println("Optional arguments: "
                    + "\n\t-sp <SERVER_PROXY_PORT>: Server Proxy Agent Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-rh <REPOSITORY_SERVER_HOSTNAME>: General Repository Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REPOSITORY_SERVER_PORT>: General Repository Server Port (Default = " + Parameters.REPOSITORY_SERVER_PORT + ")"
                    + "\n\t-i <MIN_PASSENGER>: Minimum number of passengers on a flight (Default = " + Parameters.MIN_PASSENGER + ")"
                    + "\n\t-a <MAX_PASSENGER>: Maximum number of passengers on a flight (Default = " + Parameters.MAX_PASSENGER + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        int proxyPort = Parameters.DEPARTURE_AIRPORT_SERVER_PORT; 
        String repositoryHostname = Parameters.SERVER_HOSTNAME;
        int repositoryPort = Parameters.REPOSITORY_SERVER_PORT; 
        int numPassenger = Parameters.NUM_PASSENGER; 
        int minPassenger = Parameters.MIN_PASSENGER; 
        int maxPassenger = Parameters.MAX_PASSENGER; 
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
                    case "-i": minPassenger = Integer.valueOf(args[i+1]);
                               break;
                    case "-a": maxPassenger = Integer.valueOf(args[i+1]);
                               break; 
                    default: throw new IllegalArgumentException();
                }
            }
        } catch(IllegalArgumentException ex){
            System.out.println("Optional arguments: "
                    + "\n\t-sp <SERVER_PROXY_PORT>: Server Proxy Agent Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-rh <REPOSITORY_SERVER_HOSTNAME>: General Repository Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REPOSITORY_SERVER_PORT>: General Repository Server Port (Default = " + Parameters.REPOSITORY_SERVER_PORT + ")"
                    + "\n\t-i <MIN_PASSENGER>: Minimum number of passengers on a flight (Default = " + Parameters.MIN_PASSENGER + ")"
                    + "\n\t-a <MAX_PASSENGER>: Maximum number of passengers on a flight (Default = " + Parameters.MAX_PASSENGER + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        RepositoryStub repositoryStub = new RepositoryStub(repositoryHostname, repositoryPort);
        try {
            SRDepartureAirportInterface srDepartureAirport = new SRDepartureAirportInterface(
                    new SRDepartureAirport(numPassenger, minPassenger, maxPassenger,
                                          (IRepository_DepartureAirport) repositoryStub));
            DepartureAirportProxy departureAirportProxy = new DepartureAirportProxy(srDepartureAirport, proxyPort);
            System.out.println("Departure airport server proxy agent started!");
            departureAirportProxy.start();
            try{
                departureAirportProxy.join();
            }catch(InterruptedException ex){
                System.out.println(ex.getMessage());
            }
            repositoryStub.end();
            System.out.println("Departure airport server proxy agent ended!");
        } catch (MemException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
