import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;
import java.util.List;
import models.Book;
import models.Condition;
import models.Offer;
import models.Request;
import models.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.FakeApplication;


public class ModelTest {
  private FakeApplication application;

  @Before
  public void startApp() {
    application = fakeApplication(inMemoryDatabase());
    start(application);
  }

  @After
  public void stopApp() {
    stop(application);
  }

  @Test
  public void testModel() {
    // Create data
    Student student = new Student("Student", "Email");
    Book book = new Book("Book", "1st", 1, 2);
    Request request = new Request(student, book, Condition.NEW, 0);
    Offer offer = new Offer(student, book, Condition.NEW, 0);

    // Persist data
    student.save();
    book.save();
    request.save();
    offer.save();

    // Retrieve data from database
    List<Student> dbStudents = Student.find().findList();
    List<Book> dbBooks = Book.find().findList();
    List<Request> dbRequests = Request.find().findList();
    List<Offer> dbOffers = Offer.find().findList();

    // Check that we've recovered all entities
    assertEquals("Recovered all students", 1, dbStudents.size());
    assertEquals("Recovered all books", 1, dbBooks.size());
    assertEquals("Recovered all requests", 1, dbRequests.size());
    assertEquals("Recovered all offers", 1, dbOffers.size());

    // Check that we've recovered all relationships
    assertEquals("Student-Request", dbRequests.get(0), dbStudents.get(0).requests.get(0));
    assertEquals("Request-Student", dbStudents.get(0), dbRequests.get(0).student);
    assertEquals("Student-Offer", dbOffers.get(0), dbStudents.get(0).offers.get(0));
    assertEquals("Offer-Student", dbStudents.get(0), dbOffers.get(0).student);
    assertEquals("Book-Request", dbRequests.get(0), dbBooks.get(0).requests.get(0));
    assertEquals("Request-Book", dbBooks.get(0), dbRequests.get(0).book);
    assertEquals("Book-Offer", dbOffers.get(0), dbBooks.get(0).offers.get(0));
    assertEquals("Offer-Book", dbBooks.get(0), dbOffers.get(0).book);
  }

  @Test
  public void testDeleteBook() {
    // Create data
    Student student = new Student("Student", "Email");
    Book bookA = new Book("BookA", "1st", 1, 2);
    Book bookB = new Book("BookB", "1st", 1, 2);
    Request request = new Request(student, bookA, Condition.NEW, 0);
    Offer offer = new Offer(student, bookB, Condition.NEW, 0);

    // Persist data
    student.save();
    bookA.save();
    bookB.save();
    request.save();
    offer.save();

    // Check that we've recovered all entities
    assertEquals("Recovered all students", 1, Student.find().findList().size());
    assertEquals("Recovered all books", 2, Book.find().findList().size());
    assertEquals("Recovered all requests", 1, Request.find().findList().size());
    assertEquals("Recovered all offers", 1, Offer.find().findList().size());

    // this should remove bookA and request
    bookA.delete();

    // Check that the book and request was deleted
    assertEquals("First book was deleted", 1, Book.find().findList().size());
    assertEquals("Correct book was deleted", "BookB", Book.find().findList().get(0).title);
    assertEquals("Request was deleted", 0, Request.find().findList().size());
    // Check that student and offer was not deleted
    assertEquals("Student was not deleted", 1, Student.find().findList().size());
    assertEquals("Offer was not deleted", 1, Offer.find().findList().size());

    // this should remove bookB and the offer
    bookB.delete();

    // Check that the book and offer were deleted
    assertEquals("Second book was deleted", 0, Book.find().findList().size());
    assertEquals("Offer was deleted", 0, Offer.find().findList().size());
    // Check that the student was not deleted
    assertEquals("Student was not deleted", 1, Student.find().findList().size());
  }

  @Test
  public void testDeleteStudent() {
    // Create data
    Student studentA = new Student("StudentA", "Email");
    Student studentB = new Student("StudentB", "Email");
    Book book = new Book("Book", "1st", 1, 2);
    Request request = new Request(studentA, book, Condition.NEW, 0);
    Offer offer = new Offer(studentB, book, Condition.NEW, 0);

    // Persist data
    studentA.save();
    studentB.save();
    book.save();
    request.save();
    offer.save();

    // Check that we've recovered all entities
    assertEquals("Recovered all students", 2, Student.find().findList().size());
    assertEquals("Recovered all books", 1, Book.find().findList().size());
    assertEquals("Recovered all requests", 1, Request.find().findList().size());
    assertEquals("Recovered all offers", 1, Offer.find().findList().size());

    // this should remove studentA and the request
    studentA.delete();

    // Check that the student and request was deleted
    assertEquals("First student was deleted", 1, Student.find().findList().size());
    assertEquals("Correct student was deleted", "StudentB", Student.find().findList().get(0).name);
    assertEquals("Request was deleted", 0, Request.find().findList().size());
    // Check that book and offer was not deleted
    assertEquals("Book was not deleted", 1, Book.find().findList().size());
    assertEquals("Offer was not deleted", 1, Offer.find().findList().size());

    // this should remove studentB, the offer, and the book
    studentB.delete();

    // Check that the student, offer, and book were deleted
    assertEquals("Second student was deleted", 0, Student.find().findList().size());
    assertEquals("Offer was deleted", 0, Offer.find().findList().size());
    assertEquals("Book was not deleted", 1, Book.find().findList().size());
  }

  @Test
  public void testDeleteRequestAndOffer() {

    // Create data
    Student student = new Student("Student", "Email");
    Book book = new Book("Book", "1st", 1, 2);
    Request request = new Request(student, book, Condition.NEW, 0);
    Offer offer = new Offer(student, book, Condition.NEW, 0);

    // Persist data
    student.save();
    book.save();
    request.save();
    offer.save();

    // Check that we've recovered all entities
    assertEquals("Recovered all students", 1, Student.find().findList().size());
    assertEquals("Recovered all books", 1, Book.find().findList().size());
    assertEquals("Recovered all requests", 1, Request.find().findList().size());
    assertEquals("Recovered all offers", 1, Offer.find().findList().size());

    // should only delete request
    request.delete();

    // Check that only the request was deleted
    assertEquals("Recovered all students", 1, Student.find().findList().size());
    assertEquals("Recovered all books", 1, Book.find().findList().size());
    assertEquals("Recovered all requests", 0, Request.find().findList().size());
    assertEquals("Recovered all offers", 1, Offer.find().findList().size());

    // should only delete offer
    offer.delete();

    // Check that only the request was deleted
    assertEquals("Recovered all students", 1, Student.find().findList().size());
    assertEquals("Recovered all books", 1, Book.find().findList().size());
    assertEquals("Recovered all requests", 0, Request.find().findList().size());
    assertEquals("Recovered all offers", 0, Offer.find().findList().size());
  }
}
