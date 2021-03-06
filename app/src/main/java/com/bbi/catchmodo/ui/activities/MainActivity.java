package com.bbi.catchmodo.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.SoundPlayer;
import com.bbi.catchmodo.data.local.UserSharedPreference;
import com.bbi.catchmodo.util.AdDialogFragment;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private long millisUntilFinished1 = 60000;

    private CountDownTimer moveCountdown;
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
    private TextView timerLabel, scoreText;
    private int score;
    private ImageView tabToStart;
    private boolean check_play_pause = true, checkSpeed = true;
    private boolean gameStatus = true;

    //Class
    private Timer timer;
    private Handler handler, tenSecondHandler, speedHandler;
    private Runnable mStatusChecker, stopTenRunnable, tenSecondSpeedRunnable;
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

    // private boolean timerCheck = false;
    private boolean stopTimeIcon = true;
    private boolean stopSpeedIcon = true;
    private AlertDialog.Builder dialog;
    boolean mLevel;
    private ConstraintLayout right, left;
    private ImageView cloud1, cloud2, cloud3, cloud4;
    private int screenWidth, yBonus = 0;
    private float cloud1X, cloud1Y;
    private float cloud2X, cloud2Y;
    private float cloud3X, cloud3Y;
    private float cloud4X, cloud4Y;
    private TableLayout instructions;
    private ConstraintLayout constraint_start;
    private boolean flag_start_game = false;

    private int n;
    private int tenSecond = 10, tenSecondSpeed = 10;
    private boolean isTenSecondFinished = true;
    boolean isTenSecondFinishedSpeed = false;
    private int black_num, orange_num, speed_num, time_num, pink_num;


    private int requiredCoin = 50;

    private MainActivity.OnLoadAdListener listener;

    private boolean checkLoadAd = false;
    private Boolean isBlackHit = false;


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
        instructions = findViewById(R.id.instructions);
        constraint_start = findViewById(R.id.constraint_start);
        // error in photo


        requiredCoin = 50;


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

        handler = new Handler();


        play.setOnClickListener(v -> {
            onStartGame();
            play.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
        });
        pause.setOnClickListener(v -> {
            onPauseGame();
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.INVISIBLE);


        });
        pause.setVisibility(View.INVISIBLE);
        play.setVisibility(View.INVISIBLE);
        if (getAndroidSDK_INT() > 26) {
            black_num = 165;
            speed_num = 185;
            time_num = 165;
            orange_num = 185;
            pink_num = 165;


        } else {
            black_num = 130;
            speed_num = 165;
            time_num = 145;
            orange_num = 165;
            pink_num = 145;
        }

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

        loadRewardedAd();
        startGame();
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
                speedX = (float) Math.floor(Math.random() * (frameWidth - speed.getWidth()));
            }
            if (speed_flg) {
                speedY += 20;
                float speedCenterX = speedX + stop_time.getWidth() / 2;
                float speedCenterY = speedY + stop_time.getHeight();

                if (hitCheck(speedCenterX, speedCenterY + (speed_num - yBonus))) {
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
                float timeCenterY = timeY + stop_time.getHeight();

                if (hitCheck(timeCenterX, timeCenterY + (time_num - yBonus))) {
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
        float orangeCenterX = orangeX + orange.getWidth() / 2;
        float orangeCenterY = orangeY + orange.getHeight();

        //EAT
        if (hitCheck(orangeCenterX, orangeCenterY + (orange_num - yBonus))) {
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

            if (hitCheck(pinkCenterX, pinkCenterY + (pink_num - yBonus))) {
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
        float blackCenterY = blackY + black.getHeight();


        if (hitCheck(blackCenterX, blackCenterY + (black_num - yBonus))) {
            onPauseGame();
            // RewardItem rewardItem = rewardedAd.getRewardItem();
            // int rewardAmount = rewardItem.getAmount();
            // String rewardType = rewardItem.getType();
            if (!isBlackHit)
                introduceVideoAd(100, "coins", "gameOver");

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


        scoreText.setText(score + "");
    }


    public boolean hitCheck(float x, float y) {
        if (characterX <= x && x <= (characterX + characterSize + 10) &&
                characterY <= y && y <= frameHeight + characterSize) {

            return true;
        }
        return false;
    }


    public void gameOver(String game) {

        requiredCoin = 50;

        if (tenSecondHandler != null)
            tenSecondHandler.removeCallbacks(stopTenRunnable);
        if (speedHandler != null)
            speedHandler.removeCallbacks(tenSecondSpeedRunnable);

        gameStatus = false;

        check_play_pause = false;
        checkSpeed = false;
        timerLabel.setVisibility(View.INVISIBLE);
        //stop timer
        countDownTimer.cancel();
        handler.removeCallbacks(mStatusChecker);


        // timer.cancel();
        // timer = null;
        if (timer != null) {
            timer.cancel();
        }
        start_flg = false;
        pause.setVisibility(View.INVISIBLE);
        play.setVisibility(View.INVISIBLE);
        // before showing 1 seconds
       /* try {
            TimeUnit.SECONDS.sleep(1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra("SCORE", score);
        long coins = score / 10;
        intent.putExtra("COINS", coins);
        intent.putExtra("GAME", game);
        startActivity(intent);
        finish();

        tabToStart.setVisibility(View.GONE);
        constraint_start.setVisibility(View.INVISIBLE);
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
        flag_start_game = true;
        new Handler().postDelayed(() -> {
            right_arrow.setVisibility(View.INVISIBLE);
            left_arrow.setVisibility(View.INVISIBLE);
        }, 5000);
        //timerCheck = true;
        millisUntilFinished1 = 60000;
        setTimer();
        pause.setVisibility(View.VISIBLE);
        scoreText.setVisibility(View.VISIBLE);
        scoreImage.setVisibility(View.VISIBLE);
        pause.setVisibility(View.VISIBLE);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.moodo_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(.5f, .5f);
        mediaPlayer.start();
        start_flg = true;
        //  startLayout.setVisibility(View.INVISIBLE);
        tabToStart.setVisibility(View.INVISIBLE);
        constraint_start.setVisibility(View.INVISIBLE);
        instructions.setVisibility(View.INVISIBLE);
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
        pinkY = pink.getY();
        speedY = speed.getY();
        timeY = stop_time.getY();

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


        playMove();
    }

    private void playMove() {

    /*  // Create the Handler object (on the main thread by default)
        Handler handler = new Handler();
        // Define the code block to be executed
        private Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                //Log.d("Handlers", "Called on main thread");
                // Repeat this the same runnable code block again another 2 seconds
                handler.postDelayed(runnableCode, 2000);
            }
        };
        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode);*/


      /*  moveCountdown=  new CountDownTimer(600000, 1) {

            public void onTick(long millisUntilFinished) {
               // if (start_flg) {
                    changePos();
                //}
            }

            public void onFinish() {

            }
        };
        moveCountdown.start();*/


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //if (start_flg) {
                handler.post(() -> changePos());
                // }
            }
        }, 10, 15);

    /*    mStatusChecker = new Runnable() {
            @Override
            public void run() {
                // if (start_flg) {


                changePos();
                handler.postDelayed(this,15);

                // }
            }
        };
        mStatusChecker.run();*/

     /*   try {
            // code runs in a thread
            runOnUiThread(mStatusChecker);
        } catch (final Exception ex) {
            Log.i("---", "Exception in thread");
        }*/



      /*  black.setX((float) Math.floor(Math.random() * (frameWidth - black.getWidth())));
        black.setY(-300);
        AdditiveAnimator.animate(black).setDuration(2000)
                .x(black.getX())
                .y(frameHeight)
                .start();
*/


    }

    private void stopMove() {
        //handler.removeCallbacks(mStatusChecker);
        if (timer != null)
            timer.cancel();
        //moveCountdown.cancel();
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

                if (e < 10) {
                    //for change color"red"in last 10s
                    timerLabel.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    timerLabel.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }

            public void onFinish() {
                onPauseGame();

                introduceVideoAd(100, "coins", "timeout");
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
        checkSpeed = false;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        stopMove();

        if (countDownTimer != null) {
            countDownTimer.cancel();
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.INVISIBLE);
        }

        gamePaused = true;

    }

    public void onStartGame() {
        //start_flg = true;
        check_play_pause = true;
        checkSpeed = true;
        playMove();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }


        if (isTenSecondFinished)
            setTimer();

        if (!gameOver && gamePaused) {
            gamePaused = false;

        }

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

                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();

            // question 1
            if (flag_start_game) {
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                onStartGame();
            }


        });
        AlertDialog dialog2 = dialog.create();
        dialog2.show();

    }


    public void stopTimerFor10s() {
        tenSecond = 10;
        isTenSecondFinished = false;
        stopTimeIcon = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        tenSecondHandler = new Handler();

        stopTenRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("sdsddssd", "check play pause: " + check_play_pause + "    ten second: " + tenSecond);
                if (check_play_pause) {
                    if (tenSecond == 0) {
                        stopTimeIcon = true;

                        setTimer();

                        tenSecond = 10;
                        isTenSecondFinished = true;
                        tenSecondHandler.removeCallbacks(stopTenRunnable);
                        time1.setVisibility(View.INVISIBLE);


                    } else {
                        tenSecond--;
                        tenSecondHandler.postDelayed(this, 1000);
                    }
                } else {
                    tenSecondHandler.postDelayed(this, 1000);
                }

            }
        };
        stopTenRunnable.run();

    }

    public void speedUp() {

        tenSecondSpeed = 10;
        isTenSecondFinishedSpeed = false;
        mLevel = true;
        stopSpeedIcon = false;

        speedHandler = new Handler();

        tenSecondSpeedRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("sdsddssd", "check play pause: " + checkSpeed + "    ten second: " + tenSecondSpeed);
                if (checkSpeed) {
                    if (tenSecondSpeed == 0) {
                        tenSecondSpeed = 10;
                        isTenSecondFinishedSpeed = true;
                        stopSpeedIcon = true;
                        mLevel = false;
                        speedHandler.removeCallbacks(tenSecondSpeedRunnable);
                        speed1.setVisibility(View.INVISIBLE);

                    } else {
                        tenSecondSpeed--;
                        speedHandler.postDelayed(this, 1000);
                    }
                } else {
                    speedHandler.postDelayed(this, 1000);
                }

            }
        };
        tenSecondSpeedRunnable.run();


    }

    private String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        //int sdkVersion = Build.VERSION.SDK_INT;
        return release;
    }

    private int getAndroidSDK_INT() {
        //String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return sdkVersion;
    }


    private static final String AD_UNIT_ID = "ca-app-pub-4846845351841719/3928732025";//ca-app-pub-3940256099942544/5224354917
    private static final String TAG = "MyActivity";
    private boolean gameOver;
    private boolean gamePaused;
    private RewardedAd rewardedAd;
    boolean isLoading;


    private void loadRewardedAd() {
        if (rewardedAd == null) {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    this,
                    AD_UNIT_ID,
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.d(TAG, loadAdError.getMessage());
                            rewardedAd = null;
                            MainActivity.this.isLoading = false;
                            Toast.makeText(MainActivity.this, "onAdFailedToLoad: " + loadAdError.getMessage()
                                    + "\n cause: " + loadAdError.getCause(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            MainActivity.this.rewardedAd = rewardedAd;
                            Log.d(TAG, "onAdLoaded");
                            MainActivity.this.isLoading = false;
                            Toast.makeText(MainActivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
                            checkLoadAd = true;
                            listener.onLoadFinished();
                        }
                    });
        }
    }

   /* private void addCoins(int coins) {
        coinCount += coins;
        //coinCountText.setText("Coins: " + coinCount);
    }*/

    private void startGame() {
        // Hide the retry button, load the ad, and start the timer.
        // TODO tomorrow
        if (rewardedAd != null && !isLoading) {
            loadRewardedAd();
        }
        gamePaused = false;
        gameOver = false;
    }


    private void introduceVideoAd(int rewardAmount, String rewardType, String type) {
        isBlackHit = true;

        if (rewardType.equals("coins"))
            requiredCoin *= 2;


        AdDialogFragment dialog = AdDialogFragment.newInstance(rewardAmount, rewardType, requiredCoin, checkLoadAd, type);
        dialog.setAdDialogInteractionListener(
                new AdDialogFragment.AdDialogInteractionListener() {
                    @Override
                    public void onShowAd() {
                        Log.d(TAG, "The rewarded interstitial ad is starting.");

                        showRewardedVideo();
                    }

                    @Override
                    public void onCancelAd(String type) {
                        if (type.equals("gameOver")) {
                            Log.d(TAG, "The rewarded interstitial ad was skipped before it starts.");
                            blackY = frameHeight + 100;

                            soundPlayer.playHitBlackSound();

                            gameOver("gameOver");
                        } else {
                            timerLabel.setVisibility(View.GONE);
                            if (gameStatus)
                                gameOver("timeOut");
                        }
                    }

                    @Override
                    public void onUserPayCoin(String type) {
                        isBlackHit = false;
                        Log.d(TAG, "the user pay coin ");
                        blackY = -100;
                        blackX = (float) Math.floor(Math.random() * (frameWidth - black.getWidth()));
                        if (type.equals("timeout")) {
                            millisUntilFinished1 = 15000;
                            //startGame();
                        }
                    }
                });
        dialog.show(getSupportFragmentManager(), "AdDialogFragment");
    }

    private void showRewardedVideo() {

        if (rewardedAd == null) {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
            return;
        }
        // showVideoButton.setVisibility(View.INVISIBLE);

        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "onAdShowedFullScreenContent");
                        Toast.makeText(MainActivity.this, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        Log.d(TAG, "onAdFailedToShowFullScreenContent");
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;

                        Toast.makeText(
                                MainActivity.this, "onAdFailedToShowFullScreenContent: ", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.


                        rewardedAd = null;

                        MainActivity.this.loadRewardedAd();

                        // introduceVideoAd(100, "coins1", typeGame);

                    }
                });
        Activity activityContext = MainActivity.this;
        rewardedAd.show(
                activityContext,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d("TAG", "The user earned the reward.");
                       /* int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                        //addCoins(rewardItem.getAmount()); */


                       /* blackY = -100;
                        blackX = (float) Math.floor(Math.random() * (frameWidth - black.getWidth()));*/

                        UserSharedPreference userSharedPreference = new UserSharedPreference(MainActivity.this);
                        userSharedPreference.setCoins(userSharedPreference.getCoins() + 100);
                        checkLoadAd = false;
                        Toast.makeText(MainActivity.this, "The user earned the reward.", Toast.LENGTH_SHORT)
                                .show();
                        listener.onAdFinished();
                    }
                });
    }

    public interface OnLoadAdListener {


        void onLoadFinished();

        void onAdFinished();


    }

    public void setAdDialogInteractionListener(MainActivity.OnLoadAdListener listener) {
        this.listener = listener;
    }


}


