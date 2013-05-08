package pages;

import static org.fest.assertions.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class RequestCreatePage extends FluentPage {
  private String url;

  public RequestCreatePage(WebDriver webDriver, int port) {
    super(webDriver);
    this.url = "http://localhost:" + port + "/requests/create";
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public void isAt() {
    assertThat(title()).isEqualTo("Student Book Exchange | Create Request");
  }

  public void createNewRequest(String requestId, String studentId, String bookId) {
    fill("#requestId").with(requestId);
    find("select", withId("studentId")).find("option", withText(studentId)).click();
    find("select", withId("bookId")).find("option", withText(bookId)).click();
    fill("targetPrice").with("30");
    submit("#create");
  }
}
