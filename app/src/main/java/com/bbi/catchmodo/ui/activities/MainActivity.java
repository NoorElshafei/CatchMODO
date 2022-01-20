package com.bbi.catchmodo.ui.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.SoundPlayer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private long millisUntilFinished1 = 60000;


    //frame
    private ConstraintLayout gameFrame;
    /*  ConstraintLayout character;*/
    private int frameHeight, frameWidth;

    //Image
    private ImageView black, orange, play, pause, pink, speed, stop_time, left_arrow, right_arrow, scoreImage, time1, speed1;

    //Size
    private int characterSize;
    //Position



    private float characterX, characterY;
    private float blackX, blackY;
    private float orangeX, orangeY;
    private float pinkX, pinkY;
    private float speedX, speedY;
    private float timeX, timeY;
    //Score
    private TextView timerLabel, tabToStart, scoreText;
    private int score;

    private boolean check_play_pause = true;
    private boolean gameStatus = true;


    //Class
    private Timer timer;
    private Handler handler = new Handler();
    private SoundPlayer soundPlayer;

    //Status
    private boolean start_flg = false;
    private boolean action_flg_right = false;
    private boolean action_flg_left = false;
    private boolean pink_flg = false;
    private boolean speed_flg = false;
    private boolean time_stop_flg = false;
    private int timeCount, timeCount2, timeCount3;
    private ImageView character;
    //  private ConstraintLayout character;
    // private SharedPreferences settings;
    //timer time
    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayer;

    private boolean timerCheck = false;
    private boolean stopTimeIcon = true;
    private boolean stopSpeedIcon = true;
    private AlertDialog.Builder dialog;
    boolean mLevel;
    private ConstraintLayout right, left;
    private ImageView cloud1, cloud2, cloud3, cloud4;
    private int screenWidth;
    private float cloud1X, cloud1Y;
    private float cloud2X, cloud2Y;
    private float cloud3X, cloud3Y;
    private float cloud4X, cloud4Y;

    int n;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundPlayer = new SoundPlayer(this);

        gameFrame = findViewById(R.id.gameFrame);
        tabToStart = findViewById(R.id.startLabel);
        black = findViewById(R.id.balck);
        orange = findViewById(R.id.orange);
        pink = findViewById(R.id.pink);
        timerLabel = findViewById(R.id.timer);
        scoreText = findViewById(R.id.score_text);
        character = findViewById(R.id.move);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
        timerLabel.setVisibility(View.GONE);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        stop_time = findViewById(R.id.stop_time);
        speed = findViewById(R.id.speed);
        left_arrow = findViewById(R.id.left_image);
        right_arrow = findViewById(R.id.right_image);
        cloud1 = findViewById(R.id.cloud1);
        cloud2 = findViewById(R.id.cloud2);
        cloud3 = findViewById(R.id.cloud3);
        cloud4 = findViewById(R.id.cloud4);
        scoreImage = findViewById(R.id.scoreLevel);
        speed1 = findViewById(R.id.speed1);
        time1 = findViewById(R.id.time2);

        // Screen Size for clouds
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        cloud1X = -500;
        cloud2X = -300;
        cloud3X = -400;
        cloud4X = -200;
        cloud1Y = 20;
        cloud2Y = 250;
        cloud3Y = 40;
        cloud4Y = 500;


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartGame();
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                check_play_pause = true;

            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPauseGame();
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
                check_play_pause = false;

            }
        });
        pause.setVisibility(View.INVISIBLE);
        play.setVisibility(View.INVISIBLE);


        right.setOnTouchListener((v, event) -> {
            if (start_flg) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    action_flg_right = true;
                    action_flg_left = false;

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    action_flg_right = false;
                    action_flg_left = false;
                }
            }
            return false;
        });

        left.setOnTouchListener((v, event) -> {
            if (start_flg) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    action_flg_right = false;
                    action_flg_left = true;

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    action_flg_right = false;
                    action_flg_left = false;


                }
            }
            return false;
        });
    }

    public void changePos() {
        //move clouds
        cloud1X += 3;
        if (cloud1X > screenWidth + 30) {
            cloud1X = -500;
        }
        cloud1.setX(cloud1X);
        cloud1.setY(cloud1Y);

        cloud2X += 2;
        if (cloud2X > screenWidth + 300) {
            cloud2X = -200;
        }
        cloud2.setX(cloud2X);
        cloud2.setY(cloud2Y);

        cloud3X += 1.5;
        if (cloud3X > screenWidth + 40) {
            cloud3X = -400;
        }
        cloud3.setX(cloud3X);
        cloud3.setY(cloud3Y);

        cloud4X += 2.5;
        if (cloud4X > screenWidth + 200) {
            cloud4X = -250;
        }
        cloud4.setX(cloud4X);
        cloud4.setY(cloud4Y);



        //Add timerCount
        timeCount += 20;

        if (mLevel) {
            orangeY += 5;
            pinkY += 5;
            blackY += 5;
        }


        // speed
        if (stopSpeedIcon) {
            timeCount3 += 25;
            if (!speed_flg && timeCount3 % 10000 == 0) {
                speed_flg = true;
                speedY = -15;
                speedX = (float) Math.floor(Math.random() * (frameWidth - speed.getWidth()));
            }
            if (speed_flg) {
                speedY += 20;
                float speedCenterX = speedX + stop_time.getWidth() / 2;
                float speedCenterY = speedY + stop_time.getHeight();

                if (hitCheck(speedCenterX, speedCenterY + 185)) {
                    speedY = frameHeight + 30;
                    //speed up
                    speed1.setVisibility(View.VISIBLE);
                    speedUp();

                    soundPlayer.playHitPinkSound();
                }
                if (speedY > frameHeight)
                    speed_flg = false;
                speed.setX(speedX);
                speed.setY(speedY);
            }
        }


        //for stop time 10s like pink
        if (stopTimeIcon) {
            timeCount2 += 25;
            if (!time_stop_flg && timeCount2 % 10000 == 0) {
                time_stop_flg = true;
                timeY = -20;
                timeX = (float) Math.floor(Math.random() * (frameWidth - stop_time.getWidth()));
            }
            if (time_stop_flg) {
                timeY += 35;
                float timeCenterX = timeX + stop_time.getWidth() / 2;
                float timeCenterY = timeY + stop_time.getHeight() ;

                if (hitCheck (timeCenterX, timeCenterY + 165)) {
                    timeY = frameHeight + 30;
                    //stop timer for 10s
                    stopTimerFor10s();
                    time1.setVisibility(View.VISIBLE);

                    soundPlayer.playHitPinkSound();
                }
                if (timeY > frameHeight)
                    time_stop_flg = false;
                stop_time.setX(timeX);
                stop_time.setY(timeY);
            }

        }

        // random number for nuts
        Random rand = new Random();
        n = rand.nextInt(5);


        //Orange
        orangeY += 20;

        //why this code
        float orangeCenterX = orangeX + orange.getWidth()/2;
        float orangeCenterY = orangeY + orange.getHeight();

        //EAT
        if (hitCheck(orangeCenterX, orangeCenterY+185)) {
            orangeY = frameHeight + 100;
            score += 10;
            soundPlayer.playHitOrangeSound();
            if (n == 0) {
                orange.setImageResource(R.drawable.circle);

            } else if (n == 1) {
                orange.setImageResource(R.drawable.nuts1);

            } else if (n == 2) {
                orange.setImageResource(R.drawable.nuts2);

            } else if (n == 3) {
                orange.setImageResource(R.drawable.nuts3);

            } else if (n == 4) {
                orange.setImageResource(R.drawable.nuts4);

            } else {
                orange.setImageResource(R.drawable.nuts5);

            }
   /*         orange.setX(orangeX);
            orange.setY(orangeY);*/

        }
        //HIDE
        if (orangeY > frameHeight) {
            orangeY = -100;

            orangeX = (float) Math.floor(Math.random() * (frameWidth - orange.getWidth()));
            if (n == 0) {
                orange.setImageResource(R.drawable.moodo);

            } else if (n == 1) {
                orange.setImageResource(R.drawable.nuts1);

            } else if (n == 2) {
                orange.setImageResource(R.drawable.nuts2);

            } else if (n == 3) {
                orange.setImageResource(R.drawable.nuts3);

            } else if (n == 4) {
                orange.setImageResource(R.drawable.nuts4);

            } else {
                orange.setImageResource(R.drawable.nuts5);

            }
        }

        orange.setX(orangeX);
        orange.setY(orangeY);


//pink
        if (!pink_flg && timeCount % 10000 == 0) {
            pink_flg = true;
            pinkY = -20;
            pinkX = (float) Math.floor(Math.random() * (frameWidth - pink.getWidth()));
        }
        if (pink_flg) {
            pinkY += 35;
            float pinkCenterX = pinkX + pink.getWidth() / 2;
            float pinkCenterY = pinkY + pink.getHeight();

            if (hitCheck(pinkCenterX, pinkCenterY + 165)) {
                pinkY = frameHeight + 30;
                score += 30;
                soundPlayer.playHitPinkSound();
            }
            if (pinkY > frameHeight) pink_flg = false;
            pink.setX(pinkX);
            pink.setY(pinkY);
        }
//black

        blackY += 20;
        float blackCenterX = blackX + black.getWidth() / 2;
        float blackCenterY = blackY + black.getHeight() ;


        if (hitCheck(blackCenterX, blackCenterY + 165)) {
            blackY = frameHeight + 100;

            soundPlayer.playHitBlackSound();

            gameOver("gameOver");
        }
        if (blackY > frameHeight) {
            blackY = -100;
            blackX = (float) Math.floor(Math.random() * (frameWidth - black.getWidth()));
        }
        black.setX(blackX);
        black.setY(blackY);


        //Move Box

        if (action_flg_right) {
            //touching
            characterX += 14;

            if (mLevel) {
                characterX += 5;
            }
        }
        if (action_flg_left) {
            characterX -= 14;

            if (mLevel) {
                characterX -= 5;
            }
        }


//Check character position

        if (characterX < 0) {
            characterX = 0;

        }
        if (frameWidth - characterSize < characterX) {
            characterX = frameWidth - characterSize;

        }
        character.setX(characterX);
/*
        //Check box position

        if (boxX < 0) {
            boxX = 0;

        }
        if (frameWidth - boxSize < boxX) {
            boxX = frameWidth - boxX;

        }
        box.setX(boxX);*/

        scoreText.setText(score + "");
    }



    public boolean hitCheck(float x, float y) {
        if (characterX<= x && x <= (characterX + characterSize+10)  &&
                characterY <= y && y <= frameHeight+characterSize) {

            return true;
        }
        return false;
    }




    public void gameOver(String game) {
        gameStatus = false;

        check_play_pause = false;
        timerLabel.setVisibility(View.INVISIBLE);
        //stop timer
        countDownTimer.cancel();
        timer.cancel();
        // timer = null;
        if (timer != null) {
            timer.cancel();
        }
        start_flg = false;
        pause.setVisibility(View.INVISIBLE);
        play.setVisibility(View.INVISIBLE);
        // before showing 1 seconds
        try {
            TimeUnit.SECONDS.sleep(1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("GAME", game);
        startActivity(intent);

        tabToStart.setVisibility(View.GONE);
        character.setVisibility(View.INVISIBLE);
        black.setVisibility(View.INVISIBLE);
        orange.setVisibility(View.INVISIBLE);
        pink.setVisibility(View.INVISIBLE);
        speed.setVisibility(View.INVISIBLE);
        stop_time.setVisibility(View.INVISIBLE);
        right_arrow.setVisibility(View.INVISIBLE);
        left_arrow.setVisibility(View.INVISIBLE);


    }


    public void startGame(View view) {

        new Handler().postDelayed(() -> {
            right_arrow.setVisibility(View.INVISIBLE);
            left_arrow.setVisibility(View.INVISIBLE);
        }, 5000);
        timerCheck = true;
        millisUntilFinished1 = 60000;
        setTimer();
        pause.setVisibility(View.VISIBLE);
        scoreText.setVisibility(View.VISIBLE);
        scoreImage.setVisibility(View.VISIBLE);
        pause.setVisibility(View.VISIBLE);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.moodo_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(.08f,.08f);
        mediaPlayer.start();
        start_flg = true;
        //  startLayout.setVisibility(View.INVISIBLE);
        tabToStart.setVisibility(View.INVISIBLE);
        if (frameHeight == 0) {
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();
            characterSize = character.getHeight();
            characterX = character.getX();
            characterY = character.getY();

        }

        character.setX(0.0f);
        black.setY(3000.0f);
        orange.setY(3000.0f);
        pink.setY(3000.0f);
        speed.setY(3000.0f);
        stop_time.setY(3000.0f);


        // why this code

        blackY = black.getY();
        orangeY = orange.getY();
        pinkY =  pink.getY();
        speedY = speed.getY();
        timeY =  stop_time.getY();

        character.setVisibility(View.VISIBLE);
        black.setVisibility(View.VISIBLE);
        orange.setVisibility(View.VISIBLE);
        pink.setVisibility(View.VISIBLE);
        speed.setVisibility(View.VISIBLE);
        stop_time.setVisibility(View.VISIBLE);
        right_arrow.setVisibility(View.VISIBLE);
        left_arrow.setVisibility(View.VISIBLE);


        timeCount = 0;
        timeCount2 = 0;
        score = 0;
        scoreText.setText("");

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
        }, 10, 15);


    }

    public void setTimer() {

        timerLabel.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(millisUntilFinished1, 1000) {

            public void onTick(long millisUntilFinished) {
                millisUntilFinished1 = millisUntilFinished;
                int sec = (int) millisUntilFinished / 1000;
                int min = sec / 60;
                sec = sec % 60;

                timerLabel.setText(String.format("Timer " + "%d:%02d", min, sec));
                int e = (int) millisUntilFinished / 1000;

                if (e == 10) {
                    //for change color"red"in last 10s

                    timerLabel.setTextColor(Color.parseColor("#FF0000"));

                }


            }

            public void onFinish() {
                timerLabel.setVisibility(View.GONE);
                if (gameStatus)
                    gameOver("timeOut");
            }
        };
        countDownTimer.start();


    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        onPauseGame();
    }

    public void onPauseGame() {
        check_play_pause = false;
        if(mediaPlayer != null){
            mediaPlayer.pause();
        }
        start_flg = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.INVISIBLE);
        }

    }

    public void onStartGame() {
        start_flg = true;
        mediaPlayer.start();

        if (timerCheck)
            setTimer();


    }

    @Override
    public void onBackPressed() {
        onPauseGame();
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit From Game");
        dialog.setMessage("You Are sure Exit ?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exitAppCLICK(null);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onStartGame();
            }
        });
        AlertDialog dialog2 = dialog.create();
        dialog2.show();

    }

    public void exitAppCLICK(View view) {

        finishAffinity();
        System.exit(0);

    }

    public void stopTimerFor10s() {
        stopTimeIcon = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        new Handler().postDelayed(() -> {
            stopTimeIcon = true;
            if (check_play_pause) {
                if (timerCheck)
                    setTimer();
            }
            time1.setVisibility(View.INVISIBLE);
        }, 10000);
    }

    public void speedUp() {
        mLevel = true;

        stopSpeedIcon = false;
        new Handler().postDelayed(() -> {
            // yourMethod();
            stopSpeedIcon = true;
            mLevel = false;
            speed1.setVisibility(View.INVISIBLE);
        }, 10000);


    }


    }


