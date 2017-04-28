package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.User;
import models.UserGenerator;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import views.html.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Ajax extends Controller {

    public static Result create(String user){
        DBController db = new DBController();
        db.createNew(parseIt(user));

        return ok("c");
    }

    public static Result update(String user){
        DBController db = new DBController();
        User u = parseIt(user);
        u.setUserId(Integer.parseInt(session().get("id") == null ? "0" : session().get("id")));
        db.updateUser(u);

        return ok("c");
    }

    public static Result delete(int id){

        DBController db = new DBController();
        db.deleteUser(id);
        session().remove("id");
        return ok("a");
    }

    public static Result login(String user, String pass)
    {
        DBController db = new DBController();
        int id = db.tryLogin(user,pass);
        System.out.println("ID VAL: " + id);
        if(id < 0)
        {
            return ok("fail");
        }
        else{
            session().put("id", String.valueOf(id));
            return redirect("/list");
        }
    }

    private static User parseIt(String user)
    {
        JsonObject u = new JsonParser().parse(user).getAsJsonObject();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());
        return new User(0,u.get("username").getAsString(), u.get("password").getAsString(),u.get("firstname").getAsString(), u.get("lastname").getAsString(), u.get("email").getAsString(),
                u.get("gender").getAsString(), formatted, u.get("comments").getAsString(), "..");
    }


}
