
package DestinationAirport;

import Communication.Message;
import Communication.MessageTypes;
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
        ServerCom socket;
        while((socket = serverCom.accept()) != null)
            new ProxyAgent(socket).start();
    }

    class ProxyAgent extends Thread{
        
        private final ServerCom socket;

        public ProxyAgent(ServerCom socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            Message inMessage = (Message)socket.readObject();
            Message outMessage;
            if(inMessage.getMessageType() == MessageTypes.END){
                serverCom.end();
                outMessage = new Message(MessageTypes.RSP_OK);
            } else
                outMessage = sRDestinationAirport.processAndReply(inMessage);
            socket.writeObject(outMessage);
            socket.close();
        } 
    }
}
