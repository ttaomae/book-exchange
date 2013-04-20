import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.status;
import static play.test.Helpers.stop;
import java.util.HashMap;
import java.util.Map;
import models.Book;
import models.Offer;
import models.Request;
import models.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;

public class ControllerTest {
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
  public void testStudentController() {
    // Test GET /students on an empty database
    Result result = callAction(controllers.routes.ref.Student.index());
    assertTrue("Empty students", contentAsString(result).contains("No students"));

    // Test GET /students on a database containing a single student.
    String studentId = "Student-01";
    Student student = new Student(studentId, "Name", "Email");
    student.save();
    result = callAction(controllers.routes.ref.Student.index());
    assertTrue("One student", contentAsString(result).contains(studentId));

    // Test GET /students/Student-01.
    result = callAction(controllers.routes.ref.Student.details(studentId));
    assertTrue("Student detail", contentAsString(result).contains(studentId));

    // Test GET /students/BadStudentId.
    result = callAction(controllers.routes.ref.Student.details("BadStudentId"));
    assertEquals("Student detail (bad)", NOT_FOUND, status(result));

    // Test POST /students (with simulated, valid form data).
    Map<String, String> studentData = new HashMap<String, String>();
    studentData.put("studentId", "Student-02");
    studentData.put("name", "OtherName");
    studentData.put("email", "OtherEmail");
    FakeRequest request = fakeRequest();
    request.withFormUrlEncodedBody(studentData);
    result = callAction(controllers.routes.ref.Student.newStudent(), request);
    assertEquals("Create new student", OK, status(result));

    // Test POST /students (with simulated, invalid form data).
    request = fakeRequest();
    result = callAction(controllers.routes.ref.Student.newStudent(), request);
    assertEquals("Create bad student fails", BAD_REQUEST, status(result));

    // Test DELETE /students/Student-01 (a valid studentId).
    result = callAction(controllers.routes.ref.Student.delete(studentId));
    assertEquals("Delete current student OK", OK, status(result));
    result = callAction(controllers.routes.ref.Student.details(studentId));
    assertEquals("Deleted student gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Student.delete(studentId));
    assertEquals("Delete missing student also OK", OK, status(result));
  }

  @Test
  public void testBookController() {
    // Test GET /books on an empty database
    Result result = callAction(controllers.routes.ref.Book.index());
    assertTrue("Empty books", contentAsString(result).contains("No books"));

    // Test GET /books on a database containing a single student.
    String bookId = "Book-01";
    Book book = new Book(bookId, "Title", "Edition", 1234L, 123);
    book.save();
    result = callAction(controllers.routes.ref.Book.index());
    assertTrue("One book", contentAsString(result).contains(bookId));

    // Test GET /books/Student-01.
    result = callAction(controllers.routes.ref.Book.details(bookId));
    assertTrue("Book detail", contentAsString(result).contains(bookId));

    // Test GET /books/BadStudentId.
    result = callAction(controllers.routes.ref.Book.details("BadBookId"));
    assertEquals("Book detail (bad)", NOT_FOUND, status(result));

    // Test POST /books (with simulated, valid form data).
    Map<String, String> bookData = new HashMap<String, String>();
    bookData.put("bookId", "Book-02");
    bookData.put("title", "OtherTitle");
    bookData.put("isbn", "4321");
    FakeRequest request = fakeRequest();
    request.withFormUrlEncodedBody(bookData);
    result = callAction(controllers.routes.ref.Book.newBook(), request);
    assertEquals("Create new book", OK, status(result));

    // Test POST /books (with simulated, invalid form data).
    request = fakeRequest();
    result = callAction(controllers.routes.ref.Book.newBook(), request);
    assertEquals("Create bad book fails", BAD_REQUEST, status(result));

    // Test DELETE /books/Student-01 (a valid studentId).
    result = callAction(controllers.routes.ref.Book.delete(bookId));
    assertEquals("Delete current book OK", OK, status(result));
    result = callAction(controllers.routes.ref.Book.details(bookId));
    assertEquals("Deleted book gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Book.delete(bookId));
    assertEquals("Delete missing book also OK", OK, status(result));
  }

  @Test
  public void testRequestController() {
    // Test GET /requests on an empty database.
    Result result = callAction(controllers.routes.ref.Request.index());
    assertTrue("Empty requests", contentAsString(result).contains("No requests"));

    // Test GET /requests on a database containing a single request.
    String requestId = "Request-01";
    Student student = new Student("Student-01", "Name", "Email");
    Book book = new Book("Book-01", "Title", "Edition", 1234L, 123);
    Request request = new Request(requestId, student, book, 123);
    student.save();
    book.save();
    request.save();
    result = callAction(controllers.routes.ref.Request.index());
    assertTrue("One request", contentAsString(result).contains(requestId));

    // Test GET /requests/Request-01.
    result = callAction(controllers.routes.ref.Request.details(requestId));
    assertTrue("Request detail", contentAsString(result).contains(requestId));

    // Test GET /requests/BadRequestId
    result = callAction(controllers.routes.ref.Request.details("BadRequestId"));
    assertEquals("Request detail (bad)", NOT_FOUND, status(result));

    // Test POST /requests (with simulated, valid form data).
    Map<String, String> requestData = new HashMap<String, String>();
    requestData.put("requestId", "Request-02");
    requestData.put("student.studentId", "Student-02");
    requestData.put("student.name", "OtherName");
    requestData.put("student.email", "OtherEmail");
    requestData.put("book.bookId", "Book-02");
    requestData.put("book.title", "OtherTitle");
    requestData.put("book.isbn", "4321");
    requestData.put("targetPrice", "321");
    FakeRequest fakeRequest = fakeRequest();
    fakeRequest.withFormUrlEncodedBody(requestData);
    result = callAction(controllers.routes.ref.Request.newRequest(), fakeRequest);
    assertEquals("Create new request", OK, status(result));
    assertEquals("New request has correct student", "OtherName",
        Request.find().where().eq("requestId", "Request-02").findUnique().getStudent().getName());
    assertEquals("New request has correct book", "OtherTitle",
        Request.find().where().eq("requestId", "Request-02").findUnique().getBook().getTitle());

    // Test POST /requests (with simulated, invalid form data).
    fakeRequest = fakeRequest();
    result = callAction(controllers.routes.ref.Request.newRequest(), fakeRequest);
    assertEquals("Create bad request fails", BAD_REQUEST, status(result));

    // Test DELETE /requests/Request-01 (a valid requestId).
    result = callAction(controllers.routes.ref.Request.delete(requestId));
    assertEquals("Delete current request OK", OK, status(result));
    result = callAction(controllers.routes.ref.Request.details(requestId));
    assertEquals("Deleted request gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Request.delete(requestId));
    assertEquals("Delete missing request also OK", OK, status(result));
  }

  @Test
  public void testOfferControllers() {
    // Test GET /offers on an empty database.
    Result result = callAction(controllers.routes.ref.Offer.index());
    assertTrue("Empty requests", contentAsString(result).contains("No offers"));

    // Test GET /offers on a database containing a single offer.
    String offerId = "Offer-01";
    Student student = new Student("Student-01", "Name", "Email");
    Book book = new Book("Book-01", "Title", "Edition", 1234L, 123);
    Offer offer = new Offer(offerId, student, book, 123);
    student.save();
    book.save();
    offer.save();
    result = callAction(controllers.routes.ref.Offer.index());
    assertTrue("One Offer", contentAsString(result).contains(offerId));

    // Test GET /offers/Offer-01.
    result = callAction(controllers.routes.ref.Offer.details(offerId));
    assertTrue("Offer detail", contentAsString(result).contains(offerId));

    // Test GET /offers/BadOfferId
    result = callAction(controllers.routes.ref.Request.details("BadOfferId"));
    assertEquals("Request detail (bad)", NOT_FOUND, status(result));

    // Test POST /offers (with simulated, valid form data).
    Map<String, String> offerData = new HashMap<String, String>();
    offerData.put("offerId", "Offer-02");
    offerData.put("student.studentId", "Student-02");
    offerData.put("student.name", "OtherName");
    offerData.put("student.email", "OtherEmail");
    offerData.put("book.bookId", "Book-02");
    offerData.put("book.title", "OtherTitle");
    offerData.put("book.isbn", "4321");
    offerData.put("targetPrice", "321");
    FakeRequest fakeRequest = fakeRequest();
    fakeRequest.withFormUrlEncodedBody(offerData);
    result = callAction(controllers.routes.ref.Offer.newOffer(), fakeRequest);
    assertEquals("Create new offer", OK, status(result));
    assertEquals("New offer has correct student", "OtherName",
        Offer.find().where().eq("offerId", "Offer-02").findUnique().getStudent().getName());
    assertEquals("New offer has correct book", "OtherTitle",
        Offer.find().where().eq("offerId", "Offer-02").findUnique().getBook().getTitle());

    // Test POST /offers (with simulated, invalid form data).
    fakeRequest = fakeRequest();
    result = callAction(controllers.routes.ref.Offer.newOffer(), fakeRequest);
    assertEquals("Create bad offer fails", BAD_REQUEST, status(result));

    // Test DELETE /offers/Offer-01 (a valid OfferId).
    result = callAction(controllers.routes.ref.Offer.delete(offerId));
    assertEquals("Delete current offer OK", OK, status(result));
    result = callAction(controllers.routes.ref.Offer.details(offerId));
    assertEquals("Deleted offer gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Offer.delete(offerId));
    assertEquals("Delete missing offer also OK", OK, status(result));
  }
}
