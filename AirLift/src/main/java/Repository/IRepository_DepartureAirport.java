
package Repository;

/**
 *
 *  @author Rafael Sá (104552), José Brás (74029)
 */
public interface IRepository_DepartureAirport {
    public void setPilotState(int stPilot);
    public void setHostessState(int stHostess);
    public void setHostessState(int stHostess, int passengerID);
    public void setPassengerState(int stPassenger, int passengerID);
    public void printSumUp();
}
