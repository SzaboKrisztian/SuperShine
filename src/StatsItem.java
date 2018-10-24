import java.io.Serializable;
import java.time.LocalDateTime;

public class StatsItem implements Serializable {
   private int washCardID;
   private LocalDateTime time;
   private WashType washType;

   public StatsItem(int washCardID, LocalDateTime time, WashType washType){
     this.washCardID = washCardID;
     this.time = time;
     this.washType = washType;
   }
 
   public int getWashCardID(){
      return this.washCardID;
   }
   
   public LocalDateTime getTime(){
      return this.time;
   }

   public WashType getWashType(){
      return this.washType;
   }
}