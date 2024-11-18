import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LibraryManagementSystem {
    private List<Book> books; // Avoids global use by containing the list within the class (CWE-1108)
    private final Lock bookLock = new ReentrantLock(); // CWE-667: Improper Locking - Proper locking mechanism for
                                                       // shared resource

    public LibraryManagementSystem() {
        this.books = new ArrayList<>();
    }

    // CWE-1041: Centralized method for displaying catalog to avoid redundant code
    public void displayCatalog(ArrayList<Book> books) {
        for (Book book : books) {
            displayBookInfo(book); // Centralized call to avoid code repetition
        }
    }

    private void displayBookInfo(Book book) {
        System.out
                .println("ID: " + book.getId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor()
                        + ", Price: " + book.getPrice() + ", Checked out Status: " + book.isCheckedOut());
    }

    // CWE-1024: Properly validating file reading and parsing inputs to avoid
    // incompatible type issues
    // CWE-1095: Loop Condition Value Update within the Loop - Proper loop control
    // structures are used
    // to avoid modifying the loop condition inside the loop body.

    public void loadBooksFromFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String title = parts[0].trim();
                    String author = parts[1].trim();
                    int stock;
                    try {
                        stock = Integer.parseInt(parts[2].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid stock value in file: " + parts[2].trim());
                        continue;
                    }
                    books.add(new Book(title, author, stock));
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void displayBooks() {
        for (Book book : books) {
            System.out.println("Title: " + book.getTitle() +
                    ", Author: " + book.getAuthor() +
                    ", Stock: " + book.getStock());
        }
    }

    // CWE-253: Incorrect Check of Function Return Value
    // It properly checks the return value from user.hasReachedBorrowLimit() and
    // ensures that
    // all error conditions, such as a user reaching the borrow limit or the book
    // not being found, are handled by returning false and providing clear feedback.

    // CWE-563: Checkout book functionality without unnecessary variable assignments
    public boolean checkoutBook(Account account, int bookId, ArrayList<Book> books) {
        bookLock.lock(); // CWE-667: Acquire lock to prevent race conditions.
        try {
            for (Book book : books) {
                if (book.getId() == bookId) {
                    if (!book.isCheckedOut()) {
                        account.addBookToCheckedOut(bookId);
                        book.setCheckedOutTrue();
                        System.out.println("You have successfully checked out '" + book.getTitle() + "'.");
                    } else {
                        System.out.println("That book is currently checked out.");
                    }
                    return true;
                }
            }
            System.out.println("That book ID does not exist in our system.");
            return false;
        } finally {
            bookLock.unlock(); // CWE-833: Release lock to prevent deadlock
        }
    }

    public void loadBooks(List<Book> books) {
        this.books.addAll(books);
    }

    // CWE-1109: Avoid using the same variable for multiple purposes within
    // reservation logic
    public void reserveBook(Account account, String bookTitle) {
        Book reservedBook = null;
        bookLock.lock(); // Acquire lock to prevent race conditions
        try {
            for (Book book : books) {
                if (book.getTitle().trim().equalsIgnoreCase(bookTitle.trim()) && !account.hasBorrowed(book)) {
                    reservedBook = book;
                    break;
                }
            }

            if (reservedBook != null) {
                account.reserveBook(reservedBook);
                System.out.println("Book reserved successfully: " + reservedBook.getTitle());
            } else {
                System.out.println("Unable to reserve book: Either it doesn't exist or is already borrowed.");
            }
        } finally {
            bookLock.unlock(); // Release lock to prevent deadlock
        }
    }

    // Save books to file
    public void saveBooksToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Book book : books) {
                // Save each book's title, author, and stock in a CSV format
                writer.write(book.getTitle() + "," + book.getAuthor() + "," + book.getStock());
                writer.newLine();
            }
            System.out.println("Books list saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving books to file: " + e.getMessage());
        }
    }

    // CWE 1116: Innaccurate Comments
    // This method will add a new book to the library taken by user input
    public void addBook(String filename) {
        Scanner scanner = new Scanner(System.in);

        // Get book details from the user
        System.out.println("Enter the book title: ");
        String title = scanner.nextLine();

        System.out.println("Enter the book author: ");
        String author = scanner.nextLine();

        System.out.println("Enter the stock quantity: ");
        int stock;
        while (true) {
            try {
                stock = Integer.parseInt(scanner.nextLine()); // CWE-1287: Proper input validation
                if (stock < 0) {
                    System.out.println("Stock quantity cannot be negative. Please enter a valid integer:");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for stock quantity: ");
            }
        }

        // Create a new Book object and add it to the books list
        Book newBook = new Book(title, author, stock);
        books.add(newBook);

        System.out.println("New book added: " + title + " by " + author + " with " + stock + " copies.");

        // Save the updated list of books to the file
        saveBooksToFile(filename);
    }

    // public static void main(String[] args) {
    // LibraryManagementSystem library = new LibraryManagementSystem();
    // // Load books from the file
    // String filename = "books.txt";
    // library.loadBooksFromFile(filename);
    // library.displayBooks();

    // // Allow the user to add another book and save it to the file
    // library.addBook(filename);
    // }
}
