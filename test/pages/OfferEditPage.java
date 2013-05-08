package pages;

import static org.fest.assertions.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class OfferEditPage extends FluentPage {
  private String url;

  public OfferEditPage (WebDriver webDriver, int port, int primaryKey) {
    super(webDriver);
    this.url = "http://localhost:" + port + "/offers/" + primaryKey;
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public void isAt() {
    assertThat(title()).isEqualTo("Student Book Exchange | Edit Offer");
  }

  public void editOffer(String newOfferId, String newStudentId, String newBookId) {
    fill("#offerId").with(newOfferId);
    find("select", withId("studentId")).find("option", withText(newStudentId)).click();
    find("select", withId("bookId")).find("option", withText(newBookId)).click();
    fill("targetPrice").with("30");
    submit("#update");
  }

  public void deleteOffer() {
    click("#delete");
  }
}