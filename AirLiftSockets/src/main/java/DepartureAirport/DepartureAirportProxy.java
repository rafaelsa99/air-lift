/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DepartureAirport;

import Communication.Message;
import Communication.ServerCom;

/**
 *
 * @author jcpbr
 */
public class DepartureAirportProxy extends Thread{
    
    private final SRDepartureAirportInterface sRDepartureAirport;
    private final ServerCom serverCom;
    
    public DepartureAirportProxy(SRDepartureAirportInterface sRDepartureAirport, int port) {
        this.sRDepartureAirport = sRDepartureAirport;
        this.serverCom = new ServerCom(port);
    }
    
    @Override
    public void run() {
        serverCom.start();
        while(true)
            new ProxyAgent(serverCom.accept()).start();
    }

    class ProxyAgent extends Thread{
        
        private final ServerCom socket;

        public ProxyAgent(ServerCom socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            Message inMessage = (Message)socket.readObject();
            Message outMessage = sRDepartureAirport.processAndReply(inMessage);
            socket.writeObject(outMessage);
            socket.close();
        } 
    }
}
