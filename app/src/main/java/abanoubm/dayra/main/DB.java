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
import abanoubm.dayra.model.ContactConnection;
import abanoubm.dayra.model.ContactData;
import abanoubm.dayra.model.ContactDay;
import abanoubm.dayra.model.ContactID;
import abanoubm.dayra.model.ContactLoc;
import abanoubm.dayra.model.ContactMobile;
import abanoubm.dayra.model.ContactSort;
import abanoubm.dayra.model.ContactStatistics;
import abanoubm.dayra.model.ContactUpdate;
import abanoubm.dayra.model.IntWrapper;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class DB extends SQLiteOpenHelper {
    private static String DB_NAME = "";
    private static final int DB_VERSION = 3;

    private static final String TB_CONNECTION = "conn_tb";
    private static final String CONN_A = "conn_a";
    private static final String CONN_B = "conn_b";

    private static final String TB_ATTEND = "attend_tb";
    private static final String ATTEND_ID = "attend_id";
    public static final String ATTEND_DAY = "attend_day";
    private static final String ATTEND_TYPE = "attend_type";

    private static final String TB_PHOTO = "photo_tb";
    private static final String PHOTO_ID = "photo_id";
    private static final String PHOTO_BLOB = "photo_blob";

    private static final String TB_CONTACT = "dayra_tb";
    public static final String CONTACT_ID = "_id", CONTACT_MAPLAT = "lat", CONTACT_MAPLNG = "lng",
            CONTACT_MAPZOM = "zom", CONTACT_NAME = "name", CONTACT_ATTEND_DATES = "dates",
            CONTACT_LAST_VISIT = "lvisit", CONTACT_LAST_ATTEND = "lattend", CONTACT_PHOTO = "pdir",
            CONTACT_PRIEST = "pri", CONTACT_NOTES = "comm", CONTACT_BDAY = "bday", CONTACT_EMAIL = "email",
            CONTACT_MOB1 = "mob1", CONTACT_MOB2 = "mob2", CONTACT_MOB3 = "mob3",
            CONTACT_LPHONE = "lphone", CONTACT_ADDR = "addr", CONTACT_ST = "st",
            CONTACT_SITE = "site", CONTACT_STUDY_WORK = "swork", CONTACT_CLASS_YEAR = "cyear";

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
                + CONTACT_NAME + " text, " + CONTACT_ATTEND_DATES + " text, " + CONTACT_LAST_ATTEND
                + " text, " + CONTACT_LAST_VISIT + " text, " + CONTACT_PHOTO + " text, "
                + CONTACT_PRIEST + " text, " + CONTACT_NOTES + " text, " + CONTACT_BDAY + " text, "
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

        sql = "create table " + TB_PHOTO + " ( " + PHOTO_ID + " integer, "
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

            sql = "create table " + TB_PHOTO + " ( " + PHOTO_ID + " integer, "
                    + PHOTO_BLOB + " blob)";
            db.execSQL(sql);
        }

    }

    public void deleteAttendant(String id) {
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

    public ArrayList<ContactID> getAttendantConnections(int hostID) {

        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + CONTACT_PHOTO
                + " FROM " + TB_CONTACT + " WHERE " + CONTACT_ID + " IN (SELECT "
                + CONN_B + " FROM " + TB_CONNECTION + " WHERE " + CONN_A + "="
                + hostID + ")" + " AND " + CONTACT_ID + "!=" + hostID + " ORDER BY "
                + CONTACT_NAME;
        Cursor c = readableDB.rawQuery(selectQuery, null);
        ArrayList<ContactID> result = new ArrayList<>(c.getCount());
        if (c.moveToFirst()) {
            do {
                result.add(new ContactID(c.getString(0), c.getString(1),
                        c.getString(2)));
            } while (c.moveToNext());
        }
        c.close();
        return result;
    }

    public ArrayList<ContactConnection> getAttendantConnections(String hostID,
                                                                String name) {

        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," +
                CONTACT_PHOTO + "," + CONN_B +
                " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_CONNECTION + " ON " +
                CONTACT_ID + "=" + CONN_B + " AND " + CONN_A + " = ? " +
                " WHERE " + CONTACT_ID + " != ? AND " + CONTACT_NAME +
                " LIKE ? ORDER BY " + CONTACT_NAME;
        Cursor c = readableDB.rawQuery(selectQuery, new String[]{hostID, hostID, "%" + name + "%"});
        ArrayList<ContactConnection> result = new ArrayList<>(
                c.getCount());
        if (c.moveToFirst()) {
            do {
                result.add(new ContactConnection(c.getString(0), c
                        .getString(1), c.getString(2), c.getString(2) != null));
            } while (c.moveToNext());
        }
        c.close();
        return result;
    }

    public void updateAttendant(ContactData att) {
        ContentValues values = new ContentValues();

        values.put(CONTACT_MAPLAT, att.getMapLat());
        values.put(CONTACT_MAPLNG, att.getMapLng());
        values.put(CONTACT_MAPZOM, att.getMapZoom());
        values.put(CONTACT_NAME, att.getName());
        values.put(CONTACT_ATTEND_DATES, att.getAttendDates());
        values.put(CONTACT_LAST_VISIT, att.getLastVisit());
        values.put(CONTACT_LAST_ATTEND, att.getLastAttend());
        values.put(CONTACT_PHOTO, att.getPicDir());
        values.put(CONTACT_PRIEST, att.getPriest());
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

        writableDB.update(TB_CONTACT, values, CONTACT_ID + " = ?",
                new String[]{String.valueOf(att.getId())});
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
                        case CONTACT_ATTEND_DATES:
                            values.put(CONTACT_ATTEND_DATES, att.getAttendDates());
                            break;
                        case CONTACT_LAST_VISIT:
                            values.put(CONTACT_LAST_VISIT, att.getLastVisit());
                            break;
                        case CONTACT_LAST_ATTEND:
                            values.put(CONTACT_LAST_ATTEND, att.getLastAttend());
                            break;
                        case CONTACT_PRIEST:
                            values.put(CONTACT_PRIEST, att.getPriest());
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

    public String addAttendant(String name, String mobile) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_MAPLAT, 0);
        values.put(CONTACT_MAPLNG, 0);
        values.put(CONTACT_MAPZOM, 0);
        values.put(CONTACT_NAME, name);
        values.put(CONTACT_ATTEND_DATES, "");
        values.put(CONTACT_LAST_VISIT, "");
        values.put(CONTACT_LAST_ATTEND, "");
        values.put(CONTACT_PHOTO, "");
        values.put(CONTACT_PRIEST, "");
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
        return String.valueOf(writableDB.insert(TB_CONTACT, null, values));

    }

    public int addAttendant(ContactData att) {
        ContentValues values = new ContentValues();

        values.put(CONTACT_MAPLAT, att.getMapLat());
        values.put(CONTACT_MAPLNG, att.getMapLng());
        values.put(CONTACT_MAPZOM, att.getMapZoom());
        values.put(CONTACT_NAME, att.getName());
        values.put(CONTACT_ATTEND_DATES, att.getAttendDates());
        values.put(CONTACT_LAST_VISIT, att.getLastVisit());
        values.put(CONTACT_LAST_ATTEND, att.getLastAttend());
        values.put(CONTACT_PHOTO, att.getPicDir());
        values.put(CONTACT_PRIEST, att.getPriest());
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

        return (int) writableDB.insert(TB_CONTACT, null, values);

    }

    public ContactData getAttendantData(String id) {
        Cursor c = readableDB.query(TB_CONTACT,
                new String[]{
                        CONTACT_ID
                        , CONTACT_NAME
                        , CONTACT_PHOTO
                        , CONTACT_MAPLAT
                        , CONTACT_MAPLNG
                        , CONTACT_MAPZOM

                        , CONTACT_ATTEND_DATES
                        , CONTACT_LAST_ATTEND
                        , CONTACT_PRIEST
                        , CONTACT_NOTES
                        , CONTACT_BDAY

                        , CONTACT_EMAIL
                        , CONTACT_MOB1
                        , CONTACT_MOB2
                        , CONTACT_MOB3
                        , CONTACT_LPHONE

                        , CONTACT_ADDR
                        , CONTACT_LAST_VISIT
                        , CONTACT_CLASS_YEAR
                        , CONTACT_STUDY_WORK
                        , CONTACT_ST
                        , CONTACT_SITE
                }, CONTACT_ID
                        + " = ?", new String[]{id}, null, null, null);
        ContactData result = null;
        if (c.moveToFirst()) {

            result = new ContactData(c.getString(0),
                    c.getString(1), c.getString(2), c.getDouble(3),
                    c.getDouble(4), c.getFloat(5), c.getString(6),
                    c.getString(7), c.getString(8), c.getString(9),
                    c.getString(10), c.getString(11), c.getString(12),
                    c.getString(13), c.getString(14), c.getString(15),
                    c.getString(16), c.getString(17), c.getString(18),
                    c.getString(19), c.getString(20), c.getString(21));
        }
        c.close();
        return result;
    }

    public ArrayList<ContactSort> getContactsDisplayList() {
        Cursor c = readableDB.query(TB_CONTACT, new String[]{CONTACT_ID, CONTACT_NAME,
                CONTACT_PHOTO,
                CONTACT_PRIEST,
                CONTACT_CLASS_YEAR,
                CONTACT_STUDY_WORK,
                CONTACT_ST, CONTACT_SITE,
        }, null, null, CONTACT_ID, null, CONTACT_NAME);
        ArrayList<ContactSort> result = new ArrayList<>(c.getCount());

        if (c.moveToFirst()) {

            do {
                result.add(new ContactSort(c.getString(0), c
                        .getString(1), c.getString(2), c
                        .getString(3), c.getString(4), c.getString(5), c
                        .getString(6), c.getString(7)));

            } while (c.moveToNext());
        }
        c.close();

        return result;

    }

    public ArrayList<ContactLoc> getAttendantsLoc() {
        Cursor c = readableDB.query(TB_CONTACT, new String[]{CONTACT_NAME, CONTACT_MAPLAT,
                CONTACT_MAPLNG}, null, null, CONTACT_ID, null, null);
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

    public boolean exportContactsPdf(ArrayList<String> dataTag,
                                     ArrayList<String> dataHeader, String path) {
        int required = dataTag.size();
        String selectQuery = "SELECT * FROM " + TB_CONTACT + " ORDER BY "
                + CONTACT_NAME;
        Cursor c = readableDB.rawQuery(selectQuery, null);
        Document document = new Document(PageSize.LETTER.rotate());
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Font font = FontFactory.getFont("assets/fonts/arabic.ttf",
                    BaseFont.IDENTITY_H, true, 16);

            document.add(new Paragraph("dayra - " + DB_NAME, font));
            document.add(new Paragraph(new SimpleDateFormat(
                    "yyyy-MM-dd  hh:mm a", Locale.getDefault())
                    .format(new Date()), font));
            document.add(new Paragraph(" "));

            font.setSize(14);

            PdfPTable table = new PdfPTable(dataHeader.size());
            table.setWidthPercentage(100);
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

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
            document.add(new Paragraph(" "));
            document.add(new Paragraph("made with love by dayra ©"
                    + new SimpleDateFormat("yyyy", Locale.getDefault())
                    .format(new Date())
                    + " Abanoub M.   www.facebook.com/dayraapp", font));
            document.close();
            return true;
        } catch (Exception e) {
            return false;

        }

    }

    public boolean exportReport(String path, String[] arr, boolean isEnglishMode) {
        String selectQuery = "SELECT * FROM " + TB_CONTACT + " ORDER BY "
                + CONTACT_NAME;
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
                    "yyyy-MM-dd  hh:mm a", Locale.getDefault())
                    .format(new Date()), font));

            document.add(new Paragraph(" "));

            document.add(new Paragraph("made with love by dayra 3.1 ©"
                    + new SimpleDateFormat("yyyy", Locale.getDefault())
                    .format(new Date())
                    + " Abanoub M.   www.facebook.com/dayraapp", font));

            if (c.moveToFirst()) {
                int COL_NAME = c.getColumnIndex(CONTACT_NAME);
                // int COL_ATTEND_DATES = c.getColumnIndex(CONTACT_ATTEND_DATES);
                int COL_LAST_ATTEND = c.getColumnIndex(CONTACT_LAST_ATTEND);
                int COL_PIC_DIR = c.getColumnIndex(CONTACT_PHOTO);
                int COL_PRIEST = c.getColumnIndex(CONTACT_PRIEST);
                int COL_COMM = c.getColumnIndex(CONTACT_NOTES);
                int COL_BDAY = c.getColumnIndex(CONTACT_BDAY);
                int COL_EMAIL = c.getColumnIndex(CONTACT_EMAIL);
                int COL_MOBILE1 = c.getColumnIndex(CONTACT_MOB1);
                int COL_MOBILE2 = c.getColumnIndex(CONTACT_MOB2);
                int COL_MOBILE3 = c.getColumnIndex(CONTACT_MOB3);
                int COL_LAND_PHONE = c.getColumnIndex(CONTACT_LPHONE);
                int COL_ADDRESS = c.getColumnIndex(CONTACT_ADDR);
                int COL_LAST_VISIT = c.getColumnIndex(CONTACT_LAST_VISIT);
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

                        String imagePath = c.getString(COL_PIC_DIR);
                        if (imagePath.length() != 0
                                && new File(imagePath).exists()) {
                            Image image = Image.getInstance(imagePath);
                            image.scaleToFit(200f, 200f);
                            document.add(image);
                            document.add(new Paragraph(" "));
                        }

                        table.addCell(new Paragraph(arr[0], font));
                        table.addCell(new Paragraph(c.getString(COL_NAME), font));

                        table.addCell(new Paragraph(arr[1], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_CLASS_YEAR), font));

                        table.addCell(new Paragraph(arr[2], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_STUDY_WORK), font));

                        table.addCell(new Paragraph(arr[14], font));
                        table.addCell(new Paragraph(c
                                .getString(COL_LAST_ATTEND), font));

                        // table.addCell(new Paragraph(arr[15], font));
                        // table.addCell(new Paragraph(c.getString(
                        // COL_ATTEND_DATES).replace(";", " "), font));

                        table.addCell(new Paragraph(arr[3], font));
                        table.addCell(new Paragraph(c.getString(COL_MOBILE1),
                                font));

                        table.addCell(new Paragraph(arr[4], font));
                        table.addCell(new Paragraph(c.getString(COL_MOBILE2),
                                font));

                        table.addCell(new Paragraph(arr[5], font));
                        table.addCell(new Paragraph(c.getString(COL_MOBILE3),
                                font));

                        table.addCell(new Paragraph(arr[6], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_LAND_PHONE), font));

                        table.addCell(new Paragraph(arr[7], font));
                        table.addCell(new Paragraph(c.getString(COL_EMAIL),
                                font));

                        table.addCell(new Paragraph(arr[16], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_LAST_VISIT), font));

                        table.addCell(new Paragraph(arr[13], font));
                        table.addCell(new Paragraph(c.getString(COL_PRIEST),
                                font));

                        table.addCell(new Paragraph(arr[11], font));
                        table.addCell(new Paragraph(c.getString(COL_COMM), font));

                        table.addCell(new Paragraph(arr[8], font));
                        table.addCell(new Paragraph(c.getString(COL_SITE), font));

                        table.addCell(new Paragraph(arr[9], font));
                        table.addCell(new Paragraph(c.getString(COL_STREET),
                                font));

                        table.addCell(new Paragraph(arr[10], font));
                        table.addCell(new Paragraph(c.getString(COL_ADDRESS),
                                font));

                        table.addCell(new Paragraph(arr[12], font));
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

                        String imagePath = c.getString(COL_PIC_DIR);
                        if (imagePath.length() != 0
                                && new File(imagePath).exists()) {
                            Image image = Image.getInstance(imagePath);
                            image.scaleToFit(200f, 200f);
                            document.add(image);
                            document.add(new Paragraph(" "));
                        }

                        table.addCell(new Paragraph(arr[0], font));
                        table.addCell(new Paragraph(c.getString(COL_NAME), font));

                        table.addCell(new Paragraph(arr[1], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_CLASS_YEAR), font));

                        table.addCell(new Paragraph(arr[2], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_STUDY_WORK), font));

                        table.addCell(new Paragraph(arr[14], font));
                        table.addCell(new Paragraph(c
                                .getString(COL_LAST_ATTEND), font));

                        // table.addCell(new Paragraph(arr[15], font));
                        // table.addCell(new Paragraph(c.getString(
                        // COL_ATTEND_DATES).replace(";", " "), font));

                        table.addCell(new Paragraph(arr[3], font));
                        table.addCell(new Paragraph(c.getString(COL_MOBILE1),
                                font));

                        table.addCell(new Paragraph(arr[4], font));
                        table.addCell(new Paragraph(c.getString(COL_MOBILE2),
                                font));

                        table.addCell(new Paragraph(arr[5], font));
                        table.addCell(new Paragraph(c.getString(COL_MOBILE3),
                                font));

                        table.addCell(new Paragraph(arr[6], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_LAND_PHONE), font));

                        table.addCell(new Paragraph(arr[7], font));
                        table.addCell(new Paragraph(c.getString(COL_EMAIL),
                                font));

                        table.addCell(new Paragraph(arr[16], font));
                        table.addCell(new Paragraph(
                                c.getString(COL_LAST_VISIT), font));

                        table.addCell(new Paragraph(arr[13], font));
                        table.addCell(new Paragraph(c.getString(COL_PRIEST),
                                font));

                        table.addCell(new Paragraph(arr[11], font));
                        table.addCell(new Paragraph(c.getString(COL_COMM), font));

                        table.addCell(new Paragraph(arr[8], font));
                        table.addCell(new Paragraph(c.getString(COL_SITE), font));

                        table.addCell(new Paragraph(arr[9], font));
                        table.addCell(new Paragraph(c.getString(COL_STREET),
                                font));

                        table.addCell(new Paragraph(arr[10], font));
                        table.addCell(new Paragraph(c.getString(COL_ADDRESS),
                                font));

                        table.addCell(new Paragraph(arr[12], font));
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
        String[] colNames = {CONTACT_NAME, CONTACT_CLASS_YEAR, CONTACT_STUDY_WORK, CONTACT_MOB1, CONTACT_MOB2,
                CONTACT_MOB3, CONTACT_LPHONE, CONTACT_EMAIL, CONTACT_SITE, CONTACT_ST, CONTACT_ADDR, CONTACT_NOTES, CONTACT_BDAY,
                CONTACT_PRIEST, CONTACT_LAST_ATTEND, CONTACT_ATTEND_DATES, CONTACT_LAST_VISIT, CONTACT_MAPLAT,
                CONTACT_MAPLNG, CONTACT_MAPZOM, CONTACT_PHOTO};
        String[] colNotes = context.getResources().getStringArray(
                R.array.excel_header);
        int colCount = colNames.length;
        String selectQuery = "SELECT * FROM " + TB_CONTACT + " ORDER BY "
                + CONTACT_NAME;
        Cursor c = readableDB.rawQuery(selectQuery, null);

        try {

            WritableWorkbook workbook = Workbook.createWorkbook(new File(path));
            WritableSheet sheet = workbook.createSheet("dayra", 0);

            for (int i = 0; i < colCount; i++)
                sheet.addCell(new Label(i, 0, colNotes[i]));

            if (c.moveToFirst()) {
                int[] colIndex = new int[colCount];
                for (int i = 0; i < colCount; i++)
                    colIndex[i] = c.getColumnIndex(colNames[i]);
                int rowCounter = 1;
                do {
                    for (int i = 0; i < colCount; i++)
                        sheet.addCell(new Label(i, rowCounter, c
                                .getString(colIndex[i])));
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
        Cursor c = readableDB.query(TB_CONTACT, new String[]{CONTACT_ID, CONTACT_NAME,
                CONTACT_PHOTO, CONTACT_MOB1
        }, null, null, CONTACT_ID, null, CONTACT_NAME);
        ArrayList<ContactMobile> result = new ArrayList<>(
                c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactMobile(c.getString(0), c
                        .getString(1), c.getString(2), c
                        .getString(3), c
                        .getString(3).equals("")));
            } while (c.moveToNext());
        }
        c.close();

        return result;

    }

    public ArrayList<ContactMobile> getGContactsMobile(ContentResolver resolver) {
        Cursor c = readableDB.query(TB_CONTACT, new String[]{CONTACT_ID, CONTACT_NAME,
                CONTACT_PHOTO, CONTACT_MOB1
        }, null, null, CONTACT_ID, null, CONTACT_NAME);
        ArrayList<ContactMobile> result = new ArrayList<>(
                c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactMobile(c.getString(0), c
                        .getString(1), c.getString(2), c
                        .getString(3), ContactHelper.doesContactExist(resolver, c
                        .getString(1))));
            } while (c.moveToNext());
        }
        c.close();

        return result;

    }

    public ArrayList<ContactUpdate> getDayAttendance(String type, String day,
                                                     String name, IntWrapper counter) {
        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + CONTACT_PHOTO + "," + ATTEND_DAY +
                " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_ATTEND + " ON " +
                CONTACT_ID + "=" + ATTEND_ID + " AND " + ATTEND_DAY + " = ?  AND " + ATTEND_TYPE + " = ? WHERE " + CONTACT_NAME
                + " LIKE ? ORDER BY " + CONTACT_NAME;

        Cursor c = readableDB.rawQuery(selectQuery, new String[]{day, type, "%" + name + "%"});
        ArrayList<ContactUpdate> result = new ArrayList<>(
                c.getCount());
        int updated = 0;
        if (c.moveToFirst()) {
            boolean flag;
            do {
                flag = c.getString(3) != null;
                if (flag)
                    updated++;
                result.add(new ContactUpdate(c.getString(0), c
                        .getString(1), "", flag,
                        c.getString(2)));
            } while (c.moveToNext());
        }
        c.close();
        counter.setCounter(updated);
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

        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + CONTACT_PHOTO
                + " FROM " + TB_CONTACT + " WHERE " + tag + " LIKE ? ORDER BY " + tag;
        Cursor c = readableDB.rawQuery(selectQuery, new String[]{"%" + value + "%"});
        ArrayList<ContactID> result = new ArrayList<>(c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactID(c.getString(0), c.getString(1),
                        c.getString(2)));
            } while (c.moveToNext());
        }
        c.close();
        return result;

    }

    public ArrayList<ContactDay> searchBirthdays(String dateRegex) {

        String selectQuery = "SELECT " + CONTACT_ID +
                "," + CONTACT_NAME + "," + CONTACT_PHOTO + "," + CONTACT_BDAY
                + " FROM " + TB_CONTACT + " WHERE " + CONTACT_BDAY + " LIKE ? ORDER BY " + CONTACT_BDAY;
        Cursor c = readableDB.rawQuery(selectQuery, new String[]{dateRegex});
        ArrayList<ContactDay> result = new ArrayList<>(c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactDay(c.getString(0), c.getString(1),
                        c.getString(3), c.getString(2)));
            } while (c.moveToNext());
        }
        c.close();
        return result;

    }

    public ArrayList<ContactDay> searchDates(String date, String type, String selectTag) {
        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + CONTACT_PHOTO + "," + selectTag +
                " FROM " + TB_CONTACT + " INNER OUTER JOIN " + TB_ATTEND + " ON " +
                CONTACT_ID + "=" + ATTEND_ID + " AND " + ATTEND_DAY + " = ?  AND " + ATTEND_TYPE + " = ? GROUP BY " + CONTACT_ID
                + " ORDER BY " + CONTACT_NAME;

        Cursor c = readableDB.rawQuery(selectQuery, new String[]{date, type});
        ArrayList<ContactDay> result = new ArrayList<>(c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactDay(c.getString(0), c.getString(1),
                        c.getString(3), c.getString(2)));
            } while (c.moveToNext());
        }
        c.close();
        return result;

    }

    public ArrayList<ContactStatistics> getContactsAttendanceStatistics(String type) {
        String selectQuery = "SELECT " + CONTACT_ID + "," + CONTACT_NAME + "," + CONTACT_PHOTO + ", MIN(" + ATTEND_DAY + "), " +
                "MAX(" + ATTEND_DAY + "), " + "COUNT(" + ATTEND_DAY + ")" +
                " FROM " + TB_CONTACT + " LEFT OUTER JOIN " + TB_ATTEND + " ON " +
                CONTACT_ID + "=" + ATTEND_ID + " AND " + ATTEND_TYPE + " = ? GROUP BY " + ATTEND_ID + " ORDER BY " + CONTACT_NAME;
        Cursor c = readableDB.rawQuery(selectQuery, new String[]{type});
        ArrayList<ContactStatistics> result = new ArrayList<>(
                c.getCount());

        if (c.moveToFirst()) {
            do {
                result.add(new ContactStatistics(c.getString(0), c
                        .getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5)));
            } while (c.moveToNext());
        }
        c.close();
        return result;

    }

    public String getNameId(String name) {
        String selectQuery = "SELECT " + CONTACT_ID + " FROM " + TB_CONTACT + " WHERE "
                + CONTACT_NAME + "='" + name + "' LIMIT 1";
        Cursor c = readableDB.rawQuery(selectQuery, null);
        if (c.getCount() == 0) {
            c.close();
            return "-1";
        }
        c.moveToFirst();
        String temp = c.getString(0);
        c.close();
        return temp;
    }

    public boolean ImportDayraExcel(String path) {
        String[] colNames = {CONTACT_NAME, CONTACT_CLASS_YEAR, CONTACT_STUDY_WORK, CONTACT_MOB1, CONTACT_MOB2,
                CONTACT_MOB3, CONTACT_LPHONE, CONTACT_EMAIL, CONTACT_SITE, CONTACT_ST, CONTACT_ADDR, CONTACT_NOTES, CONTACT_BDAY,
                CONTACT_PRIEST, CONTACT_LAST_ATTEND, CONTACT_ATTEND_DATES, CONTACT_LAST_VISIT, CONTACT_MAPLAT,
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

    public void divideDayra(Context context, int range, String path) {
        ArrayList<String> sites = getOptionsList(CONTACT_SITE);
        ArrayList<String> classYears = getOptionsList(CONTACT_CLASS_YEAR);
        int classYearsCount = classYears.size();
        range = Math.min(range, classYearsCount);

        int groupsCount = classYearsCount / range;
        if (classYearsCount % range != 0)
            groupsCount++;

        ArrayList<String> queryArr = new ArrayList<>(groupsCount);
        ArrayList<String> Groupnames = new ArrayList<>(groupsCount);
        String temp;
        String tempName;
        int startAt;
        for (int i = 0; i < groupsCount; i++) {
            startAt = range * i;
            temp = CONTACT_CLASS_YEAR + "=" + classYears.get(startAt);
            tempName = classYears.get(startAt);
            for (int j = startAt + 1; j < startAt + range
                    && j < classYearsCount; j++) {
                temp += " OR " + CONTACT_CLASS_YEAR + "='" + classYears.get(j) + "'";
                tempName += " " + classYears.get(j);
            }
            queryArr.add(temp);
            Groupnames.add(tempName);

        }

        TinyDB minidbm;
        int itr;
        for (String site : sites) {
            itr = -1;
            for (String queryItem : queryArr) {
                itr++;
                minidbm = new TinyDB(context, path + DB_NAME + " " + site + " "
                        + Groupnames.get(itr));

                temp = "SELECT * FROM " + TB_CONTACT + " WHERE " + CONTACT_SITE + "='"
                        + site + "' AND (" + queryItem + " )";
                Cursor c = readableDB.rawQuery(temp, null);

                if (c.moveToFirst()) {
                    int COL_MAP_LAT = c.getColumnIndex(CONTACT_MAPLAT);
                    int COL_MAP_LNG = c.getColumnIndex(CONTACT_MAPLNG);
                    int COL_MAP_ZOOM = c.getColumnIndex(CONTACT_MAPZOM);
                    int COL_NAME = c.getColumnIndex(CONTACT_NAME);
                    int COL_ATTEND_DATES = c.getColumnIndex(CONTACT_ATTEND_DATES);
                    int COL_LAST_ATTEND = c.getColumnIndex(CONTACT_LAST_ATTEND);
                    int COL_PIC_DIR = c.getColumnIndex(CONTACT_PHOTO);
                    int COL_PRIEST = c.getColumnIndex(CONTACT_PRIEST);
                    int COL_COMM = c.getColumnIndex(CONTACT_NOTES);
                    int COL_BDAY = c.getColumnIndex(CONTACT_BDAY);
                    int COL_EMAIL = c.getColumnIndex(CONTACT_EMAIL);
                    int COL_MOBILE1 = c.getColumnIndex(CONTACT_MOB1);
                    int COL_MOBILE2 = c.getColumnIndex(CONTACT_MOB2);
                    int COL_MOBILE3 = c.getColumnIndex(CONTACT_MOB3);
                    int COL_LAND_PHONE = c.getColumnIndex(CONTACT_LPHONE);
                    int COL_ADDRESS = c.getColumnIndex(CONTACT_ADDR);
                    int COL_LAST_VISIT = c.getColumnIndex(CONTACT_LAST_VISIT);
                    int COL_CLASS_YEAR = c.getColumnIndex(CONTACT_CLASS_YEAR);
                    int COL_STUDY_WORK = c.getColumnIndex(CONTACT_STUDY_WORK);
                    int COL_STREET = c.getColumnIndex(CONTACT_ST);
                    int COL_SITE = c.getColumnIndex(CONTACT_SITE);
                    ContentValues values;
                    do {
                        values = new ContentValues();
                        values.put(CONTACT_MAPLAT, c.getDouble(COL_MAP_LAT));
                        values.put(CONTACT_MAPLNG, c.getDouble(COL_MAP_LNG));
                        values.put(CONTACT_MAPZOM, c.getFloat(COL_MAP_ZOOM));
                        values.put(CONTACT_NAME, c.getString(COL_NAME));
                        values.put(CONTACT_ATTEND_DATES, c.getString(COL_ATTEND_DATES));
                        values.put(CONTACT_LAST_VISIT, c.getString(COL_LAST_VISIT));
                        values.put(CONTACT_LAST_ATTEND, c.getString(COL_LAST_ATTEND));
                        values.put(CONTACT_PHOTO, c.getString(COL_PIC_DIR));
                        values.put(CONTACT_PRIEST, c.getString(COL_PRIEST));
                        values.put(CONTACT_NOTES, c.getString(COL_COMM));
                        values.put(CONTACT_BDAY, c.getString(COL_BDAY));
                        values.put(CONTACT_EMAIL, c.getString(COL_EMAIL));
                        values.put(CONTACT_MOB1, c.getString(COL_MOBILE1));
                        values.put(CONTACT_MOB2, c.getString(COL_MOBILE2));
                        values.put(CONTACT_MOB3, c.getString(COL_MOBILE3));
                        values.put(CONTACT_LPHONE, c.getString(COL_LAND_PHONE));
                        values.put(CONTACT_ADDR, c.getString(COL_ADDRESS));
                        values.put(CONTACT_ST, c.getString(COL_STREET));
                        values.put(CONTACT_SITE, c.getString(COL_SITE));
                        values.put(CONTACT_STUDY_WORK, c.getString(COL_STUDY_WORK));
                        values.put(CONTACT_CLASS_YEAR, c.getString(COL_CLASS_YEAR));
                        minidbm.addAttendant(values);

                    } while (c.moveToNext());
                }
                c.close();
                minidbm.close();

            }

        }
    }

    public void divideDayra(Context context, String path) {
        ArrayList<String> sites = getOptionsList(CONTACT_SITE);

        TinyDB minidbm;
        String temp;
        for (String site : sites) {
            minidbm = new TinyDB(context, path + DB_NAME + " " + site);
            temp = "SELECT * FROM " + TB_CONTACT + " WHERE " + CONTACT_SITE + "='"
                    + site + "'";
            Cursor c = readableDB.rawQuery(temp, null);
            if (c.moveToFirst()) {
                int COL_MAP_LAT = c.getColumnIndex(CONTACT_MAPLAT);
                int COL_MAP_LNG = c.getColumnIndex(CONTACT_MAPLNG);
                int COL_MAP_ZOOM = c.getColumnIndex(CONTACT_MAPZOM);
                int COL_NAME = c.getColumnIndex(CONTACT_NAME);
                int COL_ATTEND_DATES = c.getColumnIndex(CONTACT_ATTEND_DATES);
                int COL_LAST_ATTEND = c.getColumnIndex(CONTACT_LAST_ATTEND);
                int COL_PIC_DIR = c.getColumnIndex(CONTACT_PHOTO);
                int COL_PRIEST = c.getColumnIndex(CONTACT_PRIEST);
                int COL_COMM = c.getColumnIndex(CONTACT_NOTES);
                int COL_BDAY = c.getColumnIndex(CONTACT_BDAY);
                int COL_EMAIL = c.getColumnIndex(CONTACT_EMAIL);
                int COL_MOBILE1 = c.getColumnIndex(CONTACT_MOB1);
                int COL_MOBILE2 = c.getColumnIndex(CONTACT_MOB2);
                int COL_MOBILE3 = c.getColumnIndex(CONTACT_MOB3);
                int COL_LAND_PHONE = c.getColumnIndex(CONTACT_LPHONE);
                int COL_ADDRESS = c.getColumnIndex(CONTACT_ADDR);
                int COL_LAST_VISIT = c.getColumnIndex(CONTACT_LAST_VISIT);
                int COL_CLASS_YEAR = c.getColumnIndex(CONTACT_CLASS_YEAR);
                int COL_STUDY_WORK = c.getColumnIndex(CONTACT_STUDY_WORK);
                int COL_STREET = c.getColumnIndex(CONTACT_ST);
                int COL_SITE = c.getColumnIndex(CONTACT_SITE);
                ContentValues values;
                do {
                    values = new ContentValues();
                    values.put(CONTACT_MAPLAT, c.getDouble(COL_MAP_LAT));
                    values.put(CONTACT_MAPLNG, c.getDouble(COL_MAP_LNG));
                    values.put(CONTACT_MAPZOM, c.getFloat(COL_MAP_ZOOM));
                    values.put(CONTACT_NAME, c.getString(COL_NAME));
                    values.put(CONTACT_ATTEND_DATES, c.getString(COL_ATTEND_DATES));
                    values.put(CONTACT_LAST_VISIT, c.getString(COL_LAST_VISIT));
                    values.put(CONTACT_LAST_ATTEND, c.getString(COL_LAST_ATTEND));
                    values.put(CONTACT_PHOTO, c.getString(COL_PIC_DIR));
                    values.put(CONTACT_PRIEST, c.getString(COL_PRIEST));
                    values.put(CONTACT_NOTES, c.getString(COL_COMM));
                    values.put(CONTACT_BDAY, c.getString(COL_BDAY));
                    values.put(CONTACT_EMAIL, c.getString(COL_EMAIL));
                    values.put(CONTACT_MOB1, c.getString(COL_MOBILE1));
                    values.put(CONTACT_MOB2, c.getString(COL_MOBILE2));
                    values.put(CONTACT_MOB3, c.getString(COL_MOBILE3));
                    values.put(CONTACT_LPHONE, c.getString(COL_LAND_PHONE));
                    values.put(CONTACT_ADDR, c.getString(COL_ADDRESS));
                    values.put(CONTACT_ST, c.getString(COL_STREET));
                    values.put(CONTACT_SITE, c.getString(COL_SITE));
                    values.put(CONTACT_STUDY_WORK, c.getString(COL_STUDY_WORK));
                    values.put(CONTACT_CLASS_YEAR, c.getString(COL_CLASS_YEAR));
                    minidbm.addAttendant(values);

                } while (c.moveToNext());
            }
            c.close();
            minidbm.close();

        }
    }
}
