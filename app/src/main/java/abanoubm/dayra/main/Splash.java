package abanoubm.dayra.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Calendar;

import abanoubm.dayra.R;

public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        ((TextView) findViewById(R.id.footer)).setText(String.format(
                getResources().getString(R.string.copyright),
                Calendar.getInstance().get(Calendar.YEAR)));

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.splash);


        findViewById(R.id.img1).setAnimation(anim);
       findViewById(R.id.img6).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
               R.anim.rotate));
//        findViewById(R.id.img3).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.splash3));
//        findViewById(R.id.img4).setAnimation(anim);
//        findViewById(R.id.img5).setAnimation(anim);

//        Thread timerThread = new Thread() {
//            public void run() {
//                try {
//                    sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    startActivity(new Intent(Splash.this, Main.class));
//                }
//            }
//        };
     //   timerThread.start();

    }

}