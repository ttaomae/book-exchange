package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

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
      return badRequest(studentCreate.render(studentForm));
    }

    models.Student student = studentForm.get();
    student.save();
    return redirect(routes.Application.index());
  }

  public static Result edit(Long primaryKey) {
    models.Student student= models.Student.find().byId(primaryKey);
    Form<models.Student> studentForm = form(models.Student.class).fill(student);
    return ok(studentEdit.render(primaryKey, studentForm));
  }

  public static Result update(Long primaryKey) {
    Form<models.Student> studentForm = form(models.Student.class).bindFromRequest();
    if (studentForm.hasErrors()) {
      return badRequest(studentEdit.render(primaryKey, studentForm));
    }

    studentForm.get().update(primaryKey);
    return redirect(routes.Application.index());
  }
  
  public static Result delete(Long primaryKey) {
    models.Student.find().byId(primaryKey).delete();
    return redirect(routes.Application.index());
  }
}
