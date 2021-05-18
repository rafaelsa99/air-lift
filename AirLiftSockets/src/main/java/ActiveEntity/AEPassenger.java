
package ActiveEntity;

import DepartureAirport.IDepartureAirport_Passenger;
import DepartureAirport.SRDepartureAirportStub;
import DestinationAirport.IDestinationAirport_Passenger;
import DestinationAirport.SRDestinationAirportStub;
import Main.Parameters;
import Plane.IPlane_Passenger;
import Plane.SRPlaneStub;

/**
 * Passenger thread, which simulates the Passenger life cycle.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class AEPassenger extends Thread{
    
    /**
     * Interface of the Passenger to the reference of the Departure Airport.
     */
    private final IDepartureAirport_Passenger iDepartureAirport;
    /**
     * Interface of the Passenger to the reference of the Destination Airport.
     */
    private final IDestinationAirport_Passenger iDestinationAirport;
    /**
     * Interface of the Passenger to the reference of the Plane.
     */
    private final IPlane_Passenger iPlane;
    /**
     * Maximum sleeping time, in milliseconds.
     */
    private final int maxSleep;
    /**
     * Passenger identification.
     */
    private final int id;
    
    /**
     * Instantiation of a Passenger thread.
     * 
     * @param pDepartureAirport_passenger interface of the Passenger to the reference of the Departure Airport  
     * @param pDestinationAirport_passenger interface of the Passenger to the reference of the Destination Airport 
     * @param pPlane_passenger interface of the Passenger to the reference of the Plane            
     * @param id passenger Id
     * @param maxSleep maximum sleeping time (ms)
     */
    public AEPassenger(IDepartureAirport_Passenger pDepartureAirport_passenger,
                       IDestinationAirport_Passenger pDestinationAirport_passenger,
                       IPlane_Passenger pPlane_passenger,
                       int id, int maxSleep) {
        super("Passenger " + id);
        this.id = id;
        iDepartureAirport   = pDepartureAirport_passenger;
        iDestinationAirport = pDestinationAirport_passenger;
        iPlane = pPlane_passenger;
        this.maxSleep = maxSleep;
    }
    /**
     * Get passenger ID.
     * @return passenger id
     */
    public int getPassengerID() {
            return id;
    }
    
    /**
     * Get maximum sleeping time.
     * @return maximum sleeping time
     */
    public int getMaxSleep() {
        return maxSleep;
    }
    
    /**
     * Life cycle of the Passenger.
     */
    @Override
    public void run(){
        travelToAirport();
        iDepartureAirport.waitInQueue(id);
        iDepartureAirport.showDocuments(id);
        iPlane.boardThePlane(id);
        iPlane.waitForEndOfFlight(id);
        iDestinationAirport.leaveThePlane(id);
        iPlane.leaveThePlane(id);
    }
    
    /**
     * Passenger traveling to the airport.
     * The thread sleeps for a random period of time, for a maximum o maxSleep time.
     */
    private void travelToAirport() {
        try {
            Thread.sleep((long)(Math.random() * maxSleep));
	} catch (InterruptedException e) {}
    }
    
     public static void main(String[] args) {
        if((args.length % 2) != 0){
            System.out.println("Optional arguments: "
                    + "\n\t-dh <DEPARTURE_AIRPORT_SERVER_HOSTNAME>: Shared Region Departure Airport Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-dp <DEPARTURE_AIRPORT_SERVER_PORT>: Shared Region Departure Airport Server Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-dsh <DESTINATION_AIRPORT_SERVER_HOSTNAME>: Shared Region Destination Airport Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-dsp <DESTINATION_AIRPORT_SERVER_PORT>: Shared Region Destination Airport Server Port (Default = " + Parameters.DESTINATION_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-ph <PLANE_SERVER_HOSTNAME>: Shared Region Plane Server Hostname (Default = \"" + Parameters.SERVER_HOSTNAME + "\")"
                    + "\n\t-pp <PLANE_SERVER_PORT>: Shared Region Plane Server Port (Default = " + Parameters.PLANE_SERVER_PORT + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of Passengers (Default = " + Parameters.NUM_PASSENGER + ")"
                    + "\n\t-s <MAX_SLEEP>: Maximum sleeping time in milliseconds (Default = " + Parameters.MAX_SLEEP + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        String depAirHostname = Parameters.SERVER_HOSTNAME;
        String planeHostname = Parameters.SERVER_HOSTNAME;
        String destAirHostname = Parameters.SERVER_HOSTNAME;
        int depAirPort = Parameters.DEPARTURE_AIRPORT_SERVER_PORT; 
        int planePort = Parameters.PLANE_SERVER_PORT;
        int destAirPort = Parameters.DESTINATION_AIRPORT_SERVER_PORT;
        int numPassengers = Parameters.NUM_PASSENGER;
        int maxSleep = Parameters.MAX_SLEEP;
        try{
            for (int i = 0; i < args.length; i+=2) {
                switch(args[i].toLowerCase()){
                    case "-dh": depAirHostname = args[i+1];
                               break;
                    case "-dp": depAirPort = Integer.valueOf(args[i+1]);
                               break;
                    case "-ph": planeHostname = args[i+1];
                               break;
                    case "-pp": planePort = Integer.valueOf(args[i+1]);
                               break;
                    case "-dsh": destAirHostname = args[i+1];
                               break;
                    case "-dsp": destAirPort = Integer.valueOf(args[i+1]);
                               break;
                    case "-p": numPassengers = Integer.valueOf(args[i+1]);
                               break;
                    case "-s": maxSleep = Integer.valueOf(args[i+1]);
                               break;
                    default: throw new IllegalArgumentException();
                }
            }
        } catch(IllegalArgumentException ex){
            System.out.println("Optional arguments: "
                    + "\n\t-dh <DEPARTURE_AIRPORT_SERVER_HOSTNAME>: Shared Region Departure Airport Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-dp <DEPARTURE_AIRPORT_SERVER_PORT>: Shared Region Departure Airport Server Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-dsh <DESTINATION_AIRPORT_SERVER_HOSTNAME>: Shared Region Destination Airport Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-dsp <DESTINATION_AIRPORT_SERVER_PORT>: Shared Region Destination Airport Server Port (Default = " + Parameters.DESTINATION_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-ph <PLANE_SERVER_HOSTNAME>: Shared Region Plane Server Hostname (Default = \"" + Parameters.SERVER_HOSTNAME + "\")"
                    + "\n\t-pp <PLANE_SERVER_PORT>: Shared Region Plane Server Port (Default = " + Parameters.PLANE_SERVER_PORT + ")"
                    + "\n\t-p <NUM_PASSENGERS>: Number of Passengers (Default = " + Parameters.NUM_PASSENGER + ")"
                    + "\n\t-s <MAX_SLEEP>: Maximum sleeping time in milliseconds (Default = " + Parameters.MAX_SLEEP + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        SRDepartureAirportStub[] srDepartureAirportStub = new SRDepartureAirportStub[numPassengers];
        SRDestinationAirportStub[] srDestinationAirportStub = new SRDestinationAirportStub[numPassengers];
        SRPlaneStub[] srPlaneStub = new SRPlaneStub[numPassengers];
        AEPassenger[] aePassenger = new AEPassenger[numPassengers];
        for (int i = 0; i < numPassengers; i++) {
            srDepartureAirportStub[i] = new SRDepartureAirportStub(depAirHostname, depAirPort);
            srDestinationAirportStub[i] = new SRDestinationAirportStub(destAirHostname, destAirPort);
            srPlaneStub[i] = new SRPlaneStub(planeHostname, planePort);
            aePassenger[i] = new AEPassenger((IDepartureAirport_Passenger)srDepartureAirportStub[i],
                                             (IDestinationAirport_Passenger)srDestinationAirportStub[i],
                                             (IPlane_Passenger)srPlaneStub[i], i, maxSleep);
        }
        System.out.println("Passenger threads started!");
        for (int i = 0; i < numPassengers; i++) 
            aePassenger[i].start();
        try{
            for (int i = 0; i < numPassengers; i++) 
                aePassenger[i].join();
        }catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }
        System.out.println("Passenger threads ended!");
    }
}
