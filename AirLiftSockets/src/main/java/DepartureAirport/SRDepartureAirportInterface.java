
package DepartureAirport;

import Communication.Message;
import Communication.MessageTypes;

/**
 * Interface to the shared region of the departure airport.
 * Responsible to process a request and generate the reply message.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class SRDepartureAirportInterface {
    
    /**
     * Shared region of the departure airport.
     */
    private final SRDepartureAirport srDepartureAirport;

    /**
     * Departure Airport interface instantiation.
     * @param srDepartureAirport shared region of the departure airport
     */
    public SRDepartureAirportInterface(SRDepartureAirport srDepartureAirport) {
        this.srDepartureAirport = srDepartureAirport;
    }
    
    /**
     * Process a request and generate the reply message.
     * @param inMessage message received with the request
     * @return the reply message
     */
    public Message processAndReply(Message inMessage){
        Message outMessage;
        switch(inMessage.getMessageType()){
                case MessageTypes.H_CHKD:
                    srDepartureAirport.checkDocuments();
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.H_WTPS:
                    int iParam = srDepartureAirport.waitForNextPassenger();
                    outMessage = new Message(MessageTypes.RSP_OK, iParam);
                    break;
                case MessageTypes.H_WTFL:
                    boolean bParam = srDepartureAirport.waitForNextFlight();
                    outMessage = new Message(MessageTypes.RSP_OK, bParam);
                    break;
                case MessageTypes.H_PBRD:
                    srDepartureAirport.prepareForPassBoarding();
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.PS_WIQ:
                    srDepartureAirport.waitInQueue(inMessage.getiParam1());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.PS_SHD:
                    srDepartureAirport.showDocuments(inMessage.getiParam1());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.P_PRFB:
                    boolean bPrm = srDepartureAirport.informPlaneReadyForBoarding();
                    outMessage = new Message(MessageTypes.RSP_OK, bPrm);
                    break;
                default:
                    outMessage = new Message(MessageTypes.RSP_ER);
            }
        return outMessage;
    }
}
