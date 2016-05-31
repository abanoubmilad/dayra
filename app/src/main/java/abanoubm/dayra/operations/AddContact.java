package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import abanoubm.dayra.R;
import abanoubm.dayra.display.ContactMap;
import abanoubm.dayra.display.DisplayContact;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactData;

public class AddContact extends Activity {
    private static final int TAKE_IMG = 2;
    private static final int BROWSE_IMG = 1;
    private static final int MAP_ADD = 3;
    private Uri fileUri;

    private EditText edit_name, edit_address, edit_bday, edit_comm, edit_email,
            edit_lphone, edit_mobile1, edit_mobile2, edit_mobile3, edit_priest,
            edit_class_year, edit_study_work, edit_street, edit_site,
            edit_last_visit;

    private ImageView img, spin_site, spin_class_year, spin_priest,
            spin_street, spin_study_work;
    private String imgPath = "";
    private TextView mapbtn;
    private double lon = 0, lat = 0;
    private float zoom = 0;

    private class GetStudyWorkTask extends
            AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(AddContact.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            return DB.getInstant(getApplicationContext()).getStudyWork();
        }

        @Override
        protected void onPostExecute(final ArrayList<String> result) {
            pBar.dismiss();
            if (result.size() == 0) {
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_available_entries, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    AddContact.this);
            builder.setTitle(getResources().getString(
                    R.string.label_choose_study_work));
            builder.setItems(
                    result.toArray(new String[result.size()]),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            edit_study_work.setText(result.get(which));

                        }

                    });
            builder.create().show();
        }
    }

    private class GetSitesTask extends AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(AddContact.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            return DB.getInstant(getApplicationContext()).getSites();
        }

        @Override
        protected void onPostExecute(final ArrayList<String> result) {
            pBar.dismiss();
            if (result.size() == 0) {
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_available_entries, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    AddContact.this);
            builder.setTitle(getResources().getString(
                    R.string.label_choose_site));
            builder.setItems(
                    result.toArray(new String[result.size()]),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            edit_site.setText(result.get(which));

                        }

                    });
            builder.create().show();
        }
    }

    private class GetStreetsTask extends
            AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(AddContact.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            return DB.getInstant(getApplicationContext()).getStreets();
        }

        @Override
        protected void onPostExecute(final ArrayList<String> result) {
            pBar.dismiss();
            if (result.size() == 0) {
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_available_entries, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    AddContact.this);
            builder.setTitle(getResources().getString(
                    R.string.label_choose_street));
            builder.setItems(
                    result.toArray(new String[result.size()]),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            edit_street.setText(result.get(which));

                        }

                    });
            builder.create().show();
        }
    }

    private class GetPriestsTask extends
            AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(AddContact.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            return DB.getInstant(getApplicationContext()).getConnFathers();
        }

        @Override
        protected void onPostExecute(final ArrayList<String> result) {
            pBar.dismiss();
            if (result.size() == 0) {
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_available_entries, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    AddContact.this);
            builder.setTitle(getResources().getString(
                    R.string.label_choose_priest));
            builder.setItems(
                    result.toArray(new String[result.size()]),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            edit_priest.setText(result.get(which));

                        }

                    });
            builder.create().show();
        }
    }

    private class GetClassYearsTask extends
            AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(AddContact.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            return DB.getInstant(getApplicationContext()).getClassYears();
        }

        @Override
        protected void onPostExecute(final ArrayList<String> result) {
            pBar.dismiss();
            if (result.size() == 0) {
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_available_entries, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    AddContact.this);
            builder.setTitle(getResources().getString(
                    R.string.label_choose_class_year));
            builder.setItems(
                    result.toArray(new String[result.size()]),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            edit_class_year.setText(result.get(which));

                        }

                    });
            builder.create().show();
        }
    }

    private class AddTask extends AsyncTask<Void, Void, Integer> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(AddContact.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Integer result) {
            pBar.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected Integer doInBackground(Void... params) {
            int msgSource;
            String name_str = edit_name.getText().toString().trim(), email_str = edit_email
                    .getText().toString().trim(), lphone_str = edit_lphone
                    .getText().toString().trim(), mobile_str1 = edit_mobile1
                    .getText().toString().trim(), mobile_str2 = edit_mobile2
                    .getText().toString().trim(), mobile_str3 = edit_mobile3
                    .getText().toString().trim(), class_year_str = edit_class_year
                    .getText().toString().trim(), site_str = edit_site
                    .getText().toString().trim();

            DB dbm = DB.getInstant(getApplicationContext());
            if (!Utility.isName(name_str)) {
                msgSource = R.string.err_msg_invalid_name;
            } else if (!dbm.getNameId(name_str).equals("-1")) {
                msgSource = R.string.err_msg_duplicate_name;
            } else if (email_str.length() != 0 && !Utility.isEmail(email_str)) {
                msgSource = R.string.err_msg_email;
            } else if (site_str.length() != 0 && !Utility.isSiteName(site_str)) {
                msgSource = R.string.err_msg_site;
            } else {

                ContactData att = new ContactData("-1", name_str, imgPath, lat,
                        lon, zoom, "", "", edit_priest.getText().toString()
                        .trim(), edit_comm.getText().toString().trim(),
                        edit_bday.getText().toString().trim(), email_str,
                        mobile_str1, mobile_str2, mobile_str3, lphone_str,
                        edit_address.getText().toString().trim(),
                        edit_last_visit.getText().toString().trim(),
                        class_year_str, edit_study_work.getText().toString()
                        .trim(), edit_street.getText().toString()
                        .trim(), site_str);

                Intent intent = new Intent(getApplicationContext(),
                        DisplayContact.class);
                intent.putExtra("id", dbm.addAttendant(att));
                startActivity(intent);
                finish();
                msgSource = R.string.msg_added;

            }
            return msgSource;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_contact);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_add_contact);

        String name = getIntent().getStringExtra("name");
        if (name == null)
            name = "";

        final DatePickerDialog picker_bday, picker_lastvisit;
        Calendar cal = Calendar.getInstance();
        picker_bday = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                edit_bday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-"
                        + year);
            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        findViewById(R.id.pick_bday)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        picker_bday.show();

                    }
                });

        picker_lastvisit = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                edit_last_visit.setText(dayOfMonth + "-" + (monthOfYear + 1)
                        + "-" + year);
            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        findViewById(R.id.pick_lvisit)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        picker_lastvisit.show();

                    }
                });

        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_name.setText(name);
        edit_address = (EditText) findViewById(R.id.edit_address);
        edit_comm = (EditText) findViewById(R.id.edit_comm);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_lphone = (EditText) findViewById(R.id.edit_lphone);

        edit_mobile1 = (EditText) findViewById(R.id.edit_mobile1);
        edit_mobile2 = (EditText) findViewById(R.id.edit_mobile2);
        edit_mobile3 = (EditText) findViewById(R.id.edit_mobile3);

        edit_priest = (EditText) findViewById(R.id.edit_priest);

        edit_bday = (EditText) findViewById(R.id.edit_bday);
        edit_last_visit = (EditText) findViewById(R.id.edit_lvisit);

        edit_class_year = (EditText) findViewById(R.id.edit_class_year);
        edit_study_work = (EditText) findViewById(R.id.edit_study_work);
        edit_street = (EditText) findViewById(R.id.edit_street);
        edit_site = (EditText) findViewById(R.id.edit_site);

        spin_class_year = (ImageView) findViewById(R.id.spin_class_year);
        spin_site = (ImageView) findViewById(R.id.spin_site);
        spin_priest = (ImageView) findViewById(R.id.spin_priest);
        spin_street = (ImageView) findViewById(R.id.spin_street);
        spin_study_work = (ImageView) findViewById(R.id.spin_study_work);

        mapbtn = (TextView) findViewById(R.id.map_btn);

        mapbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ContactMap.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                intent.putExtra("zoom", zoom);
                startActivityForResult(intent, MAP_ADD);
            }
        });

        img = (ImageView) findViewById(R.id.pic_view);
        img.setImageResource(R.mipmap.def);

        findViewById(R.id.back)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        findViewById(R.id.add)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new AddTask().execute();
                    }
                });
        img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                editImage();
            }
        });
        spin_site.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new GetSitesTask().execute();
            }
        });
        spin_class_year.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new GetClassYearsTask().execute();

            }
        });
        spin_priest.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new GetPriestsTask().execute();

            }
        });
        spin_street.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new GetStreetsTask().execute();

            }
        });
        spin_study_work.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new GetStudyWorkTask().execute();

            }
        });
    }

    private void editImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddContact.this);
        builder.setTitle(R.string.label_choose_photo);
        builder.setItems(
                getResources().getStringArray(
                        R.array.photo_menu),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {

                            Intent galleryIntent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, BROWSE_IMG);
                        } else if (which == 1) {
                            captureImage();

                        } else {
                            imgPath = "";
                            img.setImageResource(R.mipmap.def);

                        }
                    }

                });
        builder.create().show();

    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String path;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/";
        } else {
            path = android.os.Environment.getDataDirectory().getAbsolutePath()
                    + "/";
        }

        path += "img_"
                + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date()) + ".jpg";
        fileUri = Uri.fromFile(new File(path));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, TAKE_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_IMG) {
                imgPath = fileUri.getPath();

                img.setImageBitmap(ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeFile(imgPath), 250, 250));

            } else if (requestCode == BROWSE_IMG) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();

                img.setImageBitmap(ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeFile(imgPath), 250, 250));

            } else if (requestCode == MAP_ADD) {
                lon = data.getDoubleExtra("lon", lon);
                lat = data.getDoubleExtra("lat", lat);
                zoom = data.getFloatExtra("zoom", zoom);
            }
        }
    }

}
