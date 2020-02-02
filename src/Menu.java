import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Menu extends JPanel implements MouseMotionListener, MouseListener {

    public final static int buttonWidth= 200;
    public final static int buttonHeight= 80;
    public final static int MARATHON= 1;
    public final static int SPRINT1P= 3;
    public final static int SPRINT2P= 2;
    public final static int BATTLE= 4;

    public static final Color GRAD1= new Color(7, 101, 133);
    public static final Color GRAD2= Color.WHITE;
    public static final Color BUT1= new Color(0, 80, 110);
    public static final Color BUT2= new Color(5, 44, 76);

    public JFrame frame;
    public int selected= 0;
    public int[] origXPos= { 25, 25, 270, 270 };
    public int[] origYPos= { 150, 270, 150, 270 };
    public int[] buttonXPos= { 25, 25, 270, 270 };
    public int[] buttonYPos= { 150, 270, 150, 270 };
    public int[] buttonWidths= { 200, 200, 200, 200 };
    public int[] buttonHeights= { 80, 80, 80, 80 };
    public String description= "";
    public String[] desc= {
            "Try to beat 15 levels and score as many points as you can!",
            "Clear 40 lines faster than your opponent does!",
            "Clear 40 lines as fast as possible!",
            "Clear lines to send garbage to your opponent and top them out!",
            ""
    };
    public int increase= 5;
    public int shift= increase / 2;

    public static void main(String[] args) {
        Menu m= new Menu(500, 500);
    }

    public Menu(int w, int h) {
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
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 500, 500);
        Graphics2D g2= (Graphics2D) g;
        GradientPaint gradient= new GradientPaint(0, 0, GRAD1, 0, 500, GRAD2);
        g2.setPaint(gradient);
        g2.fillRect(0, 0, 500, 500);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Dialog", Font.PLAIN, 40));
        g.drawString("Tetris", 200, 100);

        g.setColor(BUT1);
        g.fillRect(buttonXPos[0], buttonYPos[0], buttonWidths[0], buttonHeights[0]);
        g.setColor(BUT2);
        g.fillRect(buttonXPos[1], buttonYPos[1], buttonWidths[1], buttonHeights[1]);
        g.setColor(BUT2);
        g.fillRect(buttonXPos[2], buttonYPos[2], buttonWidths[2], buttonHeights[2]);
        g.setColor(BUT1);
        g.fillRect(buttonXPos[3], buttonYPos[3], buttonWidths[3], buttonHeights[3]);
        g.setFont(new Font("Dialog", Font.PLAIN, 20));

        g.setColor(Color.WHITE);
        g.drawString("1 Player Marathon", 45, 195);
        g.drawString("1 Player Sprint", 305, 195);
        g.drawString("2 Player Sprint", 60, 315);
        g.drawString("2 Player Battle", 305, 315);

        g.setFont(new Font("Dialog", Font.PLAIN, 16));
        g.setColor(Color.BLACK);
        g.drawString(description, 30, 400);
        if (description.equals(desc[3])) {
            g.drawString("Clear garbage lines by dropping a piece on the bomb.", 30, 420);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // System.out.println(e.getX() + " " + e.getY());
        boolean inBox= false;
        for (int i= 0; i < buttonWidths.length; i++ ) {
            int x= buttonXPos[i];
            int y= buttonYPos[i] + 25;
            int w= buttonWidths[i];
            int h= buttonHeights[i];
            if (e.getX() >= x && e.getX() <= x + w && e.getY() >= y && e.getY() <= y + h) {
                selected= i + 1;
                inBox= true;
            }
        }
        if (!inBox) {
            selected= 0;
        }
        repaint();
        if (selected > 0) {
            frame.dispose();
            ControlsMenu h= new ControlsMenu(500, 530, selected);
        }
    }

    public void resetButtons() {
        buttonXPos= new int[] { 25, 25, 270, 270 };
        buttonYPos= new int[] { 150, 270, 150, 270 };
        for (int i= 0; i < buttonWidths.length; i++ ) {
            buttonWidths[i]= buttonWidth;
            buttonHeights[i]= buttonHeight;
        }
    }

    public void larg(int i) {
        buttonXPos[i]= origXPos[i] - shift;
        buttonYPos[i]= origYPos[i] - shift;
        buttonWidths[i]= buttonWidth + increase;
        buttonHeights[i]= buttonHeight + increase;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        boolean inBox= false;
        for (int i= 0; i < buttonWidths.length; i++ ) {
            int x= buttonXPos[i];
            int y= buttonYPos[i] + 25;
            int w= buttonWidths[i];
            int h= buttonHeights[i];
            if (e.getX() >= x && e.getX() <= x + w && e.getY() >= y && e.getY() <= y + h) {
                inBox= true;
                larg(i);
                description= desc[i];
            }
            if (!inBox) {
                description= desc[4];
                resetButtons();
            }
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent arg0) {}

    @Override
    public void mousePressed(MouseEvent arg0) {}

    @Override
    public void mouseReleased(MouseEvent arg0) {}

    @Override
    public void mouseDragged(MouseEvent arg0) {}

}
