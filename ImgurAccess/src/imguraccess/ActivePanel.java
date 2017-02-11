/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imguraccess;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import javax.imageio.ImageIO;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author Paul
 */
public class ActivePanel extends ViewPane {

    private final JButton upload = new JButton("Upload from PC");
    private final JButton download = new JButton("Search Imgur");
    private final JButton random = new JButton("Random Pic");
    private final JButton browser = new JButton("Open in Browser");
    private final ReentrantLock lock = new ReentrantLock();
    private final JTextField searchParam = new JTextField("Enter Query");
    private final JTextField outputUrl = new JTextField("placeholder url");
    private String title = "";

    private BufferedImage displayImage = new BufferedImage(400, 370, BufferedImage.TYPE_INT_RGB);

    public ActivePanel() {
        init();
    }

    private void init() {
        add(upload);
        add(download);
        add(searchParam);
        add(random);
        add(outputUrl);
        add(browser);
        setLayout(null);

        Requester r = new Requester();

        upload.setBounds(100, 100, 150, 30);
        random.setBounds(100, 200, 150, 30);
        download.setBounds(100, 350, 150, 30);
        searchParam.setBounds(100, 300, 150, 30);
        outputUrl.setBounds(320, 70, 170, 30);
        browser.setBounds(520, 70, 150, 30);

        random.addActionListener((e) -> {
            r.setHttp("https://api.imgur.com/3/gallery/random/random/" + new Random().nextInt(50), "GET");
            new Thread(r).start();
        });

        upload.addActionListener((e) -> {
            r.setHttp("", "POST");
            new Thread(r).start();
        });

        download.addActionListener((e) -> {
            r.setHttp("https://api.imgur.com/3/gallery/search/viral/all/" + new Random().nextInt(50) + "?q_all=" + searchParam.getText().replaceAll("\\s", "%20"), "GET");
            new Thread(r).start();
        });

        browser.addActionListener((e) -> {
            try {
                Desktop desk = Desktop.getDesktop() != null ? Desktop.getDesktop() : null;
                if (desk != null && desk.isSupported(Desktop.Action.OPEN) && desk.isSupported(Desktop.Action.BROWSE)) {
                    desk.browse(new URL(outputUrl.getText()).toURI());
                } else {
                    JDialog dialog = new JDialog(new JFrame(), "Desktop environment not supported");
                }
            } catch (URISyntaxException | IOException | HeadlessException ze) {
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(51,102,155));
        g2d.fillRoundRect(295, 105, 410, 380, 20, 20);
        g2d.setColor(Color.BLACK);
        g2d.fillRoundRect(300, 110, 400, 370, 20, 20);
        g2d.drawImage(displayImage, 300, 110, null);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Title: ", 300, 515);
        g2d.drawString(title, 330, 515);
    }

    private class Requester implements Runnable {

        private String endpoint, httpMethod;
        private final Object lock = new Object();
        private final ReentrantLock rLock = new ReentrantLock();
        private int timeoutCounter = 0;

        @Override
        public void run() {
            synchronized (lock) {
                if (httpMethod.equals("GET")) {
                    makeRequest();
                } else {
                    makePost();
                }
            }
        }

        public void setHttp(String... args) {
            endpoint = args[0];
            httpMethod = args[1];
        }

        public void makePost() {
            rLock.lock();
            try {
                final JFileChooser fc = new JFileChooser();
                fc.showOpenDialog(ActivePanel.this);
                File file = fc.getSelectedFile();

                //read image
                if(file == null)
                    return;
                BufferedImage image = ImageIO.read(file);
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                ImageIO.write(image, "png", byteArray);
                byte[] byteImage = byteArray.toByteArray();
                String dataImage = Base64.encode(byteImage);
                String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");
                URL url;
                url = new URL("https://api.imgur.com/3/image");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Authorization", "Client-ID " + "731092c858c169a");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                conn.connect();
                StringBuilder stb = new StringBuilder();
                BufferedReader rd;
                try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());) {
                    wr.write(data);
                    wr.flush();
                    // Get the response
                    rd = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        stb.append(line).append("\n");
                    }
                }
                rd.close();
                JsonObject obj = new JsonParser().parse(stb.toString()).getAsJsonObject().get("data").getAsJsonObject();
                String picUrl = obj.get("link").getAsString();
                String id = obj.get("id").getAsString();
                outputUrl.setText("https://www.imgur.com/gallery/" + id);
                StringBuilder sb = new StringBuilder(picUrl);
                sb.insert(picUrl.length() - 4, "m");
                picUrl = sb.toString();
                URL downloadUrl = new URL(picUrl);
                displayImage = ImageIO.read(downloadUrl);
                title = "Your uploaded pic to imgur, congrats";
                ActivePanel.this.repaint();

            } catch (HeadlessException | IOException | JsonSyntaxException ze) {
            } finally {
                rLock.unlock();
            }
        }

        public void makeRequest() {
            rLock.lock();
            try {
                URL url;
                url = new URL(endpoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Client-ID " + "731092c858c169a");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.connect();

                // Get the response
                StringBuilder stb = new StringBuilder();
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = rd.readLine()) != null) {
                        stb.append(line).append("\n");
                    }
                }
                JsonArray images = new JsonParser().parse(stb.toString())
                        .getAsJsonObject()
                        .get("data").getAsJsonArray();

                if (images.size() < 1) {
                    System.out.println("No results!");
                    return;
                }

                for (JsonElement gz : images) {

                    JsonObject temp = (JsonObject) gz;
                    if (!temp.get("is_album").getAsBoolean() && !temp.get("animated").getAsBoolean()) {
                        if (new Random().nextInt(10) > 2) {
                            break;
                        }
                        System.out.println("INFO: " + temp);
                        String picUrl = temp.get("link").getAsString();
                        String id = temp.get("id").getAsString();
                        outputUrl.setText("https://www.imgur.com/gallery/" + id);
                        StringBuilder sb = new StringBuilder(picUrl);
                        sb.insert(picUrl.length() - 4, "m");
                        picUrl = sb.toString();
                        URL downloadUrl = new URL(picUrl);
                        displayImage = ImageIO.read(downloadUrl);
                        title = temp.get("title").getAsString();
                        ActivePanel.this.repaint();
                        return;
                    }
                }
                System.out.println("Trying again for randomness");
                timeoutCounter++;
                if (timeoutCounter % 30 != 0) {
                    makeRequest();
                }

            } catch (IOException | JsonSyntaxException ze) {
                ze.printStackTrace();

            } finally {
                rLock.unlock();
            }

        }

    }

}
