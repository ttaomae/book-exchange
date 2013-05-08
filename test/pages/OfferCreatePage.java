package pages;

import static org.fest.assertions.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class OfferCreatePage extends FluentPage {
  private String url;

  public OfferCreatePage(WebDriver webDriver, int port) {
    super(webDriver);
    this.url = "http://localhost:" + port + "/offers/create";
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public void isAt() {
    assertThat(title()).isEqualTo("Student Book Exchange | Create Offers");
  }

  public void createNewOffer(String offerId, String studentId, String bookId) {
    fill("#requestId").with(offerId);
    find("select", withId("studentId")).find("option", withText(studentId)).click();
    find("select", withId("bookId")).find("option", withText(bookId)).click();
    fill("targetPrice").with("30");
    submit("#create");
  }
}
