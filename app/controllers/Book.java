package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.bookCreate;
import views.html.bookEdit;
import views.html.bookList;

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
      System.err.println("DEBUG: " + bookForm.errors());
      return badRequest(bookCreate.render(bookForm));
    }

    models.Book book = bookForm.get();
    String bookId = book.getBookId();

    // if book with bookId is in database
    if (!models.Book.find().where().eq("bookId", bookId).findList().isEmpty()) {
      System.err.println("DEBUG: found existing book with ID: " + bookId);
      return badRequest(bookCreate.render(bookForm));
    }

    book.save();
    return redirect(routes.Book.index());
  }

  public static Result edit(Long primaryKey) {
    models.Book book = models.Book.find().byId(primaryKey);

    if (book == null) {
      return notFound("Could not find book.");
    }

    Form<models.Book> bookForm = form(models.Book.class).fill(book);
    return ok(bookEdit.render(primaryKey, bookForm));
  }

  public static Result update(Long primaryKey) {
    Form<models.Book> bookForm = form(models.Book.class).bindFromRequest();
    if (bookForm.hasErrors()) {
      System.err.println("DEBUG: " + bookForm.errors());
      return badRequest(bookEdit.render(primaryKey, bookForm));
    }

    bookForm.get().update(primaryKey);
    return redirect(routes.Book.index());
  }

  public static Result delete(Long primaryKey) {
    models.Book book = models.Book.find().byId(primaryKey);
    if (book != null) {
      book.delete();
    }
    return redirect(routes.Book.index());
  }
}
