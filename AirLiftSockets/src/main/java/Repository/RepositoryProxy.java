
package Repository;

import Communication.Message;
import Communication.MessageTypes;
import Communication.ServerCom;

/**
 * General Repository Proxy.
 * Responsible to receive messages from the shared regions and process them.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class RepositoryProxy extends Thread{
    /**
     * General Repository Interface.
     */
    private final RepositoryInterface repository;
    /**
     * Server communications manager.
     */
    private final ServerCom serverCom;
    /**
     * General Repository Proxy instantiation.
     * @param repository General Repository Interface
     * @param port port for the server socket
     */
    public RepositoryProxy(RepositoryInterface repository, int port) {
        this.repository = repository;
        this.serverCom = new ServerCom(port);
    }
    /**
     * General Repository Proxy life cycle.
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
                outMessage = repository.processAndReply(inMessage);
            socket.writeObject(outMessage);
            socket.close();
        } 
    }
}
