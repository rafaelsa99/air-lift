
package Communication;

/**
 * Defines the message types.
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class MessageTypes {
    
    /**
     * Hostess - Wait for next flight.
     */
    public static final int H_WTFL = 0;
    /**
     * Hostess - Prepare for passenger boarding.
     */
    public static final int H_PBRD = 1;
    /**
     * Hostess - Check Documents.
     */
    public static final int H_CHKD = 2;
    /**
     * Hostess - Wait for the next passenger.
     */
    public static final int H_WTPS = 3;
    /**
     * Hostess - Inform plane ready to take off.
     */
    public static final int H_PRTO = 4;
    /**
     * Pilot - Inform plane ready for boarding.
     */
    public static final int P_PRFB = 5;
    /**
     * Pilot - Wait for all passengers in board.
     */
    public static final int P_WTFB = 6;
    /**
     * Pilot - Fly to the destination point.
     */
    public static final int P_FDES = 7;
    /**
     * Pilot - Announce Arrival.
     */
    public static final int P_ANAR = 8;
    /**
     * Pilot - Fly to departure point.
     */
    public static final int P_FDEP = 9;
    /**
     * Pilot - Park at transfer gate.
     */
    public static final int P_PATG = 10;
    /**
     * Passenger - Wait in Queue.
     */
    public static final int PS_WIQ = 11;
    /**
     * Passenger - Show documents.
     */
    public static final int PS_SHD = 12;
    /**
     * Passenger - Board the plane.
     */
    public static final int PS_BTP = 13;
    /**
     * Passenger - Wait for the end of flight.
     */
    public static final int PS_EOF = 14;
    /**
     * Passenger - Leave the plane.
     */
    public static final int PS_LTP = 15;
    /**
     * Response message indicating no problems.
     */
    public static final int RSP_OK = 16;
    /**
     * Response message indicating an error.
     */
    public static final int RSP_ER = 17;
    /**
     * Repository - Set pilot state.
     */
    public static final int R_PIST = 18;
    /**
     * Repository - Set passenger state.
     */
    public static final int R_PSST = 19;
    /**
     * Repository - Print sum up.
     */
    public static final int R_SUMP = 20;
    /**
     * Repository - Set hostess state with 1 parameter.
     */
    public static final int R_HST1 = 21;
    /**
     * Repository - Set hostess state with 2 parameters.
     */
    public static final int R_HST2 = 22;
    /**
     * End of the simulation.
     */
    public static final int END = 23;
    
    /**
     * It can not be instantiated.
     */
    private MessageTypes() {}
}
