import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ControlsMenu extends JPanel implements MouseListener, MouseMotionListener {

    public static final Color CONT1PGRAD1= new Color(255, 78, 80);
    public static final Color CONT1PGRAD2= new Color(249, 212, 35);
    public static final Color CONT2PGRAD1= new Color(239, 50, 217);
    public static final Color CONT2PGRAD2= new Color(137, 255, 253);

    public int mode= 0;
    public JFrame frame;
    public int origX= 195;
    public int origY= 450;
    public int currentX= 195;
    public int currentY= 450;
    public int origW= 100;
    public int origH= 40;
    public int width= 100;
    public int height= 40;

    public ControlsMenu(int w, int h, int m) {
        frame= new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(w, h));
        frame.setLocation(300, 200);
        frame.setVisible(true);
        frame.add(this);
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        frame.pack();
        frame.setResizable(false);
        mode= m;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mode == Menu.MARATHON || mode == Menu.SPRINT1P) {
            drawSingleControls(g);
        } else {
            drawDoubleControls(g);
        }
        drawButton(g);
    }

    public void drawSingleControls(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 500, 500);
        Graphics2D g2= (Graphics2D) g;
        GradientPaint gradient= new GradientPaint(0, 0, CONT1PGRAD1, 0, 500,
            CONT1PGRAD2);
        g2.setPaint(gradient);
        g2.fillRect(0, 0, 500, 500);

        JPanel panel= (JPanel) frame.getContentPane();
        BufferedImage img;
        try {
            img= ImageIO.read(new File("controls1.png"));
            g.drawImage(img, 0, 0, panel);
        } catch (IOException e) {
            System.out.println("oof");
        }
    }

    public void drawDoubleControls(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 500, 500);
        Graphics2D g2= (Graphics2D) g;
        GradientPaint gradient= new GradientPaint(0, 0, CONT2PGRAD1, 0, 500,
            CONT2PGRAD2);
        g2.setPaint(gradient);
        g2.fillRect(0, 0, 500, 500);

        JPanel panel= (JPanel) frame.getContentPane();
        BufferedImage img;
        try {
            img= ImageIO.read(new File("controls2.png"));
            g.drawImage(img, 0, 0, panel);
        } catch (IOException e) {
            System.out.println("oof");
        }
    }

    public void drawButton(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(currentX, currentY, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Dialog", Font.PLAIN, 20));
        g.drawString("Play", 225, 475);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // System.out.println(e.getX() + " " + e.getY());
        boolean inBox= false;

        int x= currentX;
        int y= currentY + 25;
        int w= width;
        int h= height;
        if (e.getX() >= x && e.getX() <= x + w && e.getY() >= y && e.getY() <= y + h) {
            frame.dispose();
            Grid.playTetris(mode);
        }
    }

    public void larg() {
        currentX= origX - 2;
        currentY= origY - 2;
        width= origW + 5;
        height= origH + 5;
    }

    public void resetButton() {
        currentX= origX;
        currentY= origY;
        width= origW;
        height= origH;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        boolean inBox= false;
        int x= currentX;
        int y= currentY + 25;
        int w= width;
        int h= height;
        if (e.getX() >= x && e.getX() <= x + w && e.getY() >= y && e.getY() <= y + h) {
            inBox= true;
            larg();
        }
        if (!inBox) {
            resetButton();
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {}

    @Override
    public void mouseExited(MouseEvent arg0) {}

    @Override
    public void mousePressed(MouseEvent arg0) {}

    @Override
    public void mouseReleased(MouseEvent arg0) {}

    @Override
    public void mouseDragged(MouseEvent arg0) {}
}
