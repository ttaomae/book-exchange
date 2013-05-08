package pages;

import static org.fest.assertions.Assertions.assertThat;
import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class BookEditPage extends FluentPage {
  private String url;

  public BookEditPage (WebDriver webDriver, int port, int primaryKey) {
    super(webDriver);
    this.url = "http://localhost:" + port + "/books/" + primaryKey;
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public void isAt() {
    assertThat(title()).isEqualTo("Student Book Exchange | Edit Book");
  }

  public void editBook(String newBookId) {
    fill("#bookId").with(newBookId);
    fill("#title").with("NewTitle");
    fill("#price").with("20");
    fill("#isbn").with("9876543210");
    submit("#update");
  }

  public void deleteBook() {
    click("#delete");
  }
}