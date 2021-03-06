package abanoubm.dayra.main;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.model.GoogleContact;

public class ContactHelper {

    public static boolean insertContact(ContentResolver contactAdder,
                                        String firstName, String mobileNumber, byte[] photo) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                        firstName).build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                        mobileNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        Phone.TYPE_MOBILE).build());

        if (photo != null)
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photo)
                    .build());


        try {
            contactAdder.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static ArrayList<GoogleContact> getGContacts(
            ContentResolver contactHelper, Context context) {

        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        ArrayList<GoogleContact> result = new ArrayList<>();
        try {
            Cursor c = contactHelper.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection, null, null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                            + " ASC");

            if (c.moveToFirst()) {
                DB db = DB.getInstant(context);
                int colNAME = c.getColumnIndex(projection[0]);
                int colNUMBER = c.getColumnIndex(projection[1]);
                do {
                    result.add(new GoogleContact(c.getString(colNAME), c
                            .getString(colNUMBER), !db.getNameId(c
                            .getString(colNAME)).equals("-1")));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
        }
        return result;
    }

    public static boolean doesGContactMobileExist(
            ContentResolver contactHelper, String mobile) {

        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        boolean check = false;
        try {
            Cursor c = contactHelper.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection, ContactsContract.CommonDataKinds.Phone.NUMBER + " = ? ", new String[]{mobile},
                    null);
            check = c.moveToFirst();
            c.close();
        } catch (Exception e) {
        }
        return check;
    }


}
