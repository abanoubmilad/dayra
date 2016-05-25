package abanoubm.dayra.main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import abanoubm.dayra.R;
import abanoubm.dayra.alarm.AttendReceiver;
import abanoubm.dayra.alarm.BDayReceiver;
import abanoubm.dayra.alarm.VisitReceiver;

public class Settings extends Activity {

    private AlarmManager manager;
    private PendingIntent AttendPIntent, VisitPIntent, bdayPIntent;
    private String dbname;
    private TextView edit_dayra;
    private SharedPreferences sp;
    private CheckBox cb_attend, cb_visit, cb_bday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2))
                .setText(R.string.label_settings);

        cb_attend = (CheckBox) findViewById(R.id.cb_attend);
        cb_visit = (CheckBox) findViewById(R.id.cb_visit);
        cb_bday = (CheckBox) findViewById(R.id.cb_bday);
        edit_dayra = (EditText) findViewById(R.id.edit_dayra);

        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        sp = getSharedPreferences("alarms", Context.MODE_PRIVATE);

        dbname = sp.getString("dbname", "");
        edit_dayra.setText(dbname);
        cb_attend.setChecked(sp.getBoolean("attend", false));
        cb_visit.setChecked(sp.getBoolean("visit", false));
        cb_bday.setChecked(sp.getBoolean("bday", false));

        VisitPIntent = PendingIntent.getBroadcast(Settings.this, 200,
                new Intent(Settings.this, VisitReceiver.class).putExtra(
                        "dbname", dbname), 0);

        bdayPIntent = PendingIntent.getBroadcast(Settings.this, 300,
                new Intent(Settings.this, BDayReceiver.class).putExtra(
                        "dbname", dbname), 0);

        ((ImageView) findViewById(R.id.spin_dayra))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        choice();

                    }
                });
        ((TextView) findViewById(R.id.save))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dbname.length() == 0)
                            return;

                        sp.edit().putString("dbname", dbname).commit();

                        boolean temp = cb_attend.isChecked();

                        sp.edit().putBoolean("attend", temp).commit();
                        AttendPIntent = PendingIntent.getBroadcast(
                                Settings.this, 100, new Intent(Settings.this,
                                        AttendReceiver.class), 0);
                        manager.cancel(AttendPIntent);

                        if (temp) {

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, 17);
                            manager.setInexactRepeating(
                                    AlarmManager.RTC_WAKEUP,
                                    calendar.getTimeInMillis(),
                                    60 * 60 * 24 * 1000 * 7, AttendPIntent);

                        }

                        temp = cb_visit.isChecked();
                        sp.edit().putBoolean("visit", temp).commit();
                        VisitPIntent = PendingIntent.getBroadcast(
                                Settings.this, 200, new Intent(Settings.this,
                                        VisitReceiver.class), 0);
                        if (temp) {

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, 17);
                            manager.setInexactRepeating(
                                    AlarmManager.RTC_WAKEUP,
                                    calendar.getTimeInMillis(),
                                    60 * 60 * 24 * 1000 * 7, VisitPIntent);

                        } else
                            manager.cancel(VisitPIntent);

                        temp = cb_bday.isChecked();
                        sp.edit().putBoolean("bday", temp).commit();
                        bdayPIntent = PendingIntent.getBroadcast(Settings.this,
                                300, new Intent(Settings.this,
                                        BDayReceiver.class), 0);
                        if (temp) {

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, 12);
                            manager.setInexactRepeating(
                                    AlarmManager.RTC_WAKEUP,
                                    calendar.getTimeInMillis(),
                                    60 * 60 * 24 * 1000, bdayPIntent);

                        } else
                            manager.cancel(bdayPIntent);

                        Toast.makeText(getApplicationContext(), R.string.label_noti_alarm_set,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void choice() {
        String inpath;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            inpath = getApplicationContext().getApplicationInfo().dataDir
                    + "/databases/";
        } else {
            inpath = "/data/data/" + getApplicationContext().getPackageName()
                    + "/databases/";
        }
        File folder = new File(inpath);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null || listOfFiles.length == 0) {
            Toast.makeText(getApplicationContext(), R.string.msg_no_dayra,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> names = new ArrayList<>(listOfFiles.length);
        for (File file : listOfFiles) {
            if (!file.getName().contains("-journal"))
                names.add(file.getName());
        }

        final CharSequence[] arr = (CharSequence[]) names
                .toArray(new String[names.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
        builder.setTitle(getResources().getString(R.string.label_select_dayra));
        builder.setItems(arr, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dbname = arr[which].toString();
                edit_dayra.setText(dbname);

            }

        });
        builder.create().show();
    }

}
