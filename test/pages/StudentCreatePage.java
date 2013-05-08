package pages;

import static org.fest.assertions.Assertions.assertThat;
import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class StudentCreatePage extends FluentPage {
  private String url;

  public StudentCreatePage(WebDriver webDriver, int port) {
    super(webDriver);
    this.url = "http://localhost:" + port + "/students/create";
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public void isAt() {
    assertThat(title()).isEqualTo("Student Book Exchange | Create Student");
  }

  public void createNewStudent(String studentId) {
    fill("#studentId").with(studentId);
    fill("#name").with("Name");
    fill("#email").with("Email");
    submit("#create");
  }
}
