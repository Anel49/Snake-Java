import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    //app window size
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    //grid size
    static final int UNIT_SIZE = 60;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    //snake speed
    static final int DELAY = 400;

    /* Image class images
     * honeycomb
     *     https://twitter.com/Jusiv_/status/1124962799892017152
     * bearHead
     * bearBody
     * trail
     * trailEnd
    */

    //we doin this?
    ArrayList<Image> imgs = new ArrayList<>();

    static Image background, honeycomb, r1a, r1b, l1a, l1b, u1a, u1b,
        d1a, d1b = null;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    //snake starting body size
    int bodyParts = 2;
    int combsEaten, combX, combY;
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
        newComb();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        draw(graphics);
    }
    public Image getImage(String path){
        Image tempImage = null;
        URL imageURL = GamePanel.class.getResource(path);
        tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
        return tempImage;
    }
    public void draw(Graphics graphics){

        honeycomb = getImage("honeycomb.png");
        r1a = getImage("R1a.png");
        r1b = getImage("R1b.png");
        l1a = getImage("L1a.png");
        l1b = getImage("L1b.png");
        u1a = getImage("U1a.png");
        u1b = getImage("U1b.png");
        d1a = getImage("D1a.png");
        d1b = getImage("D1b.png");

        Graphics2D g = (Graphics2D)graphics;
        
        

        if (running){
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++){
                //making a grid for clarity
                graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                graphics.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            //honeycomb
            g.drawImage(honeycomb, combX, combY, UNIT_SIZE, UNIT_SIZE, null);
    
            //moving the bear
            for (int i = 0; i < bodyParts; i++){
                //if direction is left
                if (direction == 'L'){
                    if (i == 0){
                        g.drawImage(l1a, x[0], y[0], UNIT_SIZE, UNIT_SIZE, null);
                    }
                    if (i == 1){
                        g.drawImage(l1b, x[1], y[1], UNIT_SIZE, UNIT_SIZE, null);
                    }
                }
                //if direction is right
                if (direction == 'R'){
                    if (i == 0){
                        g.drawImage(r1b, x[0], y[0], UNIT_SIZE, UNIT_SIZE, null);
                    }
                    if (i == 1){
                        g.drawImage(r1a, x[1], y[1], UNIT_SIZE, UNIT_SIZE, null);
                    }
                    if (i > 1) {
                        graphics.setColor(Color.yellow);
                        graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
                //if direction is up
                if (direction == 'U'){
                    if (i == 0){
                        g.drawImage(u1a, x[0], y[0], UNIT_SIZE, UNIT_SIZE, null);
                    }
                    if (i == 1){
                        g.drawImage(u1b, x[1], y[1], UNIT_SIZE, UNIT_SIZE, null);
                    }
                    if (i > 1) {
                        graphics.setColor(Color.yellow);
                        graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
                //if direction is down
                if (direction == 'D'){
                    if (i == 0){
                        g.drawImage(d1b, x[0], y[0], UNIT_SIZE, UNIT_SIZE, null);
                    }
                    if (i == 1){
                        g.drawImage(d1a, x[1], y[1], UNIT_SIZE, UNIT_SIZE, null);
                    }
                    if (i > 1) {
                        graphics.setColor(Color.yellow);
                        graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
                if (i > 1) {
                    graphics.setColor(Color.orange);
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            //score text
            graphics.setColor(Color.red);
            graphics.setFont(new Font("Calibri", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + combsEaten, ((SCREEN_WIDTH -
                metrics.stringWidth("Score: " + combsEaten)) / 2),
                graphics.getFont().getSize());
        }
        else {
            gameOver(graphics);
        }
    }
    public void newComb(){
        combX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        combY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
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
    public void checkComb(){
        if ((x[0] == combX) && (y[0] == combY)){
            bodyParts++;
            combsEaten++;
            newComb();
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
        graphics.drawString("Final Score: " + combsEaten, ((SCREEN_WIDTH -
            metrics.stringWidth("Final Score: " + combsEaten)) / 2),
            graphics.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            move();
            checkComb();
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


// ArrayList<String> fileNames = new ArrayList<>();
// fileNames.add("honeycomb");
// //right direction
// fileNames.add("R1a");
// fileNames.add("R1b");
// fileNames.add("R2a");
// fileNames.add("R2b");
// //left direction
// fileNames.add("L1a");
// fileNames.add("L1b");
// fileNames.add("L2a");
// fileNames.add("L2b");
// //up direction
// fileNames.add("U1a");
// fileNames.add("U1b");
// fileNames.add("U2a");
// fileNames.add("U2b");
// //down direction
// fileNames.add("D1a");
// fileNames.add("D1b");
// fileNames.add("D2a");
// fileNames.add("D2b");



// File currentDir = new File(".");
// File parentDir = currentDir.getParentFile();
// for (int i = 0; i < 16; i++){
// File newFile = new File(parentDir, fileNames[i] + ".png");
// }