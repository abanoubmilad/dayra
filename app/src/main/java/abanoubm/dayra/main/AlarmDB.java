package abanoubm.dayra.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmDB extends SQLiteOpenHelper {
    private static String DB_NAME = "alarm_db";
    private static String targetDBName = "";


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
        targetDBName = context.getSharedPreferences("login",
                Context.MODE_PRIVATE).getString("dbname", "");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
    }




}