package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.requestCreate;
import views.html.requestEdit;
import views.html.requestList;

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
      System.err.println("DEBUG: " + requestForm.errors());
      return badRequest(requestCreate.render(requestForm));
    }

    models.Request request = requestForm.get();
    String requestId = request.getRequestId();

    // if offer with offerId is in database
    if (!models.Request.find().where().eq("requestId", requestId).findList().isEmpty()) {
      System.err.println("DEBUG: found existing request with ID: " + requestId);
      return badRequest(requestCreate.render(requestForm));
    }

    request.save();
    return redirect(routes.Request.index());
  }

  public static Result edit(Long primaryKey) {
    models.Request request = models.Request.find().byId(primaryKey);
    Form<models.Request> requestForm = form(models.Request.class).fill(request);
    return ok(requestEdit.render(primaryKey, requestForm));
  }

  public static Result update(Long primaryKey) {
    Form<models.Request> requestForm = form(models.Request.class).bindFromRequest();
    if (requestForm.hasErrors()) {
      System.err.println("DEBUG: " + requestForm.errors());
      return badRequest(requestEdit.render(primaryKey, requestForm));
    }

    requestForm.get().update(primaryKey);
    return redirect(routes.Request.index());
  }

  public static Result delete(Long primaryKey) {
    models.Request.find().byId(primaryKey).delete();
    return redirect(routes.Request.index());
  }
}
