import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
// import src.main.java.Book;

public class LibraryManagementSystem {
    private List<Book> books; // Avoids global use by containing the list within the class (CWE-1108)
    private List<User> users; // Only relevant within the context of this library system

    public LibraryManagementSystem() {
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    // CWE-1041: Centralized method for displaying catalog to avoid redundant code
    public void displayCatalog() {
        for (Book book : books) {
            displayBookInfo(book); // Centralized call to avoid code repetition
        }
    }

    public void loadBooksFromFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String title = parts[0].trim();
                    String author = parts[1].trim();
                    int stock = Integer.parseInt(parts[2].trim());
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

    private void displayBookInfo(Book book) {
        System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Stock: " + book.getStock());
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
    // CWE-1109: Avoid using the same variable for multiple purposes within reservation logic
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
    public static void main(String[] args) {
        LibraryManagementSystem library = new LibraryManagementSystem();
        library.loadBooksFromFile("books.txt"); // Load books from the file
        library.displayBooks(); // Display all books
    }
}
