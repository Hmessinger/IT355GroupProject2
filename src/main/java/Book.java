
public class Book {
  private int id;
  private String bookTitle;
  private String author;
  private double price;
  private int stock;
  private boolean checkedOut;

  public Book(int id, String bookTitle, String author, double price, int stock, boolean checkedOut) {
    this.id = id;
    this.bookTitle = bookTitle;
    this.author = author;
    this.price = price;
    this.stock = stock;
    this.checkedOut = checkedOut;
  }

  // Returns the book title
  public int getId() {
    return id;
  }

  // Returns the book title
  public String getTitle() {
    return bookTitle;
  }

  // Returns the author
  public String getAuthor() {
    return author;
  }

  // returns the price
  public double getPrice() {
    return price;
  }

  // returns the stock
  public int getStock() {
    return stock;
  }

  // returns the checked out value
  public boolean isCheckedOut() {
    return checkedOut;
  }

  public void setCheckedOutTrue() {
    this.checkedOut = true;
  }

  public void setCheckedOutFalse() {
    this.checkedOut = false;
  }

  // Displays the book information
  @Override
  public String toString() {
    return "ID: " + id + ", Book Title: '" + bookTitle + '\'' +
        ", Author: " + author +
        ", Price: " + price +
        ", In Stock: " + stock +
        ", Checked Out Status: " + checkedOut;
  }

  public String simpleBookInfo() {
    return "ID: " + id + ", Book Title: '" + bookTitle + '\'' +
        ", Author: " + author;
  }

  // Applies a discount to the book's price if the discount is within a valid
  // range (0 to 1).
  // Example of CWE-253 by checking the return value to ensure the discount is
  // applied only
  // if it is within the valid range range.
  public boolean applyDiscount(double discount) {
    if (discount < 0 || discount > 1) {
      System.out.println("Invalid discount value.");
      return false;
    }
    this.price = price * (1 - discount);
    return true;
  }

  // Sets the amount of stock for the book, ensuring the value is within valid
  // bounds.
  // Example of CWE-190 by preventing integer overflow or wraparound.
  public void setStock(int stock) {
    if (stock < 0 || stock > Integer.MAX_VALUE) {
      System.out.println("Invalid stock amount.");
      return;
    }
    this.stock = stock;
  }

  // Calculates the price after applying the entered discount, validating the
  // discount
  // so it is between 0 and 1.
  // Example of CWE-480 by using correct operators for the calculation.
  public double calcDiscountPrice(double discount) {
    if (discount < 0 || discount > 1) {
      System.out.println("Discount must be between 0 and 1.");
      return price;
    }
    return price * (1 - discount);
  }

  // CWE-561: Avoid Dead Code - remove unused or unnecessary methods
  // Only using methods that are going to be used

  // Example of CWE-1116: Accurate Comments, comments that are accurate
  // and describe functionality.
  // Calculates the price after applying a 10% discount.
  public double calcDiscountedPrice() {
    return price * 0.90;
  }

  // Updates the book title, ensuring it is not empty or null.
  // Example of CWE-390 by detecting error conditions and taking action on them.
  public void readBookTitle(String newTitle) {
    if (newTitle == null || newTitle.isEmpty()) {
      System.out.println("Error: Title cannot be empty or null."); // Take action on error
      return;
    }
    bookTitle = newTitle;
  }

}