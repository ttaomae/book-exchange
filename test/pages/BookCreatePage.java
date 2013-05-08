package pages;

import static org.fest.assertions.Assertions.assertThat;
import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class BookCreatePage extends FluentPage {
  private String url;

  public BookCreatePage(WebDriver webDriver, int port) {
    super(webDriver);
    this.url = "http://localhost:" + port + "/books/create";
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public void isAt() {
    assertThat(title()).isEqualTo("Student Book Exchange | Create Book");
  }

  public void createNewBook(String bookId) {
    fill("#bookId").with(bookId);
    fill("#title").with("Title");
    fill("#price").with("30");
    fill("#isbn").with("1234567890");
    submit("#create");
  }
}
