package com.bbi.catchmodo.util;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.local.UserSharedPreference;
import com.bbi.catchmodo.databinding.DialogAdBinding;
import com.bbi.catchmodo.ui.activities.MainActivity;

/**
 * A dialog fragment to inform the users about an upcoming interstitial video ad and let the user
 * click the cancel button to skip the ad. This fragment inflates the dialog_ad.xml layout.
 */
public class AdDialogFragment extends DialogFragment {

    /**
     * Bundle argument's name for number of coins rewarded by watching an ad.
     */
    public static final String REWARD_AMOUNT = "rewardAmount";

    /**
     * Bundle argument's name for the unit of the reward amount.
     */
    public static final String REWARD_TYPE = "rewardType";

    /**
     * Number of seconds to count down before showing ads.
     */
    private static final long COUNTER_TIME = 5;

    /**
     * A string that represents this class in the logcat.
     */
    private static final String TAG = "AdDialogFragment";

    /**
     * A timer for counting down until showing ads.
     */
    private CountDownTimer countDownTimer;

    /**
     * Number of remaining seconds while the count down timer runs.
     */
    private long timeRemaining;

    /**
     * Delivers the events to the Main Activity when the user interacts with this dialog.
     */
    private AdDialogInteractionListener listener;

    private DialogAdBinding binding;

    private UserSharedPreference userSharedPreference;

    private static final String REQUIRED_COIN = "REQUIRED_COIN";
    private static final String CHECK_LOAD_AD = "CHECK_LOAD_AD";

    private int requiredCoin;
    private boolean checkLoadAd;
    private String gameType;
    private static final String GAME_TYPE = "GAME_TYPE";


    /**
     * Creates an instance of the AdDialogFragment and sets reward information for its title.
     *
     * @param rewardAmount Number of coins rewarded by watching an ad.
     * @param rewardType   The unit of the reward amount. For example: coins, tokens, life, etc.
     * @param type
     */
    public static AdDialogFragment newInstance(int rewardAmount, String rewardType, int requiredCoin, boolean checkLoadAd, String type) {

        AdDialogFragment fragment = new AdDialogFragment();
        Bundle args = new Bundle();
        args.putInt(REWARD_AMOUNT, rewardAmount);
        args.putInt(REQUIRED_COIN, requiredCoin);
        args.putBoolean(CHECK_LOAD_AD, checkLoadAd);
        args.putString(REWARD_TYPE, rewardType);
        args.putString(GAME_TYPE, type);
        fragment.setArguments(args);
        return fragment;

    }

    /**
     * Registers the callbacks to be invoked when the user interacts with this dialog. If there is no
     * user interactions, the dialog is dismissed and the user will see a video interstitial ad.
     *
     * @param listener The callbacks that will run when the user interacts with this dialog.
     */


    public void setAdDialogInteractionListener(AdDialogInteractionListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_ad, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().setCancelable(false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userSharedPreference = new UserSharedPreference(getContext());


        requiredCoin = getArguments().getInt(REQUIRED_COIN);

        checkLoadAd = getArguments().getBoolean(CHECK_LOAD_AD);

        gameType = getArguments().getString(GAME_TYPE);


        binding.noThanks.setOnClickListener(view1 -> {
            getDialog().cancel();
        });

        binding.watchAd.setOnClickListener(view1 -> {

            if (!checkLoadAd) {
                Toast.makeText(getContext(), "Please wait for load Ad ", Toast.LENGTH_SHORT).show();
            } else {
                //getDialog().dismiss();
                if (listener != null) {
                    Log.d(TAG, "onFinish: Calling onShowAd().");
                    listener.onShowAd();
                }
            }


        });

        binding.payCoinConstraint.setOnClickListener(view1 -> {

            if (userSharedPreference.getCoins() > requiredCoin) {
                userSharedPreference.setCoins(userSharedPreference.getCoins() - requiredCoin);
                getDialog().dismiss();

                if (listener != null) {
                    listener.onUserPayCoin(gameType);
                }
            } else {
                Toast.makeText(getContext(), "you don't have coin", Toast.LENGTH_SHORT).show();
            }

        });


        setUI();


        ((MainActivity) getActivity()).setAdDialogInteractionListener(new MainActivity.OnLoadAdListener() {
            @Override
            public void onLoadFinished() {
                checkLoadAd = true;
            }

            @Override
            public void onAdFinished() {
                binding.youHaveText.setText("You have " + userSharedPreference.getCoins() + "coins");
            }
        });

    }

    private void setUI() {
        binding.payCoinConstraint.setText("Continue with " + requiredCoin + "coins");
        binding.youHaveText.setText("You have " + userSharedPreference.getCoins() + "coins");

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }



    /* @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    // Inflate and set the layout for the dialog.
    // Pass null as the parent view because its going in the dialog layout.
    View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_ad, null);
    builder.setView(view);

    Bundle args = getArguments();
    int rewardAmount = -1;
    String rewardType = null;
    if (args != null) {
      rewardAmount = args.getInt(REWARD_AMOUNT);
      rewardType = args.getString(REWARD_TYPE);
    }
    if (rewardAmount > 0 && rewardType != null) {
      builder.setTitle(getString(R.string.more_coins_text, rewardAmount, rewardType));
    }

  */

  /*
builder.setNegativeButton(
            getString(R.string.negative_button_text),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int id) {
                getDialog().cancel();
              }
            });*/

  /*
    Dialog dialog = builder.create();
    createTimer(COUNTER_TIME, view);
    return dialog;
  }*/

    /**
     * Creates the a timer to count down until the rewarded interstitial ad.
     *
     * @param time       Number of seconds to count down.
     * @param dialogView The view of this dialog for updating the remaining seconds count.
     */


   /* private void createTimer(long time, View dialogView) {
        final TextView textView = dialogView.findViewById(R.id.timer);
        countDownTimer =
                new CountDownTimer(time * 1000, 50) {
                    @Override
                    public void onTick(long millisUnitFinished) {
                        timeRemaining = ((millisUnitFinished / 1000) + 1);
                        textView.setText(
                                String.format(getString(R.string.video_starting_in_text), timeRemaining));
                    }

                    @Override
                    public void onFinish() {
                        getDialog().dismiss();

                        if (listener != null) {
                            Log.d(TAG, "onFinish: Calling onShowAd().");
                            listener.onShowAd();
                        }
                    }
                };
        countDownTimer.start();
    }
*/

    /**
     * Called when the user clicks the "No, Thanks" button or press the back button.
     */

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        if (listener != null) {
            Log.d(TAG, "onCancel: Calling onCancelAd().");
            listener.onCancelAd(gameType);
        }
    }

    /**
     * Called when the fragment is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Cancelling the timer.");
        //    countDownTimer.cancel();
        //   countDownTimer = null;
    }

    /**
     * Callbacks when the user interacts with this dialog.
     */
    public interface AdDialogInteractionListener {

        /**
         * Called when the timer finishes without user's cancellation.
         */
        void onShowAd();

        /**
         * Called when the user clicks the "No, thanks" button or press the back button.
         */
        void onCancelAd(String type);

        void onUserPayCoin(String type);
    }
}
