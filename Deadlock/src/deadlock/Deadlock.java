/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deadlock;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.concurrent.locks.ReentrantLock;
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
    static protected TaskOne taskOne;
    static protected TaskTwo taskTwo;

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
        protected String status = "";
        protected String threadName = "";
        protected boolean lOne = false;
        protected boolean lTwo = false;

        public boolean isDeadLock() {
            return (lOne && !lTwo) || (lTwo && !lOne);
        }

        public String getThreadName() {
            return threadName;
        }

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
                    threadName = "Thread One";
                    status = "Attempting to access lock one";
                    lockOne.lock();
                    lOne = lockOne.isLocked();
                    status += lOne ? " - Lock one Accessed - " : " DEADLOCKED";
                    Thread.sleep(2000);
                    status += " - Attempting to access lock two";
                    lockTwo.lock();
                    lTwo = lockTwo.isLocked();
                    status += lTwo ? " - Lock two Accessed - " : " DEADLOCKED";
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
                    threadName = "Thread Two";
                    status = "Attempting to access lock two";
                    lockTwo.lock();
                    lTwo = lockTwo.isLocked();
                    status += lTwo ? " - Lock two Accessed - " : " DEADLOCKED";
                    Thread.sleep(2000);
                    status += " - Attempting to access lock one";
                    lockOne.lock();
                    lOne = lockOne.isLocked();
                    status += lOne ? " - Lock one Accessed - " : " DEADLOCKED";
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

                g2d.drawString(taskOne == null ? "" : taskOne.getThreadName(), 50, 50);
                g2d.drawString(taskTwo == null ? "" : taskTwo.getThreadName(), 50, 100);
                g2d.drawString(taskOne == null ? "" : taskOne.getStatus(), 50, 75);
                g2d.drawString(taskTwo == null ? "" : taskTwo.getStatus(), 50, 125);
                String tempOne = lockOne.getOwner() == null ? "None" : lockOne.getOwner().getName();
                String tempTwo = lockTwo.getOwner() == null ? "None" : lockTwo.getOwner().getName();
                g2d.drawString("Lock One State, Held By: " + tempOne, 50, 300);
                g2d.drawString("Lock Two State, Held By: " + tempTwo, 50, 350);
                if (taskOne != null && taskTwo != null) {
                    if (taskOne.isDeadLock() && taskTwo.isDeadLock()) {
                        g2d.drawString("DEADLOCKED!", 100, 200);
                    }
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
                if (!taskOne.isDeadLock()) {
                    taskOne.stop();
                    taskOne = new TaskOne();
                }
            });
            stopTwo.addActionListener((ActionEvent e) -> {
                if (!taskTwo.isDeadLock()) {
                    taskTwo.stop();
                    taskTwo = new TaskTwo();
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
            panel.add(stopOne);
            panel.add(stopTwo);
            panel.add(toggleLockOne);
            panel.add(toggleLockTwo);

        }
    }

}
