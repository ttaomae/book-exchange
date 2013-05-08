package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Request extends Controller {

  public static Result index() {
    List<models.Request> requests = models.Request.find().findList();
    return ok(requestList.render(requests));
  }

  public static Result create() {
    models.Request defaults = new models.Request("Request-01", 30);
    Form<models.Request> requestForm = form(models.Request.class).fill(defaults);
    return ok(requestCreate.render(requestForm));
  }

  public static Result save() {
    Form<models.Request> requestForm = form(models.Request.class).bindFromRequest();
    if (requestForm.hasErrors()) {
      System.err.println("DEBUG: bad reqeust" + requestForm.errors());
      return badRequest(requestCreate.render(requestForm));
    }

    models.Request request = requestForm.get();
    request.save();
    return redirect(routes.Application.index());
  }

  public static Result edit(Long primaryKey) {
    models.Request request = models.Request.find().byId(primaryKey);
    Form<models.Request> requestForm = form(models.Request.class).fill(request);
    return ok(requestEdit.render(primaryKey, requestForm));
  }
  
  public static Result update(Long primaryKey) {
    Form<models.Request> requestForm = form(models.Request.class).bindFromRequest();
    if (requestForm.hasErrors()) {
      return badRequest(requestEdit.render(primaryKey, requestForm));
    }

    requestForm.get().update(primaryKey);
    return redirect(routes.Application.index());
  }

  public static Result delete(Long primaryKey) {
    models.Request.find().byId(primaryKey).delete();
    return redirect(routes.Application.index());
  }
}
