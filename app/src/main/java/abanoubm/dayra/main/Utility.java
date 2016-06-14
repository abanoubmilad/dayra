package abanoubm.dayra.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Utility {

    public static String getDayraName(Context context) {
        return (context.getSharedPreferences("login",
                Context.MODE_PRIVATE)).getString("dbname", "");
    }

    public static final int update = Color
            .rgb(130, 199, 132), deupdate = Color.rgb(244, 143, 177);

    public static String produceDate(String month) {
        if (month.length() == 1)
            return "-----0" + month + "---";
        else
            return "-----" + month + "---";
    }

    public static String produceDate(String day, String month) {
        if (month.length() == 1) {
            if (day.length() == 1)
                return "-----0" + month + "-0" + day;
            else
                return "-----0" + month + "-" + day;
        } else {
            if (day.length() == 1)
                return "-----" + month + "--0" + day;
            else
                return "-----" + month + "--" + day;
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
                return year + "-" + month + "--0" + day;
            else
                return year + "-" + month + "--" + day;
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

    public static boolean isEmail(String str) {
        return str.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }

    public static boolean isName(String str) {
        return str.length() >= 4;
    }

    public static boolean isDBName(String str) {

        return str.length() != 0 && !str.equals("dbname") && str.matches("[أؤءةابتثجحخدذرزسشصضطظعغفقكلمنهوىيa-z0-9A-Z\\s]+");
    }

    public static boolean isSiteName(String str) {
        return str.matches("[أؤءةابتثجحخدذرزسشصضطظعغفقكلمنهوىيa-z0-9A-Z\\s]+");
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
                BitmapFactory.decodeFile(path), 250, 250);
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

}
