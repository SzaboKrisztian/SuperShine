import java.io.Serializable;

public class WashType implements Serializable {
  private String name;
  private double price;

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