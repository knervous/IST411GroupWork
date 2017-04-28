package controllers;

import models.User;
import play.db.DB;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class DBController {


    public DBController(){

    }

    public void init(){
        Connection connection = DB.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS PAUL;\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS PAUL.USERS\n" +
                "(\n" +
                "  USER_ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,\n" +
                "  username VARCHAR(255) NOT NULL,\n" +
                "  password VARCHAR(255) NOT NULL,\n" +
                "  firstname VARCHAR(255),\n" +
                "  lastname VARCHAR(255),\n" +
                "  email VARCHAR(255),\n" +
                "  gender VARCHAR(50),\n" +
                "  creationdate DATE,\n" +
                "  comments VARCHAR(255),\n" +
                "  picture VARCHAR(255)\n" +
                ");\n" +
                "\n" +
                "CREATE UNIQUE INDEX IF NOT EXISTS table_name_USER_ID_uindex ON PAUL.USERS (USER_ID);\n");
        ) {
            pstmt.executeUpdate();
            System.out.println("Creating database if not exists");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getList(){
        ArrayList<User> users = new ArrayList();
        try(Connection con = DB.getConnection();){
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM PAUL.USERS");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next())
            {
                User u = new User().instantiate(rs);
                users.add(u);
            }
        }catch(Exception e){}

        return users;
    }


    public void createNew(User u)
    {
        try(Connection con = DB.getConnection();){
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO PAUL.USERS VALUES(null,'"+u.getUsername()+"','"+u.getPassword()+"'," +
                    "'"+u.getFirstName()+"','"+u.getLastName()+"','"+u.getEmail()+"','"+u.getGender()+"','"+u.getCreationDate()+"','"+u.getComments()+"','"+u.getPicture()+"')");
            int z = pstmt.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }


}