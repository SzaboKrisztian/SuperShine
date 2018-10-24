import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class POS {
  private static WashType[] washTypes;
  private static ArrayList<WashCard> washCards;
  private static WashCard currentWashCard;
  private static final Scanner SCN = new Scanner(System.in);


  public static void main(String[] args) {
    initSystem();

    while (true) {
      displayMenu();
    }
  }

  private static void initSystem() {
    washTypes = new WashType[3];
    washTypes[0] = new WashType("Economy", 50);
    washTypes[1] = new WashType("Standard", 80);
    washTypes[2] = new WashType("De Luxe", 120);
    washCards = new ArrayList<>();
    readWashCards();
  }

  private static void displayMenu() {
    if (currentWashCard == null) {
      clearScreen();
      displayHeader();
      System.out.printf("Insert your WashCard to continue: ");
      int washCardID = readInt();
      validateWashCard(washCardID);
    } else {
      if (!currentWashCard.isAdmin()) {
        clearScreen();
        displayHeader();
        System.out.printf("Pick a function to continue: %n");
        System.out.printf("----------------------------%n");
        System.out.printf("1. Wash car%n");
        System.out.printf("2. Check WashCard balance%n");
        System.out.printf("3. Recharge WashCard%n");
        System.out.printf("4. Log out%n%nChoice: ");
        int choice = readInt();
        switch (choice) {
          case 1:
            washCar();
            break;
          case 2:
            checkBalance();
            break;
          case 3:
            rechargeWashCard();
            break;
          case 4:
            currentWashCard = null;
            break;
        }
      } else {
        clearScreen();
        displayHeader();
        System.out.printf("Pick a function to continue: %n");
        System.out.printf("----------------------------%n");
        System.out.printf("1. See system statistics%n");
        System.out.printf("2. Add WashCard(s) to the system%n");
        System.out.printf("3. Log out%n%nChoice: ");
        int choice = readInt();
        switch (choice) {
          case 1:
            //displaySystemStats();
            break;
          case 2:
            kbdInputWashCards();
            break;
          case 3:
            currentWashCard = null;
            break;
        }
      }
    }
  }

  private static void rechargeWashCard() {
    clearScreen();
    displayHeader();
    System.out.printf("Input the amount: ");
    double rechargeAmount = readDouble();
    try {
      currentWashCard.rechargeWashCard(rechargeAmount);
      saveWashCards();
      System.out.printf("Successfully recharged with %.2f kr.%n", rechargeAmount);
    } catch (IllegalArgumentException e) {
      System.out.printf("Invalid amount; recharge aborted.%n");
    }
    anyKeyToContinue();
  }

  private static void anyKeyToContinue() {
    System.out.printf("Press any key to continue.");
    SCN.nextLine();
  }

  private static void checkBalance() {
    clearScreen();
    displayHeader();
    System.out.printf("Your current balance is: %.2f%n%n", currentWashCard.getBalance());
    anyKeyToContinue();
  }

  private static void clearScreen() {
    for (int i = 0; i < 40; i++) {
      System.out.printf("%n");
    }
  }

  private static void displayHeader() {
    System.out.printf("--------------------------------------------------%n");
    System.out.printf("    Welcome To The SuperShine Automated System%n");
    System.out.printf("--------------------------------------------------%n%n%n");
  }

  private static void readWashCards() {
    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream("SuperShine.dat");
    } catch (FileNotFoundException e) {
      kbdInputWashCards();
    }
    if (fileInputStream != null) {
      System.out.printf("File opened successfully.");
      try {
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        washCards = (ArrayList<WashCard>) objectInputStream.readObject();
        objectInputStream.close();
        System.out.printf("Entries loaded: %d", washCards.size());
      } catch (IOException e) {
        // Very small chances of this happening; we won't handle this.
        System.out.printf("IO Exception: %s", e.toString());
      } catch (ClassNotFoundException e) {
        // Same-same
        System.out.printf("Class not found: %s", e.toString());
      }
    }
  }

  private static void validateWashCard(int washCardID) {
    WashCard found = null;
    for (WashCard item : washCards) {
      if (item.getId() == washCardID) {
        found = item;
        break;
      }
    }
    if (found != null) {
      currentWashCard = found;
    } else {
      System.out.printf("Invalid WashCard; try again.");
    }

  }

  private static void kbdInputWashCards() {
    int id;
    double balance;
    boolean isAdmin;

    while (true) {
      // Read the data from the keyboard
      while (true) {
        System.out.printf("ID: ");
        id = readInt();
        if (!washCards.isEmpty()) {
          boolean found = false;
          for (WashCard item : washCards) {
            if (item.getId() == id) {
              found = true;
              System.out.printf("Invalid WashCard. Try again: ");
              break;
            }
          }
          if (found) {
            continue;
          } else {
            break;
          }
        }
        break;
      }
      System.out.printf("Balance: ");
      balance = readDouble();
      System.out.printf("Is it an admin card?: ");
      isAdmin = readBoolean();

      // Get confirmation if read data is to be added to the system
      System.out.printf("Do you want to add the WashCard with the following data: %n");
      System.out.printf("ID: %d%nBalance: %.2f%nIs admin: %b%n. Are you sure? ",
          id, balance, isAdmin);
      if (readBoolean()) {
        washCards.add(new WashCard(id, balance, isAdmin));
        saveWashCards();
        System.out.printf("WashCard added to the system.%n");
      } else {
        System.out.printf("WashCard not added; inputted data has been discarded.%n");
      }

      // Ask if the user would add another WashCard
      System.out.printf("Do you want to add another WashCard? ");
      if (!readBoolean()) {
        break;
      }
    }

  }

  private static int readInt() {
    int result = 0;
    boolean success = false;

    while (!success) {
      String input = SCN.nextLine();
      try {
        result = Integer.parseInt(input);
        success = true;
      } catch (NumberFormatException e) {
        System.out.printf("Input cannot be resolved to an integer. Try again: ");
      }
    }
    return result;
  }

  private static double readDouble() {
    double result = 0;
    boolean success = false;

    while (!success) {
      String input = SCN.nextLine();
      try {
        result = Double.parseDouble(input);
        success = true;
      } catch (NumberFormatException e) {
        System.out.printf("Input cannot be resolved to a double. Try again: ");
      }
    }
    return result;
  }

  private static boolean readBoolean() {
    boolean result = false;

    String input = SCN.nextLine();
    switch (input) {
      case "y":
      case "yes":
      case "true":
      case "t":
        result = true;
      default:
        break;
    }
    return result;
  }

  private static void saveWashCards() {
    FileOutputStream fileOutputStream = null;
    ObjectOutputStream objectOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream("SuperShine.dat");
      try {
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(washCards);
        objectOutputStream.flush();
        objectOutputStream.close();
      } catch (IOException e) {
        // some chances that this might happen; we don't handle this in our case;
      }
    } catch (FileNotFoundException e) {
      // can't really happen
    }

  }

  private static void washCar() {
    clearScreen();
    displayHeader();
    System.out.printf("Choose the wash type:%n%n");
    for (int i = 0; i < washTypes.length; i++) {
      System.out.printf("%d. %s - %.2f kr.%n", i + 1, washTypes[i].getName(),
          washTypes[i].getPrice());
    }

    int option = -1;
    do {
      System.out.printf("Enter a choice between 1 - %d: ", washTypes.length);
      option = readInt() - 1;
    } while (option < 0 || option >= washTypes.length);
    WashType chosenWashType = washTypes[option];
    System.out.printf("%n%nYou have chosen a %s wash. ", chosenWashType.getName());
    double amountToCharge = chosenWashType.getPrice();
    if (checkDiscount(chosenWashType)) {
      System.out.printf("It is eligible for the early bird 20%% discount! ");
      amountToCharge *= 0.8;
    }
    System.out.printf("%.2f kr. will be charged to your WashCard.%n",
        amountToCharge);

    System.out.printf("Are you sure you wish to proceed? ");
    if (readBoolean()) {
      System.out.printf("Wash started.%n");
      currentWashCard.charge(amountToCharge);
      // Add action to statistics
    } else {
      System.out.printf("Wash aborted.%n");
    }
  }

  private static boolean checkDiscount(WashType washType) {

    //checks if it is a week day
    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    boolean weekDay = (day >= 2 && day <= 6);

    //checks if time is before 2pm
    LocalTime target = LocalTime.parse("14:00:00.000");
    LocalTime currTime = LocalTime.now();
    boolean time = (currTime.isBefore(target));

    //checks if its the correct discount type
    String wash = "De Luxe";
    boolean type = (washType.getName().equals(wash));

    if (weekDay && time && !type) {
      return true;
    } else {
      return false;
    }
  }
}