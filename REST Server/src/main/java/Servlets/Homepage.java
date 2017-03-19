/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.util.ArrayList;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;



@Path("/home")
public class Homepage {

  // This method is called if TEXT_PLAIN is request
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String sayPlainTextHello() {
    return "home";
  }

  // This method is called if XML is request
  @GET
  @Produces(MediaType.TEXT_XML)
  public String sayXMLHello() {
    return "<?xml version=\"1.0\"?>" + "<home> home" + "</home>";
  }
  
  @Path("/subfolder123/{id}")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.WILDCARD)
  public ArrayList postTest(@PathParam("id") int id, JsonObject g)
  {
      System.out.println("Here is the output: " + g.toString());
      System.out.println("Param for second: "+g.get("second"));
        
        ArrayList<String> gh = new ArrayList();
        gh.add("test 1");
        gh.add("test 2");
        return gh;
    }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String sayJSONHello()
  {
      return "{\"response\" : \"json\"}";
  }

  // This method is called if HTML is request
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String sayHtmlHello() {
      System.out.println("hit point");
    return "<html> " + "<title>" + "Delivery Mercenaries" + "</title>"
        + "<body><h1>" + "here is the homepage" + "</body></h1>" + "</html> ";
  }
}