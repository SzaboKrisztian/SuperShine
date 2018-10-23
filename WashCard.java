import java.util.Calendar;
import java.time.LocalTime;

public class WashCard {

    int id;
    double balance;
    boolean isAdmin;

    //constructor
    public WashCard (int id, double balance, boolean isAdmin) {
        this.id = id;
        this.balance = balance;
        this.isAdmin = isAdmin;
    }

    //getters
    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    //setters
    public void setId(int id) {
        this.id=id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    //recharge wash card
    public void rechargeWashCard(WashCard washCard,double amount) {
        if(amount<1000 && amount>0) {
            washCard.setBalance(amount);
        }
        else {
            throw new IllegalArgumentException("Wrong sum");
        }
    }

    public void deductAmount (WashCard washCard, WashType washType) {

        double currBalance = 0;

        if(checkDiscount(washType)) {
            currBalance = washCard.getBalance() - washType.getPrice()*0.8;
        }

        else {
            currBalance = washCard.getBalance() - washType.getPrice();

        }
        washCard.setBalance(currBalance);
    }

    private boolean checkDiscount (WashType washType) {

        //checks if it is a week day
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        boolean weekDay = (day>=2 && day<=6);

        //checks if time is before 2pm
        LocalTime target = LocalTime.parse( "14:00:00.000" );
        LocalTime currTime = LocalTime.now();
        boolean time = ( currTime.isBefore( target ) ) ;

        //checks if its the correct discount type
        String wash = "De Luxe";
        boolean type = (washType.getName().equals(wash));

        if(weekDay && time && !type) {
            return true;
        }
        else return false;
    }

}