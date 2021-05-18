
package DestinationAirport;

import Communication.Message;
import Communication.MessageTypes;

/**
 * Interface to the shared region of the destination airport.
 * Responsible to process a request and generate the reply message.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRDestinationAirportInterface {
    
    /**
     * Shared region of the destination airport.
     */
    private final SRDestinationAirport srDestinationAirport;

    /**
     * Destination Airport interface instantiation.
     * @param srDestinationAirport shared region of the destination airport
     */
    public SRDestinationAirportInterface(SRDestinationAirport srDestinationAirport) {
        this.srDestinationAirport = srDestinationAirport;
    }
    
    /**
     * Process a request and generate the reply message.
     * @param inMessage message received with the request
     * @return the reply message
     */
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
