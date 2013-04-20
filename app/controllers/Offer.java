package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Offer extends Controller {

  public static Result index() {
    List<models.Offer> offers= models.Offer.find().findList();
    return ok(offers.isEmpty() ? "No offers" : offers.toString());
  }

  public static Result details(String offerId) {
    models.Offer offer = models.Offer.find().where().eq("offerId", offerId).findUnique();
    return (offer == null) ? notFound("No offer found") : ok(offer.toString());
  }

  public static Result newOffer() {
    // Create an Offer form and bind the request variables to it.
    Form<models.Offer> offerForm = form(models.Offer.class).bindFromRequest();
    // Validate the form values.
    if (offerForm.hasErrors()) {
      return badRequest("Offer ID, student, and book required");
    }

    // form is OK, so make an Offer and save it
    models.Offer offer = offerForm.get();
    offer.save();
    return ok(offer.toString());
  }

  public static Result delete(String offerId) {
    models.Offer offer = models.Offer.find().where().eq("offerId", offerId).findUnique();
    if (offer != null) {
      offer.delete();
    }
    return ok();
  }
}
