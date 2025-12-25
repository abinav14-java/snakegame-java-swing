package com.abinav.snakegame.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.Timer;

import javax.swing.JPanel;

public class SnakeGamePanel extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;

    private static final int PANEL_WIDTH = 600;
    private static final int PANEL_HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (PANEL_WIDTH * PANEL_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 100;
    private static final Color Body_COLOR = new Color(45, 180, 0);
    private static final Font SCORE_FONT = new Font("Ink Free", Font.BOLD, 25);

    private final int x[] = new int[GAME_UNITS];
    private final int y[] = new int[GAME_UNITS];

    private int appleX;
    private int appleY;
    private int applesEaten = 0;
    private int bodyParts = 3;
    private int currentDelay = DELAY;

    private char direction = 'R';
    private boolean running = false;

    private Timer timer;
    private Random random;

    public SnakeGamePanel() {
        random = new Random();
        setPreferredSize(new java.awt.Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);
        startGame();
    }

    public void startGame() {
        initSnake();
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void initSnake() {
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 100 - i * UNIT_SIZE;
            y[i] = 100;
        }
    }

    // Drawing happens here
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            // Draw apple
            g.setColor(Color.RED);
            g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            // Draw snake
            g.setColor(Color.GREEN);
            g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
            g.setColor(Body_COLOR);
            for (int i = 1; i < bodyParts; i++) {
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
            // Draw score
            drawScore(g);
        } else {
            gameOver(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Game loop actions would go here
        if (running) {
            updateSpeed();
            move();
            checkApple();
            checkCollisions();
        }
        repaint();

    }

    private void updateSpeed() {
        int newDelay = applesEaten > 5 ? Math.max(10, DELAY - (applesEaten - 5) * 5) : DELAY;
        if (newDelay != currentDelay) {
            timer.setDelay(newDelay);
            currentDelay = newDelay;
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (!running && e.getKeyCode() == KeyEvent.VK_R) {
            resetGame();
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                direction = 'R';
                break;
            case KeyEvent.VK_UP:
                direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                direction = 'D';
                break;
        }
    }

    private void newApple() {
        boolean onSnake;
        do {
            onSnake = false;
            appleX = random.nextInt(PANEL_WIDTH / UNIT_SIZE) * UNIT_SIZE;
            appleY = random.nextInt(PANEL_HEIGHT / UNIT_SIZE) * UNIT_SIZE;

            for (int i = 0; i < bodyParts; i++) {
                if (x[i] == appleX && y[i] == appleY) {
                    onSnake = true;
                    break;
                }
            }
        } while (onSnake);
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // Check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
        if (x[0] < 0 || x[0] >= PANEL_WIDTH ||
                y[0] < 0 || y[0] >= PANEL_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(SCORE_FONT);
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(
                "Score: " + applesEaten,
                (PANEL_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                g.getFont().getSize() + 10);
    }

    public void gameOver(Graphics g) {
        // Display Game Over text
        drawScore(g);

        // Game Over text
        g.setFont(SCORE_FONT);
        g.setColor(Color.WHITE);
        g.drawString("Press R to Restart",
                (PANEL_WIDTH - g.getFontMetrics().stringWidth("Press R or ENTER to Restart")) / 2,
                PANEL_HEIGHT / 2 + 50);

    }

    public void resetGame() {
        applesEaten = 0;
        bodyParts = 3;
        direction = 'R';
        for (int i = 0; i < x.length; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        startGame();
    }

    /**
     * Invoked when a key has been released.
     * 
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
