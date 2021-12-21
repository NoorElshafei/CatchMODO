package com.bbi.catchmodo.ui.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.SoundPlayer;

import java.util.Locale;
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
    private ImageView box, black, orange, play, pause, pink;
    private Drawable imageBoxRight, imageBoxLeft;

    //Size
    private int boxSize;
    //Position

    private float boxX, boxY;
    private float blackX, blackY;
    private float orangeX, orangeY;
    private float pinkX, pinkY;

    //Score
    private TextView scoreLabel, highScoreLabel, timerLabel, text_btn, tabToStart;
    private int score, highScore, timeScore;

    //Class
    private Timer timer;
    private Handler handler = new Handler();
    private SoundPlayer soundPlayer;

    //Status
    private boolean start_flg = false;
    private boolean action_flg = false;
    private boolean action_flg_stop = false;
    private boolean action_flg_right = false;
    private boolean action_flg_left = false;
    private boolean pink_flg = false;
    private int timeCount;
    // private SharedPreferences settings;
    //timer time
    private CountDownTimer countDownTimer;
    private long timeLeftMs = 60000;//for 1 min
    MediaPlayer mediaPlayer;
    private boolean mTimerRunning;
    private boolean timerCheck = false;
    private LinearLayout btn, restart;
    private AlertDialog.Builder dialog;
    boolean hardLevel, mLevel;
    private LinearLayout right, left;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundPlayer = new SoundPlayer(this);
        gameFrame = findViewById(R.id.gameFrame);
        //  startLayout = findViewById(R.id.startLayout);
        tabToStart = findViewById(R.id.startLabel);
        box = findViewById(R.id.box);
        black = findViewById(R.id.balck);
        orange = findViewById(R.id.orange);
        pink = findViewById(R.id.pink);
        scoreLabel = findViewById(R.id.scoreLevel);
        timerLabel = findViewById(R.id.timer);
        //  highScoreLabel = findViewById(R.id.highScoreLabel);
        imageBoxLeft = getResources().getDrawable(R.drawable.cup);
        imageBoxRight = getResources().getDrawable(R.drawable.cup);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
        //High Score
        // mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound3);
        //mediaPlayer.start();
        timerLabel.setVisibility(View.GONE);
        // btn = findViewById(R.id.btn);
        //text_btn = findViewById(R.id.text_btn);
        /*restart = findViewById(R.id.restart);
        restart.setVisibility(View.GONE);*/
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartGame();
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPauseGame();
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
            }
        });
        pause.setVisibility(View.INVISIBLE);
        play.setVisibility(View.INVISIBLE);
/*
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    onPauseGame();

                } else {
                    onStartGame();

                }
            }
        });
        btn.setVisibility(View.GONE);*/
 /*       restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reStart();
            }
        });*/


/*
        right.setOnTouchListener((view, motionEvent) -> {

            if (start_flg) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    action_flg = true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    action_flg = false;

                }
            }
            return false;
        });*/
        right.setOnTouchListener(new View.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (start_flg) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        action_flg_right=true;
                        action_flg_left=false;
                        action_flg_stop=false;
                       // box.setImageDrawable(imageBoxRight);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        action_flg_right=false;
                        action_flg_left=false;
                        action_flg_stop=true;

                    }
                }
                return false;
            }
        });

        left.setOnTouchListener(new View.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (start_flg) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        action_flg_right=false;
                        action_flg_left=true;
                        action_flg_stop=false;

                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        action_flg_right=false;
                        action_flg_left=false;
                        action_flg_stop=true;



                    }
                }
                return false;
            }
        });
    }

        public void changePos () {
            //Add timerCount
            timeCount += 20;
            //Orange
            if (mLevel) {
                orangeY += 5;
                pinkY += 5;
                blackY += 5;





            }
            if (hardLevel) {
                orangeY += 10;
                pinkY += 10;
                blackY += 10;

            }
            orangeY += 20;

            //why this code
            float orangeCenterX = orangeX + orange.getWidth() / 2;
            float orangeCenterY = orangeY + orange.getWidth() / 2;

            if (hitCheck(orangeCenterX, orangeCenterY + 200)) {
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
            if (!pink_flg && timeCount % 10000 == 0) {
                pink_flg = true;
                pinkY = -20;
                pinkX = (float) Math.floor(Math.random() * (frameWidth - pink.getWidth()));
            }
            if (pink_flg) {
                pinkY += 35;
                float pinkCenterX = pinkX + pink.getWidth() / 2;
                float pinkCenterY = pinkY + pink.getWidth() / 2;

                if (hitCheck(pinkCenterX, pinkCenterY + 200)) {
                    pinkY = frameHeight + 30;
                    score += 30;
/*
                if (initialFrameWidth > frameWidth * 110 / 100) {
                    frameWidth = frameWidth * 110 / 100;
                    changeFrameWidth(frameWidth);


                }*/
                    soundPlayer.playHitPinkSound();
                }
                if (pinkY > frameHeight) pink_flg = false;
                pink.setX(pinkX);
                pink.setY(pinkY);
            }
//black
            blackY += 25;
            float blackCenterX = blackX + black.getWidth() / 2;
            float blackCenterY = blackY + black.getHeight() / 2;


            if (hitCheck(blackCenterX, blackCenterY + 185)) {
                blackY = frameHeight + 100;
                //change Frame
                // frameWidth = frameWidth * 80 / 100;
                //  changeFrameWidth(frameWidth);
                soundPlayer.playHitBlackSound();

          /*  if (frameWidth <= boxSize) {
                //game over

            }*/
                gameOver();
            }
            if (blackY > frameHeight) {
                blackY = -100;
                blackX = (float) Math.floor(Math.random() * (frameWidth - black.getWidth()));
            }
            black.setX(blackX);
            black.setY(blackY);


            //Move Box
/*      right.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (action_flg) {
                  //touching
                  boxX += 14;
                  box.setImageDrawable(imageBoxRight);
              } else {
                  //Releasing
                  boxX -= 14;
                  box.setImageDrawable(imageBoxLeft);
              }
          }
      });
      left.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (action_flg) {
                  //touching
                  boxX += 14;
                  box.setImageDrawable(imageBoxRight);
              } else {
                  //Releasing
                  boxX -= 14;
                  box.setImageDrawable(imageBoxLeft);
              }
          }
      });*/
            if (action_flg_right) {
                //touching

             boxX += 14;

             if(mLevel){
                 boxX+=5;
             }
             if(hardLevel){
                 boxX+=5;
             }


            } /*else {
                //Releasing
                boxX -= 14;
                box.setImageDrawable(imageBoxLeft);
            }*/
            if (action_flg_left){

                    boxX -= 14;

                if(mLevel){
                    boxX -= 5;
                }
                if(hardLevel){
                    boxX -= 5;
                }


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


        public boolean hitCheck ( float x, float y){
            if (boxX <= x && x <= boxX + boxSize &&
                    boxY <= y && y <= frameHeight) {
                return true;
            }
            return false;
        }

  /*  public void changeFrameWidth(int frameWidth) {
        ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
        params.width = frameWidth;
        gameFrame.setLayoutParams(params);
    }*/

        public void gameOver () {
//amany
            timerLabel.setVisibility(View.INVISIBLE);


            //stop timer
            countDownTimer.cancel();
            timer.cancel();
            timer = null;
            start_flg = false;
            pause.setVisibility(View.INVISIBLE);
            play.setVisibility(View.INVISIBLE);
            //  btn.setVisibility(View.GONE);
            // restart.setVisibility(View.GONE);
            // before showing 1 seconds
            try {
                TimeUnit.SECONDS.sleep(1);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //   changeFrameWidth(initialFrameWidth);

            //startLayout.setVisibility(View.VISIBLE);
            tabToStart.setVisibility(View.VISIBLE);
            box.setVisibility(View.INVISIBLE);
            black.setVisibility(View.INVISIBLE);
            orange.setVisibility(View.INVISIBLE);
            pink.setVisibility(View.INVISIBLE);
            //Update High score
            startActivity(new Intent(getApplicationContext(), ResultActivity.class));
            // Show ResultActivity
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);


        }

/*        @Override
        public boolean onTouchEvent (MotionEvent event){
            if (start_flg) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    action_flg = true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    action_flg = false;
                }
            }
            return true;
        }*/


        public void startGame (View view){
            timerCheck = true;
            millisUntilFinished1 = 60000;
            setTimer();
            pause.setVisibility(View.VISIBLE);


            start_flg = true;
            //  startLayout.setVisibility(View.INVISIBLE);
            tabToStart.setVisibility(View.INVISIBLE);
            if (frameHeight == 0) {
                frameHeight = gameFrame.getHeight();
                frameWidth = gameFrame.getWidth();
                //initialFrameWidth = frameWidth;

                boxSize = box.getHeight();
                boxX = box.getX();
                boxY = box.getY();
            }
            //  frameWidth = initialFrameWidth;

            box.setX(0.0f);
            black.setY(3000.0f);
            orange.setY(3000.0f);
            pink.setY(3000.0f);

            blackY = black.getY();
            orangeX = black.getY();
            pinkY = black.getY();


            box.setVisibility(View.VISIBLE);
            black.setVisibility(View.VISIBLE);
            orange.setVisibility(View.VISIBLE);
            pink.setVisibility(View.VISIBLE);


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

/*   public void quitGame(View view) {
        finish();

    }*/

        public void setTimer () {
            //   btn.setVisibility(View.VISIBLE);
            //  restart.setVisibility(View.VISIBLE);
            timerLabel.setVisibility(View.VISIBLE);
            countDownTimer = new CountDownTimer(millisUntilFinished1, 1000) {

                public void onTick(long millisUntilFinished) {
                    millisUntilFinished1 = millisUntilFinished;
                    timerLabel.setText("Timer 0:" + millisUntilFinished / 1000);


             /*   if (countDownTimer.wait(3000);) {

                }*/


           /*     if (millisUntilFinished < 3000)
                    text = String.format(Locale.getDefault(), "%02d min: %02d sec",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60, TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                tvTime.setText( text);*/

     /*      if(countDownTimer.onTick(millisUntilFinished/1000)==5000){

           }*/
                    int e = (int) millisUntilFinished / 1000;

                    if (e == 30) {
                        mLevel = true;

                    }
                    if (e == 10) {
                        hardLevel = true;
                    }



        /*


                    if (orangeY > frameHeight) {
                        orangeY = -100;
                        orangeX = (float) Math.floor(Math.random() * (frameWidth - orange.getWidth()));
                    }

                    orange.setY(orangeY);
                }*/
        /*        if(countDownTimer.equals(5000)){
                    orangeY+=120;
                }

*/

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
        protected void onStart () {
            super.onStart();

        }

        @Override
        protected void onPause () {
            super.onPause();
            onPauseGame();
        }

        public void onPauseGame () {

            //  text_btn.setText("Play");
            // mediaPlayer.pause();
            start_flg = false;
            mTimerRunning = false;
            if (countDownTimer != null) {
                countDownTimer.cancel();
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
            }

        }

        public void onStartGame () {
            //  text_btn.setText("Pause");
            //   play.setVisibility(View.INVISIBLE);
            //  pause.setVisibility(View.VISIBLE);
            start_flg = true;
            // mediaPlayer.start();
            if (timerCheck)
                setTimer();

        }

        @Override
        public void onBackPressed () {
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

        public void exitAppCLICK (View view){

            finishAffinity();
            System.exit(0);

        }

/*    public void reStart() {
        //stop timer
        countDownTimer.cancel();
        timer.cancel();
        timer = null;
        start_flg = false;

        timeCount = 0;
        score = 0;
        scoreLabel.setText("Score : 0");
        frameWidth = 100;
        changeFrameWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        startGame(null);

    }*/
    }