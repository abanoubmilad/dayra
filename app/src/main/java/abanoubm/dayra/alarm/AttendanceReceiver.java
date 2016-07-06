package abanoubm.dayra.alarm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactField;

public class AttendanceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent alarmIntent) {

        ArrayList<String> array = DBAlarm.getInstant(context).getAlarmDayras(Utility.ATTEND_ALARM_TYPE + "");
        if (array.size() == 0) {
            Utility.removeAlarming(context, Utility.ATTEND_ALARM_TYPE);
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.MONTH, false);
        String pastMonth = Utility.produceDateRegex(
                cal.get(Calendar.DAY_OF_MONTH) + "",
                (cal.get(Calendar.MONTH) + 1) + "", cal.get(Calendar.YEAR) + "");

        Bitmap defPhoto = ((BitmapDrawable)
                ContextCompat.getDrawable(context, R.mipmap.def)).getBitmap();

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
                                        R.string.label_noti_attend))
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
                            .setLargeIcon(contactDay.getPhoto() != null ? contactDay.getPhoto() : defPhoto)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(
                                    dayraName
                                            + " - "
                                            + context
                                            .getResources()
                                            .getString(
                                                    R.string.label_noti_attend))
                            .setContentText(contactDay.getName() + " - " + contactDay.getField()).setAutoCancel(true);
                    NotificationManager nm = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(start++, n.build());
                }


            }


        }
    }

}
