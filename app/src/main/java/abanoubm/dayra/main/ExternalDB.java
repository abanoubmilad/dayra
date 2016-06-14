package abanoubm.dayra.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExternalDB extends SQLiteOpenHelper {
    private static String DB_NAME = "";
    private static String DB_PATH = "";

    public static final int DB_VERSION = 3;

    public static final String TB_CONNECTION = "conn_tb",
            CONN_A = "conn_a",
            CONN_B = "conn_b";
    public static final String TB_ATTEND = "attend_tb",
            ATTEND_ID = "attend_id",
            ATTEND_DAY = "attend_day",
            ATTEND_TYPE = "attend_type";
    public static final String TB_PHOTO = "photo_tb",
            PHOTO_ID = "photo_id",
            PHOTO_BLOB = "photo_blob";
    public static final String TB_CONTACT = "dayra_tb",
            CONTACT_ID = "_id", CONTACT_MAPLAT = "lat",
            CONTACT_MAPLNG = "lng", CONTACT_CLASS_YEAR = "cyear",
            CONTACT_MAPZOM = "zom", CONTACT_NAME = "name",
            CONTACT_SUPERVISOR = "pri", CONTACT_NOTES = "comm",
            CONTACT_BDAY = "bday", CONTACT_EMAIL = "email",
            CONTACT_MOB1 = "mob1", CONTACT_MOB2 = "mob2",
            CONTACT_MOB3 = "mob3", CONTACT_LPHONE = "lphone",
            CONTACT_ADDR = "addr", CONTACT_ST = "st",
            CONTACT_SITE = "site", CONTACT_STUDY_WORK = "swork";

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
        super(context, dbName, null, DB_VERSION);
        DB_NAME = dbName;
        DB_PATH = path;
    }

    public boolean checkDB() {
        try {
            SQLiteDatabase sdb = SQLiteDatabase.openDatabase(DB_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            sdb.query(TB_ATTEND, new String[]{ATTEND_ID, ATTEND_DAY, ATTEND_TYPE}, null, null, null, null, null);
            sdb.query(TB_CONNECTION, new String[]{CONN_A, CONN_B}, null, null, null, null, null);
            sdb.query(TB_CONTACT, new String[]{
                    CONTACT_ADDR,
                    CONTACT_BDAY,
                    CONTACT_CLASS_YEAR,
                    CONTACT_EMAIL,
                    CONTACT_ID,
                    CONTACT_LPHONE,
                    CONTACT_MAPLAT,
                    CONTACT_MAPLNG,
                    CONTACT_MAPZOM,
                    CONTACT_MOB1,
                    CONTACT_MOB2,
                    CONTACT_MOB3,
                    CONTACT_SUPERVISOR,
                    CONTACT_STUDY_WORK,
                    CONTACT_SITE,
                    CONTACT_NAME,
                    CONTACT_NOTES,
                    CONTACT_ST}, null, null, null, null, null);
            sdb.query(TB_PHOTO, new String[]{PHOTO_ID, PHOTO_BLOB}, null, null, null, null, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        String sql;
        if (arg1 < 2) {
            sql = "create table " + TB_CONNECTION + " ( " + CONN_A + " integer, "
                    + CONN_B + " integer)";
            db.execSQL(sql);
        }
        if (arg1 < 3) {
            sql = "create table " + TB_ATTEND + " ( " + ATTEND_ID + " integer, "
                    + ATTEND_TYPE + " integer, "
                    + ATTEND_DAY + " integer);"

                    + "create table " + TB_PHOTO + " ( " + PHOTO_ID + " integer primary key, "
                    + PHOTO_BLOB + " blob);";
            db.execSQL(sql);


            // modifications over data
            final String CONTACT_PHOTO = "pdir",
                    CONTACT_ATTEND_DATES = "dates",
                    CONTACT_LAST_VISIT = "lvisit",
                    CONTACT_LAST_ATTEND = "lattend";

            Cursor c = db.query(TB_CONTACT,
                    new String[]{
                            CONTACT_ID,
                            CONTACT_PHOTO,
                            CONTACT_ATTEND_DATES,
                            CONTACT_LAST_VISIT,
                    }, null, null, null, null, null);
            if (c.moveToFirst()) {
                ContentValues values;
                db.beginTransaction();
                do {

                    values = new ContentValues();
                    values.put(PHOTO_ID, c.getString(0));
                    values.put(PHOTO_BLOB, Utility.getBytes(Utility.getBitmap(c.getString(1))));
                    db.insert(TB_PHOTO, null, values);
                    if (c.getString(3).length() != 0) {
                        values = new ContentValues();
                        values.put(ATTEND_ID, c.getString(0));
                        values.put(ATTEND_TYPE, "0");
                        values.put(ATTEND_DAY, Utility.migirateDate(c.getString(3)));
                        db.insert(TB_ATTEND, null, values);
                    }
                    String[] arr = c.getString(2).split(",");
                    values = new ContentValues();
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i].length() != 0) {
                            values.put(ATTEND_ID, c.getString(0));
                            values.put(ATTEND_TYPE, "1");
                            values.put(ATTEND_DAY, Utility.migirateDate(arr[i]));
                        }
                    }
                } while (c.moveToFirst());
                c.close();
                db.endTransaction();

            }


        }
    }
}
