package abanoubm.dayra.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.Field;
import abanoubm.dayra.model.IntWrapper;

public class AttendanceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent alarmIntent) {

        ArrayList<String> array = DBAlarm.getInstant(context).getAlarmDayras(Utility.ATTEND_ALARM_TYPE + "");
        if (array.size() == 0) {
            Utility.removeAlarming(context, Utility.ATTEND_ALARM_TYPE);
            return;
        }

        String msgLabel = context.getResources().getString(R.string.label_msg);
        String callLabel = context.getResources().getString(R.string.label_call);

        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.MONTH, false);
        cal.roll(Calendar.MONTH, false);
        String pastMonth = Utility.produceDateRegex(
                cal.get(Calendar.DAY_OF_MONTH) + "",
                (cal.get(Calendar.MONTH) + 1) + "", cal.get(Calendar.YEAR) + "");

        Bitmap defPhoto = ((BitmapDrawable)
                ContextCompat.getDrawable(context, R.mipmap.def)).getBitmap();

        int start = 70000;
        IntWrapper total = new IntWrapper();
        StringBuilder builder;
        for (String dayraName : array) {
            builder = new StringBuilder("");
            ArrayList<Field> result = DB.getInstant(context, dayraName).alarmAttendance(pastMonth, builder, total);
            if (total.getCounter() > 5) {
                NotificationCompat.Builder n = new NotificationCompat.Builder(
                        context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.bing))
                        .setContentTitle(
                                dayraName
                                        + " - "
                                        + context.getResources().getString(
                                        R.string.label_noti_attend))
                        .setContentText(
                                context.getResources().getString(
                                        R.string.label_noti_more)
                                        + " " + total.getCounter())
                        .setAutoCancel(true);
                if (builder.length() != 0) {
                    n.addAction(R.mipmap.ic_msg, msgLabel,
                            PendingIntent.getActivity(context,
                                    (int) System.currentTimeMillis(), new Intent(Intent.ACTION_SENDTO, Uri
                                            .parse("smsto:"
                                                    + builder.toString())), 0));
                }
                NotificationManager nm = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(start++, n.build());
            } else {
                for (Field field : result) {
                    NotificationCompat.Builder n = new NotificationCompat.Builder(
                            context)
                            .setLargeIcon(field.getPhoto() != null ? field.getPhoto() : defPhoto)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.bing))
                            .setContentTitle(
                                    dayraName
                                            + " - "
                                            + context
                                            .getResources()
                                            .getString(
                                                    R.string.label_noti_attend))
                            .setContentText(field.getName() + " " + field.getDay()).setAutoCancel(true);
                    if (field.getPhone().length() > 0) {
                        n.addAction(R.mipmap.ic_call, callLabel,
                                PendingIntent.getActivity(context,
                                        (int) System.currentTimeMillis(), new Intent(Intent.ACTION_CALL, Uri
                                                .fromParts("tel", field.getPhone(), null)), 0))
                                .addAction(R.mipmap.ic_msg, msgLabel,
                                        PendingIntent.getActivity(context,
                                                (int) System.currentTimeMillis(), new Intent(Intent.ACTION_SENDTO, Uri
                                                        .parse("smsto:"
                                                                + field.getPhone())), 0));
                    }
                    NotificationManager nm = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(start++, n.build());
                }


            }


        }
    }

}
