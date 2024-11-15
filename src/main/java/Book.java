
public class Book {
  private String bookTitle;
  private String author;
  private int stock;


  // Constructs a new Book with the a title, author, and price.
  public Book(String bookTitle, String author, int stock){
    this.bookTitle = bookTitle;
    this.author = author;
    this.stock = stock; 
  }

  // Returns the stock
  public int getStock(){
    return stock;
  }

  // CWE 190 Integer Overflow or Wraparound
  public void addStock(int amount) {
    if (amount < 0) {
        System.out.println("Error: Cannot add negative amount!");
        return;
    }
    if (Integer.MAX_VALUE - stock < amount) {
        System.out.println("Error: Adding this amount will result in overflow!");
        return;
    }
    stock += amount;
}
  
   // Reduces stock if available, used indirectly by LibraryManagementSystem
   public boolean reduceStock() {
    if (stock <= 0) {
        System.out.println("Error: Book out of stock!");
        return false;
    }
    stock--;
    return true;
  }

  // Returns the book title
  public String getTitle(){
    return bookTitle;
  }

  // Returns the author
  public String getAuthor(){
    return author;
  }


  // Sets the amount of stock for the book, ensuring the value is within valid bounds.
  // Example of CWE-190 by preventing integer overflow or wraparound.
  public void setStock(int stock) {
    if (stock < 0 || stock > Integer.MAX_VALUE) {
      System.out.println("Invalid stock amount.");
        return;
    }
    this.stock = stock;
  }
  
   // CWE-561: Avoid Dead Code - remove unused or unnecessary methods
   // Only using methods that are going to be used
 


  // Updates the book title, ensuring it is not empty or null.
  // Example of CWE-390 by detecting error conditions and taking action on them.
  public void readBookTitle(String newTitle) {
    if (newTitle == null || newTitle.isEmpty()) {
      System.out.println("Error: Title cannot be empty or null.");  // Take action on error
      return;
    }
    bookTitle = newTitle;  
  }

  // CWE-480: Use of Incorrect Operator
  public boolean isAvailable() {
    // Use `==` for comparison, not `=`
    return stock == 0;
}
}
