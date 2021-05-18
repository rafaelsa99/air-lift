
package Main;

import Common.MemException;
import Repository.Repository;
import Repository.RepositoryInterface;
import Repository.RepositoryProxy;
import java.io.IOException;

/**
 * Simulation of the air lift problem - Repository Main.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class RepositoryMain {
    /**
     * Main method.
     * @param args program arguments
     */   
    public static void main(String[] args) {
        if((args.length % 2) != 0){
            System.out.println("Optional arguments: "
                    + "\n\t-sp <SERVER_PROXY_PORT>: Server Proxy Agent Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-l <LOG_FILENAME>: Filename of the logging file (Default = \"" + Parameters.LOG_FILENAME + "\")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        int proxyPort = Parameters.REPOSITORY_SERVER_PORT; 
        String logFilename = Parameters.LOG_FILENAME;
        int numPassenger = Parameters.NUM_PASSENGER; 
        try{
            for (int i = 0; i < args.length; i+=2) {
                switch(args[i].toLowerCase()){
                    case "-sp": proxyPort = Integer.valueOf(args[i+1]);
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
                    + "\n\t-sp <SERVER_PROXY_PORT>: Server Proxy Agent Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-l <LOG_FILENAME>: Filename of the logging file (Default = \"" + Parameters.LOG_FILENAME + "\")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of passengers (Default = " + Parameters.NUM_PASSENGER + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        try {
            RepositoryInterface repository = new RepositoryInterface(new Repository(numPassenger, logFilename));
            RepositoryProxy repositoryProxy = new RepositoryProxy(repository, proxyPort);
            System.out.println("Repository server proxy agent started!");
            repositoryProxy.start();
            try{
                repositoryProxy.join();
            }catch(InterruptedException ex){
                System.out.println(ex.getMessage());
            }
            System.out.println("Repository server proxy agent ended!");
        } catch (MemException | IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
