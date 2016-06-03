package dong.dms.dong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameActivity extends Activity {

    ComNode comNode;
    private boolean connected;
    private boolean connectConfirmed;
    private SharedPreferences playerDetails;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        comNode = (ComNode)intent.getExtras().get("client");
        playerDetails = this.getSharedPreferences("playerDetails", Context.MODE_PRIVATE);
        // Inflate layout
        BallView bv = new BallView(this);
        setContentView(bv);

    }

    public void gameComplete(boolean won) {
        Intent intent = new Intent(this, GameFinishedActivity.class);
        intent.putExtra("win", won);
        startActivity(intent);

        comNode.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mp = MediaPlayer.create(this, R.raw.polka);
        mp.start();
        comNode.registerActivity(this);
        Thread thread = new Thread(comNode);
        thread.start();

    }


    public void setConnected(boolean conn) {
        connected = conn;
    }
    public void confirmConnect(boolean b) {
        connectConfirmed = b;
    }

    @Override
    public void onBackPressed() {
        mp.stop();
    }

    public class BallView extends View {

        Paint paint;
        GameLogic game ;
        Handler h = new Handler();
        Random rand = new Random();


        Runnable r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        public BallView(Context context) {
            super(context);
            paint = new Paint();
            paint.setColor(Color.rgb(playerDetails.getInt("red", 127)*2, playerDetails.getInt("green", 127)*2,  playerDetails.getInt("blue", 127)*2));
            game = new GameLogic(comNode, (GameActivity) this.getContext());
            comNode.setGameLogic(game);
            this.setBackgroundColor(Color.BLACK);

        }

        public boolean onTouchEvent(MotionEvent event) {

            if (game.gameRunning) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (event.getX() > getWidth() / 2) {
                            game.p.velocity = 20;
                        } else {
                            game.p.velocity = -20;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        game.p.velocity = 0;
                        break;

                }
                return true;
            }
            else if (connected && connectConfirmed) {
                if (game.ball == null) {
                    game.init(getWidth(), getHeight());
                    comNode.setGameLogic(game);
                    comNode.sendConfirm(true);
                    return false;
                }
                else {
                    game.restart();
                    return false;
                }
            }
            return false;
        }

        @Override
        protected void onDraw(Canvas c) {
            super.onDraw(c);
            if (game.gameRunning && connected && connectConfirmed) {

                paint.setStyle(Paint.Style.FILL);
                paint.setTextAlign(Paint.Align.LEFT);
                game.update();

                c.drawCircle(game.ball.loc_x, game.ball.loc_y, game.ball.getRadius(), paint);
                c.drawRect(game.p.getPaddleDim(), paint);
                c.drawText(game.myScore+" : "+game.oppScore, getWidth()/96 , getHeight()/8 , paint);

                h.postDelayed(r, 10);
            }
            else {
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(getWidth()/15);
                if (connected) {
                    if (connectConfirmed)
                        c.drawText(getContext().getString(R.string.touch_to_start), getWidth() / 2, getHeight() / 2, paint);
                    else
                        c.drawText(getContext().getString(R.string.waiting), getWidth() / 2, getHeight() / 2, paint);
                }
                    else  c.drawText(getContext().getString(R.string.connecting), getWidth()/2, getHeight()/2, paint);
                h.postDelayed(r, 30);
            }
        }
    }

}
