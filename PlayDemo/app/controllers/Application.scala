package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import views._




object Application extends Controller {

  def index = listContacts

  def listContacts() = Action {
    Ok(views.html.index(Contact.list(), contactForm))
  }

  def createContact() = Action { implicit request => contactForm.bindFromRequest.fold(
    errors => BadRequest(views.html.index(Contact.list(), errors))
    contact => {
      Contact.create(contact)
      Redirect(routes.Application.listContacts)
    }
    )
  }

  def deleteContact(id: Long) = Action {
    Contact.delete(id)
    Redirect(routes.)
  }
}