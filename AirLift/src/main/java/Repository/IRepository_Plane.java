
package Repository;

import Common.STHostess;
import Common.STPassenger;
import Common.STPilot;

/**
 *
 *  @author Rafael Sá (104552), José Brás (74029)
 */
public interface IRepository_Plane {
    public void setPilotState(STPilot stPilot);
    public void setHostessState(STHostess stHostess);
    public void setPassengerState(STPassenger stPassenger, int passengerID);
}
