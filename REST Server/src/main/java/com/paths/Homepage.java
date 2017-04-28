/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paths;

import io.swagger.annotations.Api;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api
@Path("/home")
public class Homepage {

    @GET
    @Produces(MediaType.TEXT_XML)
    public String sayXMLHello() {
        return "<?xml version=\"1.0\"?>" + "<home> home" + "</home>";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String sayJSONHello() {
        return "{\"response\" : \"home\"}";
    }

    // This method is called if HTML is request
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getHomepage() {
        return "<html>\n"
                + "    <head>\n"
                + "        <title>Delivery Mercenaries Homepage</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <div>Here is the homepage</div>\n"
                + "    </body>\n"
                + "</html>";
    }
}
