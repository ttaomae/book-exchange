import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.SEE_OTHER;
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
    assertTrue("Empty students", contentAsString(result).contains("Students"));

    // Test GET /students on a database containing a single student.
    String studentId = "Student-01";
    Student student = new Student(studentId, "Name", "Email");
    student.save();
    Long primaryKey = student.getPrimaryKey();
    result = callAction(controllers.routes.ref.Student.index());
    assertTrue("One student", contentAsString(result).contains(studentId));

    // Test GET /students/[primaryKey]
    result = callAction(controllers.routes.ref.Student.edit(primaryKey));
    assertTrue("Student detail", contentAsString(result).contains(studentId));

    // Test GET /students/[primaryKey + 1] (invalid primaryKey)
    result = callAction(controllers.routes.ref.Student.edit(primaryKey + 1));
    assertEquals("Student detail (bad)", NOT_FOUND, status(result));

    // Test POST /students (with simulated, valid form data).
    Map<String, String> studentData = new HashMap<String, String>();
    studentData.put("studentId", "Student-02");
    studentData.put("name", "OtherName");
    studentData.put("email", "OtherEmail");
    FakeRequest request = fakeRequest();
    request.withFormUrlEncodedBody(studentData);
    result = callAction(controllers.routes.ref.Student.save(), request);
    assertEquals("Create new student", SEE_OTHER, status(result));

    // Test POST /students (with simulated, invalid form data).
    request = fakeRequest();
    result = callAction(controllers.routes.ref.Student.save(), request);
    assertEquals("Create bad student fails", BAD_REQUEST, status(result));

    // Test DELETE /students/Student-01 (a valid studentId).
    result = callAction(controllers.routes.ref.Student.delete(primaryKey));
    assertEquals("Delete current student OK", SEE_OTHER, status(result));
    result = callAction(controllers.routes.ref.Student.edit(primaryKey));
    assertEquals("Deleted student gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Student.delete(primaryKey));
    assertEquals("Delete missing student also OK", SEE_OTHER, status(result));
  }

  @Test
  public void testBookController() {
    // Test GET /books on an empty database
    Result result = callAction(controllers.routes.ref.Book.index());
    assertTrue("Empty books", contentAsString(result).contains("Books"));

    // Test GET /books on a database containing a single student.
    String bookId = "Book-01";
    Book book = new Book(bookId, "Title", "Edition", 1234L, 123);
    book.save();
    Long primaryKey = book.getPrimaryKey();
    result = callAction(controllers.routes.ref.Book.index());
    assertTrue("One book", contentAsString(result).contains(bookId));

    // Test GET /books/[primaryKey]
    result = callAction(controllers.routes.ref.Book.edit(primaryKey));
    assertTrue("Book detail", contentAsString(result).contains(bookId));

    // Test GET /books/[primaryKey + 1] (invalid primaryKey)
    result = callAction(controllers.routes.ref.Book.edit(primaryKey + 1));
    assertEquals("Book detail (bad)", NOT_FOUND, status(result));

    // Test POST /books (with simulated, valid form data).
    Map<String, String> bookData = new HashMap<String, String>();
    bookData.put("bookId", "Book-02");
    bookData.put("title", "OtherTitle");
    bookData.put("isbn", "4321");
    FakeRequest request = fakeRequest();
    request.withFormUrlEncodedBody(bookData);
    result = callAction(controllers.routes.ref.Book.save(), request);
    assertEquals("Create new book", SEE_OTHER, status(result));

    // Test POST /books (with simulated, invalid form data).
    request = fakeRequest();
    result = callAction(controllers.routes.ref.Book.save(), request);
    assertEquals("Create bad book fails", BAD_REQUEST, status(result));

    // Test DELETE /books/Student-01 (a valid studentId).
    result = callAction(controllers.routes.ref.Book.delete(primaryKey));
    assertEquals("Delete current book OK", SEE_OTHER, status(result));
    result = callAction(controllers.routes.ref.Book.edit(primaryKey));
    assertEquals("Deleted book gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Book.delete(primaryKey));
    assertEquals("Delete missing book also OK", SEE_OTHER, status(result));
  }

  @Test
  public void testRequestController() {
    // Test GET /requests on an empty database.
    Result result = callAction(controllers.routes.ref.Request.index());
    assertTrue("Empty requests", contentAsString(result).contains("Requests"));

    // Test GET /requests on a database containing a single request.
    String requestId = "Request-01";
    Student student = new Student("Student-01", "Name", "Email");
    Book book = new Book("Book-01", "Title", "Edition", 1234L, 123);
    Request request = new Request(requestId, student, book, 123);
    student.save();
    book.save();
    request.save();
    Long primaryKey = request.getPrimaryKey();
    result = callAction(controllers.routes.ref.Request.index());
    assertTrue("One request", contentAsString(result).contains(requestId));

    // Test GET /requests/[primaryKey]
    result = callAction(controllers.routes.ref.Request.edit(primaryKey));
    assertTrue("Request detail", contentAsString(result).contains(requestId));

    // Test GET /requests/[primaryKey + 1]
    result = callAction(controllers.routes.ref.Request.edit(primaryKey + 1));
    assertEquals("Request detail (bad)", NOT_FOUND, status(result));

    // Test POST /requests (with simulated, valid form data).
    Map<String, String> requestData = new HashMap<String, String>();
    requestData.put("requestId", "Request-02");
    requestData.put("studentId", "Student-01");
    requestData.put("bookId", "Book-01");
    requestData.put("targetPrice", "321");
    FakeRequest fakeRequest = fakeRequest();
    fakeRequest.withFormUrlEncodedBody(requestData);
    result = callAction(controllers.routes.ref.Request.save(), fakeRequest);
    assertEquals("Create new request", SEE_OTHER, status(result));
    assertEquals("New request has correct student", "Name",
        Request.find().where().eq("requestId", "Request-02").findUnique().getStudent().getName());
    assertEquals("New request has correct book", "Title",
        Request.find().where().eq("requestId", "Request-02").findUnique().getBook().getTitle());

    // Test POST /requests (with simulated, invalid form data).
    fakeRequest = fakeRequest();
    result = callAction(controllers.routes.ref.Request.save(), fakeRequest);
    assertEquals("Create bad request fails", BAD_REQUEST, status(result));

    // Test DELETE /requests/Request-01 (a valid requestId).
    result = callAction(controllers.routes.ref.Request.delete(primaryKey));
    assertEquals("Delete current request OK", SEE_OTHER, status(result));
    result = callAction(controllers.routes.ref.Request.edit(primaryKey));
    assertEquals("Deleted request gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Request.delete(primaryKey));
    assertEquals("Delete missing request also OK", SEE_OTHER, status(result));
  }

  @Test
  public void testOfferControllers() {
    // Test GET /offers on an empty database.
    Result result = callAction(controllers.routes.ref.Offer.index());
    assertTrue("Empty requests", contentAsString(result).contains("Offers"));

    // Test GET /offers on a database containing a single offer.
    String offerId = "Offer-01";
    Student student = new Student("Student-01", "Name", "Email");
    Book book = new Book("Book-01", "Title", "Edition", 1234L, 123);
    Offer offer = new Offer(offerId, student, book, 123);
    student.save();
    book.save();
    offer.save();
    Long primaryKey = offer.getPrimaryKey();
    result = callAction(controllers.routes.ref.Offer.index());
    assertTrue("One Offer", contentAsString(result).contains(offerId));

    // Test GET /offers/[primaryKey]
    result = callAction(controllers.routes.ref.Offer.edit(primaryKey));
    assertTrue("Offer detail", contentAsString(result).contains(offerId));

    // Test GET /offers/[primaryKey + 1]
    result = callAction(controllers.routes.ref.Request.edit(primaryKey + 1));
    assertEquals("Request detail (bad)", NOT_FOUND, status(result));

    // Test POST /offers (with simulated, valid form data).
    Map<String, String> offerData = new HashMap<String, String>();
    offerData.put("offerId", "Offer-02");
    offerData.put("studentId", "Student-01");
    offerData.put("bookId", "Book-01");
    offerData.put("targetPrice", "321");
    FakeRequest fakeRequest = fakeRequest();
    fakeRequest.withFormUrlEncodedBody(offerData);
    result = callAction(controllers.routes.ref.Offer.save(), fakeRequest);
    assertEquals("Create new offer", SEE_OTHER, status(result));
    assertEquals("New offer has correct student", "Name",
        Offer.find().where().eq("offerId", "Offer-02").findUnique().getStudent().getName());
    assertEquals("New offer has correct book", "Title",
        Offer.find().where().eq("offerId", "Offer-02").findUnique().getBook().getTitle());

    // Test POST /offers (with simulated, invalid form data).
    fakeRequest = fakeRequest();
    result = callAction(controllers.routes.ref.Offer.save(), fakeRequest);
    assertEquals("Create bad offer fails", BAD_REQUEST, status(result));

    // Test DELETE /offers/Offer-01 (a valid OfferId).
    result = callAction(controllers.routes.ref.Offer.delete(primaryKey));
    assertEquals("Delete current offer OK", SEE_OTHER, status(result));
    result = callAction(controllers.routes.ref.Offer.edit(primaryKey));
    assertEquals("Deleted offer gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Offer.delete(primaryKey));
    assertEquals("Delete missing offer also OK", SEE_OTHER, status(result));
  }
}
