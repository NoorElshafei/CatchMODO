package com.bbi.catchmodo.util;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.ImageView;


import com.bbi.catchmodo.R;
import com.bbi.catchmodo.data.local.UserSharedPreference;

import java.util.Locale;

public class Language {

    public static void changeLang(String lang, Context context) {

        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(myLocale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());


        /*final Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration cfg = new Configuration(res.getConfiguration());
        cfg.locale = locale;
        res.updateConfiguration(cfg, res.getDisplayMetrics());*/

    }

    public static void changeBackDependsLanguage(ImageView view, Context context) {
        UserSharedPreference userSharedPreference = new UserSharedPreference(context);
        String lang = userSharedPreference.getLanguage();
        if (!Locale.getDefault().getLanguage().equals("en")) {
            view.setImageResource(R.drawable.right);
        }
    }

    public static void changeBackDependsLanguage1(ImageView view, Context context) {
        UserSharedPreference userSharedPreference = new UserSharedPreference(context);
        String lang = userSharedPreference.getLanguage();
        if (lang.equals("ar")) {
            view.setRotationY(180.0f);
        }
    }
}
