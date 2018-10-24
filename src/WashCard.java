import java.io.Serializable;

public class WashCard implements Serializable {
  int id;
  double balance;
  boolean isAdmin;

  //constructor
  public WashCard(int id, double balance, boolean isAdmin) {
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
    if (amount > 0 && amount < 1000) {
      this.balance += amount;
    } else {
      throw new IllegalArgumentException("Invalid amount");
    }
  }

  public void charge(double amount) {
    if (amount <= this.balance) {
      this.balance -= amount;
    } else {
      throw new IllegalArgumentException("Not enough funds");
    }
  }


}