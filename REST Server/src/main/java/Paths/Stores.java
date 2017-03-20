/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paths;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/stores")
public class Stores {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getPage() {
        //Placeholder for actual HTML response with webpage.
        return "<html>List of Stores: <br><div id=\"storeTable\"></div></html>";
    }
    
    @Path("list")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public <Account extends Object> String getList(@Context HttpServletRequest request) {
        //Placeholder for getting session info based on location or something
        Account account = (Account)request.getSession().getAttribute("account");
        //Placeholder for querying Database based on location info and getting the nearest stores
        String nearestStoresXml = "<stores><store name=\"Wal-Mart\"/><store name=\"Target\"/></stores>";
        return nearestStoresXml;
    }
    
    @Path("{store}/items/add/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public <ShoppingCart extends Object, Account extends Object> JsonObject addItemToCart(@Context HttpServletRequest request, 
                                                        @PathParam("store") int store, @PathParam("id") long id, String quantity)
    {
        System.out.println("Debug quantity: " + quantity);
        //Placeholder for shopping cart for current session
        ShoppingCart shoppingCart = (ShoppingCart)request.getSession().getAttribute("shoppingCart");
        
        //Placeholder for getting session info based on location or something
        Account account = (Account)request.getSession().getAttribute("account");
        
        
        // Use this info to put into SQL database or something
        java.sql.PreparedStatement putIntoShoppingCart = null;
        String sql = "update " + "Database" + "SHOPPING_CART "
                + "set ID = ?, QUANTITY = ?, STORE = ?"
                + "where SHOPID = ?";
        
        boolean success = false;
        
        // Update ID to id value, QUANTITY to quantity value, STORE to store value, SHOPID to some field in the shoppingCart data model
        // Prepare statement and execute with try / catch. If success, success is true.
        success = true;
        JsonObject status = new JsonObject();
        status.addProperty("success", success);
        return status;
        
    }
    
    @Path("{store}/items/delete/{cartItemId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public <ShoppingCart extends Object, Account extends Object> JsonObject deleteItemFromCart(@Context HttpServletRequest request, 
                                                       @PathParam("store") int store, @PathParam("cartItemId") long id)
    {
        //Placeholder for shopping cart for current session
        ShoppingCart shoppingCart = (ShoppingCart)request.getSession().getAttribute("shoppingCart");
        
        //Placeholder for getting session info based on location or something
        Account account = (Account)request.getSession().getAttribute("account");
        
        
        // Use this info to put into SQL database or something
        java.sql.PreparedStatement putIntoShoppingCart = null;
        String sql = "DELETE FROM " + "Database" + "SHOPPING_CART "
                + "WHERE ID = ? AND SHOPID = ?";
        
        boolean success = false;
        
        // Update ID to id value, SHOPID to some field in the shoppingCart data model
        // Prepare statement and execute with try / catch. If success, success is true.
        success = true;
        JsonObject status = new JsonObject();
        status.addProperty("success", success);
        return status;
        
    }
    

}
