import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class POS {
    private static WashType[] washTypes;
    private static ArrayList<WashCard> washCards;
    private static WashCard currentWashCard;
    private static Scanner scn = new Scanner(System.in);


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
        washTypes[2] = new WashType("DeLuxe", 120);
        washCards = new ArrayList<WashCard>();
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
                        //washCar();
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
            System.out.printf("Recharge successful.%n");
        } catch (IllegalArgumentException e) {
            System.out.printf("Invalid amount; recharge aborted.%n");
        }
        anyKeyToContinue();
    }

    private static void anyKeyToContinue() {
        System.out.printf("Press any key to continue.");
        scn.nextLine();
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
                washCards = (ArrayList<WashCard>)objectInputStream.readObject();
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
        for (WashCard item: washCards) {
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
                    for (WashCard item: washCards) {
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
            System.out.printf("ID: %d%nBalance: %.2f%nIs admin: %b%n. Are you sure? ", id, balance, isAdmin);
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
            String input = scn.nextLine();
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
            String input = scn.nextLine();
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
        boolean success = false;

        while (!success) {
            String input = scn.nextLine();
            try {
                result = Boolean.parseBoolean(input);
                success = true;
            } catch (NumberFormatException e) {
                System.out.printf("Input cannot be resolved to a boolean. Try again: ");
            }
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
}
