package abanoubm.dayra.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(Splash.this, Main.class));
                }
            }
        };
        timerThread.start();

    }

}