
package Repository;

import Common.STHostess;
import Common.STPassenger;
import Common.STPilot;

/**
 *
 *  @author Rafael Sá (104552), José Brás (74029)
 */
public interface IRepository_DepartureAirport {
    public void setPilotState(STPilot stPilot);
    public void setHostessState(STHostess stHostess);
    public void setHostessState(STHostess stHostess, int passengerID);
    public void setPassengerState(STPassenger stPassenger, int passengerID);
    public void printSumUp();
}
