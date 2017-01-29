/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deadlock;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author root
 */
public class Deadlock {

    final static protected MyLock lockOne = new MyLock();
    final static protected MyLock lockTwo = new MyLock();
    static protected Thread t1;
    static protected Thread t2;
    static protected TaskOne taskOne = new TaskOne();
    static protected TaskTwo taskTwo = new TaskTwo();

    static protected GuiWindow window;

    public static void main(String[] args) {
        window = new GuiWindow();
        t1 = new Thread();
        t2 = new Thread();
        mainThreadLoop();
    }

    private static void mainThreadLoop() {
        while (true) {
            try {
                Thread.sleep(100);
                window.repaint();
            } catch (InterruptedException e) {
            }
        }
    }

    static class MyLock extends ReentrantLock {
        MyLock() {
            super(true);
        }
        @Override
        protected Thread getOwner() {
            return super.getOwner(); //To change body of generated methods, choose Tools | Templates.
        }
    }

    abstract class ThreadParent {

        protected volatile boolean exit = false;
        protected String status = "not started";
        protected boolean lOne = false;
        protected boolean lTwo = false;

        public String getStatus() {
            return status;
        }

        public void stop() {
            exit = true;
        }

    }

    static class TaskOne extends ThreadParent implements Runnable {

        @Override
        public void run() {
            try {
                while (!exit) {
                    status = "Attempting to access lock one..";
                    lockOne.lock();
                    lOne = lockOne.isLocked();
                    status += lOne ? " Lock One Accessed - " : " DEADLOCKED";
                    Thread.sleep(2000);
                    status += "Attempting to access lock two..";
                    lockTwo.lock();
                    lTwo = lockTwo.isLocked();
                    status += lTwo ? " Lock Two Accessed" : " DEADLOCKED";
                    while (!exit) {
                    }
                }
            } catch (InterruptedException z) {
                z.printStackTrace();
            } finally {
                if (lOne) {
                    lockOne.unlock();
                }
                if (lTwo) {
                    lockTwo.unlock();
                }
            }
        }
    }

    static class TaskTwo extends ThreadParent implements Runnable {

        @Override
        public void run() {
            try {
                while (!exit) {
                    status = "Attempting to access lock two..";
                    lockTwo.lock();
                    lTwo = lockTwo.isLocked();
                    status += lTwo ? " Lock Two Accessed - " : " DEADLOCKED";
                    Thread.sleep(2000);
                    status += "Attempting to access lock one..";
                    lockOne.lock();
                    lOne = lockOne.isLocked();
                    status += lOne ? " Lock One Accessed" : " DEADLOCKED";
                    while (!exit) {
                    }
                }
            } catch (InterruptedException z) {
                z.printStackTrace();
            } finally {
                if (lOne) {
                    lockOne.unlock();
                }
                if (lTwo) {
                    lockTwo.unlock();
                }
            }
        }
    }

    static class GuiWindow extends JFrame {

        private JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
                g2d.drawString("Thread One Status:", 50, 70);
                g2d.drawString("Thread Two Status:", 50, 120);
                g2d.drawString(taskOne == null ? "" : taskOne.getStatus(), 60, 95);
                g2d.drawString(taskTwo == null ? "" : taskTwo.getStatus(), 60, 145);
                String tempOne = lockOne.getOwner() == null ? "None" : lockOne.getOwner().getName();
                String tempTwo = lockTwo.getOwner() == null ? "None" : lockTwo.getOwner().getName();
                g2d.drawString("Lock One State, Held By: " + tempOne, 50, 300);
                g2d.drawString("Lock Two State, Held By: " + tempTwo, 50, 350);
                g2d.drawString("Thread 1 State: " + t1.getState(), 500, 300);
                g2d.drawString("Thread 2 State: " + t2.getState(), 500, 350);
                if (t1.getState() == Thread.State.WAITING && t2.getState() == Thread.State.WAITING) {
                    g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
                    g2d.setColor(Color.red);
                    g2d.drawString("THREAD 1 AND 2 DEADLOCKED!", 200, 220);
                }
            }
        };

        private final JButton startOne = new JButton("Start Thread One");
        private final JButton startTwo = new JButton("Start Thread Two");

        private final JButton stopOne = new JButton("Stop Thread One");
        private final JButton stopTwo = new JButton("Stop Thread Two");

        private final JButton toggleLockOne = new JButton("Toggle Lock One");
        private final JButton toggleLockTwo = new JButton("Toggle Lock Two");

        public GuiWindow() {
            super();
            super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            super.setVisible(true);
            super.setSize(new Dimension(900, 400));
            this.add(panel);
            panel.setLayout(new FlowLayout());
            startOne.addActionListener((ActionEvent e) -> {
                if (!t1.isInterrupted() && !t1.isAlive()) {
                    t1 = new Thread(taskOne = new TaskOne());
                    t1.start();
                }
            });
            startTwo.addActionListener((ActionEvent e) -> {
                if (!t2.isInterrupted() && !t2.isAlive()) {
                    t2 = new Thread(taskTwo = new TaskTwo());
                    t2.start();
                }
            });
            stopOne.addActionListener((ActionEvent e) -> {
                if (taskOne != null) {
                    if (t1.getState() != Thread.State.WAITING) {
                        taskOne.stop();
                        taskOne = new TaskOne();
                    }
                }

            });
            stopTwo.addActionListener((ActionEvent e) -> {
                if (taskTwo != null) {
                    if (t2.getState() != Thread.State.WAITING) {
                        taskTwo.stop();
                        taskTwo = new TaskTwo();
                    }
                }

            });
            toggleLockOne.addActionListener((ActionEvent e) -> {
                if (lockOne.isLocked()) {
                    if (!(lockOne.getOwner() == t1 || lockOne.getOwner() == t2)) {
                        lockOne.unlock();
                    }
                } else if (!lockOne.isLocked()) {
                    lockOne.tryLock();
                }
            });
            toggleLockTwo.addActionListener((ActionEvent e) -> {
                if (lockTwo.isLocked()) {
                    if (!(lockTwo.getOwner() == t2 || lockTwo.getOwner() == t1)) {
                        lockTwo.unlock();
                    }
                } else if (!lockTwo.isLocked()) {
                    lockTwo.tryLock();
                }

            });

            panel.add(startOne);
            panel.add(startTwo);
            panel.add(Box.createHorizontalStrut(20));
            panel.add(stopOne);
            panel.add(stopTwo);
            panel.add(Box.createHorizontalStrut(20));
            panel.add(toggleLockOne);
            panel.add(toggleLockTwo);

        }
    }

}
