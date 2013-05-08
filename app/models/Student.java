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
public class Student extends Model {
  private static final long serialVersionUID = -1729390609083717186L;
  @Id
  private Long primaryKey;
  @Column(nullable=false, unique=true)
  @Required
  private String studentId;
  @Required
  private String name;
  @Required
  private String email;
  @OneToMany(mappedBy="student", cascade=CascadeType.ALL)
  private List<Request> requests = new ArrayList<>();
  @OneToMany(mappedBy="student", cascade=CascadeType.ALL)
  private List<Offer> offers = new ArrayList<>();

  public Student(String studentId, String name, String email) {
    this.studentId = studentId;
    this.name = name;
    this.email = email;
  }

  @Override
  public void delete() {
    // if the student is being deleted, the requests and offers should be deleted too.
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

  public static Finder<Long, Student> find() {
    return new Finder<Long, Student>(Long.class, Student.class);
  }

  @Override
  public String toString() {
    return String.format("[Student %s %s %s]", this.studentId, this.name, this.email);
  }

  public Long getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(Long primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public static List<String> getStudentIds() {
    List<String> students = new ArrayList<>();
    for (Student student : Student.find().all()) {
      students.add(student.getStudentId());
    }

    return students;
  }
}
