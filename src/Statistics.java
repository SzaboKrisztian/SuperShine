import java.util.ArrayList;
import java.lang.Math;

public class Statistics{
   
   private ArrayList<StatsItem> actionLog = new ArrayList<>();
   
   /* Print Stats */
   public void printStats(){
      System.out.println("*Statistics*");
      System.out.println("The amount of each Wash Type used: ");
      System.out.println("- Economy: " + eachWashTypeStat(washTypeCount.get(0)));
      System.out.println("- Standard: " + eachWashTypeStat(washTypeCount.get(1)));
      System.out.println("- De Luxe: " + eachWashTypeStat(washTypeCount.get(2)));
      System.out.println("The most used wash type: " + washTypeStat());
      System.out.println("How many times customers got discounts: " + countDiscounts());
   }
   
   /* Stats */
    
   public ArrayList <Integer> eachWashTypeStat(){
   
   int ecoCount = 0;
   int stanCount = 0;
   int delCount = 0;

      for(StatsItem item : actionLog){
         if(item.getWashType().getName().equals("Economy")) {
            ecoCount ++;
         } else if(item.getWashType().getName().equals("Standard")) {
            stanCount ++;
         } else if(item.getWashType().getName().equals("De Luxe")) {
            delCount++;
         }
      }
      ArrayList <Integer> washTypeCount = new ArrayList<>(3);
      washTypeCount.add(0, ecoCount);
      washTypeCount.add(1, stanCount);
      washTypeCount.add(2, delCount);  
         
      return washTypeCount;
   }
   
   public String washTypeStat(){
      
      int maxNum = Math.max(ecoCount, Math.max(stanCount, delCount));
      
      switch(maxNum){
         case ecoCount: 
            return "Economy";
            break;
         case stanCount:
            return "Standard";
            break;
         case delCount:
            return "De Luxe";
            break;
      }
     
   }
   
   public void totalPrice(){
      ArrayList <Integer> washTypePrice = eachWashTypeStat();
         
         washTypePrice.add(0, washTypeStat.get(0) * 50);
         washTypePrice.add(1, washTypeStat.get(1) * 80);
         washTypePrice.add(2, washTypeStat.get(2) * 120);
         washTypePrice.add(3, washTypePrice.get(0) + washTypePrice.get(1) + washTypePrice.get(2));
     
      System.out.println("Total price customers spent: " + washTypePrice.get(3)); 
   }
   
}