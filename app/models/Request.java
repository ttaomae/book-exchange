package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import play.db.ebean.Model;

@Entity
public class Request extends Model {
  private static final long serialVersionUID = 7490409966063340450L;
  @Id
  public Long id;
  @ManyToOne(cascade=CascadeType.ALL)
  public Student student;
  @ManyToOne(cascade=CascadeType.ALL)
  public Book book;
  public Condition condition;
  public int targetPrice;

  public Request(Student student, Book book, Condition condition, int targetPrice) {
    this.student = student;
    this.book = book;
    this.condition = condition;
    this.targetPrice = targetPrice;

    this.student.requests.add(this);
    this.book.requests.add(this);
  }

  @Override
  public void delete() {
    // remove this entry from the book's and student's lists
    this.book.requests.remove(this);
    this.student.requests.remove(this);

    // kill link to books and students
    this.book = null;
    this.student = null;

    super.delete();
  }

  public static Finder<Long, Request> find() {
    return new Finder<Long, Request>(Long.class, Request.class);
  }
}
