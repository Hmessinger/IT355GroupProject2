import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private List<Book> borrowedBooks;
    private List<Book> reservedBooks;
    private static final int BORROW_LIMIT = 3; //Avoid excessive global variables (CWE-1108)

    public User(String username) {
        this.username = username;
        this.borrowedBooks = new ArrayList<>();
        this.reservedBooks = new ArrayList<>();
    }

    public boolean hasReachedBorrowLimit() {
        return borrowedBooks.size() >= BORROW_LIMIT;
    }

    public void borrowBook(Book book) {
        if (!hasReachedBorrowLimit()) {
            borrowedBooks.add(book);
        }
    }

    public void reserveBook(Book book) {
        reservedBooks.add(book);
    }

    public boolean hasBorrowed(Book book) {
        return borrowedBooks.contains(book);
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
}