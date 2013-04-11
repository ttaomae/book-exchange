package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import play.db.ebean.Model;

@Entity
public class Offer extends Model {
  private static final long serialVersionUID = 5326921134590534622L;
  @Id
  public Long id;
  @ManyToOne(cascade=CascadeType.ALL)
  public Student student;
  @ManyToOne(cascade=CascadeType.ALL)
  public Book book;
  public Condition condition;
  public int targetPrice;

  public Offer(Student student, Book book, Condition condition, int targetPrice) {
    this.student = student;
    this.book = book;
    this.condition = condition;
    this.targetPrice = targetPrice;

    this.student.offers.add(this);
    this.book.offers.add(this);
  }

  @Override
  public void delete() {
    // remove this entry from the book's and student's lists
    this.book.offers.remove(this);
    this.student.offers.remove(this);

    // kill link to books and students
    this.book = null;
    this.student = null;

    super.delete();
  }

  public static Finder<Long, Offer> find() {
    return new Finder<Long, Offer>(Long.class, Offer.class);
  }
}
