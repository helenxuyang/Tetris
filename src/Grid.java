import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Grid extends JPanel implements KeyListener, ActionListener {

    private static int[] thombusKeys= { KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN,
            KeyEvent.VK_SPACE, KeyEvent.VK_UP, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT };
    private static int[] p1Keys= { KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_SPACE,
            KeyEvent.VK_X, KeyEvent.VK_Z, KeyEvent.VK_SHIFT };
    private static int[] p2Keys= { KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN,
            KeyEvent.VK_PERIOD, KeyEvent.VK_UP, KeyEvent.VK_P, KeyEvent.VK_U };
    // L, R, SD, HD, rotCW, rotCCW, hold

    public static final int WIDTH= 575;
    public static final int HEIGHT= 800;
    public static final int BOXSIZE= 30;
    public static final int BIGBOXSIZE= 3 * BOXSIZE;
    public static final int SMOLBOXSIZE= 3 * (BOXSIZE / 5);
    public static final int ROWS= 20;
    public static final int COLS= 10;
    public static final int XMARGIN= (WIDTH - COLS * BOXSIZE) / 2;
    public static final int YMARGIN= (HEIGHT - ROWS * BOXSIZE) / 2;
    public static final int GRIDXMARGIN= XMARGIN / 4 + BIGBOXSIZE + 10;
    public static final int BARXMARGIN= XMARGIN + BOXSIZE * COLS + 2;
    public static final int BARYMARGIN= YMARGIN;
    public static final int BARLINESIZE= 30;
    public static final int BARMAXLINES= 20;
    public static final int NEXTGRIDXMARGIN= GRIDXMARGIN + BOXSIZE *
        COLS + 15;
    public static final int NEXTGRIDYMARGIN= YMARGIN + BOXSIZE * 2;
    public static final int HOLDGRIDXMARGIN= GRIDXMARGIN - BIGBOXSIZE - 15;

    public static final Color LIGHTBLUE= new Color(55, 248, 255);
    public static final Color DARKBLUE= new Color(106, 130, 226);
    public static final Color ORANGE= new Color(234, 184, 98);
    public static final Color YELLOW= new Color(248, 255, 55);
    public static final Color GREEN= new Color(144, 237, 145);
    public static final Color PURPLE= new Color(174, 136, 232);
    public static final Color RED= new Color(244, 115, 115);

    public static final Color MARAGRAD1= new Color(253, 252, 71);
    public static final Color MARAGRAD2= new Color(36, 254, 65);

    public static final Color SPRINT1PGRAD1= new Color(230, 92, 0);
    public static final Color SPRINT1PGRAD2= new Color(249, 212, 35);

    public static final Color SPRINT2PGRAD1= new Color(178, 254, 250);
    public static final Color SPRINT2PGRAD2= new Color(14, 210, 247);

    public static final Color BATTLEGRAD1= new Color(127, 0, 255);
    public static final Color BATTLEGRAD2= new Color(225, 0, 255);

    private Graphics dontlookatme;

    private int numPlaced= 0;
    private boolean win= false;
    private boolean dead= false;
    private boolean needWrite= false;
    private boolean needWrite2= false;
    private int combo;
    private boolean alreadyAddedLines= false;
    private boolean needCombo= false;
    private boolean prevRotate= false;
    private boolean tspin= false;
    private boolean needpc= false;
    private boolean needpc2= false;

    private int gameMode= 0;
    private int gamePlayers= 0;
    private int[] keys;
    private ArrayList<Tetromino> nextMinos= new ArrayList<>();
    private Tetromino currentMino;
    private Tetromino heldMino;
    private TetrisArray array= new TetrisArray(this);
    private Grid opponent;

    private Timer timer;
    boolean waitTick;
    private int timerDelay= 1000;
    private int time= 0;

    private int marathonLevel= 1;
    private int marathonGoal= 5;
    private int sprintGoal= 40;

    private int linesCleared= 0;
    private boolean gotHeld= false;
    private boolean drawX= false;
    private int score= 0;
    private String write= "";
    public int barLines= 0;

    public Grid(int[] kesy) {
        for (int i= 0; i < 6; i++ ) {
            addNextMino(new Tetromino(array));
        }
        setCurrentMino(nextMinos.remove(0));
        currentMino.setInitialPos();
        setUpTimer();
        keys= kesy;
    }

    public static void playTetris(int mode) {
        if (mode == Menu.MARATHON) {
            singlePlayerSetup(new Grid(thombusKeys), Menu.MARATHON);
        } else if (mode == Menu.SPRINT1P) {
            singlePlayerSetup(new Grid(thombusKeys), Menu.SPRINT1P);
        } else if (mode == Menu.SPRINT2P) {
            twoPlayerSetup(new Grid(p1Keys), new Grid(p2Keys),
                Menu.SPRINT2P);
        } else if (mode == Menu.BATTLE) {
            twoPlayerSetup(new Grid(p1Keys), new Grid(p2Keys), Menu.BATTLE);
        }
    }

    public static void singlePlayerSetup(Grid player, int mode) {
        JFrame f= new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        f.add(player);
        player.setGameMode(mode);
        player.setGamePlayers(1);
        f.setResizable(false);
        f.pack();
        f.addKeyListener(player);
        f.setVisible(true);
    }

    public static void twoPlayerSetup(Grid p1, Grid p2, int mode) {
        JFrame f= new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(2 * WIDTH, HEIGHT));
        f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.X_AXIS));
        f.add(p1);
        f.add(p2);
        p1.setOpponent(p2);
        p2.setOpponent(p1);
        p1.setGameMode(mode);
        p2.setGameMode(mode);
        p1.setGamePlayers(2);
        p2.setGamePlayers(2);
        f.setResizable(false);
        f.pack();
        f.addKeyListener(p1);
        f.addKeyListener(p2);
        f.setVisible(true);
    }

    public void setUpTimer() {
        timer= new Timer(timerDelay, this);
        timer.setInitialDelay(10);
        timer.start();
    }

    public void speedUpTimer() {
        timer.stop();
        timerDelay-= 66;
        timer= new Timer(timerDelay, this);
        timer.start();
    }

    public void setKeys(int[] keyList) {
        keys= keyList;
    }

    public void setGameMode(int mode) {
        gameMode= mode;
    }

    public Graphics getGp() {
        return dontlookatme;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGamePlayers(int p) {
        gamePlayers= p;
    }

    public int getGamePlayers() {
        return gamePlayers;
    }

    public int getLevel() {
        return marathonLevel;
    }

    public void nextLevel() {
        marathonLevel++ ;
        marathonGoal= 5 * marathonLevel;
        speedUpTimer();
    }

    public void setMGoal(int goal) {
        marathonGoal= goal;
    }

    public void setSGoal(int goal) {
        sprintGoal= goal;
    }

    public int getMGoal() {
        return marathonGoal;
    }

    public int getSGoal() {
        return sprintGoal;
    }

    public void setLinesCleared(int lines) {
        linesCleared= lines;
    }

    public int getLinesCleared() {
        return linesCleared;
    }

    public void setOpponent(Grid opp) {
        opponent= opp;
    }

    public Grid getOpponent() {
        return opponent;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBG(g);
        dontlookatme= g;
        /*for (int i = 0; i < 255; i++) {
            g.setColor(new Color(255, i, 100));
            g.fillRect(0, i * 3, WIDTH, 3);
        }
        g.fillRect(0, 255 * 3, WIDTH, 50);*/

        /* lol just trolling you can delete this
        g.setColor(Color.RED);
        g.fillRect(WIDTH / 6, YMARGIN / 4, 50, 50);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Dialog", Font.BOLD, 50));
        g.drawString("B", WIDTH / 6 + 8, YMARGIN / 2 + 20);
        g.setFont(new Font("Dialog", Font.BOLD, 50));
        g.drawString("etris", WIDTH / 3 - 30, YMARGIN / 2 + 20);
         */

        drawStrings(g);
        drawGrid(g);
        if (gameMode == Menu.BATTLE) {
            drawBar(g);
            drawBarLines(g);
        }
        drawNextGrid(g);
        drawNextMinos(g);
        drawHoldGrid(g);
        drawHoldMino(g);
        if (drawX) {
            drawX(g);
        }
        if (!currentMino.atBottom()) {
            drawGhostPiece(g);
        }
        drawCurrentMino(g);
        drawFromArray(g);

        if (needWrite2) {
            g.setColor(new Color(50, 0, 150));
            g.setFont(new Font("Dialog", Font.PLAIN, 19));
            FontMetrics metric= g.getFontMetrics();
            int lines= 0;
            for (String s : write.split("\n")) {
                g.drawString(s, HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
                    metric.stringWidth(s) / 2,
                    HEIGHT / 4 + 100 + lines *
                        (metric.getHeight() + 5));
                lines++ ;
            }
        }
        if (needWrite) {
            g.setColor(new Color(50, 0, 150));
            g.setFont(new Font("Dialog", Font.PLAIN, 19));
            FontMetrics metric= g.getFontMetrics();
            int lines= 0;
            for (String s : write.split("\n")) {
                g.drawString(s, HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
                    metric.stringWidth(s) / 2,
                    HEIGHT / 4 + 100 + lines *
                        (metric.getHeight() + 5));
                lines++ ;
            }
        }

        if (needCombo) {
            g.setColor(new Color(50, 0, 150));
            g.setFont(new Font("Dialog", Font.PLAIN, 22));
            FontMetrics metric= g.getFontMetrics();
            String s;
            if (combo > 1) {
                s= new Integer(combo).toString() + " Combo!";
                if (!alreadyAddedLines && getGamePlayers() == 2) {
                    if (combo == 1 || combo == 2) {
                        doBarLines(1);
                    } else if (combo == 3 || combo == 4) {
                        doBarLines(2);
                    } else if (combo == 5 || combo == 6) {
                        doBarLines(3);
                    } else {
                        doBarLines(4);
                    }
                    alreadyAddedLines= true;
                }
            } else {
                s= "";
            }
            g.drawString(s, HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
                metric.stringWidth(s) / 2, HEIGHT / 4 + 150);
        }
        if (needpc) {
            g.setColor(new Color(50, 0, 150));
            g.setFont(new Font("Dialog", Font.PLAIN, 22));
            FontMetrics metric= g.getFontMetrics();
            String s= "Perfect";
            String t= "Clear!";
            g.drawString(s, HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
                metric.stringWidth(s) / 2, HEIGHT / 4 + 150 + 50);
            g.drawString(t, HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
                metric.stringWidth(t) / 2, HEIGHT / 4 + 150 + 55 + metric.getHeight());
        }
        if (needpc2) {
            g.setColor(new Color(50, 0, 150));
            g.setFont(new Font("Dialog", Font.PLAIN, 22));
            FontMetrics metric= g.getFontMetrics();
            String s= "Perfect";
            String t= "Clear!";
            g.drawString(s, HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
                metric.stringWidth(s) / 2, HEIGHT / 4 + 150 + 50);
            g.drawString(t, HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
                metric.stringWidth(t) / 2, HEIGHT / 4 + 150 + 55 + metric.getHeight());
        }
        if (dead) {
            timer.stop();
            g.setColor(Color.WHITE);
            g.setFont(new Font("Dialog", Font.BOLD, 40));
            FontMetrics fm= g.getFontMetrics();
            g.drawString("You died.", WIDTH / 2 - fm.stringWidth("You died.") / 2,
                HEIGHT / 3);
            g.setFont(new Font("Dialog", Font.BOLD, 10));
            // g.drawString("no u", WIDTH / 3 + 90, HEIGHT / 3 + 30);
            if (gameMode == Menu.BATTLE) {
                opponent.setWin(true);
                opponent.drawWin(opponent.getGp());
            }
        }

        if (win) {
            timer.stop();
            g.setColor(Color.WHITE);
            g.setFont(new Font("Dialog", Font.BOLD, 40));
            FontMetrics fm1= g.getFontMetrics();
            g.drawString("You win!", WIDTH / 2 - fm1.stringWidth("You win!") / 2,
                HEIGHT / 3);
            g.setFont(new Font("Dialog", Font.PLAIN, 30));
            if (gameMode != Menu.BATTLE) {
                FontMetrics fm= g.getFontMetrics();
                String s1= "Your time:";
                g.drawString(s1, WIDTH / 2 - fm.stringWidth(s1) / 2, HEIGHT / 3 + 100);
                String s2= new Integer(time).toString() + " sec";
                g.drawString(s2, WIDTH / 2 - fm.stringWidth(s2) / 2, HEIGHT / 3 + 150);
            }
        }
    }

    public void drawWin(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Dialog", Font.BOLD, 40));
        FontMetrics fm1= g.getFontMetrics();
        g.drawString("You win!", WIDTH / 2 - fm1.stringWidth("You win!") / 2,
            HEIGHT / 3);
        g.setFont(new Font("Dialog", Font.PLAIN, 30));
        if (gameMode != Menu.BATTLE) {
            FontMetrics fm= g.getFontMetrics();
            String s1= "Your time:";
            g.drawString(s1, WIDTH / 2 - fm.stringWidth(s1) / 2, HEIGHT / 3 + 100);
            String s2= new Integer(time).toString() + " sec";
            g.drawString(s2, WIDTH / 2 - fm.stringWidth(s2) / 2, HEIGHT / 3 + 150);
        }
        repaint();
    }

    public void drawBG(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        Graphics2D g2= (Graphics2D) g;
        GradientPaint gradient= new GradientPaint(0, 0, Color.WHITE, 0, 0,
            Color.WHITE);
        if (gameMode == Menu.MARATHON) {
            gradient= new GradientPaint(0, 0, MARAGRAD1, 0, WIDTH,
                MARAGRAD2);
        } else if (gameMode == Menu.SPRINT1P) {
            gradient= new GradientPaint(0, 0, SPRINT1PGRAD1, 0, WIDTH,
                SPRINT1PGRAD2);
        } else if (gameMode == Menu.SPRINT2P) {
            gradient= new GradientPaint(0, 0, SPRINT2PGRAD1, 0, WIDTH,
                SPRINT2PGRAD2);
        } else if (gameMode == Menu.BATTLE) {
            gradient= new GradientPaint(0, 0, BATTLEGRAD1, 0, WIDTH,
                BATTLEGRAD2);
        }
        g2.setPaint(gradient);
        g2.fillRect(0, 0, WIDTH, HEIGHT);
    }

    public void drawStrings(Graphics g) {
        if (gameMode == Menu.MARATHON) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.WHITE);
        }
        g.setFont(new Font("Dialog", Font.BOLD, 40));
        // g.drawString(new Integer(barLines).toString(), (WIDTH / 5) + 50, YMARGIN - 10);
        g.drawString("Tetris", WIDTH / 3 + 50, YMARGIN - 10);

        g.setFont(new Font("Dialog", Font.BOLD, 20));
        FontMetrics metric= g.getFontMetrics();
        g.drawString("NEXT", NEXTGRIDXMARGIN + BIGBOXSIZE / 2 -
            metric.stringWidth("NEXT") / 2, 3 * (YMARGIN / 2));
        g.drawString("HOLD", HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
            metric.stringWidth("HOLD") / 2, 3 * (YMARGIN / 2));
        g.drawString("SCORE", HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
            metric.stringWidth("SCORE") / 2, HEIGHT / 2 + 30);
        if (gameMode == Menu.MARATHON) {
            g.drawString("LEVEL", HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
                metric.stringWidth("LEVEL") / 2, HEIGHT / 2 + 90);
        }
        if (gameMode != Menu.BATTLE) {
            g.drawString("GOAL", HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
                metric.stringWidth("GOAL") / 2, HEIGHT / 2 + 150);
        }

        g.setFont(new Font("Dialog", Font.BOLD, 16));
        FontMetrics metric2= g.getFontMetrics();
        g.drawString(new Integer(score).toString(), HOLDGRIDXMARGIN +
            BIGBOXSIZE / 2 - metric2.stringWidth(new Integer(score).toString()) / 2,
            HEIGHT / 2 + 60);
        if (gameMode == Menu.MARATHON) {
            g.drawString(new Integer(marathonLevel).toString(),
                HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
                    metric2.stringWidth(new Integer(marathonLevel).toString()) / 2,
                HEIGHT / 2 + 120);
            g.drawString(new Integer(marathonGoal).toString(),
                HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
                    metric2.stringWidth(new Integer(marathonGoal).toString()) / 2,
                HEIGHT / 2 + 180);
        } else if (gameMode == Menu.SPRINT1P || gameMode == Menu.SPRINT2P) {
            g.drawString(new Integer(sprintGoal).toString(), HOLDGRIDXMARGIN + BIGBOXSIZE / 2 -
                metric2.stringWidth(new Integer(sprintGoal).toString()) / 2, HEIGHT / 2 + 180);
        }

    }

    public void drawGrid(Graphics g) {
        for (int c= 0; c < COLS; c++ ) {
            for (int r= 0; r < ROWS; r++ ) {
                drawBox(g, Color.DARK_GRAY, r, c);
                g.setColor(Color.BLACK);
                g.drawRect(c * BOXSIZE + GRIDXMARGIN, r * BOXSIZE + YMARGIN, BOXSIZE, BOXSIZE);
            }
        }
    }

    public void drawBar(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(BARXMARGIN, BARYMARGIN, 5, BOXSIZE * ROWS);
        g.setColor(Color.BLACK);
        g.drawRect(BARXMARGIN, BARYMARGIN, 5, BOXSIZE * ROWS);
    }

    public void drawBarLines(Graphics g) {
        g.setColor(Color.RED);
        if (barLines < BARMAXLINES) {
            g.fillRect(BARXMARGIN + 1, BARYMARGIN + BOXSIZE * ROWS - barLines * BARLINESIZE, 4,
                barLines * BARLINESIZE);
        } else {
            g.fillRect(BARXMARGIN + 1, BARYMARGIN + BOXSIZE * ROWS - BARMAXLINES * BARLINESIZE, 4,
                BARMAXLINES *
                    BARLINESIZE - 1);
        }
    }

    public void drawNextGrid(Graphics g) {
        int size= BIGBOXSIZE;
        for (int r= 0; r < 5; r++ ) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(NEXTGRIDXMARGIN, r * size + NEXTGRIDYMARGIN,
                size, size);
            g.setColor(Color.BLACK);
            g.drawRect(NEXTGRIDXMARGIN, r * size +
                NEXTGRIDYMARGIN, size, size);
        }
    }

    public void drawHoldGrid(Graphics g) {
        int size= BIGBOXSIZE;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(HOLDGRIDXMARGIN, NEXTGRIDYMARGIN, size, size);
        g.setColor(Color.BLACK);
        g.drawRect(HOLDGRIDXMARGIN, NEXTGRIDYMARGIN, size, size);
    }

    public void drawHoldBox(Graphics g, Color c, int row, int col) {
        int smolSize= SMOLBOXSIZE;
        int xPos= HOLDGRIDXMARGIN + col * smolSize + smolSize;
        int yPos= NEXTGRIDYMARGIN + row * smolSize + smolSize;
        g.setColor(c);
        g.fillRect(xPos + 1 + getXShift(c), yPos + 1 + getYShift(c), smolSize - 1,
            smolSize - 1);
    }

    public void drawHoldMino(Graphics g) {
        if (heldMino != null) {
            for (int[] coords : heldMino.getInitialPosCopy()) {
                drawHoldBox(g, heldMino.getShape().getColor(), coords[0],
                    coords[1]);
            }
        }
    }

    public void drawX(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Dialog", Font.BOLD, 30));
        g.drawString("X", HOLDGRIDXMARGIN + BIGBOXSIZE - 30,
            NEXTGRIDYMARGIN + BIGBOXSIZE - 5);
        drawX= false;
    }

    public static int getXShift(Color c) {
        int smolSize= SMOLBOXSIZE;
        int xShift= 0;
        Color iColor= TetrominoShape.I.getColor();
        Color oColor= TetrominoShape.O.getColor();

        if (c.equals(iColor)) {
            xShift= -1 * (smolSize / 2);
        } else if (c.equals(oColor)) {
            xShift= smolSize / 2;
        }

        return xShift;
    }

    public static int getYShift(Color c) {
        int smolSize= SMOLBOXSIZE;
        int yShift= 0;
        Color iColor= TetrominoShape.I.getColor();

        if (!c.equals(iColor)) {
            yShift= smolSize / 2;
        } else {
            yShift= smolSize;
        }

        return yShift;
    }

    public void drawTrash(Graphics g, int row, int col) {
        // for (int c = 0; c < COLS; c++) {
        drawBox(g, Color.LIGHT_GRAY, row, col);
        // }
    }

    public void drawBomb(Graphics g, int row, int col) {
        int xPos= GRIDXMARGIN + col * BOXSIZE - 1;
        int yPos= YMARGIN + row * BOXSIZE - 1;
        int arcXPos= GRIDXMARGIN + col * BOXSIZE + 5;
        int arcYPos= YMARGIN + row * BOXSIZE + 5;
        int arcSize= BOXSIZE - 10;
        // Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW};
        // Color c = colors[(int)(Math.random() * 3)];
        drawBox(g, Color.BLACK, row, col);
        g.setColor(Color.YELLOW);
        g.fillArc(arcXPos, arcYPos, arcSize, arcSize, 0, 60);
        g.fillArc(arcXPos, arcYPos, arcSize, arcSize, 120, 60);
        g.fillArc(arcXPos, arcYPos, arcSize, arcSize, 240, 60);
        g.setColor(Color.BLACK);
        g.fillOval(xPos + BOXSIZE / 2 - 4, yPos + BOXSIZE / 2 - 4, 10, 10);
        g.setColor(Color.YELLOW);
        g.fillOval(xPos + BOXSIZE / 2, yPos + BOXSIZE / 2, 3, 3);

    }

    public void drawBox(Graphics g, Color c, int row, int col) {
        int xPos= GRIDXMARGIN + col * BOXSIZE;
        int yPos= YMARGIN + row * BOXSIZE;
        // actual code:
        g.setColor(c);
        g.fillRect(xPos + 1, yPos + 1, BOXSIZE - 1, BOXSIZE - 1);

        // // /* lol just trolling you can delete this
        // g.setColor(Color.RED);
        // g.fillRect(xPos + 1, yPos + 1, BOXSIZE - 1, BOXSIZE - 1);
        // g.setColor(Color.WHITE);
        // g.setFont(new Font("Dialog", Font.BOLD, 20));
        // g.drawString("B", xPos + BOXSIZE / 3, yPos + BOXSIZE - BOXSIZE / 3);
        // // */
    }

    public void drawNextBox(Graphics g, Color c, int index, int row, int col) {
        int smolSize= SMOLBOXSIZE;
        int xPos= NEXTGRIDXMARGIN + col * smolSize + smolSize;
        int yPos= NEXTGRIDYMARGIN + row * smolSize + BOXSIZE * 3 * index + smolSize;
        g.setColor(c);
        g.fillRect(xPos + 1 + getXShift(c), yPos + 1 + getYShift(c), smolSize - 1,
            smolSize - 1);
    }

    public void drawNextMinos(Graphics g) {
        for (int i= 0; i < nextMinos.size(); i++ ) {
            Tetromino mino= nextMinos.get(i);
            for (int[] coords : mino.getInitialPosCopy()) {
                drawNextBox(g, mino.getShape().getColor(), i, coords[0],
                    coords[1]);
            }
        }
    }

    public void drawGhostPiece(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);

        int[][] pos= new int[4][2];
        for (int i= 0; i < pos.length; i++ ) {
            for (int j= 0; j < pos[0].length; j++ ) {
                pos[i][j]= currentMino.getPos()[i][j];
            }
        }
        while (!Tetromino.atBottom(pos) && !array.checkCollision(pos,
            TetrisArray.BELOW)) {
            for (int[] coords : pos) {
                coords[0]++ ;
            }
        }

        for (int[] coords : pos) {
            g.drawRect(coords[1] * BOXSIZE + GRIDXMARGIN, coords[0] *
                BOXSIZE + YMARGIN, BOXSIZE, BOXSIZE);
            // drawBox(g, Color.LIGHT_GRAY, coords[0], coords[1]);
        }
    }

    public void drawCurrentMino(Graphics g) {
        int[][] pos= currentMino.getPos();
        for (int[] coord : pos) {
            drawBox(g, currentMino.getShape().getColor(), coord[0], coord[1]);
        }
    }

    public void drawFromArray(Graphics g) {
        for (int r= 0; r < ROWS; r++ ) {
            for (int c= 0; c < COLS; c++ ) {
                Point p= array.getPoint(r, c);

                if (p == Point.TRASH) {
                    drawTrash(g, r, c);
                } else if (p == Point.BOMB) {
                    drawBomb(g, r, c);
                } else if (p != Point.EMPTY && p != Point.CURRMINO) {
                    drawBox(g, p.getColor(), r, c);
                }
            }
        }
    }

    public void setCurrentMino(Tetromino mino) {
        currentMino= mino;
    }

    public void addNextMino(Tetromino mino) {
        nextMinos.add(mino);
    }

    public void putDownCurrentMino() {
        Point setPoint= Point.SETMINOI;
        for (Point p : Point.getSetMinoList()) {
            if (p.getColor() == currentMino.getColor()) {
                setPoint= p;
            }
        }
        setCurrentMinoPoints(setPoint);
    }

    public void newMino() {
        prevRotate= false;
        score+= 50 * combo;
        if (combo >= 1) {
            needCombo= true;
        }
        currentMino= nextMinos.remove(0);
        addNextMino(new Tetromino(array));
        currentMino.setInitialPos();

        for (int[] coord : currentMino.getPos()) {
            if (array.getPoint(coord[0], coord[1]) != Point.EMPTY &&
                array.getPoint(coord[0], coord[1]) != Point.CURRMINO) {
                dead= true;
                stopTimer();
                if (gamePlayers == 2) {
                    opponent.setWin(true);
                    opponent.stopTimer();
                }
            }
        }
        if (array.checkPerfClear() && numPlaced > 0) {
            score+= 2000;
            needpc= true;
        }
        if (array.getCleared()) {
            combo++ ;
            alreadyAddedLines= false;
        } else {
            combo= 0;
            alreadyAddedLines= false;
        }
        setCurrentMinoPoints(Point.CURRMINO);
        repaint();
    }

    public void doBarLines(int lines) {
        int linesCopy= lines;
        while (barLines > 0 && linesCopy > 0) {
            barLines-- ;
            linesCopy-- ;
        }
        if (linesCopy > 0) {
            opponent.addBarLines(linesCopy);
        }

    }

    public void getLines() {
        if (gameMode == Menu.BATTLE) {
            if (barLines > 0) {
                array.addLines(barLines);
                barLines= 0;
            }
        }
    }

    public void addBarLines(int lines) {
        if (gameMode == Menu.BATTLE) {
            barLines+= lines;
        }
    }

    public void decreaseBarLines(int lines) {
        if (gameMode == Menu.BATTLE) {
            while (lines > 0 && barLines > 0) {
                barLines-= 1;
                lines-- ;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // array.print();
        // System.out.println();
        if (!dead && !win) {
            int keyInt= e.getKeyCode();
            boolean onLeftEdge= false;
            boolean onRightEdge= false;
            boolean atBottom= false;
            boolean collideLeft= false;
            boolean collideRight= false;

            setCurrentMinoPoints(Point.EMPTY);

            for (int[] coord : currentMino.getPos()) {
                int row= coord[0];
                int col= coord[1];
                if (col == 0)
                    onLeftEdge= true;
                else if (col == COLS - 1)
                    onRightEdge= true;
                if (row == ROWS - 1)
                    atBottom= true;
                if (!onLeftEdge && array.checkCollision(currentMino,
                    TetrisArray.LEFT)) {
                    collideLeft= true;
                }
                if (!onRightEdge && array.checkCollision(currentMino,
                    TetrisArray.RIGHT)) {
                    collideRight= true;
                }
            }

            // L, R, SD, HD, rotCW, rotCCW

            // left
            if (keyInt == keys[0] && !collideLeft && !onLeftEdge) {
                currentMino.changePos(0, -1);
                prevRotate= false;
            }

            // right
            else if (keyInt == keys[1] && !collideRight && !onRightEdge) {
                currentMino.changePos(0, 1);
                prevRotate= false;
                // System.out.println("move right");
            }

            // down
            else if (keyInt == keys[2] && !array.checkCollision(currentMino,
                TetrisArray.BELOW) && !atBottom) {
                    currentMino.changePos(1, 0);
                    score++ ;
                    prevRotate= false;
                    // System.out.println("move down");
                }

            // hard drop
            else if (keyInt == keys[3]) {
                if (prevRotate && currentMino.getShape() == TetrominoShape.T &&
                    array.immobile(currentMino.getPos())) {
                    tspin= true;
                }
                prevRotate= false;
                hardDrop();
                int[][] pos= currentMino.getPos();
                putDownCurrentMino();
                gotHeld= false;
                array.clearLines(pos);
                getLines();
                tspin= false;
                newMino();
                numPlaced++ ;
                repaint();
            }

            // rotate cw
            else if (keyInt == keys[4]) {
                prevRotate= true;
                currentMino.rotateCW();
            }

            // rotate ccw
            else if (keyInt == keys[5]) {
                prevRotate= true;
                currentMino.rotateCCW();
            }

            // hold
            else if (keyInt == keys[6]) {
                prevRotate= false;
                if (!gotHeld) {
                    gotHeld= true;
                    Tetromino temp;
                    if (heldMino == null) {
                        heldMino= currentMino;
                        newMino();
                    } else {
                        temp= new Tetromino(currentMino.getShape(),
                            array);
                        currentMino= new Tetromino(heldMino.getShape(), array);
                        heldMino= temp;
                        currentMino.setInitialPos();
                    }
                } else {
                    drawX= true;
                }
            }
            setCurrentMinoPoints(Point.CURRMINO);
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {}

    @Override
    public void keyTyped(KeyEvent arg0) {}

    public void hardDrop() {
        while (!currentMino.atBottom() && !array.checkCollision(currentMino,
            TetrisArray.BELOW)) {
            currentMino.changePos(1, 0);
            score+= 2;
        }
    }

    public void setCurrentMinoPoints(Point p) {
        for (int[] coord : currentMino.getPos()) {
            array.setPoint(coord[0], coord[1], p);
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        time+= 1;
        if (needWrite2) {
            needWrite2= false;
        }
        if (needWrite) {
            needWrite= false;
            needWrite2= true;
        }
        if (needCombo) {
            needCombo= false;
        }
        if (needpc2) {
            needpc2= false;
        }
        if (needpc) {
            needpc2= true;
            needpc= false;
        }
        if (!dead && !win) {
            boolean collides= array.checkCollision(currentMino,
                TetrisArray.BELOW);
            if (!currentMino.atBottom() && !collides) {
                setCurrentMinoPoints(Point.EMPTY);
                prevRotate= false;
                currentMino.changePos(1, 0);
                setCurrentMinoPoints(Point.CURRMINO);
            }

            if (waitTick && (currentMino.atBottom() || collides)) {
                if (prevRotate && currentMino.getShape() == TetrominoShape.T &&
                    array.immobile(currentMino.getPos())) {
                    tspin= true;
                }
                int[][] pos= currentMino.getPos();
                putDownCurrentMino();
                numPlaced++ ;
                array.clearLines(pos);
                getLines();
                tspin= false;
                newMino();
                gotHeld= false;
            }
            waitTick= false;
            if (currentMino.atBottom() || collides) {
                waitTick= true;
            }
            repaint();
        }

        else {
            repaint();
        }
    }

    public void addScore(int lines) {
        if (lines == 1) {
            score+= 100;
        } else if (lines == 2) {
            score+= 300;
        } else if (lines == 3) {
            score+= 500;
        } else if (lines == 4) {
            score+= 800;
        } else if (lines == 5) {
            score+= 1200;
        } else if (lines == 6) {
            score+= 800;
        } else if (lines == 7) {
            score+= 1200;
        } else if (lines == 8) {
            score+= 1600;
        } else if (lines == 9) {
            score+= 1200;
        } else if (lines == 10) {
            score+= 1800;
        } else if (lines == 11) {
            score+= 2400;
        }
    }

    public void changeLinesCleared(int num) {
        linesCleared+= num;
    }

    public void incrementLinesCleared(int lines) {
        linesCleared+= lines;
    }

    public void setWrittenString(String newStr) {
        write= newStr;
        needWrite= true;
    }

    public void setWin(boolean wni) {
        win= wni;
    }

    public boolean getTspin() {
        return tspin;
    }

    public void stopTimer() {
        timer.stop();
    }

    public static void print(int[][] array) {
        for (int[] i : array) {
            System.out.println(Arrays.toString(i));
        }
    }
}
