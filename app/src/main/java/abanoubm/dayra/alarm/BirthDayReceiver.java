package abanoubm.dayra.alarm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;

public class BirthDayReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent alarmIntent) {

        String dbname = context.getSharedPreferences("alarms",
                Context.MODE_PRIVATE).getString("dbname", "");
        if (dbname.length() == 0 || !DB.isDBExists(context, dbname))
            return;
        DB db = DB.getInstance(context, dbname);

        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        ArrayList<String> arr;
        try {
            arr = db.checkBDay(month, day);
            if (arr != null && arr.size() != 0) {
                int start = 100000;
                if (arr.size() > 10) {
                    NotificationCompat.Builder n = new NotificationCompat.Builder(
                            context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(
                                    dbname
                                            + " - "
                                            + context.getResources().getString(
                                            R.string.label_noti_bday))
                            .setContentText(
                                    context.getResources().getString(
                                            R.string.label_noti_more)
                                            + " " + arr.size())
                            .setAutoCancel(true);
                    NotificationManager nm = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(start++, n.build());
                } else {
                    for (String str : arr) {
                        NotificationCompat.Builder n = new NotificationCompat.Builder(
                                context)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(
                                        dbname
                                                + " - "
                                                + context
                                                .getResources()
                                                .getString(
                                                        R.string.label_noti_bday))
                                .setContentText(str).setAutoCancel(true);
                        NotificationManager nm = (NotificationManager) context
                                .getSystemService(Context.NOTIFICATION_SERVICE);
                        nm.notify(start++, n.build());
                    }
                }
            }
        } catch (Exception e) {
        }

    }

}
