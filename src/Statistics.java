import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.lang.Math;
import java.util.HashMap;

public class Statistics {
  private ArrayList<StatsItem> actionLog = new ArrayList<>();
  private WashType[] washTypes;

  public Statistics(WashType[] washTypes) {
    this.washTypes = washTypes;
    this.reloadData();
  }

  public void add(StatsItem item) {
    actionLog.add(item);
    this.saveData();
  }

  private void reloadData() {
    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream("SuperShineStats.dat");
    } catch (FileNotFoundException e) {
      // No data found, just initialize a new ArrayList
      this.actionLog = new ArrayList<>();
    }
    if (fileInputStream != null) {
      try {
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        this.actionLog = (ArrayList<StatsItem>) objectInputStream.readObject();
        objectInputStream.close();
      } catch (IOException e) {
        System.out.printf("%s", e);
        // Very small chances of this happening; we won't handle this.
      } catch (ClassNotFoundException e) {
        System.out.printf("%s", e);
        // Same-same
      }
    }
  }

  private void saveData() {
    FileOutputStream fileOutputStream = null;
    ObjectOutputStream objectOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream("SuperShineStats.dat");
      try {
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this.actionLog);
        objectOutputStream.flush();
        objectOutputStream.close();
      } catch (IOException e) {
        // some chances that this might happen; we don't handle this in our case;
      }
    } catch (FileNotFoundException e) {
      // can't really happen
    }
  }

  private class StatisticsCounts {
    private HashMap<String, Integer> countPerWashType;
    private HashMap<String, Integer> countDiscountedPerWashType;
    private HashMap<String, Double> incomePerWashType;
    private int countUniqueCustomers;

    public StatisticsCounts(ArrayList<StatsItem> data,
                            LocalDateTime lowerBoundary, LocalDateTime upperBoundary) {
      countPerWashType = new HashMap<>();
      countDiscountedPerWashType = new HashMap<>();
      incomePerWashType = new HashMap<>();

      // Init all the HashMaps based on the already known WashTypes
      for (WashType item: washTypes) {
        countPerWashType.put(item.getName(), 0);
        countDiscountedPerWashType.put(item.getName(), 0);
        incomePerWashType.put(item.getName(), 0.0);
      }

      ArrayList<Integer> uniqueCustomers = new ArrayList<>();
      for (StatsItem item: data) {
        if (item.getTime().isAfter(lowerBoundary) && item.getTime().isBefore(upperBoundary)) {
          // Count all the unique customer IDs found in the data
          if (!uniqueCustomers.contains(item.getWashCardID())) {
            uniqueCustomers.add(item.getWashCardID());
          }

          // Count how many times each of the different wash types were purchased.
          String washName = item.getWashType().getName();
          countPerWashType.replace(washName, countPerWashType.get(washName) + 1);

          // Count how many times purchases got discounted, per wash type
          boolean wasDiscounted = POS.checkDiscountEligibility(item.getWashType(), item.getTime());
          if (wasDiscounted) {
            countDiscountedPerWashType.replace(washName,
                countDiscountedPerWashType.get(washName) + 1);
          }

          // Count the income totals per wash type
          double price = item.getWashType().getPrice();
          if (wasDiscounted) {
            price *= 0.8;
          }
          incomePerWashType.replace(washName, incomePerWashType.get(washName) + price);
        }
      }
      this.countUniqueCustomers = uniqueCustomers.size();
    }

    public HashMap<String, Integer> getCountPerWashType() {
      return this.countPerWashType;
    }

    public HashMap<String, Integer> getCountDiscountedPerWashType() {
      return this.countDiscountedPerWashType;
    }

    public HashMap<String, Double> getIncomePerWashType() {
      return this.incomePerWashType;
    }

    public int getCountUniqueCustomers() {
      return this.countUniqueCustomers;
    }
  }

  public void printStats(LocalDateTime lowerBoundary, LocalDateTime upperBoundary) {
    StatisticsCounts stats = new StatisticsCounts(actionLog, lowerBoundary, upperBoundary);
    System.out.printf("%n%nStatistics%n--------------------%n%n");
    String[] washTypeNames = new String[washTypes.length];
    for (int i = 0; i < washTypes.length; i++) {
      washTypeNames[i] = washTypes[i].getName();
    }
    for (String name: washTypeNames) {
      System.out.printf("- %s: Total: %d Discounted: %d Income: %.2f kr.%n", name,
          stats.getCountPerWashType().get(name),
          stats.getCountDiscountedPerWashType().get(name),
          stats.getIncomePerWashType().get(name));
    }

    int numPurchases = 0;
    for (int count: stats.getCountPerWashType().values()) {
      numPurchases += count;
    }

    System.out.printf("Total number of purchases: %d%n", numPurchases);

    int numDiscounts = 0;
    for (int count: stats.getCountDiscountedPerWashType().values()) {
      numDiscounts += count;
    }
    System.out.printf("Total number of discounts: %d%n", numDiscounts);

    double totalIncome = 0;
    for (double income: stats.getIncomePerWashType().values()) {
      totalIncome += income;
    }
    System.out.printf("Total income: %.2f kr.%n", totalIncome);

    System.out.printf("Number of unique customers: %d%n", stats.getCountUniqueCustomers());
  }

  /*
  public ArrayList<Integer> eachWashTypeStat() {

    int ecoCount = 0;
    int stanCount = 0;
    int delCount = 0;

    for (StatsItem item : actionLog) {
      if (item.getWashType().getName().equals("Economy")) {
        ecoCount++;
      } else if (item.getWashType().getName().equals("Standard")) {
        stanCount++;
      } else if (item.getWashType().getName().equals("De Luxe")) {
        delCount++;
      }
    }
    ArrayList<Integer> washTypeCount = new ArrayList<>(3);
    washTypeCount.add(0, ecoCount);
    washTypeCount.add(1, stanCount);
    washTypeCount.add(2, delCount);

    return washTypeCount;
  }

  public String washTypeStat() {

    int maxNum = Math.max(ecoCount, Math.max(stanCount, delCount));

    switch (maxNum) {
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

  public void totalPrice() {
    ArrayList<Integer> washTypePrice = eachWashTypeStat();

    washTypePrice.add(0, washTypeStat.get(0) * 50);
    washTypePrice.add(1, washTypeStat.get(1) * 80);
    washTypePrice.add(2, washTypeStat.get(2) * 120);
    washTypePrice.add(3, washTypePrice.get(0) + washTypePrice.get(1) + washTypePrice.get(2));

    System.out.println("Total price customers spent: " + washTypePrice.get(3));
  }
  */
}