package controllers;

import models.User;
import models.UserGenerator;
import play.*;
import play.api.libs.concurrent.Akka;
import play.db.DB;
import play.mvc.*;


import java.sql.*;
import java.util.ArrayList;

import play.twirl.api.Html;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        DBController db = new DBController();
        db.init();
        db.createNew(UserGenerator.generateRandom());
        db.createNew(UserGenerator.generateRandom());
        db.createNew(UserGenerator.generateRandom());
        Html content = home.render("Welcome to Account Land");
        Result htmlResult = ok("<h1>Hello World!</h1>").as("text/html");
        return ok(index.render("sesh",content));
    }

    public static Result list(){
        ArrayList<User> userList = new DBController().getList();
        Html content = list.render(userList);
        System.out.println("SIZE OF USER LIST: " + userList.size());
       return ok(index.render("sesh", content));
    }

    public static Result create(){
        Html content = create.render("hello");
        return ok(index.render("sesh", content));
    }

    public static Result edit(){
        Html content = edit.render("edit page");
        return ok(index.render("sesh", content));
    }






}
