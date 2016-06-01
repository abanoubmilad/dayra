package abanoubm.dayra.main;

import android.content.Context;
import android.graphics.Color;

public class Utility {

    public static String getDayraName(Context context) {
        return (context.getSharedPreferences("login",
                Context.MODE_PRIVATE)).getString("dbname", "");
    }

    public static final int update = Color
            .rgb(130, 199, 132);

    public static boolean isDay(String str) {
        try {
            if (str.length() > 0) {
                int n = Integer.parseInt(str);
                return n < 32 && n > 0;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isMonth(String str) {
        try {
            if (str.length() > 0) {
                int n = Integer.parseInt(str);
                return n < 13 && n > 0;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

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

}
