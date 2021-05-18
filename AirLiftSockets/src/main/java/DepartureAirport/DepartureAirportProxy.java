
package DepartureAirport;

import Communication.Message;
import Communication.MessageTypes;
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
                outMessage = sRDepartureAirport.processAndReply(inMessage);
            socket.writeObject(outMessage);
            socket.close();
        } 
    }
}
