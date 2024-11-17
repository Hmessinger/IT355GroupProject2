import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {

        // A hashmap would reduce the number of for loops
        ArrayList<Book> books = new ArrayList<Book>();
        books.add(new Book(1, "To Kill a Mockingbird", "Harper Lee", 30.0, 10,
                false));
        books.add(new Book(2, "1984", "George Orwell", 15.50, 8, false));
        books.add(new Book(3, "The Great Gatsby", "F. Scott Fitzgerald", 40.0, 5,
                false));
        books.add(new Book(4, "The Catcher in the Rye", "J.D. Salinger", 15.99, 7,
                false));
        books.add(new Book(5, "Moby Dick", "Herman Melville", 12.99, 6, false));
        books.add(new Book(6, "Pride and Prejudice", "Jane Austen", 23.0, 4,
                false));
        books.add(new Book(7, "The Hobbit", "J.R.R. Tolkien", 48.99, 9, false));
        books.add(new Book(8, "War and Peace", "Leo Tolstoy", 9.99, 3, false));
        books.add(new Book(9, "Crime and Punishment", "Fyodor Dostoevsky", 18.0,
                4, false));
        books.add(new Book(10, "The Odyssey", "Homer", 20.0, 7, false));

        // Create an array of books that are in the library
        // Book[] books = {
        // new Book(1, "To Kill a Mockingbird", "Harper Lee", 30.0, 10, false),
        // new Book(2, "1984", "George Orwell", 15.50, 8, false),
        // new Book(3, "The Great Gatsby", "F. Scott Fitzgerald", 40.0, 5, false),
        // new Book(4, "The Catcher in the Rye", "J.D. Salinger", 15.99, 7, false),
        // new Book(5, "Moby Dick", "Herman Melville", 12.99, 6, false),
        // new Book(6, "Pride and Prejudice", "Jane Austen", 23.0, 4, false),
        // new Book(7, "The Hobbit", "J.R.R. Tolkien", 48.99, 9, false),
        // new Book(8, "War and Peace", "Leo Tolstoy", 9.99, 3, false),
        // new Book(9, "Crime and Punishment", "Fyodor Dostoevsky", 18.0, 4, false),
        // new Book(10, "The Odyssey", "Homer", 20.0, 7, false)
        // };

        Scanner scan = new Scanner(System.in);
        String userName;
        String userPassword;

        System.out.println("Welcome to the Library System.");

        /*
         * CWE-20: Improper Input Validation
         * When we ask the user for a username and password we are ensuring that they
         * enter in valid values. We do this by performing input validation. We are
         * ensuring that they aren't inserting an empty username or password, and we are
         * making sure their password has at least characters in it.
         */
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

        // LibraryManagementSystem Constructor
        LibraryManagementSystem lbm = new LibraryManagementSystem();

        System.out.print("\nAccount created successfully!\n");
        System.out.print("\nPlease LOG IN!\n");

        boolean logInStatus = false;
        while (!logInStatus) {
            System.out.println("Username: ");
            String username = scan.nextLine();
            System.out.println("Password: ");
            String password = scan.nextLine();

            logInStatus = account.logIn(username, password);
        }

        System.out.println("\n     Library Menu");
        System.out.println("======================");

        // Loop is working correctly
        int choice = 0;
        while (choice != 9) {
            System.out.println("1: Account Menu");
            System.out.println("2: Reserve a book");
            System.out.println("3: Display Books");
            System.out.println("4: Retrieve a Book");
            System.out.println("5: Display Specific Number of Books");
            System.out.println("6: Checkout a Book");
            System.out.println("7: Return a Book");
            System.out.println("8: Display the books you currently have checked out");
            System.out.println("9: Exit");

            System.out.println("Please input your choice <1-9>");
            choice = scan.nextInt();
            scan.nextLine();
            System.out.println("You selected: " + choice);

            if (choice == 1) {

                int accountChoice = 0;
                while (accountChoice != 3) {
                    System.out.println("\nAccount Menu");
                    System.out.println("----------------------------------");
                    System.out.println("1: Print account details");
                    System.out.println("2: Change Password");
                    System.out.println("3: Back to Menu");
                    accountChoice = scan.nextInt();
                    scan.nextLine();
                    System.out.println("You selected: " + accountChoice);
                    if (accountChoice == 1) {
                        System.out.println("Username: " + account.getUsername());
                        System.out.println("User ID: " + account.getID());
                        System.out.println("Hashed Password: " + account.getHashedPassword());
                        System.out.println("Auth Token: " + account.getAuthToken());
                        System.out.println("Books Checked Out: " + account.getCheckoutBooks());
                    }

                    if (accountChoice == 2) {
                        System.out.println("please enter new password: ");
                        String password = scan.nextLine();
                        account.changePassword(account.getAuthToken(), password);
                    }
                }

            }

            if (choice == 2) {
                System.out.println("Enter the title of the book to reserve: ");
                String reserveTitle = scan.nextLine();
                lbm.reserveBook(account, reserveTitle);
            }

            if (choice == 3) {
                System.out.println("\nAll of the books in our library:");
                lbm.displayCatalog(books);
            }

            if (choice == 4) {
                System.out.print("\nEnter the id of the book you would like to see: ");
                /*
                 * CWE-209: Generation of Error Message Containing Sensitive Information
                 * This try catch block returns an error message if the user does not enter the
                 * correct type of input. The error message does not contain any sensitive
                 * information, rather it displays an informative message that explains what the
                 * user or system did wrong. Also, the loop operation will return a message
                 * stating the book doesn't exist in the system if the id entered cannot be
                 * found.
                 */
                boolean bookFound = false;
                try {
                    int bookIdChoice = scan.nextInt();
                    scan.nextLine();
                    for (Book book : books) {
                        if (book.getId() == bookIdChoice) {
                            System.out.println(book.toString());
                            bookFound = true;
                            break;
                        }
                    }
                    if (!bookFound) {
                        System.out.println("That book ID does not exist in our system.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("The input you entered was not an integer value.");
                    scan.nextLine();
                }

            }

            if (choice == 5) {
                System.out.print("\nEnter how many books you would like to see: ");
                /*
                 * CWE-606: Unchecked Input for Loop Condition
                 * Here we are accepting user input to be used for a loop condition. Before we
                 * use the user input, we are are validating the input to make sure it won't
                 * cause any issues like a buffer overflow or an infinite loop. Once the input
                 * is validated, then we are able to perform the loop operation.
                 */
                try {
                    int numOfBooks = scan.nextInt();
                    scan.nextLine();
                    if (numOfBooks <= books.size()) {
                        for (int i = 0; i < numOfBooks; i++) {
                            System.out.println(books.get(i).toString());
                        }
                    } else {
                        System.out.println(
                                "The number you entered is greater than the number of books that are in the library. There are only "
                                        + books.size() + " books in the library.");
                    }

                } catch (InputMismatchException e) {
                    System.out.println("The input you entered was not an integer value.");
                    scan.nextLine();
                }

            }

            if (choice == 6) {
                System.out.print("\nEnter the ID of the book you would like to checkout: ");
                /*
                 * CWE-248: Uncaught Exception
                 * Here we are allowing a user to enter the ID of a book they would like to
                 * checkout. The application is expecting an integer from the user, but if the
                 * user enters anything other than an integer, the catch block will catch the
                 * exception. We are ensuring that if an exception is thrown it will not go
                 * uncaught.
                 */
                try {
                    int checkoutBookId = scan.nextInt();
                    scan.nextLine();
                    lbm.checkoutBook(account, checkoutBookId, books);
                } catch (InputMismatchException e) {
                    System.out.println("The input you entered was not an integer value.");
                    scan.nextLine();
                }

            }

            if (choice == 7) {
                System.out.print("\nEnter the ID of the book you would like to return: ");
                try {
                    int returnBookId = scan.nextInt();
                    scan.nextLine();
                    if (account.getCheckoutBooks().contains(returnBookId)) {
                        for (Book book : books) {
                            if (book.getId() == returnBookId) {
                                account.removeBookFromCheckedOut(returnBookId);
                                book.setCheckedOutFalse();
                                System.out.println("You have successfully returned '" + book.getTitle() + "'.");
                                break;
                            }
                        }

                    } else {
                        System.out.println("You do not have the book id you entered checked out.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("The input you entered was not an integer value.");
                    scan.nextLine();
                }
            }

            if (choice == 8) {
                ArrayList<Integer> checkedOutList = account.getCheckoutBooks();
                /*
                 * CWE-1071: Empty Code Block
                 * In this section of the code and throughout the entire code base, we do not
                 * have any empty blocks. In situation we have an if else statement and neither
                 * blocks are empty. Empty code blocks can cause some confusion between
                 * developers. They also could signify that there is some missing code
                 * functionality/logic.
                 */
                if (!checkedOutList.isEmpty()) {
                    System.out.println("\nThe books you currently have checked out are: ");
                    for (Book book : books) {
                        if (isCheckedOut(book.getId(), checkedOutList)) {
                            System.out.println(book.simpleBookInfo());
                        }
                    }
                } else {
                    System.out.println("You currently don't have any books checked out.");
                }
            }

            if (choice != 9) {
                System.out.println("\n     Library");
                System.out.println("======================");
            }
        }
        System.out.println("\nYou have exited the library system.");
        scan.close();

    }

    // Helper method to check if a book ID is in the Account's checkedOut array
    public static boolean isCheckedOut(int bookId, ArrayList<Integer> checkedOutIds) {
        for (int id : checkedOutIds) {
            if (bookId == id) {
                return true;
            }
        }
        return false;
    }
}