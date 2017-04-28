
package db;

import com.google.gson.JsonObject;

import java.sql.*;


public class Database {
    public Database() {
    }

    public boolean create(JsonObject user)
    {

        return false;
    }

    public boolean update(JsonObject user)
    {

        return false;
    }

    public JsonObject read(String user,String pass)
    {
        JsonObject found = new JsonObject();

        return found;
    }

    public boolean delete(int id)
    {
        return false;
    }


}