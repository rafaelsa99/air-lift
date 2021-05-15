
package Plane;

import Communication.Message;
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
            Message outMessage = srPlane.processAndReply(inMessage);
            socket.writeObject(outMessage);
            socket.close();
        } 
    }
}
