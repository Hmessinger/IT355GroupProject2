import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LibraryManagementSystem {
    private List<Book> books; // Avoids global use by containing the list within the class (CWE-1108)
    private List<User> users; // Only relevant within the context of this library system

    public LibraryManagementSystem() {
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
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

    // CWE-563: Checkout book functionality without unnecessary variable assignments
    public boolean checkoutBook(User user, String bookTitle) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(bookTitle)) {
                if (!user.hasReachedBorrowLimit()) {
                    user.borrowBook(book);
                    System.out.println("Book checked out successfully.");
                    return true; // Return here to prevent unnecessary assignments
                } else {
                    System.out.println("User has reached the borrowing limit.");
                    return false;
                }
            }
        }
        System.out.println("Book not found.");
        return false;
    }

    // CWE-1109: Avoid using the same variable for multiple purposes within
    // reservation logic
    public void reserveBook(User user, String bookTitle) {
        Book reservedBook = null;
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(bookTitle) && !user.hasBorrowed(book)) {
                reservedBook = book;
                break;
            }
        }
        if (reservedBook != null) {
            user.reserveBook(reservedBook); // Dedicated variable for reservation status
            System.out.println("Book reserved successfully.");
        } else {
            System.out.println("Unable to reserve book.");
        }
    }
}
