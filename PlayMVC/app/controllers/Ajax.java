package controllers;

import com.google.gson.JsonObject;
import models.User;
import models.UserGenerator;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import views.html.*;

import java.util.ArrayList;

public class Ajax extends Controller {

    public static Result create(){

        System.out.println("Request: " + request().toString());

        return ok("c");
    }

    public static Result edit(){

        return ok("b");
    }

    public static Result delete(int id){

        return ok("a");
    }


}
