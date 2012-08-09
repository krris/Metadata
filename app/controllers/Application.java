package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.about;
import views.html.contact;
import views.html.index;

public class Application extends Controller 
{

  public static Result index() 
  {
	  String errorMessage = flash("error");
	  return ok(index.render(errorMessage));
  }

  public static Result about()
  {
	  return ok(about.render("About"));
  }
  
  public static Result contact()
  {
	  return ok(contact.render("Contacts"));
  }
  
  
  
}
