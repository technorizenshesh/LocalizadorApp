package com.my.localizadorapp;

import static com.my.localizadorapp.Preference.LANGUAGE;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;

import com.my.localizadorapp.act.SignUpActivity;

import java.util.Locale;

public class Localizadorapp extends Application {



    public static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public  static void changeLangDialog(Activity mContext) {
        String language = Preference.get(mContext, LANGUAGE);
        Dialog dialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.change_language_dialog);
        dialog.setCancelable(true);
        Button btContinue = dialog.findViewById(R.id.btContinue);
        RadioButton radioEng = dialog.findViewById(R.id.radioEng);
        RadioButton radioSpanish = dialog.findViewById(R.id.radioSpanish);
        if ("es".equals(language)) {
            radioSpanish.setChecked(true);
        } else {
            radioEng.setChecked(true);
        }

        dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);

        btContinue.setOnClickListener(v -> {
            if (radioEng.isChecked()) {
                Localizadorapp.updateResources(mContext, "en");
                Preference.save(mContext, LANGUAGE, "en");
                mContext.finish();
                mContext.startActivity(new Intent(mContext, mContext.getClass()));
                dialog.dismiss();
             //   mContext.recreate();

            } else if (radioSpanish.isChecked()) {
                Localizadorapp.updateResources(mContext, "es");
                Preference.save(mContext, LANGUAGE, "es");
                mContext.finish();
                mContext.startActivity(new Intent(mContext, mContext.getClass()));
                dialog.dismiss();
               // mContext.recreate();

            }
        });

        dialog.show();

    }
    public static PendingIntent createPendingIntentGetActivity(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.getActivity(context,
                    0 , intent,
                    PendingIntent.FLAG_IMMUTABLE);
        } else {
            return PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_ONE_SHOT);
        }
    }
    public static PendingIntent createPendingIntentGetActivity2(Context context, Intent intent,int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.getActivity(context,
                    requestCode , intent,
                    PendingIntent.FLAG_IMMUTABLE);
        } else {
            return PendingIntent.getActivity(context, requestCode, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_ONE_SHOT);
        }
    }
}
