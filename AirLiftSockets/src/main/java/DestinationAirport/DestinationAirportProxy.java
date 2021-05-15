
package DestinationAirport;

import Communication.Message;
import Communication.ServerCom;

/**
 *
 * @author jcpbr
 */
public class DestinationAirportProxy extends Thread{
    
    private final SRDestinationAirportInterface sRDestinationAirport;
    private final ServerCom serverCom;

    public DestinationAirportProxy(SRDestinationAirportInterface sRDestinationAirport, int port) {
        this.sRDestinationAirport = sRDestinationAirport;
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
            Message outMessage = sRDestinationAirport.processAndReply(inMessage);
            socket.writeObject(outMessage);
            socket.close();
        } 
    }
}
