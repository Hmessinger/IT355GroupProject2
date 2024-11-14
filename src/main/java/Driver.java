import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String userName;
        String userPassword;

        System.out.println("Welcome to the Library System.");

        // Asks for users name
        while (true) {
            System.out.println("Please input your username: ");
            userName = scan.nextLine();
            if (userName != null && !userName.isEmpty()) {
                break;
            } else {
                System.out.println("Invalid input. Please enter a name that is not empty.");
            }
        }

        // Ask for the account password, it will loop until a valid password is entered
        while (true) {
            System.out.println("\nPlease input the account password: ");
            userPassword = scan.nextLine();
            if (userPassword != null && !userPassword.isEmpty()) {
                if (userPassword.codePointCount(0, userPassword.length()) < 8) {
                    System.out.println("Password must contain at least 8 characters.");
                } else {
                    break;
                }
            } else {
                System.out.println("Invalid input. Please enter a password that is not empty.");
            }
        }

        // Account Constructor
        Account account = new Account(userName, userPassword);

        System.out.print("\nAccount created successfully!\n");

        System.out.println("\n     Library Menu");
        System.out.println("======================");

        // Loop is working correctly
        int choice = 0;
        while (choice != 9) {
            System.out.println("1: Deposit");
            System.out.println("2: Withdraw");
            System.out.println("3: Display Account");
            System.out.println("4: Manually Change Password");
            System.out.println("5: Generate Random Password");
            System.out.println("6: Transaction History");
            System.out.println("7: Currency Conversion");
            System.out.println("8: Transfer Funds");
            System.out.println("9: Exit");

            System.out.println("Please input your choice <1-9>");
            choice = scan.nextInt();
            scan.nextLine();
            System.out.println("You selected: " + choice);

            if (choice == 1) {

            }

            if (choice == 2) {

            }

            if (choice == 3) {

            }

            if (choice == 4) {

            }

            if (choice == 5) {

            }

            if (choice == 6) {

            }

            if (choice == 7) {

            }

            if (choice == 8) {

            }

            if (choice != 9) {
                System.out.println("\n     Bank Menu");
                System.out.println("======================");
            }
        }
        System.out.println("\nYou have exited the library system.");
        scan.close();
    }

}
