
package DestinationAirport;

import Communication.Message;
import Communication.MessageTypes;

/**
 *
 * @author rafael
 */
public class SRDestinationAirportInterface {
    
    private final SRDestinationAirport srDestinationAirport;

    public SRDestinationAirportInterface(SRDestinationAirport srDestinationAirport) {
        this.srDestinationAirport = srDestinationAirport;
    }
    
    public Message processAndReply(Message inMessage){
        Message outMessage;
        switch(inMessage.getMessageType()){
                case MessageTypes.PS_LTP:
                    srDestinationAirport.leaveThePlane(inMessage.getiParam1());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                default:
                    outMessage = new Message(MessageTypes.RSP_ER);
            }
        return outMessage;
    }
}
