package abanoubm.dayra.main;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import abanoubm.dayra.model.ContactData;

public class UpdaterDB extends SQLiteOpenHelper {
    private static String DB_PATH = "";
    private static final String Tb_NAME = "dayra_tb";
    public static final String ID = "_id", MAP_LAT = "lat", MAP_LNG = "lng",
            MAP_ZOOM = "zom", NAME = "name", ATTEND_DATES = "dates",
            LAST_VISIT = "lvisit", LAST_ATTEND = "lattend", PIC_DIR = "pdir",
            PRIEST = "pri", COMM = "comm", BDAY = "bday", EMAIL = "email",
            MOBILE1 = "mob1", MOBILE2 = "mob2", MOBILE3 = "mob3",
            LAND_PHONE = "lphone", ADDRESS = "addr", STREET = "st",
            SITE = "site", STUDY_WORK = "swork", CLASS_YEAR = "cyear";

    private SQLiteDatabase sdb;

    public UpdaterDB(Context context, String dbName, String path) {
        super(context, dbName, null, 1);
        DB_PATH = path;
    }

    public boolean checkDB() {
        try {
            sdb = SQLiteDatabase.openDatabase(DB_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            String sql = "SELECT " + ID + "," + MAP_LAT + "," + MAP_LNG + ","
                    + MAP_ZOOM + "," + NAME + "," + ATTEND_DATES + ","
                    + LAST_VISIT + "," + LAST_ATTEND + "," + PIC_DIR + ","
                    + PRIEST + "," + COMM + "," + BDAY + "," + EMAIL + ","
                    + MOBILE1 + "," + MOBILE2 + "," + MOBILE3 + ","
                    + LAND_PHONE + "," + ADDRESS + "," + STREET + "," + SITE
                    + "," + STUDY_WORK + "," + CLASS_YEAR + " FROM " + Tb_NAME
                    + " LIMIT 1";
            if (sdb.rawQuery(sql, null) == null)
                return false;
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

    public ArrayList<ContactData> getAttendantsData() {
        String selectQuery = "SELECT * FROM " + Tb_NAME;
        Cursor c = sdb.rawQuery(selectQuery, null);
        ArrayList<ContactData> result = new ArrayList<>(
                c.getCount());

        if (c.moveToFirst()) {
            int COL_ID = c.getColumnIndex(ID);
            int COL_MAP_LAT = c.getColumnIndex(MAP_LAT);
            int COL_MAP_LNG = c.getColumnIndex(MAP_LNG);
            int COL_MAP_ZOOM = c.getColumnIndex(MAP_ZOOM);
            int COL_NAME = c.getColumnIndex(NAME);
            int COL_ATTEND_DATES = c.getColumnIndex(ATTEND_DATES);
            int COL_LAST_ATTEND = c.getColumnIndex(LAST_ATTEND);
            int COL_PIC_DIR = c.getColumnIndex(PIC_DIR);
            int COL_PRIEST = c.getColumnIndex(PRIEST);
            int COL_COMM = c.getColumnIndex(COMM);
            int COL_BDAY = c.getColumnIndex(BDAY);
            int COL_EMAIL = c.getColumnIndex(EMAIL);
            int COL_MOBILE1 = c.getColumnIndex(MOBILE1);
            int COL_MOBILE2 = c.getColumnIndex(MOBILE2);
            int COL_MOBILE3 = c.getColumnIndex(MOBILE3);
            int COL_LAND_PHONE = c.getColumnIndex(LAND_PHONE);
            int COL_ADDRESS = c.getColumnIndex(ADDRESS);
            int COL_LAST_VISIT = c.getColumnIndex(LAST_VISIT);
            int COL_CLASS_YEAR = c.getColumnIndex(CLASS_YEAR);
            int COL_STUDY_WORK = c.getColumnIndex(STUDY_WORK);
            int COL_STREET = c.getColumnIndex(STREET);
            int COL_SITE = c.getColumnIndex(SITE);
            do {

                result.add(new ContactData(c.getInt(COL_ID), c
                        .getString(COL_NAME), c.getString(COL_PIC_DIR), c
                        .getDouble(COL_MAP_LAT), c.getDouble(COL_MAP_LNG), c
                        .getFloat(COL_MAP_ZOOM), c.getString(COL_ATTEND_DATES),
                        c.getString(COL_LAST_ATTEND), c.getString(COL_PRIEST),
                        c.getString(COL_COMM), c.getString(COL_BDAY), c
                        .getString(COL_EMAIL),
                        c.getString(COL_MOBILE1), c.getString(COL_MOBILE2), c
                        .getString(COL_MOBILE3), c
                        .getString(COL_LAND_PHONE), c
                        .getString(COL_ADDRESS), c
                        .getString(COL_LAST_VISIT), c
                        .getString(COL_CLASS_YEAR), c
                        .getString(COL_STUDY_WORK), c
                        .getString(COL_STREET), c.getString(COL_SITE)));
            } while (c.moveToNext());
        }
        c.close();
        sdb.close();

        return result;

    }
}
