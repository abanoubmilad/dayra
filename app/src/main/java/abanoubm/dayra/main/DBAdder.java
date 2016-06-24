package abanoubm.dayra.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBAdder extends SQLiteOpenHelper {
    private static String DB_PATH = "";
    private SQLiteDatabase sdb;

    public DBAdder(Context context, String dbName, String path) {
        super(context, dbName, null, DB.DB_VERSION);
        DB_PATH = path;
    }

    public boolean checkDB() {
        try {
            sdb = SQLiteDatabase.openDatabase(DB_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);

//            sdb.query(TB_ATTEND,
//                    new String[]{ATTEND_ID, ATTEND_DAY, ATTEND_TYPE}, null, null, null, null, null, " LIMIT 1").close();
//            sdb.query(TB_CONNECTION,
//                    new String[]{CONN_A, CONN_B}, null, null, null, null, null, " LIMIT 1").close();

            sdb.query(DB.TB_CONTACT, new String[]{
                    DB.CONTACT_ADDR,
                    DB.CONTACT_BDAY,
                    DB.CONTACT_CLASS_YEAR,
                    DB.CONTACT_EMAIL,
                    DB.CONTACT_ID,
                    DB.CONTACT_LPHONE,
                    DB.CONTACT_MAPLAT,
                    DB.CONTACT_MAPLNG,
                    DB.CONTACT_MAPZOM,
                    DB.CONTACT_MOB1,
                    DB.CONTACT_MOB2,
                    DB.CONTACT_MOB3,
                    DB.CONTACT_SUPERVISOR,
                    DB.CONTACT_STUDY_WORK,
                    DB.CONTACT_SITE,
                    DB.CONTACT_NAME,
                    DB.CONTACT_NOTES,
                    DB.CONTACT_ST}, null, null, null, null, null, " LIMIT 1").close();
//            sdb.query(TB_PHOTO,
//                    new String[]{PHOTO_ID, PHOTO_BLOB}, null, null, null, null, null, " LIMIT 1").close();

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


            // modifications over data
            final String CONTACT_PHOTO = "pdir",
                    CONTACT_ATTEND_DATES = "dates",
                    CONTACT_LAST_VISIT = "lvisit",
                    CONTACT_LAST_ATTEND = "lattend";

            Cursor c = db.query(DB.TB_CONTACT,
                    new String[]{
                            DB.CONTACT_ID,
                            CONTACT_PHOTO,
                            CONTACT_ATTEND_DATES,
                            CONTACT_LAST_VISIT,
                    }, null, null, null, null, null);
            if (c.moveToFirst()) {
                ContentValues values;
                db.beginTransaction();
                do {

                    values = new ContentValues();
                    values.put(DB.PHOTO_ID, c.getString(0));
                    values.put(DB.PHOTO_BLOB, Utility.getBytes(Utility.getBitmap(c.getString(1))));
                    db.insert(DB.TB_PHOTO, null, values);
                    if (c.getString(3).length() != 0) {
                        values = new ContentValues();
                        values.put(DB.ATTEND_ID, c.getString(0));
                        values.put(DB.ATTEND_TYPE, "0");
                        values.put(DB.ATTEND_DAY, Utility.migirateDate(c.getString(3)));
                        db.insert(DB.TB_ATTEND, null, values);
                    }
                    String[] arr = c.getString(2).split(",");
                    values = new ContentValues();
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i].length() != 0) {
                            values.put(DB.ATTEND_ID, c.getString(0));
                            values.put(DB.ATTEND_TYPE, "1");
                            values.put(DB.ATTEND_DAY, Utility.migirateDate(arr[i]));
                        }
                    }
                } while (c.moveToFirst());
                c.close();
                db.endTransaction();

            }


        }
    }


    public ArrayList<String[]> getContactsData() {
        String sel =
                DB.CONTACT_NAME + "," +
                        DB.CONTACT_CLASS_YEAR + "," +
                        DB.CONTACT_STUDY_WORK + "," +

                        DB.CONTACT_MOB1 + "," +
                        DB.CONTACT_MOB2 + "," +
                        DB.CONTACT_MOB3 + "," +
                        DB.CONTACT_LPHONE + "," +
                        DB.CONTACT_EMAIL + "," +

                        DB.CONTACT_SITE + "," +
                        DB.CONTACT_ST + "," +
                        DB.CONTACT_ADDR + "," +

                        DB.CONTACT_NOTES + "," +
                        DB.CONTACT_SUPERVISOR + "," +

                        DB.CONTACT_BDAY + "," +
                        DB.CONTACT_MAPLAT + "," +
                        DB.CONTACT_MAPLNG + "," +
                        DB.CONTACT_MAPZOM;
        String selectQuery = "SELECT " + sel +
                " FROM " + DB.TB_CONTACT;

        Cursor c = sdb.rawQuery(selectQuery, null);

        ArrayList<String[]> result = new ArrayList<>(
                c.getCount());

        if (c.moveToFirst()) {

            do {
                result.add(new String[]{
                        c.getString(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getString(6),
                        c.getString(7),
                        c.getString(8),
                        c.getString(9),
                        c.getString(10),
                        c.getString(11),
                        c.getString(12),
                        c.getString(13),
                        c.getString(14),
                        c.getString(15),
                        c.getString(16)
                });

            } while (c.moveToNext());
        }
        c.close();
        return result;

    }

    public void close() {
        sdb.close();
    }

}
