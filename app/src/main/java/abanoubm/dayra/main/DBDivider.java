package abanoubm.dayra.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBDivider extends SQLiteOpenHelper {
    private SQLiteDatabase writableDB;

    public DBDivider(Context context, String dbName) {
        super(context, dbName, null, DB.DB_VERSION);
        writableDB = getWritableDatabase();
        writableDB.delete(DB.TB_PHOTO, null, null);
        writableDB.delete(DB.TB_CONTACT, null, null);
        writableDB.delete(DB.TB_CONNECTION, null, null);
        writableDB.delete(DB.TB_ATTEND, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + DB.TB_CONTACT + " ( " + DB.CONTACT_ID
                + " integer primary key autoincrement, " + DB.CONTACT_MAPLAT
                + " double, " + DB.CONTACT_MAPLNG + " double, " + DB.CONTACT_MAPZOM + " float, "
                + DB.CONTACT_NAME + DB.CONTACT_SUPERVISOR + " text, " + DB.CONTACT_NOTES + " text, "
                + DB.CONTACT_BDAY + " text, "
                + DB.CONTACT_EMAIL + " text, " + DB.CONTACT_MOB1 + " text, " + DB.CONTACT_MOB2 + " text, "
                + DB.CONTACT_MOB3 + " text, " + DB.CONTACT_LPHONE + " text, " + DB.CONTACT_ST
                + " text, " + DB.CONTACT_SITE + " text, " + DB.CONTACT_CLASS_YEAR + " integer, "
                + DB.CONTACT_STUDY_WORK + " text, " + DB.CONTACT_ADDR + " text)";
        db.execSQL(sql);

        sql = "create table " + DB.TB_CONNECTION + " ( " + DB.CONN_A + " integer, "
                + DB.CONN_B + " integer)";
        db.execSQL(sql);

        sql = "create table " + DB.TB_ATTEND + " ( " + DB.ATTEND_ID + " integer, "
                + DB.ATTEND_TYPE + " integer, "
                + DB.ATTEND_DAY + " integer)";

        db.execSQL(sql);

        sql = "create table " + DB.TB_PHOTO + " ( " + DB.PHOTO_ID + " integer primary key, "
                + DB.PHOTO_BLOB + " blob)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    public void close() {
        writableDB.close();
    }

    public void addAttendant(ContentValues values) {
        writableDB.insert(DB.TB_CONTACT, null, values);
    }
}