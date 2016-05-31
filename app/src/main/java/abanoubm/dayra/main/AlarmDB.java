package abanoubm.dayra.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmDB extends SQLiteOpenHelper {
    private static String DB_NAME = "alarm_db";

    private static final String TB_ALARM = "alarm_tb";
    private static final String ALARM_DB_NAME = "alarm_db_name";
    private static final String ALARM_TYPE = "alarm_type";

    private static AlarmDB dbm;
    private SQLiteDatabase readableDB, writableDB;

    public static AlarmDB getInstance(Context context) {
        if (dbm != null)
            return dbm;
        else {
            dbm = new AlarmDB(context);
            return dbm;
        }
    }

    private AlarmDB(Context context) {
        super(context, DB_NAME, null, 1);
        readableDB = getReadableDatabase();
        writableDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TB_ALARM + " ( " + ALARM_DB_NAME
                + " text, " + ALARM_TYPE
                + " text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
    }

    public void removeAlarm(String dbname, String type) {
        writableDB.delete(TB_ALARM, ALARM_DB_NAME + " = ? AND " + ALARM_TYPE + " = ?",
                new String[]{dbname, type});
    }

    public void closeDB() {
        readableDB.close();
        writableDB.close();
    }

    public void addAlarm(String dbname, String type) {
        ContentValues values = new ContentValues();
        values.put(ALARM_DB_NAME, dbname);
        values.put(ALARM_TYPE, dbname);
        writableDB.insert(TB_ALARM, null, values);
    }




}