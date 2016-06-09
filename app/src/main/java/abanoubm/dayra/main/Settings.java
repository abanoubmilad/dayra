package abanoubm.dayra.main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import abanoubm.dayra.R;
import abanoubm.dayra.alarm.AttendanceReceiver;
import abanoubm.dayra.alarm.BirthDayReceiver;

public class Settings extends Activity {

    private CheckBox attend, bday;
    private AlarmManager manager;
    private PendingIntent attendPIntent, bdayPIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2))
                .setText(R.string.label_settings);

        attend = (CheckBox) findViewById(R.id.attend);
        bday = (CheckBox) findViewById(R.id.bday);

        new FetchAlarmStatesTask().execute();

        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        attendPIntent =
                PendingIntent.getBroadcast(Settings.this, 100,
                        new Intent(Settings.this, AttendanceReceiver.class), 0);
        bdayPIntent = PendingIntent.getBroadcast(Settings.this, 200,
                new Intent(Settings.this, BirthDayReceiver.class), 0);

        attend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attend.isChecked()) {
                    new addAlarmTask().execute(1);
                } else {
                    new removeAlarmTask().execute(1);
                }


            }
        });
        bday.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bday.isChecked()) {
                    new addAlarmTask().execute(2);
                } else {
                    new removeAlarmTask().execute(2);

                }

            }
        });


    }

    private class FetchAlarmStatesTask extends
            AsyncTask<Void, Void, Boolean[]> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(Settings.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected Boolean[] doInBackground(Void... params) {
            Boolean[] arr = new Boolean[2];
            DB db = DB.getInstant(getApplicationContext());
            arr[0] = db.doesAlarmExist("0");
            arr[1] = db.doesAlarmExist("1");
            return arr;


        }

        @Override
        protected void onPostExecute(Boolean[] result) {
            attend.setChecked(result[0]);
            attend.setChecked(result[1]);
            pBar.dismiss();


        }

    }

    private class removeAlarmTask extends
            AsyncTask<Integer, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(Settings.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            boolean check = DB.getInstant(getApplicationContext()).removeAlarm(params[0] + "");
            if (!check) {
                if (params[0] == 1)
                    manager.cancel(attendPIntent);
                else
                    manager.cancel(bdayPIntent);
            }
            return null;


        }

        @Override
        protected void onPostExecute(Void result) {
            pBar.dismiss();
            Toast.makeText(getApplicationContext(), R.string.label_noti_alarm_set,
                    Toast.LENGTH_SHORT).show();

        }

    }

    private class addAlarmTask extends
            AsyncTask<Integer, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(Settings.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected Void doInBackground(Integer... params) {

            boolean check = DB.getInstant(getApplicationContext()).addAlarm(params[0] + "");
            if (check) {
                if (params[0] == 1) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, 15);
                    manager.setInexactRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            60 * 60 * 24 * 1000 * 7, attendPIntent);
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, 14);
                    manager.setInexactRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            60 * 60 * 24 * 1000, bdayPIntent);
                }
            }
            return null;


        }

        @Override
        protected void onPostExecute(Void result) {
            pBar.dismiss();
            Toast.makeText(getApplicationContext(), R.string.label_noti_alarm_set,
                    Toast.LENGTH_SHORT).show();

        }

    }
}
