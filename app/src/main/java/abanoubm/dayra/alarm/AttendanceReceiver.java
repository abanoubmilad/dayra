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
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactField;

public class AttendanceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent alarmIntent) {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.MONTH, false);
        String pastMonth = Utility.produceDate(
                cal.get(Calendar.DAY_OF_MONTH) + "",
                (cal.get(Calendar.MONTH) + 1) + "", cal.get(Calendar.YEAR) + "");

        ArrayList<String> array = DBAlarm.getInstant(context).getAlarmDayras(Utility.ATTEND_ALARM_TYPE+"");
        int start = 70000;
        for (String dayraName : array) {
            ArrayList<ContactField> result = DB.getInstant(context, dayraName).getContactsAttendanceAbsence(pastMonth);
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
