package com.clinicapp.catchmodo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private long millisUntilFinished1 = 60000;

    //frame
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialFrameWidth;
    private LinearLayout startLayout;

    //Image
    private ImageView box, black, orange, play, pause;
    private Drawable imageBoxRight, imageBoxLeft;

    //Size
    private int boxSize;
    //Position

    private float boxX, boxY;
    private float blackX, blackY;
    private float orangeX, orangeY;
    private float pinkX, pinkY;

    //Score
    private TextView scoreLabel, highScoreLabel, timerLabel;
    private int score, highScore, timeScore;

    //Class
    private Timer timer;
    private Handler handler = new Handler();
    private SoundPlayer soundPlayer;

    //Status
    private boolean start_flg = false;
    private boolean action_flg = false;
    private boolean pink_flg = false;
    private int timeCount;
    // private SharedPreferences settings;
    //timer time
    private CountDownTimer countDownTimer;
    private long timeLeftMs = 60000;//for 1 min
    MediaPlayer mediaPlayer;
    private boolean mTimerRunning;
    private boolean timerCheck = false;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundPlayer = new SoundPlayer(this);

        gameFrame = findViewById(R.id.gameFrame);
        startLayout = findViewById(R.id.startLayout);
        box = findViewById(R.id.box);
        black = findViewById(R.id.balck);
        orange = findViewById(R.id.orange);
        // pink=findViewById(R.id.pink);
        scoreLabel = findViewById(R.id.scoreLevel);
        timerLabel = findViewById(R.id.timer);
        highScoreLabel = findViewById(R.id.highScoreLabel);

        imageBoxLeft = getResources().getDrawable(R.drawable.ic_games);
        imageBoxRight = getResources().getDrawable(R.drawable.ic_games);
        //High Score

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound3);

        mediaPlayer.start();
        timerLabel.setVisibility(View.GONE);
        btn=findViewById(R.id.btn);


 /*       play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTimer();

            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               onPauseTimer();
            }
        });*/

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimerRunning){
                    onPauseGame();
                    btn.setText("Play");
                }else {
                    onStartGame();
                    btn.setText("Pause");
                }
            }
        });
        btn.setVisibility(View.GONE);

    }

    public void changePos() {
        //Add timerCount
        timeCount += 20;
        //Orange
        orangeY += 12;
        float orangeCenterX = orangeX + orange.getWidth() / 2;
        float orangeCenterY = orangeY + orange.getWidth() / 2;

        if (hitCheck(orangeCenterX, orangeCenterY)) {
            orangeY = frameHeight + 100;
            score += 10;
            soundPlayer.playHitOrangeSound();
        }
        if (orangeY > frameHeight) {
            orangeY = -100;
            orangeX = (float) Math.floor(Math.random() * (frameWidth - orange.getWidth()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);
//pink
       /* if(!pink_flg && timeCount % 10000==0){
            pink_flg=true;
            pinkY=-20;
            pinkX=(float)Math.floor(Math.random()*(frameWidth-pink.getWidth()));
        }
        if(pink_flg){
            pinkY+=20;
            float pinkCenterX= pinkX+pink.getWidth()/2;
            float pinkCenterY=pinkY+pink.getWidth()/2;

            if(hitCheck(pinkCenterX,pinkCenterY)){
                pinkY=frameHeight+30;
                score+=30;

                if(initialFrameWidth> frameWidth*110/100){
                    frameWidth=frameWidth*110/100;
                    changeFrameWidth(frameWidth);
                }
                soundPlayer.playHitPinkSound();
            }
            if(pinkY>frameHeight)pink_flg=false;
            pink.setX(pinkX);
            pink.setY(pinkY);
        }*/
//black
        blackY += 18;
        float blackCenterX = blackX + black.getWidth() / 2;
        float blackCenterY = blackY + black.getHeight() / 2;


        if (hitCheck(blackCenterX, blackCenterY)) {
            blackY = frameHeight + 100;
            //change Frame
            frameWidth = frameWidth * 80 / 100;
            changeFrameWidth(frameWidth);
            soundPlayer.playHitBlackSound();

            if (frameWidth <= boxSize) {
                //game over
                gameOver();
            }
        }
        if (blackY > frameHeight) {
            blackY = -100;
            blackX = (float) Math.floor(Math.random() * (frameWidth - black.getWidth()));
        }
        black.setX(blackX);
        black.setY(blackY);


        //Move Box

        if (action_flg) {
            boxX += 14;
            box.setImageDrawable(imageBoxRight);
        } else {
            //Releasing
            boxX -= 14;
            box.setImageDrawable(imageBoxLeft);
        }

        //Check box position
        if (boxX < 0) {
            boxX = 0;
            box.setImageDrawable(imageBoxRight);
        }
        if (frameWidth - boxSize < boxX) {
            boxX = frameWidth - boxSize;
            box.setImageDrawable(imageBoxLeft);
        }
        box.setX(boxX);
        // scoreLabel.setText("Score:"+ score);
        scoreLabel.setText("Score:" + score);
    }


    public boolean hitCheck(float x, float y) {
        if (boxX <= x && x <= boxX + boxSize &&
                boxY <= y && y <= frameHeight) {
            return true;
        }
        return false;
    }

    public void changeFrameWidth(int frameWidth) {
        ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
        params.width = frameWidth;
        gameFrame.setLayoutParams(params);
    }

    public void gameOver() {
        //stop timer
        timer.cancel();
        timer = null;
        start_flg = false;
        btn.setVisibility(View.GONE);
        // before showing 1 seconds
        try {
            TimeUnit.SECONDS.sleep(1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        changeFrameWidth(initialFrameWidth);

        startLayout.setVisibility(View.VISIBLE);
        box.setVisibility(View.INVISIBLE);
        black.setVisibility(View.INVISIBLE);
        orange.setVisibility(View.INVISIBLE);
        //  pink.setVisibility(View.INVISIBLE);
        //Update High score


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flg) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }
        return true;
    }


    public void startGame(View view) {
        timerCheck = true;
        setTimer();


        start_flg = true;
        startLayout.setVisibility(View.INVISIBLE);
        if (frameHeight == 0) {
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();
            initialFrameWidth = frameWidth;

            boxSize = box.getHeight();
            boxX = box.getX();
            boxY = box.getY();
        }
        frameWidth = initialFrameWidth;

        box.setX(0.0f);
        black.setY(3000.0f);
        orange.setY(3000.0f);
        // pink.setY(3000.0f);

        blackY = black.getY();
        orangeX = black.getY();
        // pinkY=black.getY();


        box.setVisibility(View.VISIBLE);
        black.setVisibility(View.VISIBLE);
        orange.setVisibility(View.VISIBLE);
        //  pink.setVisibility(View.VISIBLE);


        timeCount = 0;
        score = 0;
        scoreLabel.setText("Score : 0");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (start_flg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }
        }, 0, 20);


    }

    public void quitGame(View view) {
        finish();

    }

    public void setTimer() {
        btn.setVisibility(View.VISIBLE);
        timerLabel.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(millisUntilFinished1, 1000) {

            public void onTick(long millisUntilFinished) {
                millisUntilFinished1 = millisUntilFinished;
                timerLabel.setText("Timer 0:" + millisUntilFinished / 1000);

            }

            public void onFinish() {
                timerLabel.setVisibility(View.GONE);
                mTimerRunning = false;
                gameOver();
            }
        };
        countDownTimer.start();
        mTimerRunning = true;



    }


    @Override
    protected void onStart() {
        super.onStart();
        onStartGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPauseGame();
    }

    public void onPauseGame() {
        mediaPlayer.pause();
        start_flg = false;
        mTimerRunning = false;
        countDownTimer.cancel();


    }

    public void onStartGame() {
        start_flg = true;
        mediaPlayer.start();
        if (timerCheck)
            setTimer();

    }

}