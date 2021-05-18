
package Plane;

import Communication.Message;
import Communication.MessageTypes;
import Communication.ServerCom;

/**
 *
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class PlaneProxy extends Thread{
    
    private final SRPlaneInterface srPlane;
    private final ServerCom serverCom;

    public PlaneProxy(SRPlaneInterface srPlane, int port) {
        this.srPlane = srPlane;
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
                outMessage = srPlane.processAndReply(inMessage);
            socket.writeObject(outMessage);
            socket.close();
        } 
    }
}
