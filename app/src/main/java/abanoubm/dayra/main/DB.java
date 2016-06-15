package abanoubm.dayra.main;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import abanoubm.dayra.R;
import abanoubm.dayra.model.ContactCheck;
import abanoubm.dayra.model.ContactData;
import abanoubm.dayra.model.ContactDay;
import abanoubm.dayra.model.ContactID;
import abanoubm.dayra.model.ContactLoc;
import abanoubm.dayra.model.ContactLocation;
import abanoubm.dayra.model.ContactMobile;
import abanoubm.dayra.model.ContactSort;
import abanoubm.dayra.model.ContactStatistics;
import abanoubm.dayra.model.IntWrapper;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class DB extends SQLiteOpenHelper {
    private static String DB_NAME = "";
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

    private static DB dbm;
    private SQLiteDatabase readableDB, writableDB;

    public static DB getInstant(Context context) {
        String dbName = context.getSharedPreferences("login",
                Context.MODE_PRIVATE).getString("dbname", "");
        if (dbm != null && DB_NAME.equals(dbName))
            return dbm;
        else {
            dbm = new DB(context, dbName);
            return dbm;
        }
    }

    public static DB getInstant(Context context, String dbName) {
        if (dbm != null && DB_NAME.equals(dbName))
            return dbm;
        else {
            dbm = new DB(context, dbName);
            return dbm;
        }
    }

    private DB(Context context, String dbName) {
        super(context, dbName, null, DB_VERSION);
        DB_NAME = dbName;
        readableDB = getReadableDatabase();
        writableDB = getWritableDatabase();
    }

    public static boolean isDBExists(Context context, String name) {
        try {
            File dbFile = context.getDatabasePath(name);
            if (dbFile != null && dbFile.exists())
                SQLiteDatabase.openDatabase(dbFile.getPath(), null,
                        SQLiteDatabase.OPEN_READONLY).close();
            else
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public File getDBFile(Context context) {
        return context.getDatabasePath(DB_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table " + TB_CONTACT + " ( " + CONTACT_ID
                + " integer primary key autoincrement, " + CONTACT_MAPLAT
                + " double, " + CONTACT_MAPLNG + " double, " + CONTACT_MAPZOM + " float, "
                + CONTACT_NAME + " text, " + CONTACT_SUPERVISOR
                + " text, " + CONTACT_NOTES + " text, " + CONTACT_BDAY + " text, "
                + CONTACT_EMAIL + " text, " + CONTACT_MOB1 + " text, " + CONTACT_MOB2 + " text, "
                + CONTACT_MOB3 + " text, " + CONTACT_LPHONE + " text, " + CONTACT_ST
                + " text, " + CONTACT_SITE + " text, " + CONTACT_CLASS_YEAR + " integer, "
                + CONTACT_STUDY_WORK + " text, " + CONTACT_ADDR + " text)";
        db.execSQL(sql);

        sql = "create table " + TB_CONNECTION + " ( " + CONN_A + " integer, "
                + CONN_B + " integer)";
        db.execSQL(sql);

        sql = "create table " + TB_ATTEND + " ( " + ATTEND_ID + " integer, "
                + ATTEND_TYPE + " integer, "
                + ATTEND_DAY + " integer)";
        db.execSQL(sql);

        sql = "create table " + TB_PHOTO + " ( " + PHOTO_ID + " integer primary key, "
                + PHOTO_BLOB + " blob)";

        db.execSQL(sql);
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
                    + ATTEND_DAY + " integer)";
            db.execSQL(sql);

            sql = "create table " + TB_PHOTO + " ( " + PHOTO_ID + " integer primary key, "
                    + PHOTO_BLOB + " blob)";
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

    public void deleteContact(String id) {
        removeAttendantConnections(id);
        writableDB.delete(TB_ATTEND, ATTEND_ID + " = ?",
                new String[]{id});
        writableDB.delete(TB_PHOTO, PHOTO_ID + " = ?",
                new String[]{id});
        writableDB.delete(TB_CONTACT, CONTACT_ID + " = ?",
                new String[]{id});
    }

    public boolean deleteDB(Context context) {
        dbm = null;
        readableDB.close();
        writableDB.close();
        return context.deleteDatabase(DB_NAME);
    }

    public boolean isValidDB(Context context) {
        try {
            readableDB.query(TB_ATTEND, new String[]{ATTEND_ID, ATTEND_DAY, ATTEND_TYPE}, null, null, null, null, null);
            readableDB.query(TB_CONNECTION, new String[]{CONN_A, CONN_B}, null, null, null, null, null);
            readableDB.query(TB_CONTACT, new String[]{
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
            readableDB.query(TB_PHOTO, new String[]{PHOTO_ID, PHOTO_BLOB}, null, null, null, null, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            dbm = null;
            readableDB.close();
            writableDB.close();
            context.deleteDatabase(DB_NAME);
            return false;
        }
    }

    public void closeDB() {
        readableDB.close();
        writableDB.close();
    }

    public void addConnection(String conA, String conB) {
        ContentValues values = new ContentValues();

        values.put(CONN_A, conA);
        values.put(CONN_B, conB);
        writableDB.insert(TB_CONNECTION, null, values);

        values.put(CONN_A, conB);
        values.put(CONN_B, conA);
        writableDB.insert(TB_CONNECTION, null, values);

    }

    public void removeConnection(String conA, String conB) {
        writableDB.delete(TB_CONNECTION, CONN_A + " = ? and " + CONN_B
                        + " = ? or " + CONN_A + " = ? and " + CONN_B + " = ? ",
                new String[]{conA, conB,
                        conB, conA});
    }

    public void removeAttendantConnections(String hostID) {
        writableDB
                .delete(TB_CONNECTION,
                        CONN_A + " = ? or " + CONN_B + " = ?",
                        new String[]{hostID,
                                hostID});
    }

    public ArrayList<ContactID> getAttendantConnections(String hostID) {

        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," +
                PHOTO_BLOB + " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_PHOTO +
                " ON " + CONTACT_ID + "=" + PHOTO_ID +
                " WHERE " + CONTACT_ID + " IN (SELECT "
                + CONN_B + " FROM " + TB_CONNECTION + " WHERE " + CONN_A + "="
                + hostID + ")" + " AND " + CONTACT_ID + "!=" + hostID + " ORDER BY "
                + CONTACT_NAME;
        Cursor c = readableDB.rawQuery(selectQuery, null);
        ArrayList<ContactID> result = new ArrayList<>(c.getCount());
        if (c.moveToFirst()) {
            do {
                result.add(new ContactID(c.getString(0), c.getString(1),
                        Utility.getBitmap(c.getBlob(2))));
            } while (c.moveToNext());
        }
        c.close();
        return result;
    }

    public ArrayList<ContactCheck> getAttendantConnections(String hostID,
                                                           String name) {

        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + CONN_B + "," +
                PHOTO_BLOB + " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_PHOTO +
                " ON " + CONTACT_ID + "=" + PHOTO_ID +
                " LEFT OUTER JOIN " + TB_CONNECTION + " ON " +
                CONTACT_ID + "=" + CONN_B + " AND " + CONN_A + " = ? " +
                " WHERE " + CONTACT_ID + " != ? AND " + CONTACT_NAME +
                " LIKE ? ORDER BY " + CONTACT_NAME;
        Cursor c = readableDB.rawQuery(selectQuery, new String[]{hostID, hostID, "%" + name + "%"});
        ArrayList<ContactCheck> result = new ArrayList<>(
                c.getCount());
        if (c.moveToFirst()) {
            do {
                result.add(new ContactCheck(c.getString(0), c
                        .getString(1), Utility.getBitmap(c.getBlob(3)), c.getString(2) != null));
            } while (c.moveToNext());
        }
        c.close();
        return result;
    }

    public void updateContact(ContentValues values, byte[] photo, String id) {
        writableDB.update(TB_CONTACT, values, CONTACT_ID + " = ?",
                new String[]{id});

        values = new ContentValues();
        values.put(PHOTO_BLOB, photo);
        writableDB.update(TB_PHOTO, values, PHOTO_ID + " = ?",
                new String[]{id});
    }

    public void updateContact(ContentValues values, String id) {
        writableDB.update(TB_CONTACT, values, CONTACT_ID + " = ?",
                new String[]{id});
    }

    public void externalUpdater(ArrayList<ContactData> arr,
                                ArrayList<String> dataTag) {
        for (ContactData att : arr) {
            String idCheck = getNameId(att.getName());
            if (idCheck.equals("-1")) {
                addAttendant(att);
            } else {
                ContentValues values = new ContentValues();
                for (String str : dataTag) {
                    switch (str) {
                        case CONTACT_MAPLAT:
                            values.put(CONTACT_MAPLAT, att.getMapLat());
                            break;
                        case CONTACT_MAPLNG:
                            values.put(CONTACT_MAPLNG, att.getMapLng());
                            break;
                        case CONTACT_MAPZOM:
                            values.put(CONTACT_MAPZOM, att.getMapZoom());
                            break;
                        case CONTACT_SUPERVISOR:
                            values.put(CONTACT_SUPERVISOR, att.getPriest());
                            break;
                        case CONTACT_NOTES:
                            values.put(CONTACT_NOTES, att.getComm());
                            break;
                        case CONTACT_BDAY:
                            values.put(CONTACT_BDAY, att.getBirthDay());
                            break;
                        case CONTACT_EMAIL:
                            values.put(CONTACT_EMAIL, att.getEmail());
                            break;
                        case CONTACT_MOB1:
                            values.put(CONTACT_MOB1, att.getMobile1());
                            break;
                        case CONTACT_MOB2:
                            values.put(CONTACT_MOB2, att.getMobile2());
                            break;
                        case CONTACT_MOB3:
                            values.put(CONTACT_MOB3, att.getMobile3());
                            break;
                        case CONTACT_LPHONE:
                            values.put(CONTACT_LPHONE, att.getLandPhone());
                            break;
                        case CONTACT_ADDR:
                            values.put(CONTACT_ADDR, att.getAddress());
                            break;
                        case CONTACT_ST:
                            values.put(CONTACT_ST, att.getStreet());
                            break;
                        case CONTACT_SITE:
                            values.put(CONTACT_SITE, att.getSite());
                            break;
                        case CONTACT_STUDY_WORK:
                            values.put(CONTACT_STUDY_WORK, att.getStudyWork());
                            break;
                        case CONTACT_CLASS_YEAR:
                            values.put(CONTACT_CLASS_YEAR, att.getClassYear());
                            break;
                    }
                }

                // values.put(CONTACT_NAME, att.getName());
                // values.put(CONTACT_PHOTO, att.getPicDir());

                writableDB.update(TB_CONTACT, values, CONTACT_ID + " = ?",
                        new String[]{String.valueOf(idCheck)});

            }
        }

    }

    public String addContact(ContentValues values, byte[] photo) {
        String id = String.valueOf(writableDB.insert(TB_CONTACT, null, values));

        values = new ContentValues();
        values.put(PHOTO_ID, id);
        values.put(PHOTO_BLOB, photo);

        writableDB.insert(TB_PHOTO, null, values);
        return id;

    }

    public String addContact(String name, String mobile) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_MAPLAT, 0);
        values.put(CONTACT_MAPLNG, 0);
        values.put(CONTACT_MAPZOM, 0);
        values.put(CONTACT_NAME, name);
        values.put(CONTACT_SUPERVISOR, "");
        values.put(CONTACT_NOTES, "");
        values.put(CONTACT_BDAY, "");
        values.put(CONTACT_EMAIL, "");
        values.put(CONTACT_MOB1, mobile);
        values.put(CONTACT_MOB2, "");
        values.put(CONTACT_MOB3, "");
        values.put(CONTACT_LPHONE, "");
        values.put(CONTACT_ADDR, "");
        values.put(CONTACT_ST, "");
        values.put(CONTACT_SITE, "");
        values.put(CONTACT_STUDY_WORK, "");
        values.put(CONTACT_CLASS_YEAR, "");

        String id = String.valueOf(writableDB.insert(TB_CONTACT, null, values));

        values = new ContentValues();
        values.put(PHOTO_ID, id);
        values.putNull(PHOTO_BLOB);

        writableDB.insert(TB_PHOTO, null, values);

        return id;

    }

    public String addAttendant(ContactData att) {
        ContentValues values = new ContentValues();

        values.put(CONTACT_MAPLAT, att.getMapLat());
        values.put(CONTACT_MAPLNG, att.getMapLng());
        values.put(CONTACT_MAPZOM, att.getMapZoom());
        values.put(CONTACT_NAME, att.getName());
        values.put(CONTACT_SUPERVISOR, att.getPriest());
        values.put(CONTACT_NOTES, att.getComm());
        values.put(CONTACT_BDAY, att.getBirthDay());
        values.put(CONTACT_EMAIL, att.getEmail());
        values.put(CONTACT_MOB1, att.getMobile1());
        values.put(CONTACT_MOB2, att.getMobile2());
        values.put(CONTACT_MOB3, att.getMobile3());
        values.put(CONTACT_LPHONE, att.getLandPhone());
        values.put(CONTACT_ADDR, att.getAddress());
        values.put(CONTACT_ST, att.getStreet());
        values.put(CONTACT_SITE, att.getSite());
        values.put(CONTACT_STUDY_WORK, att.getStudyWork());
        values.put(CONTACT_CLASS_YEAR, att.getClassYear());

        String id = String.valueOf(writableDB.insert(TB_CONTACT, null, values));

        values = new ContentValues();
        values.put(PHOTO_ID, id);
        values.put(PHOTO_BLOB, Utility.getBytes(att.getPhoto()));

        writableDB.insert(TB_PHOTO, null, values);
        return id;

    }

    public ContactData getContactInfo(String id) {
        Cursor c = readableDB.query(TB_CONTACT,
                new String[]{
                        CONTACT_ID
                        , CONTACT_NAME
                        , CONTACT_MAPLAT
                        , CONTACT_MAPLNG
                        , CONTACT_MAPZOM

                        , CONTACT_SUPERVISOR
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
                }, CONTACT_ID
                        + " = ?", new String[]{id}, null, null, null);
        ContactData result = null;
        if (c.moveToFirst()) {

            result = new ContactData(c.getString(0),
                    c.getString(1), null, c.getDouble(2), c.getDouble(3),
                    c.getFloat(4), c.getString(5), c.getString(6),
                    c.getString(7), c.getString(8), c.getString(9),
                    c.getString(10), c.getString(11), c.getString(12),
                    c.getString(13), c.getString(14), c.getString(15),
                    c.getString(16), c.getString(17));
            c = readableDB.query(TB_PHOTO, new String[]{
                    PHOTO_BLOB
            }, PHOTO_ID
                    + " = ?", new String[]{id}, null, null, null);
            if (c.moveToFirst())
                result.setPhoto(Utility.getBitmap(c.getBlob(0)));
        }
        c.close();
        return result;
    }

    public ArrayList<ContactSort> getContactsDisplayList() {
        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + PHOTO_BLOB +
                "," + CONTACT_SUPERVISOR + "," + CONTACT_CLASS_YEAR +
                "," + CONTACT_STUDY_WORK +
                "," + CONTACT_ST +
                "," + CONTACT_SITE +
                " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_PHOTO +
                " ON " + CONTACT_ID + "=" + PHOTO_ID +
                " ORDER BY " + CONTACT_NAME;


        Cursor c = readableDB.rawQuery(selectQuery, null);
        ArrayList<ContactSort> result = new ArrayList<>(c.getCount());

        if (c.moveToFirst()) {

            do {
                result.add(new ContactSort(c.getString(0), c
                        .getString(1), Utility.getBitmap(c.getBlob(2)), c
                        .getString(3), c.getString(4), c.getString(5), c
                        .getString(6), c.getString(7)));

            } while (c.moveToNext());
        }
        c.close();

        return result;

    }

    public ArrayList<ContactLoc> getContactsLocations() {
        Cursor c = readableDB.query(TB_CONTACT, new String[]{CONTACT_NAME, CONTACT_MAPLAT,
                        CONTACT_MAPLNG},
                CONTACT_MAPLAT + "!=0 OR " + CONTACT_MAPLNG + "!=0", null, null, null, null);
        ArrayList<ContactLoc> result = new ArrayList<>(c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactLoc(c.getString(0), c
                        .getDouble(1), c.getDouble(2)));
            } while (c.moveToNext());
        }
        c.close();

        return result;

    }

    public ContactLocation getContactLocation(String id) {
        Cursor c = readableDB.query(TB_CONTACT, new String[]{CONTACT_MAPLAT,
                CONTACT_MAPLNG, CONTACT_MAPZOM}, CONTACT_ID + " = ?", new String[]{id}, null, null, null);

        if (c.moveToFirst()) {
            new ContactLocation(c.getDouble(0), c
                    .getDouble(1), c.getFloat(2));
        }
        c.close();

        return new ContactLocation(0, 0, 0);

    }

    public ArrayList<String> getOptionsList(String tag) {
        Cursor c = readableDB.query(true, TB_CONTACT, new String[]{tag},
                tag + "!=''", null, null,
                null, tag, null);
        ArrayList<String> result = new ArrayList<>(c.getCount());
        if (c.moveToFirst()) {
            do {
                result.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();

        return result;

    }

    public int getClassYearsCount() {
        String selectQuery = "SELECT DISTINCT " + CONTACT_CLASS_YEAR + " FROM "
                + TB_CONTACT + " WHERE " + CONTACT_CLASS_YEAR + "!=''";
        Cursor c = readableDB.rawQuery(selectQuery, null);

        int count = c.getCount();
        c.close();

        return count;

    }

    public boolean exportInformationTable(ArrayList<String> dataTag,
                                          ArrayList<String> dataHeader, String path) {

        String selectQuery = "SELECT * FROM " + TB_CONTACT +
                " ORDER BY " + CONTACT_NAME;

        Cursor c = readableDB.rawQuery(selectQuery, null);
        Document document = new Document(PageSize.LETTER.rotate());
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Font font = FontFactory.getFont("assets/fonts/arabic.ttf",
                    BaseFont.IDENTITY_H, true, 16);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("dayra - " + DB_NAME, font));
            document.add(new Paragraph(new SimpleDateFormat(
                    "yyyy-MM-dd  hh:mm:ss a", Locale.getDefault())
                    .format(new Date()), font));
            document.add(new Paragraph("made with love by dayra ©"
                    + new SimpleDateFormat("yyyy", Locale.getDefault())
                    .format(new Date()), font));
            document.add(new Paragraph("Contact @ Abanoub Milad Hanna abanoubcs@gmail.com", font));
            document.add(new Paragraph("Follow @ www.facebook.com/dayraapp", font));
            document.add(new Paragraph(" "));

            font.setSize(14);

            PdfPTable table = new PdfPTable(dataHeader.size());
            table.setWidthPercentage(100);
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            int required = dataTag.size();
            for (int i = 0; i < required; i++)
                table.addCell(new PdfPCell(new Paragraph((dataHeader.get(i)),
                        font)));

            if (c.moveToFirst()) {
                int[] colTags = new int[required];
                if (dataTag.get(0).equals(" ")) {
                    int counter = 1;
                    for (int i = 1; i < required; i++)
                        colTags[i] = c.getColumnIndex(dataTag.get(i));
                    do {
                        table.addCell(new PdfPCell(new Paragraph(counter + "",
                                font)));
                        for (int i = 1; i < required; i++)
                            table.addCell(new PdfPCell(new Paragraph(c
                                    .getString(colTags[i]), font)));
                        counter++;
                    } while (c.moveToNext());
                } else {
                    for (int i = 0; i < required; i++)
                        colTags[i] = c.getColumnIndex(dataTag.get(i));
                    do {
                        for (int i = 0; i < required; i++)
                            table.addCell(new PdfPCell(new Paragraph(c
                                    .getString(colTags[i]), font)));
                    } while (c.moveToNext());
                }
            }
            c.close();
            document.add(table);
            document.close();
            return true;
        } catch (Exception e) {
            return false;

        }

    }

    public boolean exportAttendanceReport(String path, String dateRegex, String[] header, boolean isEnglishMode) {
        String selectQuery = "SELECT " + CONTACT_NAME + "," + PHOTO_BLOB + "," + ATTEND_TYPE +
                ", MIN(" + ATTEND_DAY + "), " +
                "MAX(" + ATTEND_DAY + "), " + "COUNT(" + ATTEND_DAY + ")" +
                " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_PHOTO +
                " ON " + CONTACT_ID + "=" + PHOTO_ID +
                " LEFT OUTER JOIN " + TB_ATTEND + " ON " +
                CONTACT_ID + "=" + ATTEND_ID + " AND " + ATTEND_DAY + " LIKE " + dateRegex +
                " GROUP BY " + CONTACT_ID + "," + ATTEND_TYPE + " ORDER BY " + CONTACT_NAME;

        Cursor c = readableDB.rawQuery(selectQuery, null);
        Document document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Font font = FontFactory.getFont("assets/fonts/arabic.ttf",
                    BaseFont.IDENTITY_H, true, 16);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("dayra - " + DB_NAME, font));
            document.add(new Paragraph(new SimpleDateFormat(
                    "yyyy-MM-dd  hh:mm:ss a", Locale.getDefault())
                    .format(new Date()), font));
            document.add(new Paragraph("made with love by dayra ©"
                    + new SimpleDateFormat("yyyy", Locale.getDefault())
                    .format(new Date()), font));
            document.add(new Paragraph("Contact @ Abanoub Milad Hanna abanoubcs@gmail.com", font));
            document.add(new Paragraph("Follow @ www.facebook.com/dayraapp", font));
            document.add(new Paragraph(" "));

            if (c.moveToFirst()) {
                int counter = 0;
                if (isEnglishMode) {
                    do {
                        document.newPage();
                        document.add(new Paragraph(++counter + ""));
                        PdfPTable table = new PdfPTable(4);
                        table.setWidthPercentage(100);
                        table.setWidths(new float[]{25f, 25f, 25f, 25f});

                        byte[] photo = c.getBlob(1);
                        if (photo != null) {
                            Image image = Image.getInstance(photo);
                            image.scaleToFit(200f, 200f);
                            document.add(image);
                            document.add(new Paragraph(" "));
                        } else
                            document.add(new Paragraph(" "));

                        new Paragraph(c.getString(1), font);
                        document.add(new Paragraph(" "));

                        table.addCell(new Paragraph(header[0], font));
                        table.addCell(new Paragraph(header[1], font));
                        table.addCell(new Paragraph(header[2], font));
                        table.addCell(new Paragraph(header[3], font));

                        table.addCell(new Paragraph(c.getString(2), font));
                        table.addCell(new Paragraph(c.getString(3), font));
                        table.addCell(new Paragraph(c.getString(4), font));
                        table.addCell(new Paragraph(c.getString(5), font));
                        document.add(table);
                    } while (c.moveToNext());
                } else {
                    do {
                        document.newPage();
                        document.add(new Paragraph(++counter + ""));
                        PdfPTable table = new PdfPTable(4);
                        table.setWidthPercentage(100);
                        table.setWidths(new float[]{25f, 25f, 25f, 25f});
                        table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

                        byte[] photo = c.getBlob(1);
                        if (photo != null) {
                            Image image = Image.getInstance(photo);
                            image.scaleToFit(200f, 200f);
                            document.add(image);
                            document.add(new Paragraph(" "));
                        } else
                            document.add(new Paragraph(" "));

                        new Paragraph(c.getString(1), font);
                        document.add(new Paragraph(" "));

                        table.addCell(new Paragraph(header[0], font));
                        table.addCell(new Paragraph(header[1], font));
                        table.addCell(new Paragraph(header[2], font));
                        table.addCell(new Paragraph(header[3], font));

                        table.addCell(new Paragraph(c.getString(2), font));
                        table.addCell(new Paragraph(c.getString(3), font));
                        table.addCell(new Paragraph(c.getString(4), font));
                        table.addCell(new Paragraph(c.getString(5), font));
                        document.add(table);
                    } while (c.moveToNext());

                }

            }
            c.close();
            document.close();
            return true;
        } catch (Exception e) {
            return false;

        }

    }

    public boolean exportInformationReport(String path, String[] headerArray, boolean isEnglishMode) {
        String selectQuery = "SELECT * FROM " + TB_CONTACT +
                " LEFT OUTER JOIN " + TB_PHOTO +
                " ON " + CONTACT_ID + "=" + PHOTO_ID +
                " ORDER BY " + CONTACT_NAME;
        Cursor c = readableDB.rawQuery(selectQuery, null);
        Document document = new Document(PageSize.LETTER);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Font font = FontFactory.getFont("assets/fonts/arabic.ttf",
                    BaseFont.IDENTITY_H, true, 16);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("dayra - " + DB_NAME, font));
            document.add(new Paragraph(new SimpleDateFormat(
                    "yyyy-MM-dd  hh:mm:ss a", Locale.getDefault())
                    .format(new Date()), font));
            document.add(new Paragraph("made with love by dayra ©"
                    + new SimpleDateFormat("yyyy", Locale.getDefault())
                    .format(new Date()), font));
            document.add(new Paragraph("Contact @ Abanoub Milad Hanna abanoubcs@gmail.com", font));
            document.add(new Paragraph("Follow @ www.facebook.com/dayraapp", font));
            document.add(new Paragraph(" "));

            if (c.moveToFirst()) {
                int COL_NAME = c.getColumnIndex(CONTACT_NAME);
                int COL_PHOTO = c.getColumnIndex(PHOTO_BLOB);
                int COL_SUPERVISOR = c.getColumnIndex(CONTACT_SUPERVISOR);
                int COL_NOTES = c.getColumnIndex(CONTACT_NOTES);
                int COL_BDAY = c.getColumnIndex(CONTACT_BDAY);
                int COL_EMAIL = c.getColumnIndex(CONTACT_EMAIL);
                int COL_MOBILE1 = c.getColumnIndex(CONTACT_MOB1);
                int COL_MOBILE2 = c.getColumnIndex(CONTACT_MOB2);
                int COL_MOBILE3 = c.getColumnIndex(CONTACT_MOB3);
                int COL_LAND_PHONE = c.getColumnIndex(CONTACT_LPHONE);
                int COL_ADDRESS = c.getColumnIndex(CONTACT_ADDR);
                int COL_CLASS_YEAR = c.getColumnIndex(CONTACT_CLASS_YEAR);
                int COL_STUDY_WORK = c.getColumnIndex(CONTACT_STUDY_WORK);
                int COL_STREET = c.getColumnIndex(CONTACT_ST);
                int COL_SITE = c.getColumnIndex(CONTACT_SITE);
                int counter = 0;
                if (isEnglishMode) {
                    do {
                        document.newPage();
                        document.add(new Paragraph(++counter + ""));
                        PdfPTable table = new PdfPTable(2);
                        table.setWidthPercentage(100);
                        table.setWidths(new float[]{18f, 82f});

                        byte[] photo = c.getBlob(COL_PHOTO);
                        if (photo != null) {
                            Image image = Image.getInstance(photo);
                            image.scaleToFit(200f, 200f);
                            document.add(image);
                            document.add(new Paragraph(" "));
                        } else
                            document.add(new Paragraph(" "));

                        table.addCell(new Paragraph(headerArray[0], font));
                        table.addCell(new Paragraph(c.getString(COL_NAME), font));

                        table.addCell(new Paragraph(headerArray[1], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_CLASS_YEAR), font));

                        table.addCell(new Paragraph(headerArray[2], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_STUDY_WORK), font));

                        table.addCell(new Paragraph(headerArray[3], font));
                        table.addCell(new Paragraph(c.getString(COL_MOBILE1),
                                font));

                        table.addCell(new Paragraph(headerArray[4], font));
                        table.addCell(new Paragraph(c.getString(COL_MOBILE2),
                                font));

                        table.addCell(new Paragraph(headerArray[5], font));
                        table.addCell(new Paragraph(c.getString(COL_MOBILE3),
                                font));

                        table.addCell(new Paragraph(headerArray[6], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_LAND_PHONE), font));


                        table.addCell(new Paragraph(headerArray[7], font));
                        table.addCell(new Paragraph(c.getString(COL_EMAIL),
                                font));

                        table.addCell(new Paragraph(headerArray[9], font));
                        table.addCell(new Paragraph(c.getString(COL_STREET),
                                font));

                        table.addCell(new Paragraph(headerArray[8], font));
                        table.addCell(new Paragraph(c.getString(COL_SITE), font));

                        table.addCell(new Paragraph(headerArray[10], font));
                        table.addCell(new Paragraph(c.getString(COL_ADDRESS),
                                font));

                        table.addCell(new Paragraph(headerArray[13], font));
                        table.addCell(new Paragraph(c.getString(COL_SUPERVISOR),
                                font));

                        table.addCell(new Paragraph(headerArray[11], font));
                        table.addCell(new Paragraph(c.getString(COL_NOTES), font));

                        table.addCell(new Paragraph(headerArray[12], font));
                        table.addCell(new Paragraph(c.getString(COL_BDAY), font));


                        document.add(table);
                    } while (c.moveToNext());
                } else {
                    do {
                        document.newPage();
                        document.add(new Paragraph(++counter + ""));
                        PdfPTable table = new PdfPTable(2);
                        table.setWidthPercentage(100);
                        table.setWidths(new float[]{82f, 18f});
                        table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

                        byte[] photo = c.getBlob(COL_PHOTO);
                        if (photo != null) {
                            Image image = Image.getInstance(photo);
                            image.scaleToFit(200f, 200f);
                            document.add(image);
                            document.add(new Paragraph(" "));
                        } else
                            document.add(new Paragraph(" "));


                        table.addCell(new Paragraph(headerArray[0], font));
                        table.addCell(new Paragraph(c.getString(COL_NAME), font));

                        table.addCell(new Paragraph(headerArray[1], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_CLASS_YEAR), font));

                        table.addCell(new Paragraph(headerArray[2], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_STUDY_WORK), font));

                        table.addCell(new Paragraph(headerArray[3], font));
                        table.addCell(new Paragraph(c.getString(COL_MOBILE1),
                                font));

                        table.addCell(new Paragraph(headerArray[4], font));
                        table.addCell(new Paragraph(c.getString(COL_MOBILE2),
                                font));

                        table.addCell(new Paragraph(headerArray[5], font));
                        table.addCell(new Paragraph(c.getString(COL_MOBILE3),
                                font));

                        table.addCell(new Paragraph(headerArray[6], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_LAND_PHONE), font));


                        table.addCell(new Paragraph(headerArray[7], font));
                        table.addCell(new Paragraph(c.getString(COL_EMAIL),
                                font));

                        table.addCell(new Paragraph(headerArray[9], font));
                        table.addCell(new Paragraph(c.getString(COL_STREET),
                                font));

                        table.addCell(new Paragraph(headerArray[8], font));
                        table.addCell(new Paragraph(c.getString(COL_SITE), font));

                        table.addCell(new Paragraph(headerArray[10], font));
                        table.addCell(new Paragraph(c.getString(COL_ADDRESS),
                                font));

                        table.addCell(new Paragraph(headerArray[13], font));
                        table.addCell(new Paragraph(c.getString(COL_SUPERVISOR),
                                font));

                        table.addCell(new Paragraph(headerArray[11], font));
                        table.addCell(new Paragraph(c.getString(COL_NOTES), font));

                        table.addCell(new Paragraph(headerArray[12], font));
                        table.addCell(new Paragraph(c.getString(COL_BDAY), font));


                        document.add(table);

                    } while (c.moveToNext());

                }

            }
            c.close();
            document.close();
            return true;
        } catch (Exception e) {
            return false;

        }

    }

    public boolean exportDayraExcel(Context context, String path) {

        String[] colNames = {CONTACT_NAME, CONTACT_CLASS_YEAR,
                CONTACT_STUDY_WORK, CONTACT_MOB1, CONTACT_MOB2,
                CONTACT_MOB3, CONTACT_LPHONE, CONTACT_EMAIL,
                CONTACT_SITE, CONTACT_ST, CONTACT_ADDR,
                CONTACT_NOTES, CONTACT_BDAY,
                CONTACT_SUPERVISOR, CONTACT_MAPLAT,
                CONTACT_MAPLNG, CONTACT_MAPZOM, PHOTO_BLOB};

        String[] colNotes = context.getResources().getStringArray(
                R.array.excel_header);

        String selectQuery = "SELECT * FROM " + TB_CONTACT +
                " LEFT OUTER JOIN " + TB_PHOTO +
                " ON " + CONTACT_ID + "=" + PHOTO_ID +
                " ORDER BY " + CONTACT_NAME;
        Cursor c = readableDB.rawQuery(selectQuery, null);

        try {

            WritableWorkbook workbook = Workbook.createWorkbook(new File(path));
            WritableSheet sheet = workbook.createSheet("dayra", 0);

            int colCount = colNames.length;
            for (int i = 0; i < colCount; i++)
                sheet.addCell(new Label(i, 0, colNotes[i]));

            if (c.moveToFirst()) {
                int[] colIndex = new int[colCount];
                for (int i = 0; i < colCount; i++)
                    colIndex[i] = c.getColumnIndex(colNames[i]);
                int rowCounter = 1;
                do {
                    for (int i = 0; i < colCount - 1; i++)
                        sheet.addCell(new Label(i, rowCounter, c
                                .getString(colIndex[i])));
                    byte[] photo = c
                            .getBlob(colIndex[colCount - 1]);
                    if (photo != null)
                        sheet.addImage(new WritableImage(colCount - 1, rowCounter, 1,
                                1, photo));
                    rowCounter++;
                } while (c.moveToNext());

            }
            c.close();
            workbook.write();
            workbook.close();
            return true;
        } catch (Exception e) {
            return false;

        }
    }

    public ArrayList<ContactMobile> getContactsMobile() {
        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + PHOTO_BLOB +
                "," + CONTACT_MOB1 +
                " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_PHOTO +
                " ON " + CONTACT_ID + "=" + PHOTO_ID +
                " ORDER BY " + CONTACT_NAME;

        Cursor c = readableDB.rawQuery(selectQuery, null);
        ArrayList<ContactMobile> result = new ArrayList<>(
                c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactMobile(c.getString(0), c
                        .getString(1), Utility.getBitmap(c.getBlob(2)), c
                        .getString(3), c
                        .getString(3).equals("")));
            } while (c.moveToNext());
        }
        c.close();

        return result;

    }

    public ArrayList<ContactMobile> getGContactsMobile(ContentResolver resolver) {
        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + PHOTO_BLOB +
                "," + CONTACT_MOB1 +
                " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_PHOTO +
                " ON " + CONTACT_ID + "=" + PHOTO_ID +
                " ORDER BY " + CONTACT_NAME;

        Cursor c = readableDB.rawQuery(selectQuery, null);
        ArrayList<ContactMobile> result = new ArrayList<>(
                c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactMobile(c.getString(0), c
                        .getString(1), Utility.getBitmap(c.getBlob(2)), c
                        .getString(3), ContactHelper.doesContactExist(resolver, c
                        .getString(1))));
            } while (c.moveToNext());
        }
        c.close();

        return result;

    }

    public ArrayList<ContactCheck> getDayAttendance(String type, String day,
                                                    String name, IntWrapper counter) {
        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + ATTEND_DAY + "," +
                PHOTO_BLOB + " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_PHOTO +
                " ON " + CONTACT_ID + "=" + PHOTO_ID +
                " LEFT OUTER JOIN " + TB_ATTEND + " ON " +
                CONTACT_ID + "=" + ATTEND_ID + " AND " + ATTEND_DAY + " = ?  AND " + ATTEND_TYPE + " = ? WHERE " + CONTACT_NAME
                + " LIKE ? ORDER BY " + CONTACT_NAME;

        Cursor c = readableDB.rawQuery(selectQuery, new String[]{day, type, "%" + name + "%"});
        ArrayList<ContactCheck> result = new ArrayList<>(
                c.getCount());
        int updated = 0;
        if (c.moveToFirst()) {
            boolean flag;
            do {
                flag = c.getString(2) != null;
                if (flag)
                    updated++;
                result.add(new ContactCheck(c.getString(0), c
                        .getString(1), Utility.getBitmap(c.getBlob(3)), flag));
            } while (c.moveToNext());
        }
        c.close();
        counter.setCounter(updated);
        return result;
    }

    public boolean isContactDayAttendance(String id, String day, String type) {
        String selectQuery = "SELECT " + ATTEND_DAY +
                " FROM " + TB_CONTACT + " INNER JOIN " +
                TB_ATTEND + " ON " +
                CONTACT_ID + "=" + ATTEND_ID +
                " AND " + CONTACT_ID + " = ? " +
                " AND " + ATTEND_TYPE + " = ?  AND " + ATTEND_DAY + " = ?";

        Cursor c = readableDB.rawQuery(selectQuery, new String[]{id, type, day});
        boolean result = false;
        if (c.moveToFirst())
            result = true;
        c.close();
        return result;
    }

    public ArrayList<String> getAttendances(String id, String type) {
        String selectQuery = "SELECT " + ATTEND_DAY + " FROM " + TB_ATTEND +
                " WHERE " + ATTEND_ID + " = ? AND " + ATTEND_TYPE + " = ? ORDER BY " + ATTEND_DAY + " DESC";

        Cursor c = readableDB.rawQuery(selectQuery, new String[]{id, type});
        ArrayList<String> result = new ArrayList<>(
                c.getCount());
        if (c.moveToFirst()) {
            do {
                result.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        return result;
    }

    public void addDay(String id, String type, String day) {
        ContentValues values = new ContentValues();
        values.put(ATTEND_ID, id);
        values.put(ATTEND_DAY, day);
        values.put(ATTEND_TYPE, type);
        writableDB.insert(TB_ATTEND, null, values);
    }

    public void removeDay(String id, String type, String day) {
        writableDB.delete(TB_ATTEND, ATTEND_ID + " = ? AND " + ATTEND_TYPE
                        + " = ? AND " + ATTEND_DAY + " = ?",
                new String[]{id, type,
                        day});
    }

    public ArrayList<ContactID> search(String value, String tag) {

        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + PHOTO_BLOB +
                " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_PHOTO +
                " ON " + CONTACT_ID + "=" + PHOTO_ID +
                " WHERE " + tag + " LIKE ? ORDER BY " + tag;
        Cursor c = readableDB.rawQuery(selectQuery, new String[]{"%" + value + "%"});
        ArrayList<ContactID> result = new ArrayList<>(c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactID(c.getString(0), c.getString(1),
                        Utility.getBitmap(c.getBlob(2))));
            } while (c.moveToNext());
        }
        c.close();
        return result;

    }

    public ArrayList<ContactDay> searchBirthdays(String dateRegex) {

        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + PHOTO_BLOB + "," +
                CONTACT_BDAY +
                " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_PHOTO
                + " ON " + CONTACT_ID + "=" + PHOTO_ID
                + " WHERE " + CONTACT_BDAY + " LIKE ? ORDER BY " + CONTACT_BDAY;
        Cursor c = readableDB.rawQuery(selectQuery, new String[]{dateRegex});
        ArrayList<ContactDay> result = new ArrayList<>(c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactDay(c.getString(0), c.getString(1),
                        c.getString(3), Utility.getBitmap(c.getBlob(2))));
            } while (c.moveToNext());
        }
        c.close();
        return result;

    }

    public ArrayList<ContactDay> searchDates(String date, String type, String selectTag) {
        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + selectTag + "," +
                PHOTO_BLOB + " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_PHOTO +
                " ON " + CONTACT_ID + "=" + PHOTO_ID +
                " INNER JOIN " + TB_ATTEND + " ON " +
                CONTACT_ID + "=" + ATTEND_ID + " AND " + ATTEND_DAY + " = ?  AND " + ATTEND_TYPE + " = ? GROUP BY " + CONTACT_ID
                + " ORDER BY " + CONTACT_NAME;

        Cursor c = readableDB.rawQuery(selectQuery, new String[]{date, type});
        ArrayList<ContactDay> result = new ArrayList<>(c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactDay(c.getString(0), c.getString(1),
                        c.getString(2), Utility.getBitmap(c.getBlob(3))));
            } while (c.moveToNext());
        }
        c.close();
        return result;

    }

    public ArrayList<ContactStatistics> getContactsAttendanceStatistics(String type) {
        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + PHOTO_BLOB +
                ", MIN(" + ATTEND_DAY + "), " +
                "MAX(" + ATTEND_DAY + "), " + "COUNT(" + ATTEND_DAY + ")" +
                " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_PHOTO +
                " ON " + CONTACT_ID + "=" + PHOTO_ID +
                " LEFT OUTER JOIN " + TB_ATTEND + " ON " +
                CONTACT_ID + "=" + ATTEND_ID + " AND " + ATTEND_TYPE + " = ? GROUP BY " + CONTACT_ID + " ORDER BY " + CONTACT_NAME;
        Cursor c = readableDB.rawQuery(selectQuery, new String[]{type});
        ArrayList<ContactStatistics> result = new ArrayList<>(
                c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactStatistics(c.getString(0), c
                        .getString(1), Utility.getBitmap(c.getBlob(2)), c.getString(3), c.getString(4), c.getInt(5)));
            } while (c.moveToNext());
        }
        c.close();
        return result;

    }

    public ArrayList<String> getContactAttendanceStatistics(String id, String type) {
        String selectQuery = "SELECT MIN(" + ATTEND_DAY + "), " +
                "MAX(" + ATTEND_DAY + "), " + "COUNT(" + ATTEND_DAY + ")" +
                " FROM " + TB_ATTEND + " WHERE " +
                ATTEND_ID + " = ? " +
                " AND " + ATTEND_TYPE + " = ? ";
        Cursor c = readableDB.rawQuery(selectQuery, new String[]{id, type});
        ArrayList<String> result = new ArrayList<>(
                c.getCount());

        if (c.moveToFirst()) {

            result.add(c.getString(0));
            result.add(c.getString(1));
            result.add(c.getString(2));

        }
        c.close();
        return result;

    }

    public ArrayList<String> getExistingYears() {
        String selectQuery = "SELECT DISTINCT SUBSTR(" + ATTEND_DAY + ",0,4) FROM " +
                TB_ATTEND;
        Cursor c = readableDB.rawQuery(selectQuery, null);
        ArrayList<String> result = new ArrayList<>(
                c.getCount() + 1);
        result.add("any year");

        if (c.moveToFirst()) {
            do {
                result.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        return result;

    }

    public String getNameId(String name) {
        String selectQuery = "SELECT " + CONTACT_ID + " FROM " + TB_CONTACT + " WHERE "
                + CONTACT_NAME + "= ? LIMIT 1";
        Cursor c = readableDB.rawQuery(selectQuery, new String[]{name});
        if (c.getCount() == 0) {
            c.close();
            return "-1";
        }
        c.moveToFirst();
        String temp = c.getString(0);
        c.close();
        return temp;
    }

    public boolean AddDayraExcel(String path) {
        String[] colNames = {CONTACT_NAME, CONTACT_CLASS_YEAR, CONTACT_STUDY_WORK, CONTACT_MOB1, CONTACT_MOB2,
                CONTACT_MOB3, CONTACT_LPHONE, CONTACT_EMAIL, CONTACT_SITE, CONTACT_ST, CONTACT_ADDR, CONTACT_NOTES, CONTACT_BDAY,
                CONTACT_SUPERVISOR, CONTACT_MAPLAT,
                CONTACT_MAPLNG, CONTACT_MAPZOM, CONTACT_PHOTO};
        ContentValues values;

        try {
            Workbook workbook = Workbook.getWorkbook(new File(path));
            Sheet sheet = workbook.getSheet(0);

            if (sheet.getColumns() != 21)
                return false;
            int rows = sheet.getRows();
            int rowCounter = 1;
            while (rowCounter < rows) {
                values = new ContentValues();
                for (int i = 0; i < 21; i++)
                    values.put(colNames[i], sheet.getCell(i, rowCounter)
                            .getContents().trim());
                if (sheet.getCell(17, rowCounter).getContents().trim()
                        .equals("")
                        || sheet.getCell(18, rowCounter).getContents().trim()
                        .equals("")
                        || sheet.getCell(19, rowCounter).getContents().trim()
                        .equals("")) {
                    values.put(colNames[17], "0");
                    values.put(colNames[18], "0");
                    values.put(colNames[19], "0");
                }
                writableDB.insert(TB_CONTACT, null, values);
                rowCounter++;
            }
            workbook.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    public void divideDayra(Context context, int range, String path) {
//        ArrayList<String> sites = getOptionsList(CONTACT_SITE);
//        ArrayList<String> classYears = getOptionsList(CONTACT_CLASS_YEAR);
//        int classYearsCount = classYears.size();
//        range = Math.min(range, classYearsCount);
//
//        int groupsCount = classYearsCount / range;
//        if (classYearsCount % range != 0)
//            groupsCount++;
//
//        ArrayList<String> queryArr = new ArrayList<>(groupsCount);
//        ArrayList<String> Groupnames = new ArrayList<>(groupsCount);
//        String temp;
//        String tempName;
//        int startAt;
//        for (int i = 0; i < groupsCount; i++) {
//            startAt = range * i;
//            temp = CONTACT_CLASS_YEAR + "=" + classYears.get(startAt);
//            tempName = classYears.get(startAt);
//            for (int j = startAt + 1; j < startAt + range
//                    && j < classYearsCount; j++) {
//                temp += " OR " + CONTACT_CLASS_YEAR + "='" + classYears.get(j) + "'";
//                tempName += " " + classYears.get(j);
//            }
//            queryArr.add(temp);
//            Groupnames.add(tempName);
//
//        }
//
//        DBDivider minidbm;
//        int itr;
//        for (String site : sites) {
//            itr = -1;
//            for (String queryItem : queryArr) {
//                itr++;
//                minidbm = new DBDivider(context, path + DB_NAME + " " + site + " "
//                        + Groupnames.get(itr));
//
//                temp = "SELECT * FROM " + TB_CONTACT + " WHERE " + CONTACT_SITE + "='"
//                        + site + "' AND (" + queryItem + " )";
//                Cursor c = readableDB.rawQuery(temp, null);
//
//                if (c.moveToFirst()) {
//                    int COL_MAP_LAT = c.getColumnIndex(CONTACT_MAPLAT);
//                    int COL_MAP_LNG = c.getColumnIndex(CONTACT_MAPLNG);
//                    int COL_MAP_ZOOM = c.getColumnIndex(CONTACT_MAPZOM);
//                    int COL_NAME = c.getColumnIndex(CONTACT_NAME);
//                    int COL_PIC_DIR = c.getColumnIndex(CONTACT_PHOTO);
//                    int COL_PRIEST = c.getColumnIndex(CONTACT_SUPERVISOR);
//                    int COL_COMM = c.getColumnIndex(CONTACT_NOTES);
//                    int COL_BDAY = c.getColumnIndex(CONTACT_BDAY);
//                    int COL_EMAIL = c.getColumnIndex(CONTACT_EMAIL);
//                    int COL_MOBILE1 = c.getColumnIndex(CONTACT_MOB1);
//                    int COL_MOBILE2 = c.getColumnIndex(CONTACT_MOB2);
//                    int COL_MOBILE3 = c.getColumnIndex(CONTACT_MOB3);
//                    int COL_LAND_PHONE = c.getColumnIndex(CONTACT_LPHONE);
//                    int COL_ADDRESS = c.getColumnIndex(CONTACT_ADDR);
//                    int COL_CLASS_YEAR = c.getColumnIndex(CONTACT_CLASS_YEAR);
//                    int COL_STUDY_WORK = c.getColumnIndex(CONTACT_STUDY_WORK);
//                    int COL_STREET = c.getColumnIndex(CONTACT_ST);
//                    int COL_SITE = c.getColumnIndex(CONTACT_SITE);
//                    ContentValues values;
//                    do {
//                        values = new ContentValues();
//                        values.put(CONTACT_MAPLAT, c.getDouble(COL_MAP_LAT));
//                        values.put(CONTACT_MAPLNG, c.getDouble(COL_MAP_LNG));
//                        values.put(CONTACT_MAPZOM, c.getFloat(COL_MAP_ZOOM));
//                        values.put(CONTACT_NAME, c.getString(COL_NAME));
//                        values.put(CONTACT_PHOTO, c.getString(COL_PIC_DIR));
//                        values.put(CONTACT_SUPERVISOR, c.getString(COL_PRIEST));
//                        values.put(CONTACT_NOTES, c.getString(COL_COMM));
//                        values.put(CONTACT_BDAY, c.getString(COL_BDAY));
//                        values.put(CONTACT_EMAIL, c.getString(COL_EMAIL));
//                        values.put(CONTACT_MOB1, c.getString(COL_MOBILE1));
//                        values.put(CONTACT_MOB2, c.getString(COL_MOBILE2));
//                        values.put(CONTACT_MOB3, c.getString(COL_MOBILE3));
//                        values.put(CONTACT_LPHONE, c.getString(COL_LAND_PHONE));
//                        values.put(CONTACT_ADDR, c.getString(COL_ADDRESS));
//                        values.put(CONTACT_ST, c.getString(COL_STREET));
//                        values.put(CONTACT_SITE, c.getString(COL_SITE));
//                        values.put(CONTACT_STUDY_WORK, c.getString(COL_STUDY_WORK));
//                        values.put(CONTACT_CLASS_YEAR, c.getString(COL_CLASS_YEAR));
//                        minidbm.addAttendant(values);
//
//                    } while (c.moveToNext());
//                }
//                c.close();
//                minidbm.close();
//
//            }
//
//        }
//    }

    //    public void divideDayra(Context context, String path) {
//        ArrayList<String> sites = getOptionsList(CONTACT_SITE);
//
//        DBDivider minidbm;
//        String temp;
//        for (String site : sites) {
//            minidbm = new DBDivider(context, path + DB_NAME + " " + site);
//            temp = "SELECT * FROM " + TB_CONTACT + " WHERE " + CONTACT_SITE + "='"
//                    + site + "'";
//            Cursor c = readableDB.rawQuery(temp, null);
//            if (c.moveToFirst()) {
//                int COL_MAP_LAT = c.getColumnIndex(CONTACT_MAPLAT);
//                int COL_MAP_LNG = c.getColumnIndex(CONTACT_MAPLNG);
//                int COL_MAP_ZOOM = c.getColumnIndex(CONTACT_MAPZOM);
//                int COL_NAME = c.getColumnIndex(CONTACT_NAME);
//                int COL_ATTEND_DATES = c.getColumnIndex(CONTACT_ATTEND_DATES);
//                int COL_LAST_ATTEND = c.getColumnIndex(CONTACT_LAST_ATTEND);
//                int COL_PIC_DIR = c.getColumnIndex(CONTACT_PHOTO);
//                int COL_PRIEST = c.getColumnIndex(CONTACT_SUPERVISOR);
//                int COL_COMM = c.getColumnIndex(CONTACT_NOTES);
//                int COL_BDAY = c.getColumnIndex(CONTACT_BDAY);
//                int COL_EMAIL = c.getColumnIndex(CONTACT_EMAIL);
//                int COL_MOBILE1 = c.getColumnIndex(CONTACT_MOB1);
//                int COL_MOBILE2 = c.getColumnIndex(CONTACT_MOB2);
//                int COL_MOBILE3 = c.getColumnIndex(CONTACT_MOB3);
//                int COL_LAND_PHONE = c.getColumnIndex(CONTACT_LPHONE);
//                int COL_ADDRESS = c.getColumnIndex(CONTACT_ADDR);
//                int COL_LAST_VISIT = c.getColumnIndex(CONTACT_LAST_VISIT);
//                int COL_CLASS_YEAR = c.getColumnIndex(CONTACT_CLASS_YEAR);
//                int COL_STUDY_WORK = c.getColumnIndex(CONTACT_STUDY_WORK);
//                int COL_STREET = c.getColumnIndex(CONTACT_ST);
//                int COL_SITE = c.getColumnIndex(CONTACT_SITE);
//                ContentValues values;
//                do {
//                    values = new ContentValues();
//                    values.put(CONTACT_MAPLAT, c.getDouble(COL_MAP_LAT));
//                    values.put(CONTACT_MAPLNG, c.getDouble(COL_MAP_LNG));
//                    values.put(CONTACT_MAPZOM, c.getFloat(COL_MAP_ZOOM));
//                    values.put(CONTACT_NAME, c.getString(COL_NAME));
//                    values.put(CONTACT_ATTEND_DATES, c.getString(COL_ATTEND_DATES));
//                    values.put(CONTACT_LAST_VISIT, c.getString(COL_LAST_VISIT));
//                    values.put(CONTACT_LAST_ATTEND, c.getString(COL_LAST_ATTEND));
//                    values.put(CONTACT_PHOTO, c.getString(COL_PIC_DIR));
//                    values.put(CONTACT_SUPERVISOR, c.getString(COL_PRIEST));
//                    values.put(CONTACT_NOTES, c.getString(COL_COMM));
//                    values.put(CONTACT_BDAY, c.getString(COL_BDAY));
//                    values.put(CONTACT_EMAIL, c.getString(COL_EMAIL));
//                    values.put(CONTACT_MOB1, c.getString(COL_MOBILE1));
//                    values.put(CONTACT_MOB2, c.getString(COL_MOBILE2));
//                    values.put(CONTACT_MOB3, c.getString(COL_MOBILE3));
//                    values.put(CONTACT_LPHONE, c.getString(COL_LAND_PHONE));
//                    values.put(CONTACT_ADDR, c.getString(COL_ADDRESS));
//                    values.put(CONTACT_ST, c.getString(COL_STREET));
//                    values.put(CONTACT_SITE, c.getString(COL_SITE));
//                    values.put(CONTACT_STUDY_WORK, c.getString(COL_STUDY_WORK));
//                    values.put(CONTACT_CLASS_YEAR, c.getString(COL_CLASS_YEAR));
//                    minidbm.addAttendant(values);
//
//                } while (c.moveToNext());
//            }
//            c.close();
//            minidbm.close();
//
//        }
//    }
    public ArrayList<ContactDay> getContactsAttendanceAbsence(String previousWeekRegex) {
        String selectQuery = "SELECT " + CONTACT_NAME + "," + PHOTO_BLOB +
                "MAX(" + ATTEND_DAY + ")" +
                " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_PHOTO +
                " ON " + CONTACT_ID + "=" + PHOTO_ID +
                " LEFT OUTER JOIN " + TB_ATTEND + " ON " +
                CONTACT_ID + "=" + ATTEND_ID + " WHERE " + ATTEND_DAY + " < ? GROUP BY " + ATTEND_ID + " ORDER BY " + CONTACT_NAME;
        Cursor c = readableDB.rawQuery(selectQuery, new String[]{previousWeekRegex});
        ArrayList<ContactDay> result = new ArrayList<>(
                c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactDay(null, c.getString(0), c.getString(1),
                        Utility.getBitmap(c.getBlob(1))));
            } while (c.moveToNext());
        }
        c.close();
        return result;

    }
}
