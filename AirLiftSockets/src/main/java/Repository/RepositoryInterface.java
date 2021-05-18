
package Repository;

import Communication.Message;
import Communication.MessageTypes;

/**
 * Interface to the shared region of the general repository.
 * Responsible to process a request and generate the reply message.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class RepositoryInterface {
    /**
     * Shared region of the general repository.
     */
    private final Repository repository;
    
    /**
     * General Repository interface instantiation.
     * @param repository shared region of the general repository
     */
    public RepositoryInterface(Repository repository) {
        this.repository = repository;
    }
    /**
     * Process a request and generate the reply message.
     * @param inMessage message received with the request
     * @return the reply message
     */
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
