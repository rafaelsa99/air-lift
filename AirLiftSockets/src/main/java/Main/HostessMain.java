
package Main;

import ActiveEntity.AEHostess;
import DepartureAirport.IDepartureAirport_Hostess;
import DepartureAirport.SRDepartureAirportStub;
import Plane.IPlane_Hostess;
import Plane.SRPlaneStub;

/**
 *
 * @author rafael
 */
public class HostessMain {
    public static void main(String[] args) {
        if((args.length % 2) != 0){
            System.out.println("Optional arguments: "
                    + "\n\t-dh <DEPARTURE_AIRPORT_SERVER_HOSTNAME>: Shared Region Departure Airport Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-dp <DEPARTURE_AIRPORT_SERVER_PORT>: Shared Region Departure Airport Server Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-ph <PLANE_SERVER_HOSTNAME>: Shared Region Plane Server Hostname (Default = \"" + Parameters.SERVER_HOSTNAME + "\")"
                    + "\n\t-pp <PLANE_SERVER_PORT>: Shared Region Plane Server Port (Default = " + Parameters.PLANE_SERVER_PORT + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        String depAirHostname = Parameters.SERVER_HOSTNAME;
        String planeHostname = Parameters.SERVER_HOSTNAME;
        int depAirPort = Parameters.DEPARTURE_AIRPORT_SERVER_PORT; 
        int planePort = Parameters.PLANE_SERVER_PORT;
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
                    default: throw new IllegalArgumentException();
                }
            }
        } catch(IllegalArgumentException ex){
            System.out.println("Optional arguments: "
                    + "\n\t-dh <DEPARTURE_AIRPORT_SERVER_HOSTNAME>: Shared Region Departure Airport Server Hostname(Default = " + Parameters.SERVER_HOSTNAME + ")"
                    + "\n\t-dp <DEPARTURE_AIRPORT_SERVER_PORT>: Shared Region Departure Airport Server Port (Default = " + Parameters.DEPARTURE_AIRPORT_SERVER_PORT + ")"
                    + "\n\t-ph <PLANE_SERVER_HOSTNAME>: Shared Region Plane Server Hostname (Default = \"" + Parameters.SERVER_HOSTNAME + "\")"
                    + "\n\t-pp <PLANE_SERVER_PORT>: Shared Region Plane Server Port (Default = " + Parameters.PLANE_SERVER_PORT + ")");
            throw new IllegalArgumentException("Invalid Arguments");
        }
        SRDepartureAirportStub srDepartureAirportStub = new SRDepartureAirportStub(depAirHostname, depAirPort);
        SRPlaneStub srPlaneStub = new SRPlaneStub(planeHostname, planePort);
        AEHostess aeHostess = new AEHostess((IDepartureAirport_Hostess)srDepartureAirportStub,
                                  (IPlane_Hostess)srPlaneStub);
        System.out.println("Hostess thread started!");
        aeHostess.start();
        try{
            aeHostess.join();
        }catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }
        System.out.println("Hostess thread ended!");
    }
}
