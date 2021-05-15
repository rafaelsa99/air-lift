
package Plane;

import Communication.Message;
import Communication.MessageTypes;

/**
 *
 * @author rafael
 */
public class SRPlaneInterface {
    
    private final SRPlane srPlane;

    public SRPlaneInterface(SRPlane srPlane) {
        this.srPlane = srPlane;
    }
    
    public Message processAndReply(Message inMessage){
        Message outMessage;
        switch(inMessage.getMessageType()){
                case MessageTypes.P_WTFB:
                    srPlane.waitForAllInBoard();
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.P_FDES:
                    srPlane.flyToDestinationPoint();
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.P_ANAR:
                    srPlane.announceArrival();
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.P_FDEP:
                    srPlane.flyToDeparturePoint();
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.P_PATG:
                    srPlane.parkAtTransferGate();
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.H_PRTO:
                    srPlane.informPlaneReadyToTakeOff(inMessage.getiParam1());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.PS_BTP:
                    srPlane.boardThePlane(inMessage.getiParam1());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.PS_EOF:
                    srPlane.waitForEndOfFlight(inMessage.getiParam1());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                case MessageTypes.PS_LTP:
                    srPlane.leaveThePlane(inMessage.getiParam1());
                    outMessage = new Message(MessageTypes.RSP_OK);
                    break;
                default:
                    outMessage = new Message(MessageTypes.RSP_ER);
            }
        return outMessage;
    }
}
