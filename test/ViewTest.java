import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;
import org.junit.Test;
import pages.BookCreatePage;
import pages.BookEditPage;
import pages.IndexPage;
import pages.OfferCreatePage;
import pages.OfferEditPage;
import pages.RequestCreatePage;
import pages.RequestEditPage;
import pages.StudentCreatePage;
import pages.StudentEditPage;
import play.libs.F.Callback;
import play.test.TestBrowser;


public class ViewTest {
  public static final int PORT = 3333;

  @Test
  public void testIndexPage() {
    running(testServer(PORT, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
      @Override
      public void invoke(TestBrowser browser) {
        IndexPage homepage = new IndexPage(browser.getDriver(), PORT);
        browser.goTo(homepage);
        homepage.isAt();

        // return to homepage before clicking each link
        browser.goTo(homepage);
        homepage.gotoViewBooks();
        assertThat(homepage.title()).isEqualTo("Student Book Exchange | Find Books");

        browser.goTo(homepage);
        homepage.gotoViewStudents();
        assertThat(homepage.title()).isEqualTo("Student Book Exchange | Find Students");

        browser.goTo(homepage);
        homepage.gotoViewRequests();
        assertThat(homepage.title()).isEqualTo("Student Book Exchange | Find Requests");

        browser.goTo(homepage);
        homepage.gotoViewOffers();
        assertThat(homepage.title()).isEqualTo("Student Book Exchange | Find Offers");
      }
    });
  }

  @Test
  public void testBookCreatePage() {
    running(testServer(PORT, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
      @Override
      public void invoke(TestBrowser browser) {
        // Create the pages.
        BookCreatePage bookPage = new BookCreatePage(browser.getDriver(), PORT);
        // Now test the page.
        browser.goTo(bookPage);
        bookPage.isAt();
        String bookId = "Book-01";
        bookPage.createNewBook(bookId);

        // should redirect to book list
        assertThat(bookPage.title()).isEqualTo("Student Book Exchange | Find Books");
        assertThat(bookPage.pageSource()).contains(bookId);
      }
    });
  }

  @Test
  public void testBookEditPage() {
    running(testServer(PORT, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
      @Override
      public void invoke(TestBrowser browser) {
        // Create the pages.
        BookCreatePage bookPage = new BookCreatePage(browser.getDriver(), PORT);
        // Now test the page.
        browser.goTo(bookPage);
        bookPage.isAt();
        String bookId = "Book-01";
        bookPage.createNewBook(bookId);

        // should redirect to book list
        assertThat(bookPage.title()).isEqualTo("Student Book Exchange | Find Books");
        assertThat(bookPage.pageSource()).contains(bookId);

        // edit book
        BookEditPage editPage = new BookEditPage(browser.getDriver(), PORT, 1);
        browser.goTo(editPage);
        editPage.isAt();
        String newBookId = "Book-02";
        editPage.editBook(newBookId);

        // should redirect to book list
        assertThat(editPage.title()).isEqualTo("Student Book Exchange | Find Books");
        assertThat(editPage.pageSource()).contains(newBookId);

        // delete book
        browser.goTo(editPage);
        editPage.deleteBook();
        assertThat(editPage.pageSource()).doesNotContain(newBookId);
      }
    });
  }

  @Test
  public void testStudentCreatePage() {
    running(testServer(PORT, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
      @Override
      public void invoke(TestBrowser browser) {
        // Create the pages.
        StudentCreatePage studentPage = new StudentCreatePage(browser.getDriver(), PORT);
        // Now test the page.
        browser.goTo(studentPage);
        studentPage.isAt();
        String studentId = "Student-01";
        studentPage.createNewStudent(studentId);

        // should redirect to student list
        assertThat(studentPage.title()).isEqualTo("Student Book Exchange | Find Students");
        assertThat(studentPage.pageSource()).contains(studentId);
      }
    });
  }

  @Test
  public void testStudentEditPage() {
    running(testServer(PORT, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
      @Override
      public void invoke(TestBrowser browser) {
        // Create the pages.
        StudentCreatePage studentPage = new StudentCreatePage(browser.getDriver(), PORT);
        // Now test the page.
        browser.goTo(studentPage);
        studentPage.isAt();
        String studentId = "Student-01";
        studentPage.createNewStudent(studentId);

        // should redirect to student list
        assertThat(studentPage.title()).isEqualTo("Student Book Exchange | Find Students");
        assertThat(studentPage.pageSource()).contains(studentId);

        // edit student
        StudentEditPage editPage = new StudentEditPage(browser.getDriver(), PORT, 1);
        browser.goTo(editPage);
        editPage.isAt();
        String newStudentId = "Student-02";
        editPage.editStudent(newStudentId);

        // should redirect to student list
        assertThat(editPage.title()).isEqualTo("Student Book Exchange | Find Students");
        assertThat(editPage.pageSource()).contains(newStudentId);

        // delete student
        browser.goTo(editPage);
        editPage.deleteStudent();
        assertThat(editPage.pageSource()).doesNotContain(newStudentId);
      }
    });
  }

  @Test
  public void testRequestCreatePage() {
    running(testServer(PORT, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
      @Override
      public void invoke(TestBrowser browser) {
        // Create a book
        BookCreatePage bookPage = new BookCreatePage(browser.getDriver(), PORT);
        String bookId = "Book-01";
        browser.goTo(bookPage);
        bookPage.createNewBook(bookId);

        // Create a student
        StudentCreatePage studentPage = new StudentCreatePage(browser.getDriver(), 3333);
        String studentId = "Student-01";
        browser.goTo(studentPage);
        studentPage.createNewStudent(studentId);

        // Create a request
        RequestCreatePage requestPage = new RequestCreatePage(browser.getDriver(), PORT);
        String requestId = "Request-01";
        browser.goTo(requestPage);
        requestPage.createNewRequest(requestId, studentId, bookId);

        // should redirect to request list
        assertThat(requestPage.title()).isEqualTo("Student Book Exchange | Find Requests");
        assertThat(requestPage.pageSource()).contains(requestId);
      }
    });
  }

  @Test
  public void testRequestEditPage() {
    running(testServer(PORT, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
      @Override
      public void invoke(TestBrowser browser) {
        // Create a book
        BookCreatePage bookPage = new BookCreatePage(browser.getDriver(), PORT);
        String bookId = "Book-01";
        browser.goTo(bookPage);
        bookPage.createNewBook(bookId);

        // Create a student
        StudentCreatePage studentPage = new StudentCreatePage(browser.getDriver(), 3333);
        String studentId = "Student-01";
        browser.goTo(studentPage);
        studentPage.createNewStudent(studentId);

        // Create a request
        RequestCreatePage requestPage = new RequestCreatePage(browser.getDriver(), PORT);
        String requestId = "Request-01";
        browser.goTo(requestPage);
        requestPage.createNewRequest(requestId, studentId, bookId);

        // should redirect to request list
        assertThat(requestPage.title()).isEqualTo("Student Book Exchange | Find Requests");
        assertThat(requestPage.pageSource()).contains(requestId);

        // edit request
        RequestEditPage editPage = new RequestEditPage(browser.getDriver(), PORT, 1);
        browser.goTo(editPage);
        editPage.isAt();
        String newRequestId = "Request-02";
        editPage.editRequest(newRequestId, studentId, bookId);

        // should redirect to request list
        assertThat(requestPage.title()).isEqualTo("Student Book Exchange | Find Requests");
        assertThat(requestPage.pageSource()).contains(newRequestId);

        // delete request
        browser.goTo(editPage);
        editPage.deleteRequest();
        assertThat(editPage.pageSource()).doesNotContain(newRequestId);
      }
    });
  }

  @Test
  public void testOfferCreatePage() {
    running(testServer(PORT, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
      @Override
      public void invoke(TestBrowser browser) {
        // Create a book
        BookCreatePage bookPage = new BookCreatePage(browser.getDriver(), PORT);
        String bookId = "Book-01";
        browser.goTo(bookPage);
        bookPage.createNewBook(bookId);

        // Create a student
        StudentCreatePage studentPage = new StudentCreatePage(browser.getDriver(), 3333);
        String studentId = "Student-01";
        browser.goTo(studentPage);
        studentPage.createNewStudent(studentId);

        // Create a request
        OfferCreatePage offerPage = new OfferCreatePage(browser.getDriver(), PORT);
        String offerId = "Offer-01";
        browser.goTo(offerPage);
        offerPage.createNewOffer(offerId, studentId, bookId);

        // should redirect to offer list
        assertThat(offerPage.title()).isEqualTo("Student Book Exchange | Find Offers");
        assertThat(offerPage.pageSource()).contains(offerId);
      }
    });
  }

  @Test
  public void testOfferEditPage() {
    running(testServer(PORT, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
      @Override
      public void invoke(TestBrowser browser) {
        // Create a book
        BookCreatePage bookPage = new BookCreatePage(browser.getDriver(), PORT);
        String bookId = "Book-01";
        browser.goTo(bookPage);
        bookPage.createNewBook(bookId);

        // Create a student
        StudentCreatePage studentPage = new StudentCreatePage(browser.getDriver(), 3333);
        String studentId = "Student-01";
        browser.goTo(studentPage);
        studentPage.createNewStudent(studentId);

        // Create a request
        OfferCreatePage offerPage = new OfferCreatePage(browser.getDriver(), PORT);
        String offerId = "Offer-01";
        browser.goTo(offerPage);
        offerPage.createNewOffer(offerId, studentId, bookId);

        // should redirect to offer list
        assertThat(offerPage.title()).isEqualTo("Student Book Exchange | Find Offers");
        assertThat(offerPage.pageSource()).contains(offerId);

        // edit offer
        OfferEditPage editPage = new OfferEditPage(browser.getDriver(), PORT, 1);
        browser.goTo(editPage);
        editPage.isAt();
        String newOfferId = "Offer-02";
        editPage.editOffer(newOfferId, studentId, bookId);

        // should redirect to offer list
        assertThat(offerPage.title()).isEqualTo("Student Book Exchange | Find Offers");
        assertThat(offerPage.pageSource()).contains(newOfferId);

        // delete offer
        browser.goTo(editPage);
        editPage.deleteOffer();
        assertThat(editPage.pageSource()).doesNotContain(newOfferId);
      }
    });
  }
}