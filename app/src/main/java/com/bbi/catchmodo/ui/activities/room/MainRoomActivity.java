package com.bbi.catchmodo.ui.activities.room;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.SoundPlayer;
import com.bbi.catchmodo.databinding.ActivityMainRoomBinding;
import com.bbi.catchmodo.ui.activities.ResultActivity;
import com.bbi.catchmodo.ui.activities.StartActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainRoomActivity extends AppCompatActivity {


    private long millisUntilFinished1 = 60000;

    //Position
    private float characterX, characterY;
    private float blackX, blackY;
    private float orangeX, orangeY;
    private float pinkX, pinkY;
    private float speedX, speedY;
    private float timeX, timeY;
    private float cloud1X = -500;
    private float cloud2X = -300;
    private float cloud3X = -400;
    private float cloud4X = -200;

    //Score
    private int score;
    private int frameHeight, frameWidth;
    //Size
    private int characterSize;


    //Class
    private Timer timer;
    private Handler handler;
    private SoundPlayer soundPlayer;

    //Status
    private boolean start_flg = false;
    private boolean action_flg_right = false;
    private boolean action_flg_left = false;
    private boolean pink_flg = false;
    private boolean speed_flg = false;
    private boolean time_stop_flg = false;

    private boolean timerCheck = false;
    private boolean stopTimeIcon = true;
    private boolean stopSpeedIcon = true;

    private boolean mLevel;

    private boolean check_play_pause = true;
    private boolean gameStatus = true;

    private boolean flag_start_game = false;

    private int timeCount, timeCount2, timeCount3;

    private int screenWidth, yBonus = 0;;


    //timer time
    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayer;


    private AlertDialog.Builder dialog;

    private ActivityMainRoomBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_room);

        handler = new Handler();
        soundPlayer = new SoundPlayer(this);


        // Screen Size for clouds
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;


        onClick();


    }

    @SuppressLint("ClickableViewAccessibility")
    private void onClick() {

        binding.play.setOnClickListener(v -> {
            onStartGame();
            binding.play.setVisibility(View.INVISIBLE);
            binding.pause.setVisibility(View.VISIBLE);
            check_play_pause = true;

        });
        binding.pause.setOnClickListener(v -> {
            onPauseGame();
            binding.play.setVisibility(View.VISIBLE);
            binding.pause.setVisibility(View.INVISIBLE);
            check_play_pause = false;

        });


        binding.right.setOnTouchListener((v, event) -> {
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

        binding.left.setOnTouchListener((v, event) -> {
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
        binding.cloud1.setX(cloud1X);
        float cloud1Y = 20;
        binding.cloud1.setY(cloud1Y);

        cloud2X += 2;
        if (cloud2X > screenWidth + 300) {
            cloud2X = -200;
        }
        binding.cloud2.setX(cloud2X);
        float cloud2Y = 250;
        binding.cloud2.setY(cloud2Y);

        cloud3X += 1.5;
        if (cloud3X > screenWidth + 40) {
            cloud3X = -400;
        }
        binding.cloud3.setX(cloud3X);
        float cloud3Y = 40;
        binding.cloud3.setY(cloud3Y);

        cloud4X += 2.5;
        if (cloud4X > screenWidth + 200) {
            cloud4X = -250;
        }
        binding.cloud4.setX(cloud4X);
        float cloud4Y = 500;
        binding.cloud4.setY(cloud4Y);

        //exception case for android 12
        if (getAndroidVersion().equals("12")) {
            yBonus = 40;
        }
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
                speedX = (float) Math.floor(Math.random() * (frameWidth - binding.speed.getWidth()));
            }
            if (speed_flg) {
                speedY += 20;
                float speedCenterX = speedX + binding.stopTime.getWidth() / 2;
                float speedCenterY = speedY + binding.stopTime.getHeight();

                if (hitCheck(speedCenterX, speedCenterY + (185- yBonus))) {
                    speedY = frameHeight + 30;
                    //speed up
                    binding.speed1.setVisibility(View.VISIBLE);
                    speedUp();

                    soundPlayer.playHitPinkSound();
                }
                if (speedY > frameHeight)
                    speed_flg = false;
                binding.speed.setX(speedX);
                binding.speed.setY(speedY);
            }
        }


        //for stop time 10s like pink
        if (stopTimeIcon) {
            timeCount2 += 25;
            if (!time_stop_flg && timeCount2 % 10000 == 0) {
                time_stop_flg = true;
                timeY = -20;
                timeX = (float) Math.floor(Math.random() * (frameWidth - binding.stopTime.getWidth()));
            }
            if (time_stop_flg) {
                timeY += 35;
                float timeCenterX = timeX + binding.stopTime.getWidth() / 2;
                float timeCenterY = timeY + binding.stopTime.getHeight();

                if (hitCheck(timeCenterX, timeCenterY + (165- yBonus))) {
                    timeY = frameHeight + 30;
                    //stop timer for 10s
                    stopTimerFor10s();
                    binding.time2.setVisibility(View.VISIBLE);

                    soundPlayer.playHitPinkSound();
                }
                if (timeY > frameHeight)
                    time_stop_flg = false;
                binding.stopTime.setX(timeX);
                binding.stopTime.setY(timeY);
            }

        }

        // random number for nuts
        Random rand = new Random();
        int randomNuts = rand.nextInt(5);


        //Orange
        orangeY += 20;

        //why this code
        float orangeCenterX = orangeX + binding.orange.getWidth() / 2;
        float orangeCenterY = orangeY + binding.orange.getHeight();

        //EAT
        if (hitCheck(orangeCenterX, orangeCenterY + (185- yBonus))) {
            orangeY = frameHeight + 100;
            score += 10;
            soundPlayer.playHitOrangeSound();
            if (randomNuts == 0) {
                binding.orange.setImageResource(R.drawable.circle);

            } else if (randomNuts == 1) {
                binding.orange.setImageResource(R.drawable.nuts1);

            } else if (randomNuts == 2) {
                binding.orange.setImageResource(R.drawable.nuts2);

            } else if (randomNuts == 3) {
                binding.orange.setImageResource(R.drawable.nuts3);

            } else if (randomNuts == 4) {
                binding.orange.setImageResource(R.drawable.nuts4);

            } else {
                binding.orange.setImageResource(R.drawable.nuts5);

            }


        }
        //HIDE
        if (orangeY > frameHeight) {
            orangeY = -100;

            orangeX = (float) Math.floor(Math.random() * (frameWidth - binding.orange.getWidth()));
            if (randomNuts == 0) {
                binding.orange.setImageResource(R.drawable.moodo);

            } else if (randomNuts == 1) {
                binding.orange.setImageResource(R.drawable.nuts1);

            } else if (randomNuts == 2) {
                binding.orange.setImageResource(R.drawable.nuts2);

            } else if (randomNuts == 3) {
                binding.orange.setImageResource(R.drawable.nuts3);

            } else if (randomNuts == 4) {
                binding.orange.setImageResource(R.drawable.nuts4);

            } else {
                binding.orange.setImageResource(R.drawable.nuts5);

            }
        }

        binding.orange.setX(orangeX);
        binding.orange.setY(orangeY);


//pink
        if (!pink_flg && timeCount % 10000 == 0) {
            pink_flg = true;
            pinkY = -20;
            pinkX = (float) Math.floor(Math.random() * (frameWidth - binding.pink.getWidth()));
        }
        if (pink_flg) {
            pinkY += 35;
            float pinkCenterX = pinkX + binding.pink.getWidth() / 2;
            float pinkCenterY = pinkY + binding.pink.getHeight();

            if (hitCheck(pinkCenterX, pinkCenterY + (165- yBonus))) {
                pinkY = frameHeight + 30;
                score += 30;
                soundPlayer.playHitPinkSound();
            }
            if (pinkY > frameHeight) pink_flg = false;
            binding.pink.setX(pinkX);
            binding.pink.setY(pinkY);
        }
//black

        blackY += 20;
        float blackCenterX = blackX + binding.balck.getWidth() / 2;
        float blackCenterY = blackY + binding.balck.getHeight();


        if (hitCheck(blackCenterX, blackCenterY + (165- yBonus))) {
            blackY = frameHeight + 100;

            soundPlayer.playHitBlackSound();

            gameOver("gameOver");
        }
        if (blackY > frameHeight) {
            blackY = -100;
            blackX = (float) Math.floor(Math.random() * (frameWidth - binding.balck.getWidth()));
        }
        binding.balck.setX(blackX);
        binding.balck.setY(blackY);


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
        binding.move.setX(characterX);


        binding.scoreText.setText(score + "");
    }


    public boolean hitCheck(float x, float y) {
        if (characterX <= x && x <= (characterX + characterSize + 10) &&
                characterY <= y && y <= frameHeight + characterSize) {

            return true;
        }
        return false;
    }


    public void gameOver(String game) {
        gameStatus = false;

        check_play_pause = false;
        binding.timer.setVisibility(View.INVISIBLE);
        //stop timer
        countDownTimer.cancel();
        timer.cancel();
        // timer = null;
        if (timer != null) {
            timer.cancel();
        }
        start_flg = false;
        binding.pause.setVisibility(View.INVISIBLE);
        binding.play.setVisibility(View.INVISIBLE);
        // before showing 1 seconds
        try {
            TimeUnit.SECONDS.sleep(1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(getApplicationContext(), ResultRoomActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("GAME", game);
        startActivity(intent);
        finish();

        binding.startLabel.setVisibility(View.GONE);
        binding.constraintStart.setVisibility(View.INVISIBLE);
        binding.move.setVisibility(View.INVISIBLE);
        binding.balck.setVisibility(View.INVISIBLE);
        binding.orange.setVisibility(View.INVISIBLE);
        binding.pink.setVisibility(View.INVISIBLE);
        binding.speed.setVisibility(View.INVISIBLE);
        binding.stopTime.setVisibility(View.INVISIBLE);
        binding.rightImage.setVisibility(View.INVISIBLE);
        binding.leftImage.setVisibility(View.INVISIBLE);


    }


    public void startGame(View view) {
        flag_start_game = true;
        new Handler().postDelayed(() -> {
            binding.rightImage.setVisibility(View.INVISIBLE);
            binding.leftImage.setVisibility(View.INVISIBLE);
        }, 5000);
        timerCheck = true;
        millisUntilFinished1 = 60000;
        setTimer();
        binding.pause.setVisibility(View.VISIBLE);
        binding.scoreText.setVisibility(View.VISIBLE);
        binding.scoreLevel.setVisibility(View.VISIBLE);
        binding.pause.setVisibility(View.VISIBLE);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.moodo_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(.08f, .08f);
        mediaPlayer.start();
        start_flg = true;
        //  startLayout.setVisibility(View.INVISIBLE);
        binding.startLabel.setVisibility(View.INVISIBLE);
        binding.constraintStart.setVisibility(View.INVISIBLE);
        binding.instructions.setVisibility(View.INVISIBLE);
        if (frameHeight == 0) {
            frameHeight = binding.gameFrame.getHeight();
            frameWidth = binding.gameFrame.getWidth();
            characterSize = binding.move.getHeight();
            characterX = binding.move.getX();
            characterY = binding.move.getY();

        }

        binding.move.setX(0.0f);
        binding.balck.setY(3000.0f);
        binding.orange.setY(3000.0f);
        binding.pink.setY(3000.0f);
        binding.speed.setY(3000.0f);
        binding.stopTime.setY(3000.0f);


        // why this code

        blackY = binding.balck.getY();
        orangeY = binding.orange.getY();
        pinkY = binding.pink.getY();
        speedY = binding.speed.getY();
        timeY = binding.stopTime.getY();

        binding.move.setVisibility(View.VISIBLE);
        binding.balck.setVisibility(View.VISIBLE);
        binding.orange.setVisibility(View.VISIBLE);
        binding.pink.setVisibility(View.VISIBLE);
        binding.speed.setVisibility(View.VISIBLE);
        binding.stopTime.setVisibility(View.VISIBLE);
        binding.rightImage.setVisibility(View.VISIBLE);
        binding.leftImage.setVisibility(View.VISIBLE);


        timeCount = 0;
        timeCount2 = 0;
        score = 0;
        binding.scoreText.setText("");

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

        binding.timer.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(millisUntilFinished1, 1000) {

            public void onTick(long millisUntilFinished) {
                millisUntilFinished1 = millisUntilFinished;
                int sec = (int) millisUntilFinished / 1000;
                int min = sec / 60;
                sec = sec % 60;

                binding.timer.setText(String.format("Timer " + "%d:%02d", min, sec));
                int e = (int) millisUntilFinished / 1000;

                if (e == 10) {
                    //for change color"red"in last 10s

                    binding.timer.setTextColor(Color.parseColor("#FF0000"));

                }


            }

            public void onFinish() {
                binding.timer.setVisibility(View.GONE);
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
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        start_flg = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            binding.play.setVisibility(View.VISIBLE);
            binding.pause.setVisibility(View.INVISIBLE);
        }

    }

    public void onStartGame() {
        start_flg = true;
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }


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

                Intent intent = new Intent(MainRoomActivity.this, StartRoomActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            if (flag_start_game) {
                binding.play.setVisibility(View.INVISIBLE);
                binding.pause.setVisibility(View.VISIBLE);
            }

            onStartGame();
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
            binding.time2.setVisibility(View.INVISIBLE);
        }, 10000);
    }

    public void speedUp() {
        mLevel = true;

        stopSpeedIcon = false;
        new Handler().postDelayed(() -> {
            // yourMethod();
            stopSpeedIcon = true;
            mLevel = false;
            binding.speed1.setVisibility(View.INVISIBLE);
        }, 10000);


    }

    private String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        //int sdkVersion = Build.VERSION.SDK_INT;
        return release;
    }
}