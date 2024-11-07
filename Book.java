

public class Book {
    string bookTitle;
    string author;
    double price;


    public Book(string bookTitle, string author, double price){
      this.bookTitle = bookTitle;
      this.author = author;
      this.price = price;
    }

    public String getTitle(){
      return bookTitle;
    }

    public String getAuthor(){
      return author;
    }

    public double getPrice(){
      return price;
    }
}
