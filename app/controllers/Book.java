package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Book extends Controller {

  public static Result index() {
    List<models.Book> books = models.Book.find().findList();
    return ok(bookList.render(books));
  }

  public static Result create() {
    models.Book defaults = new models.Book("Book-01", "BookTitle", 1234567890L, 30);
    Form<models.Book> bookForm = form(models.Book.class).fill(defaults);
    return ok(bookCreate.render(bookForm));
  }

  public static Result save() {
    Form<models.Book> bookForm = form(models.Book.class).bindFromRequest();
    if (bookForm.hasErrors()) {
      return badRequest(bookCreate.render(bookForm));
    }

    models.Book book = bookForm.get();
    book.save();
    return redirect(routes.Application.index());
  }

  public static Result edit(Long primaryKey) {
    models.Book book = models.Book.find().byId(primaryKey);
    Form<models.Book> bookForm = form(models.Book.class).fill(book);
    return ok(bookEdit.render(primaryKey, bookForm));
  }

  public static Result update(Long primaryKey) {
    Form<models.Book> bookForm = form(models.Book.class).bindFromRequest();
    if (bookForm.hasErrors()) {
      return badRequest(bookEdit.render(primaryKey, bookForm));
    }

    bookForm.get().update(primaryKey);
    return redirect(routes.Application.index());
  }

  public static Result delete(Long primaryKey) {
    models.Book.find().byId(primaryKey).delete();
    return redirect(routes.Application.index());
  }
}
