package pages;

import static org.fest.assertions.Assertions.assertThat;
import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class StudentEditPage extends FluentPage {
  private String url;

  public StudentEditPage (WebDriver webDriver, int port, int primaryKey) {
    super(webDriver);
    this.url = "http://localhost:" + port + "/students/" + primaryKey;
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public void isAt() {
    assertThat(title()).isEqualTo("Student Book Exchange | Edit Student");
  }

  public void editStudent(String newStudentId) {
    fill("#studentId").with(newStudentId);
    fill("#name").with("NewName");
    fill("#email").with("NewEmail");
    submit("#update");
  }

  public void deleteStudent() {
    click("#delete");
  }
}