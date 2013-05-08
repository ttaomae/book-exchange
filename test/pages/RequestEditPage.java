package pages;

import static org.fest.assertions.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class RequestEditPage extends FluentPage {
  private String url;

  public RequestEditPage (WebDriver webDriver, int port, int primaryKey) {
    super(webDriver);
    this.url = "http://localhost:" + port + "/requests/" + primaryKey;
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public void isAt() {
    assertThat(title()).isEqualTo("Student Book Exchange | Edit Request");
  }

  public void editRequest(String newRequestId, String newStudentId, String newBookId) {
    fill("#requestId").with(newRequestId);
    find("select", withId("studentId")).find("option", withText(newStudentId)).click();
    find("select", withId("bookId")).find("option", withText(newBookId)).click();
    fill("targetPrice").with("30");
    submit("#update");
  }

  public void deleteRequest() {
    click("#delete");
  }
}