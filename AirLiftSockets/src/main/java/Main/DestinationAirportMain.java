
package Main;

import DestinationAirport.DestinationAirportProxy;
import DestinationAirport.SRDestinationAirport;
import DestinationAirport.SRDestinationAirportInterface;
import Repository.IRepository_DestinationAirport;
import Repository.RepositoryStub;

/**
 *
 * @author rafael
 */
public class DestinationAirportMain {
    
    public static void main(String[] args) {
        if((args.length % 2) != 0){
            System.out.println("Optional arguments: "
                    + "\n\t-sp <SERVER_PROXY_PORT>: Server Proxy Agent Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-rh <REPOSITORY_SERVER_HOSTNAME>: General Repository Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REPOSITORY_SERVER_PORT>: General Repository Server Port (Default = " + Parameters.REPOSITORY_SERVER_PORT + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        int proxyPort = Parameters.DESTINATION_AIRPORT_SERVER_PORT; 
        String repositoryHostname = Parameters.SERVER_HOSTNAME;
        int repositoryPort = Parameters.REPOSITORY_SERVER_PORT; 
        try{
            for (int i = 0; i < args.length; i+=2) {
                switch(args[i].toLowerCase()){
                    case "-sp": proxyPort = Integer.valueOf(args[i+1]);
                               break;
                    case "-rh": repositoryHostname = args[i+1];
                               break;
                    case "-rp": repositoryPort = Integer.valueOf(args[i+1]);
                               break;
                    default: throw new IllegalArgumentException();
                }
            }
        } catch(IllegalArgumentException ex){
            System.out.println("Optional arguments: "
                    + "\n\t-sp <SERVER_PROXY_PORT>: Server Proxy Agent Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-rh <REPOSITORY_SERVER_HOSTNAME>: General Repository Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-rp <REPOSITORY_SERVER_PORT>: General Repository Server Port (Default = " + Parameters.REPOSITORY_SERVER_PORT + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        RepositoryStub repositoryStub = new RepositoryStub(repositoryHostname, repositoryPort);
        SRDestinationAirportInterface srDestinationAirport = new SRDestinationAirportInterface(
                new SRDestinationAirport((IRepository_DestinationAirport) repositoryStub));
        DestinationAirportProxy destinationAirportProxy = new DestinationAirportProxy(srDestinationAirport, proxyPort);
        System.out.println("Destination airport server proxy agent started!");
        destinationAirportProxy.start();
        try {
            destinationAirportProxy.join();
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Destination airport server proxy agent ended!");
    }
}
