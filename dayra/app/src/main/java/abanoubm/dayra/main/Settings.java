package abanoubm.dayra.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import abanoubm.dayra.R;
import abanoubm.dayra.alarm.DBAlarm;

public class Settings extends Activity {

    private CheckBox attend, bday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2))
                .setText(R.string.label_settings);
        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        attend = (CheckBox) findViewById(R.id.attend);
        bday = (CheckBox) findViewById(R.id.bday);

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attend.isChecked()) {
                    new addAlarmTask().execute(Utility.ATTEND_ALARM_TYPE);
                } else {
                    new removeAlarmTask().execute(Utility.ATTEND_ALARM_TYPE);
                }
            }
        });

        bday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bday.isChecked()) {
                    new addAlarmTask().execute(Utility.BDAY_ALARM_TYPE);
                } else {
                    new removeAlarmTask().execute(Utility.BDAY_ALARM_TYPE);
                }
            }
        });

        new FetchAlarmStatesTask().execute();

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
            DBAlarm db = DBAlarm.getInstant(getApplicationContext());
            String dbname = Utility.getDayraName(getApplicationContext());
            arr[0] = db.doesAlarmExist(Utility.ATTEND_ALARM_TYPE + "", dbname);
            arr[1] = db.doesAlarmExist(Utility.BDAY_ALARM_TYPE + "", dbname);
            return arr;


        }

        @Override
        protected void onPostExecute(Boolean[] result) {
            attend.setChecked(result[0]);
            bday.setChecked(result[1]);
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
            boolean check = DBAlarm.getInstant(getApplicationContext()).
                    removeAlarm(params[0] + "", Utility.getDayraName(getApplicationContext()));
            if (!check) {
                if (params[0] == Utility.ATTEND_ALARM_TYPE)
                    Utility.removeAlarming(getApplicationContext(), Utility.ATTEND_ALARM_TYPE);
                else
                    Utility.removeAlarming(getApplicationContext(), Utility.BDAY_ALARM_TYPE);
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

            boolean check = DBAlarm.getInstant(getApplicationContext()).
                    addAlarm(params[0] + "", Utility.getDayraName(getApplicationContext()));
            if (check) {
                if (params[0] == Utility.ATTEND_ALARM_TYPE)
                    Utility.addAlarming(getApplicationContext(), Utility.ATTEND_ALARM_TYPE);
                else
                    Utility.addAlarming(getApplicationContext(), Utility.BDAY_ALARM_TYPE);
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
