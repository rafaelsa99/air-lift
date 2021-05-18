/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import Communication.Message;
import Communication.MessageTypes;
import Communication.ServerCom;

/**
 *
 * @author rafael
 */
public class RepositoryProxy extends Thread{
    
    private final RepositoryInterface repository;
    private final ServerCom serverCom;

    public RepositoryProxy(RepositoryInterface repository, int port) {
        this.repository = repository;
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
                outMessage = repository.processAndReply(inMessage);
            socket.writeObject(outMessage);
            socket.close();
        } 
    }
}
