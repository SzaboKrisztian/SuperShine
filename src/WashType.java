import java.io.Serializable;

/**
 * Provides the details of a particular wash type available.
 */
public class WashType implements Serializable {
  private String name;
  private double price;

  /**
   * Constructor that takes all the needed info
   * @param name A string representation of the wash type's name.
   * @param price The standard price for this wash type
   */
  public WashType(String name, double price) {
    this.name = name;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public double getPrice() {
    return price;
  }
}