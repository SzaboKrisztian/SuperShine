import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Provides the record for a single wash event on the system.
 */
public class StatsItem implements Serializable {
  private int washCardID;
  private LocalDateTime time;
  private WashType washType;

  /**
   * Constructor that takes all the needed data for the record
   * @param washCardID The ID of the WashCard that performed the action
   * @param time The moment in time that the action was performed
   * @param washType Reference to a {@link WashType} instance describing the user's choice
   */
  public StatsItem(int washCardID, LocalDateTime time, WashType washType) {
    this.washCardID = washCardID;
    this.time = time;
    this.washType = washType;
  }

  public int getWashCardID() {
    return this.washCardID;
  }

  public LocalDateTime getTime() {
    return this.time;
  }

  public WashType getWashType() {
    return this.washType;
  }
}