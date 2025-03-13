package org.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;

    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;

    static final int DELAY = 70;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    int bodyParts = 4;
    int foodEaten;
    int foodX;
    int foodY;

    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.getHSBColor(0.58f, 0.35f, 0.13f));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        draw(g2d);
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.getHSBColor(0.58f, 0.43f, 0.09f));

        if(running) {
//            for(int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g2d.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g2d.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }

            g2d.setColor(Color.getHSBColor(0.41f, 1.00f, 0.78f));
            g2d.fillRoundRect(foodX, foodY, UNIT_SIZE, UNIT_SIZE, 20, 20);

            for(int i = 0; i < bodyParts; i++) {
                g2d.setColor(Color.getHSBColor(0.83f, 1.00f, 0.50f));
                if(i > 5) {
                    g2d.fillRoundRect(x[i] + 5,y[i] + 5, UNIT_SIZE - 10, UNIT_SIZE - 10, 20, 20);
                }
                else {
                    g2d.fillRoundRect(x[i] + i,y[i] + i, UNIT_SIZE - i * 2, UNIT_SIZE - i * 2, 20, 20);
                }
            }
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("OptimusPrinceps", Font.PLAIN, 35));
            FontMetrics metrics = getFontMetrics(g2d.getFont());
            g2d.drawString("Score: " + foodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, g2d.getFont().getSize());
        }
        else {
            gameOver(g2d);
        }
    }

    public void newFood() {
        foodX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
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

    public void checkFood() {
        if((x[0] == foodX) && (y[0] == foodY)) {
            bodyParts++;
            foodEaten++;
            newFood();
        }
    }

    public void checkCollisions() {
        // body collision
        for(int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // border collision
        if(x[0] < 0) running = false;
        if(x[0] > SCREEN_WIDTH) running = false;
        if(y[0] < 0) running = false;
        if(y[0] > SCREEN_HEIGHT) running = false;

        if(!running) timer.stop();
    }

    public void gameOver(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("OptimusPrinceps", Font.PLAIN, 35));
        FontMetrics metrics1 = getFontMetrics(g2d.getFont());
        g2d.drawString("Score: " + foodEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + foodEaten)) / 2, g2d.getFont().getSize());

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("OptimusPrinceps", Font.PLAIN, 75));
        FontMetrics metrics2 = getFontMetrics(g2d.getFont());
        g2d.drawString("YOU DIED", (SCREEN_WIDTH - metrics2.stringWidth("YOU DIED")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkFood();
            checkCollisions();
        }

        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_A:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_D:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_W:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_S:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
