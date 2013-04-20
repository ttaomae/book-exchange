package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Book extends Model {
  private static final long serialVersionUID = -2658435387158619753L;
  @Id
  private Long primaryKey;
  @Column(nullable=false)
  @Required
  private String bookId;
  @Required
  private String title;
  private String edition;
  @Required
  private long isbn;
  private int price;
  @OneToMany(mappedBy="book", cascade=CascadeType.ALL)
  private List<Request> requests = new ArrayList<>();
  @OneToMany(mappedBy="book", cascade=CascadeType.ALL)
  private List<Offer> offers= new ArrayList<>();

  public Book(String bookId, String title, String edition, long isbn, int price) {
    this.bookId = bookId;
    this.title = title;
    this.edition = edition;
    this.isbn = isbn;
    this.price = price;
  }

  public Book(String bookId, String title, long isbn, int price) {
    this(bookId, title, null, isbn, price);
  }

  @Override
  public void delete() {
    // if the book is being deleted, the requests and offers should be deleted too.
    while (this.requests.size() > 0) {
      // delete from database
      this.requests.get(0).delete();
    }

    while (this.offers.size() > 0) {
      // delete from database
      this.offers.get(0).delete();
    }

    super.delete();
  }

  public static Finder<Long, Book> find() {
    return new Finder<Long, Book>(Long.class, Book.class);
  }

  @Override
  public String toString() {
    return String.format("[Book %s %s %d]", this.bookId, this.title, this.isbn);
  }

  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }

  public Long getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(Long primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getEdition() {
    return edition;
  }

  public void setEdition(String edition) {
    this.edition = edition;
  }

  public long getIsbn() {
    return isbn;
  }

  public void setIsbn(long isbn) {
    this.isbn = isbn;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public List<Request> getRequests() {
    return requests;
  }

  public void setRequests(List<Request> requests) {
    this.requests = requests;
  }

  public List<Offer> getOffers() {
    return offers;
  }

  public void setOffers(List<Offer> offers) {
    this.offers = offers;
  }
}
