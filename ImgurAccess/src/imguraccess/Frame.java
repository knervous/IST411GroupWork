/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imguraccess;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Paul
 */
public class Frame extends UIFrame {
    
    private final JPanel panel = new JPanel(){};
    private final JButton upload = new JButton("Upload from PC");
    private final JButton download = new JButton("Download from Imgur");
    
    public Frame()
    {
        
        panel.setBackground(Color.BLUE);
        panel.setLayout(null);
        panel.add(upload);
        panel.add(download);
        upload.setBounds(50,50,150,50);
        download.setBounds(400,50,150,50);
        upload.addActionListener(new IUpload());
        download.addActionListener(new IDownload());
        
    }
    
    private class IDownload implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
            URL url;
                url = new URL("https://api.imgur.com/3/gallery/search/?q=lol");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Client-ID " + "731092c858c169a");
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                conn.connect();
                StringBuilder stb = new StringBuilder();
//                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//                wr.write(data1);
//                wr.flush();

                // Get the response
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    stb.append(line).append("\n");
                }
//                wr.close();
                rd.close();
                JsonArray images = new JsonParser().parse(stb.toString())
                        .getAsJsonObject()
                        .get("data").getAsJsonArray();

                for(JsonElement gz : images)
                {
                    JsonObject temp = (JsonObject) gz;
                    System.out.println("ELEMENT ID: " + temp.get("id").getAsString());
                }
                
                System.out.println("RESPONSE FROM SERVER: "+stb.toString());
                
            }
            catch(Exception ze){}
        }
    }
    
    private class IUpload implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
           final JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(panel);
            try {
                BufferedImage image = null;
                File file = fc.getSelectedFile();
                //read image
                image = ImageIO.read(file);
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                ImageIO.write(image, "png", byteArray);
                byte[] byteImage = byteArray.toByteArray();
                String dataImage = Base64.encode(byteImage);
                String data1 = URLEncoder.encode("image", "UTF-8") + "="
                        + URLEncoder.encode(dataImage, "UTF-8");
                URL url;
                url = new URL("https://api.imgur.com/3/image");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Client-ID " + "731092c858c169a");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                conn.connect();
                StringBuilder stb = new StringBuilder();
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data1);
                wr.flush();

                // Get the response
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    stb.append(line).append("\n");
                }
                wr.close();
                rd.close();
                
                System.out.println("RESPONSE FROM SERVER: "+stb.toString());
//                org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
//                org.json.simple.JSONObject data = (org.json.simple.JSONObject) parser.parse(stb.toString());
//                String link = data.get("data").toString();
//                org.json.simple.JSONObject linkJ = (org.json.simple.JSONObject) parser.parse(link);
//
//                createUser.setPicUrl(linkJ.get("link").toString());

            } catch (Exception ze) {

                try {
//                    URL url = new URL(createUser.getPicUrl());
//                    Image image = ImageIO.read(url);
//                    //BufferedImage temp = ImageIO.read(fc.getSelectedFile());
//                    BufferedImage temp = ImageIO.read(url);
//
//                    createUser.setProfilePic(temp);
//                    createUser.repaint();

                } catch (Exception ez) {
//                    JDialog popup = new JDialog(frame, "File is not a valid image file!");
                }
            }
        }
    }
    
}
