package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Offer extends Model {
  private static final long serialVersionUID = 5326921134590534622L;
  @Id
  private Long primaryKey;

  @Column(nullable = false)
  @Required
  private String offerId;

  @Transient
  public String studentId;
//  @Required
  @ManyToOne(cascade = CascadeType.PERSIST)
  private Student student;

  @Transient
  public String bookId;
//  @Required
  @ManyToOne(cascade = CascadeType.PERSIST)
  private Book book;

  private Condition condition;

  @Required
  private int targetPrice;

  public Offer(String offerId, int targetPrice) {
    this.offerId = offerId;
    this.targetPrice = targetPrice;
  }

  public Offer(String offerId, Student student, Book book, int targetPrice) {
    this.offerId = offerId;
    this.student = student;
    this.book = book;
    this.targetPrice = targetPrice;

    this.student.getOffers().add(this);
    this.book.getOffers().add(this);
  }

  @Override
  public void delete() {
    // remove this entry from the book's and student's lists
    this.book.getOffers().remove(this);
    this.student.getOffers().remove(this);

    // kill link to books and students
    this.book = null;
    this.student = null;

    super.delete();
  }

  public static Finder<Long, Offer> find() {
    return new Finder<Long, Offer>(Long.class, Offer.class);
  }

  @Override
  public String toString() {
    return String.format("[Offer %s %s %s %d]", this.offerId, this.student.getName(),
        this.book.getTitle(), this.targetPrice);
  }

  public Long getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(Long primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getOfferId() {
    return offerId;
  }

  public void setOfferId(String offerId) {
    this.offerId = offerId;
  }

  public String getStudentId() {
    return this.studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public String getBookId() {
    return this.bookId;
  }

  public void setBookId(String bookid) {
    this.bookId = bookid;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public Condition getCondition() {
    return condition;
  }

  public void setCondition(Condition condition) {
    this.condition = condition;
  }

  public int getTargetPrice() {
    return targetPrice;
  }

  public void setTargetPrice(int targetPrice) {
    this.targetPrice = targetPrice;
  }

  public String validate() {
    if (this.studentId != null) {
      this.student = Student.find().where().eq("studentId", this.studentId).findUnique();
      if (this.student == null) {
        return "Could not find the student with ID: " + this.studentId;
      }
    }

    if (this.bookId != null) {
      this.book = Book.find().where().eq("bookId", this.bookId).findUnique();
      if (this.book == null) {
        return "Could not find the book with ID: " + this.bookId;
      }
    }

    return null;
  }
}
