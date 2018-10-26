import java.io.Serializable;

/**
 * Provides a record of the id, balance and permission level of
 * a single WashCard within the SuperShine system.
 */
public class WashCard implements Serializable {
  int id;
  double balance;
  boolean isAdmin;

  /**
   * The only constructor, the one that takes all the required data fields.
   * @param id a unique identifier for the WashCard on the system
   * @param balance the WashCard's balance
   * @param isAdmin whether the WashCard is registered as having admin privilege
   */
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

  /**
   * Recharge a WashCard with a certain amount, by the way of a credit card transfer
   * @param amount The amount to be transferred to the WashCard's balance. Must be
   *               between 50 and 1000.
   * @throws IllegalArgumentException If the amount is outside the defined bounds.
   */
  public void rechargeWashCard(double amount) {
    if (amount >= 50 && amount <= 1000) {
      this.balance += amount;
    } else {
      throw new IllegalArgumentException("Invalid amount");
    }
  }

  /**
   * Method to charge a certain amount to the WashCard.
   * @param amount The amount that is to be charged to the WashCard
   * @throws IllegalArgumentException if the WashCard doesn't have enough balance
   */
  public void charge(double amount) {
    if (amount <= this.balance) {
      this.balance -= amount;
    } else {
      throw new IllegalArgumentException("Not enough funds");
    }
  }
}