package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.offerCreate;
import views.html.offerEdit;
import views.html.offerList;

public class Offer extends Controller {

  public static Result index() {
    List<models.Offer> offers = models.Offer.find().findList();
    return ok(offerList.render(offers));
  }

  public static Result create() {
    models.Offer defaults = new models.Offer("Offer-01", 30);
    Form<models.Offer> offerForm = form(models.Offer.class).fill(defaults);
    return ok(offerCreate.render(offerForm));
  }

  public static Result save() {
    Form<models.Offer> offerForm = form(models.Offer.class).bindFromRequest();
    if (offerForm.hasErrors()) {
      System.err.println("DEBUG: " + offerForm.errors());
      return badRequest(offerCreate.render(offerForm));
    }

    models.Offer offer = offerForm.get();
    String offerId = offer.getOfferId();

    // if offer with offerId is in database
    if (!models.Offer.find().where().eq("offerId", offerId).findList().isEmpty()) {
      System.err.println("DEBUG: found existing offer with ID: " + offerId);
      return badRequest(offerCreate.render(offerForm));
    }

    offer.save();
    return redirect(routes.Offer.index());
  }

  public static Result edit(Long primaryKey) {
    models.Offer offer = models.Offer.find().byId(primaryKey);

    if (offer == null) {
      return notFound("Could not find offer.");
    }

    Form<models.Offer> offerForm = form(models.Offer.class).fill(offer);
    return ok(offerEdit.render(primaryKey, offerForm));
  }

  public static Result update(Long primaryKey) {
    Form<models.Offer> offerForm = form(models.Offer.class).bindFromRequest();
    if (offerForm.hasErrors()) {
      System.err.println("DEBUG: " + offerForm.errors());
      return badRequest(offerEdit.render(primaryKey, offerForm));
    }

    offerForm.get().update(primaryKey);
    return redirect(routes.Offer.index());
  }

  public static Result delete(Long primaryKey) {
    models.Offer offer = models.Offer.find().byId(primaryKey);
    if (offer != null) {
      offer.delete();
    }
    return redirect(routes.Offer.index());
  }
}