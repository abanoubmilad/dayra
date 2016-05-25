package abanoubm.dayra.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExternalDB extends SQLiteOpenHelper {
    private static String DB_NAME = "";
    private static String DB_PATH = "";
    private static final String Tb_NAME = "dayra_tb";
    public static final String ID = "_id", MAP_LAT = "lat", MAP_LNG = "lng",
            MAP_ZOOM = "zom", NAME = "name", ATTEND_DATES = "dates",
            LAST_VISIT = "lvisit", LAST_ATTEND = "lattend", PIC_DIR = "pdir",
            PRIEST = "pri", COMM = "comm", BDAY = "bday", EMAIL = "email",
            MOBILE1 = "mob1", MOBILE2 = "mob2", MOBILE3 = "mob3",
            LAND_PHONE = "lphone", ADDRESS = "addr", STREET = "st",
            SITE = "site", STUDY_WORK = "swork", CLASS_YEAR = "cyear";

    private static ExternalDB dbm;

    public static ExternalDB getInstance(Context context, String dbName,
                                         String path) {
        if (dbm != null && DB_NAME.equals(dbName) && DB_PATH.equals(path))
            return dbm;
        else {
            dbm = new ExternalDB(context, dbName, path);
            return dbm;
        }
    }

    private ExternalDB(Context context, String dbName, String path) {
        super(context, dbName, null, 1);
        DB_NAME = dbName;
        DB_PATH = path;
    }

    public boolean checkDB() {
        try {
            SQLiteDatabase sdb = SQLiteDatabase.openDatabase(DB_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            String sql = "SELECT " + ID + "," + MAP_LAT + "," + MAP_LNG + ","
                    + MAP_ZOOM + "," + NAME + "," + ATTEND_DATES + ","
                    + LAST_VISIT + "," + LAST_ATTEND + "," + PIC_DIR + ","
                    + PRIEST + "," + COMM + "," + BDAY + "," + EMAIL + ","
                    + MOBILE1 + "," + MOBILE2 + "," + MOBILE3 + ","
                    + LAND_PHONE + "," + ADDRESS + "," + STREET + "," + SITE
                    + "," + STUDY_WORK + "," + CLASS_YEAR + " FROM " + Tb_NAME
                    + " LIMIT 1";
            sdb.rawQuery(sql, null).close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}
