package imguraccess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UIFrame extends JFrame {

    private JPanel viewPane = new JPanel();

    public UIFrame(JPanel inf_ViewPane) {
        super();
        viewPane = inf_ViewPane;
        initFrame();
    }

    public UIFrame()
    {
        super();
        initFrame();
        removeAll();
    }

    public void initFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }

        this.setUndecorated(true);
        this.setContentPane((new ContentPane()));
        this.setBackground(new Color(0, 0, 0, 0));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(viewPane);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
    }
    
    public JPanel getViewPane()
    {
        return viewPane;
    }
    
    public void setViewPane(JPanel value, JPanel remove) {
        this.remove(remove);
        this.add(value);
        this.getContentPane().repaint();
        this.setVisible(true);

    }

    class ContentPane extends JPanel {

        public ContentPane() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            //g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

}
