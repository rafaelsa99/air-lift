
package DepartureAirport;

import Communication.Message;
import Communication.MessageTypes;
import Communication.ServerCom;

/**
 * Departure Airport Proxy.
 * Responsible to receive messages from the active entities and process them.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class DepartureAirportProxy extends Thread{
    
    /**
     * Departure Airport Interface.
     */
    private final SRDepartureAirportInterface sRDepartureAirport;
    /**
     * Server communications manager.
     */
    private final ServerCom serverCom;
    
    /**
     * Departure Airport Proxy instantiation.
     * @param sRDepartureAirport Departure Airport Interface
     * @param port port for the server socket
     */
    public DepartureAirportProxy(SRDepartureAirportInterface sRDepartureAirport, int port) {
        this.sRDepartureAirport = sRDepartureAirport;
        this.serverCom = new ServerCom(port);
    }
    
    /**
     * Departure Airport Proxy life cycle.
     */
    @Override
    public void run() {
        serverCom.start();
        ServerCom socket;
        while((socket = serverCom.accept()) != null)
            new ProxyAgent(socket).start();
    }

    /**
     * Proxy Agent for processing a given message and reply to the client.
     */
    class ProxyAgent extends Thread{
        
        /**
         * Socket connected to the client.
         */
        private final ServerCom socket;

        /**
         * Proxy agent instantiation.
         * @param socket socket connected to the client
         */
        public ProxyAgent(ServerCom socket) {
            this.socket = socket;
        }

        /**
         * Proxy agent life cycle.
         */
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
