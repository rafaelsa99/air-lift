/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DestinationAirport;

/**
 *
 * @author jcpbr
 */
public class SRDestinationAirportStub implements IDestinationAirport_Passenger{
    /**
    * Destination Airport server host name.
    */
    private String serverHostName;
    /**
    * Destination Airport server port.
    */
    private int serverPort;

    public SRDestinationAirportStub(String serverHostName, int serverPort) {
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
    }
    
    @Override
    public void leaveThePlane(int passengerID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
