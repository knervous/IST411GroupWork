package controllers;

import com.google.gson.Gson;
import models.User;
import models.UserGenerator;
import play.mvc.*;
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
        Html content = home.render("Welcome to Account Land", getSessionName(), db.getList().size());
        return ok(index.render(getSessionName(),content));
    }

    public static Result list(){
        ArrayList<User> userList = new DBController().getList();
        Html content = list.render(userList);
       return ok(index.render(getSessionName(), content));
    }

    public static Result create(){
        Html content = create.render("hello");
        return ok(index.render(getSessionName(), content));
    }

    public static Result edit(){
        if(session().get("id") == null){
            return ok(index.render(getSessionName(), Html.apply("You are not logged in!")));
        }else{
            int id = Integer.parseInt(session().get("id"));
            System.out.println("ID VAL: " + id);
            DBController db = new DBController();
            User u = db.getSingle(id);
            System.out.println("SINGLE: " + u.getFirstName());
            Html content = edit.render("edit page", u);
            return ok(index.render(getSessionName(), content));
        }
    }

    public static Result login(){
        Html content = login.render("login");
        return ok(index.render(getSessionName(), content));
    }

    private static String getSessionName(){
        if(session().get("id") == null){
            return "None";
        }else{
            int id = Integer.parseInt(session().get("id"));
            DBController db = new DBController();
            return db.getFullName(id);
        }
    }

}