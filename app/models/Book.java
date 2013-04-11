package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import play.db.ebean.Model;

@Entity
public class Book extends Model {
  private static final long serialVersionUID = -2658435387158619753L;
  @Id
  public Long id;
  public String title;
  public String edition;
  public long isbn;
  public int price;
  @OneToMany(mappedBy="book", cascade=CascadeType.ALL)
  public List<Request> requests = new ArrayList<>();
  @OneToMany(mappedBy="book", cascade=CascadeType.ALL)
  public List<Offer> offers= new ArrayList<>();

  public Book(String title, String edition, long isbn, int price) {
    this.title = title;
    this.edition = edition;
    this.isbn = isbn;
    this.price = price;
  }

  public Book(String title, long isbn, int price) {
    this(title, null, isbn, price);
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
}
