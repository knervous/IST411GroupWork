package models;

import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Paul on 4/27/2017.
 */

public class UserGenerator {

    private final static String[] firstNames = {"Adam","Dom","Delbert","Chuck","Arnado","Dan","Devin", "Eloisa", "Ina", "Takisha", "Marietta","Cecelia"};
    private final static String[] lastNames = {"Smith","West","Miller","Ruzicka","Carrera","Nost","Ono","Carrell","Sommerfeld","Schwarzenegger"};
    private final static String[] gender = {"Male","Female","Whatever"};



    public static User generateRandom(){
        Random r = new Random();
        String first = firstNames[r.nextInt(firstNames.length)];
        String last = lastNames[r.nextInt(lastNames.length)];
        String gen = gender[r.nextInt(gender.length)];
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());

// Output "2012-09-26"
        User u = new User(0, "","",first,last,"Fake Email",gen,formatted,"comments here", "path");

        return u;
    }


}
