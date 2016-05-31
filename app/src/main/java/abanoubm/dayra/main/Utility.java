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
