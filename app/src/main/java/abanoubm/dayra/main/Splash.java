package abanoubm.dayra.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import abanoubm.dayra.R;

public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        ((TextView) findViewById(R.id.footer)).setText(String.format(
                getResources().getString(R.string.copyright),
                Calendar.getInstance().get(Calendar.YEAR)));

        findViewById(R.id.layout).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade));

        Thread timerThread = new Thread() {
            public void run() {
                try {

                    MediaPlayer.create(getApplicationContext(), R.raw.splash_music).start();

                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    if (Utility.getArabicLang(getApplicationContext()) == 1) {
                        Utility.setArabicLang(getApplicationContext(), 2);
                        Locale myLocale = new Locale("ar");
                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);
                    }

                    if (!Utility.getDayraName(getApplicationContext()).equals(""))
                        startActivity(new Intent(getApplicationContext(), Home.class));
                    else
                        startActivity(new Intent(Splash.this, Main.class));

                    finish();
                }
            }
        };
        timerThread.start();

    }

}