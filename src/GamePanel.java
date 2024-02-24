import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    //app window size
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    //grid size
    static final int UNIT_SIZE = 40;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    //snake speed
    static final int DELAY = 75;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    //snake starting body size
    int bodyParts = 6;
    //TODO rename everything that says "apple" to "comb" (for honeycomb)
    int applesEaten;
    int appleX;
    int appleY;
    //starting direction
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel (){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        draw(graphics);
    }
    public void draw(Graphics graphics){
        if (running){
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++){
                //making a grid for clarity
                graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                graphics.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            graphics.setColor(Color.red);
            //TODO make it a honeycomb
            graphics.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
    
            for (int i = 0; i < bodyParts; i++){
                //head of snake
                if (i == 0){
                    graphics.setColor(Color.green);
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                //body of snake
                else {
                    graphics.setColor(new Color(45,181,0));
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            //score text
            graphics.setColor(Color.red);
            graphics.setFont(new Font("Calibri", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + applesEaten, ((SCREEN_WIDTH -
                metrics.stringWidth("Score: " + applesEaten)) / 2),
                graphics.getFont().getSize());
        }
        else {
            gameOver(graphics);
        }
    }
    //TODO rename to newComb
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }
    public void move(){
        for (int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
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
    //TODO rename to checkComb
    public void checkApple(){
        if ((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        //checks if head hits body
        for (int i = bodyParts; i > 0; i--){
            if ((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        //left border
        if (x[0] < 0){
            running = false;
        }
        //right border
        if (x[0] > SCREEN_WIDTH){
            running = false;
        }
        //top border
        if (y[0] < 0){
            running = false;
        }
        //bottom border
        if (y[0] > SCREEN_HEIGHT){
            running = false;
        }
        //stop running if hit
        if (!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics graphics){
        //game over text
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Calibri", Font.BOLD, 75));
        FontMetrics gameOverMetrics = getFontMetrics(graphics.getFont());
        graphics.drawString("GAME OVER", ((SCREEN_WIDTH -
            gameOverMetrics.stringWidth("GAME OVER")) / 2), (SCREEN_HEIGHT / 2));
        //final score
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Calibri", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Final Score: " + applesEaten, ((SCREEN_WIDTH -
            metrics.stringWidth("Final Score: " + applesEaten)) / 2),
            graphics.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
