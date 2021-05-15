/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import Communication.Message;
import Communication.MessageTypes;

/**
 *
 * @author rafael
 */
public class RepositoryInterface {
    
    private final Repository repository;

    public RepositoryInterface(Repository repository) {
        this.repository = repository;
    }
    
    public Message processAndReply(Message inMessage){
        Message outMessage;
        switch(inMessage.getMessageType()){
                case MessageTypes.R_HST1:
                    repository.setHostessState(inMessage.getiParam1());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.R_HST2:
                    repository.setHostessState(inMessage.getiParam1(), inMessage.getiParam2());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.R_PIST:
                    repository.setPilotState(inMessage.getiParam1());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.R_PSST:
                    repository.setPassengerState(inMessage.getiParam1(), inMessage.getiParam2());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.R_SUMP:
                    repository.printSumUp();
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                default:
                    outMessage = new Message(MessageTypes.RSP_ER);
            }
        return outMessage;
    }
}
