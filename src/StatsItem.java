import java.time.LocalDate;
import java.time.LocalTime;

public class StatsItem{
   private int washCardID;
   private LocalTime time;
   private LocalDate date;
   private WashType washType;

   public StatsItem(int washCardID, LocalTime time, LocalDate date, WashType washType){
     this.washCardID = washCardID;
     this.time = time;
     this.date = date;
     this.washType = washType;
   }
 
   public int getWashCardID(){
      return this.washCardID;
   }
   
   public LocalTime getTime(){
      return this.time;
   }
   
   public LocalDate getDate(){
      return this.date;
   }

   public WashType getWashType(){
      return this.washType;
   }
}