package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Student extends Controller {
  public static Result index() {
    List<models.Student> students = models.Student.find().findList();
    return ok(students.isEmpty() ? "No students" : students.toString());
  }

  public static Result details(String studentId) {
    models.Student student = models.Student.find().where().eq("studentId", studentId).findUnique();
    return (student == null) ? notFound("No student found") : ok(student.toString());
  }

  public static Result newStudent() {
    // Create a Student form and bind the request variables to it.
    Form<models.Student> studentForm = form(models.Student.class).bindFromRequest();
    // Validate the form values.
    if (studentForm.hasErrors()) {
      return badRequest("Student ID, name, and email requred");
    }

    // form is OK, so make a Student and save it.
    models.Student student = studentForm.get();
    student.save();
    return ok(student.toString());
  }

  public static Result delete(String studentId) {
    models.Student student = models.Student.find().where().eq("studentId", studentId).findUnique();
    if (student != null) {
      student.delete();
    }
    return ok();
  }
}
