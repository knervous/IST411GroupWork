
package imguraccess;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ViewPane extends JPanel {

    private BufferedImage backgroundFrame;
    private BufferedImage background;
    private BufferedImage exit;
    private int lWidth;
    private int lHeight;
    protected GridBagConstraints constraints;
    private final RootMover rm = new RootMover();
    protected JFrame parent;

    public ViewPane() {
        init("Images/silverframe.png", "Images/background.jpg");
    }

    public ViewPane(String frame, String bg) {
        init(frame, bg);
    }

    private void init(String frame, String bg) {
        setOpaque(false);
        setLayout(null);
        this.setPreferredSize((new Dimension(800, 600)));
        this.addMouseListener(rm);
        this.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();

        try {
            backgroundFrame = ImageIO.read(new File(frame));

        } catch (Exception e) {

        }

        try {
            background = ImageIO.read(new File(bg));
        } catch (Exception e) {

        }
        try {
            exit = ImageIO.read(new File("Images/exit.png"));
        } catch (Exception e) {

        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        lWidth = getPreferredSize().width;
        lHeight = getPreferredSize().height;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(153, 196, 210));
        g2d.drawImage(background, 20, 20, getParent().getWidth() - 40, getParent().getHeight() - 40, null);
        g2d.drawImage(backgroundFrame, 0, 0, getParent().getWidth(), getParent().getHeight(), null);
        g2d.drawImage(exit, lWidth - 60, 10, 45, 45, null);
        //g2d.fillRoundRect(53, 40, (int) (getParent().getWidth() - 106), (int) (getParent().getHeight() - 80), 20, 20);

        g2d.dispose();

    }

    public RootMover getRootMover() {
        return rm;
    }

    public class RootMover extends MouseAdapter {

        Point mStart = new Point(0, 0);
        boolean exitPressed = false;

        @Override
        public void mousePressed(MouseEvent e) {
            mStart = e.getPoint();
            repaint();

            if (mStart.x > lWidth - 60 && mStart.y < 55) {
                try {
                    exit = ImageIO.read(new File("Images/exitPressed.png"));

                } catch (Exception z) {
                }
                exitPressed = true;
            } else {
                exitPressed = false;
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            repaint();
            if (!exitPressed) {
                findRoot(getParent());
                int dx = e.getX() - mStart.x;
                int dy = e.getY() - mStart.y;
                parent.setLocation(parent.getX() + dx, parent.getY() + dy);
                mStart = e.getPoint();
            }
            mStart = e.getPoint();
            if (mStart.x > lWidth - 60 && mStart.y < 55 && exitPressed) {
                findRoot(getParent());
                parent.dispose();
            }

            try {
                exit = ImageIO.read(new File("Images/exit.png"));
            } catch (Exception z) {
            }

        }

    }

    public void findRoot(Component c) {
        Component temp = c.getParent();
        if (temp instanceof JFrame) {
            parent = (JFrame) temp;
        } else {
            findRoot(temp);
        }
    }

}
