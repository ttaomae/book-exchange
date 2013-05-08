package pages;

import static org.fest.assertions.Assertions.assertThat;
import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class IndexPage extends FluentPage {
  private String url;

  public IndexPage(WebDriver webDriver, int port) {
    super(webDriver);
    this.url = "http://localhost:" + port;
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public void isAt() {
    assertThat(title()).isEqualTo("Student Book Exchange | Homepage");
  }

  public void gotoViewBooks() {
    click("#viewBooks");
  }

  public void gotoViewStudents() {
    click("#viewStudents");
  }

  public void gotoViewRequests() {
    click("#viewRequests");
  }

  public void gotoViewOffers() {
    click("#viewOffers");
  }
}
