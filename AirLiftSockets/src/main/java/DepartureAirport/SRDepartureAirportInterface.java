
package DepartureAirport;

import Communication.Message;
import Communication.MessageTypes;

/**
 *
 * @author rafael
 */
public class SRDepartureAirportInterface {
    
    private final SRDepartureAirport srDepartureAirport;

    public SRDepartureAirportInterface(SRDepartureAirport srDepartureAirport) {
        this.srDepartureAirport = srDepartureAirport;
    }
    
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
