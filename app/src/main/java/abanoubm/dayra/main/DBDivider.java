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
                + DB.CONTACT_NAME + " text, " + DB.CONTACT_SUPERVISOR
                + " text, " + DB.CONTACT_NOTES + " text, " + DB.CONTACT_BDAY + " text, "
                + DB.CONTACT_EMAIL + " text, " + DB.CONTACT_MOB1 + " text, " + DB.CONTACT_MOB2 + " text, "
                + DB.CONTACT_MOB3 + " text, " + DB.CONTACT_LPHONE + " text, " + DB.CONTACT_ST
                + " text, " + DB.CONTACT_SITE + " text, " + DB.CONTACT_CLASS_YEAR + " integer, "
                + DB.CONTACT_STUDY_WORK + " text, " + DB.CONTACT_ADDR + " text)";
        db.execSQL(sql);

        sql = "create table " + DB.TB_CONNECTION + " ( " + DB.CONN_A + " integer, "
                + DB.CONN_B + " integer, " +
                "primary key (" + DB.CONN_A + "," + DB.CONN_B + "))";
        db.execSQL(sql);

        sql = "create table " + DB.TB_ATTEND + " ( " + DB.ATTEND_ID + " integer, "
                + DB.ATTEND_TYPE + " integer, "
                + DB.ATTEND_DAY + " integer, " +
                "primary key (" + DB.ATTEND_ID + "," + DB.ATTEND_TYPE + "," + DB.ATTEND_DAY + "))";
        db.execSQL(sql);

        sql = "create table " + DB.TB_PHOTO + " ( " + DB.PHOTO_ID + " integer primary key, "
                + DB.PHOTO_BLOB + " blob)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        String sql;
        if (arg1 < 2) {
            sql = "create table " + DB.TB_CONNECTION + " ( " + DB.CONN_A + " integer, "
                    + DB.CONN_B + " integer, " +
                    "primary key (" + DB.CONN_A + "," + DB.CONN_B + "))";
            db.execSQL(sql);
        }
        if (arg1 < 3) {
            sql = "create table " + DB.TB_ATTEND + " ( " + DB.ATTEND_ID + " integer, "
                    + DB.ATTEND_TYPE + " integer, "
                    + DB.ATTEND_DAY + " integer, " +
                    "primary key (" + DB.ATTEND_ID + "," + DB.ATTEND_TYPE + "," + DB.ATTEND_DAY + "))";
            db.execSQL(sql);

            sql = "create table " + DB.TB_PHOTO + " ( " + DB.PHOTO_ID + " integer primary key, "
                    + DB.PHOTO_BLOB + " blob)";
            db.execSQL(sql);
        }
    }


    public String addContact(ContentValues values, byte[] photo) {
        String id = String.valueOf(writableDB.insert(DB.TB_CONTACT, null, values));

        values = new ContentValues();
        values.put(DB.PHOTO_ID, id);
        values.put(DB.PHOTO_BLOB, photo);

        writableDB.insert(DB.TB_PHOTO, null, values);
        return id;

    }

    public void close() {
        writableDB.close();
    }

}