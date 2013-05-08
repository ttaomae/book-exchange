package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.studentCreate;
import views.html.studentEdit;
import views.html.studentList;

public class Student extends Controller {
  public static Result index() {
    List<models.Student> students = models.Student.find().findList();
    return ok(studentList.render(students));
  }

  public static Result create() {
    models.Student defaults = new models.Student("Student-01", "StudentName", "StudentEmail");
    Form<models.Student> studentForm = form(models.Student.class).fill(defaults);
    return ok(studentCreate.render(studentForm));
  }

  public static Result save() {
    Form<models.Student> studentForm = form(models.Student.class).bindFromRequest();

    if (studentForm.hasErrors()) {
//      System.err.println("DEBUG: " + studentForm.errors());
      return badRequest(studentCreate.render(studentForm));
    }

    models.Student student = studentForm.get();
    String studentId = student.getStudentId();

    // if student with studentId is in database
    if (!models.Student.find().where().eq("studentId", studentId).findList().isEmpty()) {
//      System.err.println("DEBUG: found existing student with ID: " + studentId);
      return badRequest(studentCreate.render(studentForm));
    }

    student.save();
    return redirect(routes.Student.index());
  }

  public static Result edit(Long primaryKey) {
    models.Student student = models.Student.find().byId(primaryKey);

    if (student == null) {
      return notFound("Could not find student.");
    }

    Form<models.Student> studentForm = form(models.Student.class).fill(student);
    return ok(studentEdit.render(primaryKey, studentForm));
  }

  public static Result update(Long primaryKey) {
    Form<models.Student> studentForm = form(models.Student.class).bindFromRequest();
    if (studentForm.hasErrors()) {
//      System.err.println("DEBUG: " + studentForm.errors());
      return badRequest(studentEdit.render(primaryKey, studentForm));
    }

    studentForm.get().update(primaryKey);
    return redirect(routes.Student.index());
  }

  public static Result delete(Long primaryKey) {
    models.Student student = models.Student.find().byId(primaryKey);
    if (student != null) {
      student.delete();
    }
    return redirect(routes.Student.index());
  }
}
