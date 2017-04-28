/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paths;

import io.swagger.annotations.Api;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api
@Path("/search")
public class Search {

    @GET
    @Path("zip/{zip}")
    @Produces(MediaType.APPLICATION_XML)
    public String getZip(@PathParam("zip") String zip) throws Exception {
        
        /*
            Here we are ironically calling a SOAP method from another web service
            in order to get location info based on ZIP.
            Ideally, we'd use this info to check against a local store DB to match 
            service locations.
        */
        
        HttpURLConnection connection = null;
        URL url = new URL("http://www.webservicex.net/uszip.asmx/GetInfoByZIP?USZip=" + zip);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Length",
                Integer.toString(zip.length()));
        connection.setRequestProperty("Content-Language", "en-US");
        connection.setUseCaches(false);
        connection.setDoOutput(true);

        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String xml = new String();
        String line;
        while ((line = rd.readLine()) != null) {
            xml += line;
        }
        rd.close();
        return xml;
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
    public ArrayList postTest(@PathParam("id") int id, JsonObject g) {
        System.out.println("Here is the output: " + g.toString());
        System.out.println("Param for second: " + g.get("second"));

        ArrayList<String> gh = new ArrayList();
        gh.add("test 1");
        gh.add("test 2");
        return gh;
    }
}
