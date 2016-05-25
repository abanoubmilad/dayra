package abanoubm.dayra.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TinyDB extends SQLiteOpenHelper {
    private static final String Tb_NAME = "dayra_tb";
    public static final String ID = "_id", MAP_LAT = "lat", MAP_LNG = "lng",
            MAP_ZOOM = "zom", NAME = "name", ATTEND_DATES = "dates",
            LAST_VISIT = "lvisit", LAST_ATTEND = "lattend", PIC_DIR = "pdir",
            PRIEST = "pri", COMM = "comm", BDAY = "bday", EMAIL = "email",
            MOBILE1 = "mob1", MOBILE2 = "mob2", MOBILE3 = "mob3",
            LAND_PHONE = "lphone", ADDRESS = "addr", STREET = "st",
            SITE = "site", STUDY_WORK = "swork", CLASS_YEAR = "cyear";

    private SQLiteDatabase writableDB;

    public TinyDB(Context context, String dbName) {
        super(context, dbName, null, 1);
        writableDB = getWritableDatabase();
        writableDB.delete(Tb_NAME, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + Tb_NAME + " ( " + ID
                + " integer primary key autoincrement, " + MAP_LAT
                + " double, " + MAP_LNG + " double, " + MAP_ZOOM + " float, "
                + NAME + " text, " + ATTEND_DATES + " text, " + LAST_ATTEND
                + " text, " + LAST_VISIT + " text, " + PIC_DIR + " text, "
                + PRIEST + " text, " + COMM + " text, " + BDAY + " text, "
                + EMAIL + " text, " + MOBILE1 + " text, " + MOBILE2 + " text, "
                + MOBILE3 + " text, " + LAND_PHONE + " text, " + STREET
                + " text, " + SITE + " text, " + CLASS_YEAR + " integer, "
                + STUDY_WORK + " text, " + ADDRESS + " text) ";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    public void close() {
        writableDB.close();
    }

    public void addAttendant(ContentValues values) {
        writableDB.insert(Tb_NAME, null, values);
    }
}