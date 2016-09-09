package abanoubm.dayra.main;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import abanoubm.dayra.R;
import abanoubm.dayra.alarm.AttendanceReceiver;
import abanoubm.dayra.alarm.BirthDayReceiver;

public class Utility {

    public final static int BDAY_ALARM_TYPE = 1, ATTEND_ALARM_TYPE = 0;

    public static int getArabicLang(Context context) {
        return context.getSharedPreferences("lang",
                Context.MODE_PRIVATE).getInt("ar", 0);
    }

    public static void setArabicLang(Context context, int flag) {
        context.getSharedPreferences("lang",
                Context.MODE_PRIVATE).edit().putInt("ar", flag).apply();
    }


    public static String getDayraName(Context context) {
        return (context.getSharedPreferences("login",
                Context.MODE_PRIVATE)).getString("dbname", "");
    }

    public static void clearLogin(Context context) {
        context.getSharedPreferences("login",
                Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static void makeLogin(Context context, String dayra) {
        context.getSharedPreferences("login",
                Context.MODE_PRIVATE).edit()
                .putString("dbname", dayra).apply();
    }

    public static String produceDateRegex(String day, String month) {
        if (month.length() == 1) {
            if (day.length() == 1)
                return "____-0" + month + "-0" + day;
            else
                return "____-0" + month + "-" + day;
        } else {
            if (day.length() == 1)
                return "____-" + month + "-0" + day;
            else
                return "____-" + month + "-" + day;
        }
    }

    public static String produceDateRegex(String day, String month, String year) {
        if (month.length() == 1) {
            if (day.length() == 1)
                return year + "-0" + month + "-0" + day;
            else
                return year + "-0" + month + "-" + day;
        } else {
            if (day.length() == 1)
                return year + "-" + month + "-0" + day;
            else
                return year + "-" + month + "-" + day;
        }
    }
    public static String produceDate(String day, String month, String year) {
        if (month.length() == 1) {
            if (day.length() == 1)
                return year + "-0" + month + "-0" + day;
            else
                return year + "-0" + month + "-" + day;
        } else {
            if (day.length() == 1)
                return year + "-" + month + "-0" + day;
            else
                return year + "-" + month + "-" + day;
        }
    }

    public static String produceDate(int day, int month, int year) {
        if (month < 10) {
            if (day < 10)
                return year + "-0" + month + "-0" + day;
            else
                return year + "-0" + month + "-" + day;
        } else {
            if (day < 10)
                return year + "-" + month + "-0" + day;
            else
                return year + "-" + month + "-" + day;
        }
    }

    public static boolean isInvlaidDBName(String str) {
        return str.length() == 0 || !str.matches("[أؤءةابتثجحخدذرزسشصضطظعغفقكلمنهوىيa-z0-9A-Z\\s]+") ||
                str.contains("journal");
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getBitmap(byte[] image) {
        if (image == null)
            return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    // convert from image path to bitmap
    public static Bitmap getBitmap(String path) {
        if (path == null || path.length() == 0
                || !new File(path).exists())
            return null;
        return ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(path), 175, 175);
    }

    // convert to bitmap thumbnail
    public static Bitmap getThumbnail(Bitmap bitmap) {
        return ThumbnailUtils.extractThumbnail(bitmap, 175, 175);
    }


    public static String migirateDate(String oldDate) {
        if (!oldDate.matches("[0-9]{1,2}-[0-9]{1,2}-[0-9]{4}"))
            return "";
        String[] arr = oldDate.split("-");
        if (arr[1].length() == 1) {
            if (arr[0].length() == 1)
                return arr[2] + "-0" + arr[1] + "-0" + arr[0];
            else
                return arr[2] + "-0" + arr[1] + "-" + arr[0];
        } else {
            if (arr[0].length() == 1)
                return arr[2] + "-" + arr[1] + "-0" + arr[0];
            else
                return arr[2] + "-" + arr[1] + "-" + arr[0];
        }

    }

    public static String getDayraFolder() {
        String path;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/";
        } else {
            path = android.os.Environment.getDataDirectory()
                    .getAbsolutePath() + "/";
        }
        path += "dayra folder";
        new File(path).mkdirs();
        return path;
    }

   // public static String getRealPath(Uri uri) {
//        return uri.getPath().toString();
//    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public static String getRealPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public static void removeAlarming(Context context, int alarm_type) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarm_type == BDAY_ALARM_TYPE)
            manager.cancel(PendingIntent.getBroadcast(context, 200,
                    new Intent(context, BirthDayReceiver.class), 0));
        else if (alarm_type == ATTEND_ALARM_TYPE)
            manager.cancel(PendingIntent.getBroadcast(context, 100,
                    new Intent(context, AttendanceReceiver.class), 0));


    }

    public static void addAlarming(Context context, int alarm_type) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarm_type == BDAY_ALARM_TYPE) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 14);
            manager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(context, 200,
                            new Intent(context, BirthDayReceiver.class), 0));

        } else if (alarm_type == ATTEND_ALARM_TYPE) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 18);
            manager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 30, PendingIntent.getBroadcast(context, 100,
                            new Intent(context, AttendanceReceiver.class), 0));
        }


    }

    public static CharSequence[] getAttendanceTypes(Context context) {
        CharSequence[] types = context.getResources().getTextArray(R.array.attendance_type);
        SharedPreferences sf = context.getSharedPreferences("attend",
                Context.MODE_PRIVATE);
        if (sf.getBoolean("active", false)) {
            for (int i = 0; i < types.length; i++)
                types[i] = sf.getString(i + "", types[i].toString());
        }
        return types;

    }

    public static String[] getModifiedAttendanceTypes(Context context) {
        String[] types = new String[5];
        SharedPreferences sf = context.getSharedPreferences("attend",
                Context.MODE_PRIVATE);
        for (int i = 0; i < types.length; i++)
            types[i] = sf.getString(i + "", null);
        return types;

    }

    public static void setAttendanceTypes(Context context, String[] newTypes) {
        SharedPreferences.Editor editor = context.getSharedPreferences("attend",
                Context.MODE_PRIVATE).edit();
        editor.clear().apply();

        boolean active = false;
        for (int i = 0; i < newTypes.length; i++) {
            if (newTypes[i] != null) {
                active = true;
                editor.putString(i + "", newTypes[i]);

            }

        }
        editor.putBoolean("active", active);
        editor.apply();


    }

}
