import java.io.Serializable;
import java.util.Calendar;
import java.time.LocalTime;

public class WashCard implements Serializable {
    int id;
    double balance;
    boolean isAdmin;

    //constructor
    public WashCard (int id, double balance, boolean isAdmin) {
        this.id = id;
        this.balance = balance;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public int getId() {
        return this.id;
    }

    public double getBalance() {
        return this.balance;
    }

    public void rechargeWashCard(double amount) {
        if(amount > 0 && amount < 1000) {
            this.balance = amount;
        }
        else {
            throw new IllegalArgumentException("Invalid amount");
        }
    }

    public void charge(double amount) {
        if (amount >= this.balance) {
            this.balance -= amount;
        } else {
            throw new IllegalArgumentException("Not enough funds");
        }
    }

    private boolean checkDiscount(WashType washType) {

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