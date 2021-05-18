
package DestinationAirport;

import Communication.Message;
import Communication.MessageTypes;
import Communication.ServerCom;

/**
 * Destination Airport Proxy.
 * Responsible to receive messages from the active entities and process them.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class DestinationAirportProxy extends Thread{
    
    /**
     * Destination Airport Interface.
     */
    private final SRDestinationAirportInterface sRDestinationAirport;
    /**
     * Server communications manager.
     */
    private final ServerCom serverCom;

    /**
     * Destination Airport Proxy instantiation.
     * @param sRDestinationAirport Destination Airport Interface
     * @param port port for the server socket
     */
    public DestinationAirportProxy(SRDestinationAirportInterface sRDestinationAirport, int port) {
        this.sRDestinationAirport = sRDestinationAirport;
        this.serverCom = new ServerCom(port);
    }
    
    /**
     * Destination Airport Proxy life cycle.
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
                outMessage = sRDestinationAirport.processAndReply(inMessage);
            socket.writeObject(outMessage);
            socket.close();
        } 
    }
}
