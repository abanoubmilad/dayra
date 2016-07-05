package abanoubm.dayra.alarm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactField;

public class BirthDayReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent alarmIntent) {
        Log.i("herebyu","ana hnnnnnnnnnnnnna");
        String date = Utility.produceDate(
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "",
                (Calendar.getInstance().get(Calendar.MONTH) + 1) + "");

        ArrayList<String> array = DBAlarm.getInstant(context).getAlarmDayras(Utility.BDAY_ALARM_TYPE+"");
        int start = 7;
        for (String dayraName : array) {
            ArrayList<ContactField> result = DB.getInstant(context, dayraName).searchBirthdays(date);
            if (result.size() > 5) {
                NotificationCompat.Builder n = new NotificationCompat.Builder(
                        context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(
                                dayraName
                                        + " - "
                                        + context.getResources().getString(
                                        R.string.label_noti_bday))
                        .setContentText(
                                context.getResources().getString(
                                        R.string.label_noti_more)
                                        + " " + result.size())
                        .setAutoCancel(true);
                NotificationManager nm = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(start++, n.build());
            } else {
                for (ContactField contactDay : result) {
                    NotificationCompat.Builder n = new NotificationCompat.Builder(
                            context)
                            .setLargeIcon(contactDay.getPhoto())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(
                                    dayraName
                                            + " - "
                                            + context
                                            .getResources()
                                            .getString(
                                                    R.string.label_noti_bday))
                            .setContentText(contactDay.getName()).setAutoCancel(true);
                    NotificationManager nm = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(start++, n.build());
                }


            }


        }
    }

}
