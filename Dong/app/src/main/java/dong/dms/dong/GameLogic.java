package dong.dms.dong;

import android.graphics.Rect;
import android.util.Log;

/**
 * Created by Naki on 25/05/2016.
 */
public class GameLogic {

    public boolean gameRunning;
    public boolean hasBall;
    public boolean hasReceived;
    public int wins;
    public Ball ball;
    public Paddle p;
    public int screenWidth;
    public int screenHeight;
    public ComNode node;

    public void init(int width, int height, ComNode node) {
        gameRunning = true;
        wins = 0;
        this.node = node;

        hasReceived = node instanceof DongServer;
        hasBall = node instanceof  DongServer;
        p = new Paddle(width, height);
        ball = new Ball(width, height, hasBall);
        screenHeight = height;
        screenWidth = width;

    }

    public void restart() {
        gameRunning = true;
        p = new Paddle(screenWidth, screenHeight);
        ball = new Ball(screenWidth, screenHeight, hasBall);
    }

    public void update() {

        if (ball.loc_x  >= screenWidth - ball.getRadius()) {
            ball.loc_x = screenWidth - ball.getRadius();
            ball.velocity_x *= -1;
        }
        if (ball.loc_x  <= 0 + ball.getRadius()) {
            ball.loc_x = 0 + ball.getRadius();
            ball.velocity_x *= -1;
        }

        if (ball.loc_y  <= 0-ball.getRadius() && hasReceived) {
            if (hasBall) {
                Log.d("send", ball.velocity_x+","+ball.velocity_y+","+ball.loc_x);
                sendBallVelocity();
                hasBall = false;
                hasReceived = false;
                ball.velocity_x = 0;
                ball.velocity_y = 0;
            }
        }
        if (ball.loc_y + ball.getRadius() >= p.loc_y) {
            if ((ball.loc_x <= p.getPaddleDim().right*1.05 && ball.loc_x >= p.getPaddleDim().left*0.95)) {
                if (ball.velocity_x < 0) ball.velocity_x-=3;
                if (ball.velocity_x > 0) ball.velocity_x+=3;
                ball.velocity_y *= -1;
                hasReceived = true;
                changeAngle();
            }
            else {
                sendWinMessage();
            }
        }
        moveBall();
        movePaddle();
    }


    private void sendWinMessage() {

        GameObject go = new GameObject();

        if (wins == 3) {
            go.isWonMatch = true;
            gameRunning = false;
        }
        else {
            go.isWonRound = true;
            gameRunning = false;
        }
    }

    private void sendBallVelocity() {
        GameObject go =  new GameObject();
        go.isWonMatch = this.wins == 3;
        go.isWonRound = false;
        go.velocityX = -ball.velocity_x;
        go.velocityY = -ball.velocity_y;
        go.x = screenWidth/ball.loc_x;

        node.forward(go);
    }

    public void receiveMessage(GameObject go) {
        if (!go.isWonRound) {
            ball.velocity_x = (int) go.velocityX;
            ball.velocity_y = (int) go.velocityY;
            ball.loc_x = (int) (screenWidth - ((double) screenWidth) / go.x);
            hasBall = true;
            Log.d("receive", go.velocityX + "," + go.velocityY + "," + ball.loc_x);
        }
        else {
            if (go.isWonMatch) {
                //TODO: send information to trigger closing threads and switch activity
            }
            else {
                wins++;
                gameRunning = false;
            }
        }
    }

    private void movePaddle() {
        if (p.velocity > 0 && p.getPaddleDim().right <= screenWidth*0.98)
            p.loc_x += p.velocity;
        if (p.velocity < 0 && p.getPaddleDim().left >= screenWidth*0.02) {
            p.loc_x += p.velocity;
        }
    }

    private void moveBall() {
        ball.loc_x += ball.velocity_x;
        ball.loc_y += ball.velocity_y;
    }

    private void changeAngle() {
        if (ball.loc_x < p.loc_x)
            ball.velocity_x = - Math.abs(ball.velocity_x);
        if (ball.loc_x > p.loc_x)
            ball.velocity_x = Math.abs(ball.velocity_x);
        if (ball.loc_x == p.loc_x)
            ball.velocity_x = 0;

    }

    public class Ball {
        int loc_x;
        int loc_y;
        int velocity_x = -15;
        int velocity_y = 15;
        int radius;

        public Ball(int width, int height, boolean hasBall) {

            radius = 32;
            if (hasBall) {
                loc_x = width/2;
                loc_y = p.loc_y - (width/radius)-50;
            }
            else {
                loc_x = width/2;
                loc_y = -(width/radius);
                velocity_x = 0;
                velocity_y = 0;
            }

        }

        public int getRadius() {
            return screenWidth/radius;
        }

    }

    public class Paddle {
        int loc_x;
        int loc_y;
        int velocity;

        public Paddle(int width, int height) {
            loc_x = width/2;
            loc_y = (int) (height*0.95);
        }

        public Rect getPaddleDim() {
            return new Rect(loc_x - screenWidth/16,loc_y, loc_x + screenWidth/16, (int)(screenHeight*0.98));
        }
    }
}
