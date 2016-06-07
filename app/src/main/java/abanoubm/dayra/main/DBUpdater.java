package abanoubm.dayra.main;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import abanoubm.dayra.model.ContactData;

public class DBUpdater extends SQLiteOpenHelper {
    private static String DB_PATH = "";

    private static final int DB_VERSION = 3;

    private static final String TB_PHOTO = "photo_tb";
    private static final String PHOTO_ID = "photo_id";
    private static final String PHOTO_BLOB = "photo_blob";

    private static final String TB_CONTACT = "dayra_tb";
    public static final String CONTACT_ID = "_id", CONTACT_MAPLAT = "lat", CONTACT_MAPLNG = "lng",
            CONTACT_MAPZOM = "zom", CONTACT_NAME = "name",
            CONTACT_PRIEST = "pri", CONTACT_NOTES = "comm",
            CONTACT_BDAY = "bday", CONTACT_EMAIL = "email",
            CONTACT_MOB1 = "mob1", CONTACT_MOB2 = "mob2",
            CONTACT_MOB3 = "mob3", CONTACT_LPHONE = "lphone",
            CONTACT_ADDR = "addr", CONTACT_ST = "st",
            CONTACT_SITE = "site", CONTACT_STUDY_WORK = "swork",
            CONTACT_CLASS_YEAR = "cyear";

    private SQLiteDatabase sdb;

    public DBUpdater(Context context, String dbName, String path) {
        super(context, dbName, null, DB_VERSION);
        DB_PATH = path;
    }

    public boolean checkDB() {
        try {
            sdb = SQLiteDatabase.openDatabase(DB_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);

            Cursor c = sdb.query(TB_CONTACT,
                    new String[]{
                            CONTACT_ID
                            , CONTACT_NAME
                            , CONTACT_MAPLAT
                            , CONTACT_MAPLNG
                            , CONTACT_MAPZOM

                            , CONTACT_PRIEST
                            , CONTACT_NOTES
                            , CONTACT_BDAY

                            , CONTACT_EMAIL
                            , CONTACT_MOB1
                            , CONTACT_MOB2
                            , CONTACT_MOB3
                            , CONTACT_LPHONE

                            , CONTACT_ADDR
                            , CONTACT_CLASS_YEAR
                            , CONTACT_STUDY_WORK
                            , CONTACT_ST
                            , CONTACT_SITE
                    }, CONTACT_ID, null, null, null, null);
            c.close();
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
        final String CONTACT_PHOTO = "pdir",
                CONTACT_ATTEND_DATES = "dates",
                CONTACT_LAST_VISIT = "lvisit",
                CONTACT_LAST_ATTEND = "lattend";

        String sql;
        if (arg1 < 3) {
            sql = "create table " + TB_PHOTO + " ( " + PHOTO_ID + " integer, "
                    + PHOTO_BLOB + " blob)";
            db.execSQL(sql);
        }

    }


    public ArrayList<ContactData> getAttendantsData() {

        Cursor c = sdb.query(TB_CONTACT,
                new String[]{
                        CONTACT_ID
                        , CONTACT_NAME
                        , CONTACT_MAPLAT
                        , CONTACT_MAPLNG
                        , CONTACT_MAPZOM

                        , CONTACT_PRIEST
                        , CONTACT_NOTES
                        , CONTACT_BDAY

                        , CONTACT_EMAIL
                        , CONTACT_MOB1
                        , CONTACT_MOB2
                        , CONTACT_MOB3
                        , CONTACT_LPHONE

                        , CONTACT_ADDR
                        , CONTACT_CLASS_YEAR
                        , CONTACT_STUDY_WORK
                        , CONTACT_ST
                        , CONTACT_SITE
                }, CONTACT_ID, null, null, null, null);

        ArrayList<ContactData> result = new ArrayList<>(
                c.getCount());

        if (c.moveToFirst()) {

            do {
                result.add(new ContactData(c.getString(0),
                        c.getString(1), null, c.getDouble(2), c.getDouble(3),
                        c.getFloat(4), c.getString(5), c.getString(6),
                        c.getString(7), c.getString(8), c.getString(9),
                        c.getString(10), c.getString(11), c.getString(12),
                        c.getString(13), c.getString(14), c.getString(15),
                        c.getString(16), c.getString(17)));
            } while (c.moveToNext());
        }
        c.close();
        sdb.close();

        return result;

    }
}
