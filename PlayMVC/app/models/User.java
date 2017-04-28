package models;


import java.util.Date;
import java.sql.*;


public class User {

    private int userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String picture;
    private String creationDate;
    private String gender;
    private String comments;


    public User() {
        username = "na";
        password = "na";
        firstName = "na";
        lastName = "na";
        email = "na";
        picture = null;
        creationDate = new Date().toString();
        gender = "na";
        comments = "na";
    }

    public User(int userId, String userName, String password, String firstName, String lastName, String email, String gender, String creationDate, String comments, String picture) {
        this.setUserId(userId);
        this.username = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.creationDate = creationDate;
        this.comments = comments;
        this.picture = picture;
    }

    public User instantiate(ResultSet rs) {
        User u = new User();
        try {
            u = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(6), rs.getString(7), rs.getDate(8).toString(),
                    rs.getString(9), rs.getString(10));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}