package abanoubm.dayra.alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBAlarm extends SQLiteOpenHelper {
    private static String DB_NAME = "alarm_db_journal";
    private static final String TB_ALARM = "alarm_tb";
    private static final String ALARM_DB_NAME = "alarm_db_name";
    private static final String ALARM_TYPE = "alarm_type";
    private static final int DB_VERSION = 1;

    private static DBAlarm dbm;
    private SQLiteDatabase readableDB, writableDB;

    public static DBAlarm getInstant(Context context) {
        if (dbm != null)
            return dbm;
        else {
            dbm = new DBAlarm(context);
            return dbm;
        }
    }

    private DBAlarm(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        readableDB = getReadableDatabase();
        writableDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TB_ALARM + " ( " + ALARM_DB_NAME
                + " text, " + ALARM_TYPE
                + " text, " +
                "primary key (" + ALARM_DB_NAME + "," + ALARM_TYPE + "))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
    }


    // delete alarm and return true if there is still
    // an alarm enabled for the same type of any other dayra
    public boolean removeAlarm(String type, String dbname) {
        writableDB.delete(TB_ALARM, ALARM_DB_NAME + " = ? AND " + ALARM_TYPE + " = ?",
                new String[]{dbname, type});

        Cursor c = readableDB.query(TB_ALARM, new String[]{ALARM_DB_NAME
                }, ALARM_TYPE + " = ?",
                new String[]{type}, null, null, null);
        boolean check = c.getCount() != 0;
        c.close();
        return check;

    }

    public boolean doesAlarmExist(String type, String dbname) {
        Cursor c = readableDB.query(TB_ALARM, new String[]{ALARM_DB_NAME
                }, ALARM_DB_NAME + " = ? AND " + ALARM_TYPE + " = ?",
                new String[]{dbname, type}, null, null, null);
        boolean check = c.getCount() != 0;
        c.close();
        return check;

    }

    // add alarm and return true if it's the first
    // alarm enabled for this type
    public boolean addAlarm(String type, String dbname) {
        ContentValues values = new ContentValues();
        values.put(ALARM_DB_NAME, dbname);
        values.put(ALARM_TYPE, type);
        writableDB.insert(TB_ALARM, null, values);

        Cursor c = readableDB.query(TB_ALARM, new String[]{ALARM_DB_NAME
                }, ALARM_TYPE + " = ?",
                new String[]{type}, null, null, null);
        boolean check = c.getCount() == 1;
        c.close();
        return check;
    }

    public ArrayList<String> getAlarmDayras(String type) {
        Cursor c = readableDB.query(TB_ALARM, new String[]{ALARM_DB_NAME
                }, ALARM_TYPE + " = ?",
                new String[]{type}, null, null, null);
        ArrayList<String> result = new ArrayList<>(c.getCount());
        if (c.moveToFirst()) {
            do {
                result.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        return result;
    }


}