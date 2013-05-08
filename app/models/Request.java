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
public class Request extends Model {
  private static final long serialVersionUID = 7490409966063340450L;
  @Id
  private Long primaryKey;

  @Column(nullable=false)
  @Required
  private String requestId;

  @Transient
  public String studentName;
//  @Required
  @ManyToOne(cascade = CascadeType.PERSIST)
  private Student student;

  @Transient
  private String bookTitle;
//  @Required
  @ManyToOne(cascade = CascadeType.PERSIST)
  private Book book;

  private Condition condition;

  @Required
  private int targetPrice;

  public Request(String requestId, int targetPrice) {
    this.requestId = requestId;
    this.targetPrice = targetPrice;
  }

  public Request(String requestId, Student student, Book book, int targetPrice) {
    this.requestId = requestId;
    this.student = student;
    this.book = book;
    this.targetPrice = targetPrice;

    this.student.getRequests().add(this);
    this.book.getRequests().add(this);
  }

  @Override
  public void delete() {
    // remove this entry from the book's and student's lists
    this.book.getRequests().remove(this);
    this.student.getRequests().remove(this);

    // kill link to books and students
    this.book = null;
    this.student = null;

    super.delete();
  }

  public static Finder<Long, Request> find() {
    return new Finder<Long, Request>(Long.class, Request.class);
  }

  @Override
  public String toString() {
    return String.format("[Request %s %s %s %d]", this.requestId, this.student.getName(),
        this.book.getTitle(), this.targetPrice);
  }

  public Long getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(Long primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getStudentName() {
    return this.studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public String getBookTitle() {
    return this.bookTitle;
  }

  public void setBookTitle(String bookTitle) {
    this.bookTitle = bookTitle;
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

  public Integer getTargetPrice() {
    return targetPrice;
  }

  public void setTargetPrice(Integer targetPrice) {
    this.targetPrice = targetPrice;
  }

  public String validate() {
    if (this.studentName != null) {
      this.student = Student.find().where().eq("name", this.studentName).findUnique();
      if (this.student == null) {
        return "Could not find the student named: " + this.studentName;
      }
    }

    if (this.bookTitle != null) {
      this.book = Book.find().where().eq("title", this.bookTitle).findUnique();
      if (this.book == null) {
        return "Could not find book named: " + this.bookTitle;
      }
    }

    return null;
  }
}
