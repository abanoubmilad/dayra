package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.LayoutInflater;
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

public class EditContact extends Activity {
    private EditText edit_name, edit_address, edit_bday, edit_comm, edit_email,
            edit_lphone, edit_mobile1, edit_mobile2, edit_mobile3, edit_priest,
            edit_class_year, edit_study_work, edit_street, edit_site,
            edit_last_visit;

    private ImageView img;
    private TextView dis_last_attend;

    private ContactData attData;

    private static final int TAKE_IMG = 2;
    private static final int BROWSE_IMG = 1;
    private static final int MAP_OPR = 3;

    private Uri fileUri;
    private String imgPath = "";

    private class EditTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pBar;
        private int msgSource;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(EditContact.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {

            pBar.dismiss();
            Toast.makeText(getApplicationContext(), msgSource,
                    Toast.LENGTH_SHORT).show();
            if (result) {
                finish();
                startActivity(new Intent(getApplicationContext(),
                        DisplayContact.class).putExtra("id", attData.getId()));
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String name_str = edit_name.getText().toString().trim(), email_str = edit_email
                    .getText().toString().trim(), lphone_str = edit_lphone
                    .getText().toString().trim(), mobile_str1 = edit_mobile1
                    .getText().toString().trim(), mobile_str2 = edit_mobile2
                    .getText().toString().trim(), mobile_str3 = edit_mobile3
                    .getText().toString().trim(), class_year_str = edit_class_year
                    .getText().toString().trim(), site_str = edit_site
                    .getText().toString().trim();

            DB dbm = DB.getInstance(getApplicationContext(),
                    getSharedPreferences("login", Context.MODE_PRIVATE)
                            .getString("dbname", ""));
            String check = dbm.getNameId(name_str);
            if (!Utility.isName(name_str)) {
                msgSource = R.string.err_msg_invalid_name;
            } else if (!check.equals("-1") && !check.equals(attData.getId())) {
                msgSource = R.string.err_msg_duplicate_name;
            } else if (email_str.length() != 0 && !Utility.isEmail(email_str)) {
                msgSource = R.string.err_msg_email;
            } else if (site_str.length() != 0 && !Utility.isSiteName(site_str)) {
                msgSource = R.string.err_msg_site;
            } else {

                attData = new ContactData(attData.getId(), name_str, imgPath,
                        attData.getMapLat(), attData.getMapLng(),
                        attData.getMapZoom(), attData.getAttendDates(),
                        attData.getLastAttend(), edit_priest.getText()
                        .toString().trim(), edit_comm.getText()
                        .toString().trim(), edit_bday.getText()
                        .toString().trim(), email_str, mobile_str1,
                        mobile_str2, mobile_str3, lphone_str, edit_address
                        .getText().toString().trim(), edit_last_visit
                        .getText().toString().trim(), class_year_str,
                        edit_study_work.getText().toString().trim(),
                        edit_street.getText().toString().trim(), site_str);

                dbm.updateAttendant(attData);
                msgSource = R.string.msg_updated;
                return true;
            }
            return false;

        }
    }

    private class GetStreetsTask extends
            AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(EditContact.this);
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
                    EditContact.this);
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
            pBar = new ProgressDialog(EditContact.this);
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
                    EditContact.this);
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

    private class GetSitesTask extends AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(EditContact.this);
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
                    EditContact.this);
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

    private class GetStudyWorkTask extends
            AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(EditContact.this);
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
                    EditContact.this);
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

    private class GetClassYearsTask extends
            AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(EditContact.this);
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
                    EditContact.this);
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

    private class GetTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(EditContact.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            setFields();
            pBar.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            attData = DB.getInstant(getApplicationContext()).getAttendantData(
                    getIntent().getIntExtra("id", -1));
            imgPath = attData.getPicDir();
            return null;
        }
    }

    private class DeleteTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(EditContact.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            finish();
            pBar.dismiss();

        }

        @Override
        protected Void doInBackground(Void... params) {
            DB.getInstant(getApplicationContext()).deleteAttendant(
                    attData.getId());
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_contact);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2))
                .setText(R.string.subhead_edit_contact);

        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_address = (EditText) findViewById(R.id.edit_address);

        edit_bday = (EditText) findViewById(R.id.edit_bday);
        edit_last_visit = (EditText) findViewById(R.id.edit_lvisit);

        edit_comm = (EditText) findViewById(R.id.edit_comm);
        edit_email = (EditText) findViewById(R.id.edit_email);

        dis_last_attend = (TextView) findViewById(R.id.dis_last_attend);

        edit_lphone = (EditText) findViewById(R.id.edit_lphone);
        edit_mobile1 = (EditText) findViewById(R.id.edit_mobile1);
        edit_mobile2 = (EditText) findViewById(R.id.edit_mobile2);
        edit_mobile3 = (EditText) findViewById(R.id.edit_mobile3);

        edit_priest = (EditText) findViewById(R.id.edit_priest);
        img = (ImageView) findViewById(R.id.pic_view);

        edit_class_year = (EditText) findViewById(R.id.edit_class_year);
        edit_study_work = (EditText) findViewById(R.id.edit_study_work);
        edit_street = (EditText) findViewById(R.id.edit_street);
        edit_site = (EditText) findViewById(R.id.edit_site);

        new GetTask().execute();

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

        findViewById(R.id.map_btn)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),
                                ContactMap.class);
                        intent.putExtra("lon", attData.getMapLng());
                        intent.putExtra("lat", attData.getMapLat());
                        intent.putExtra("zoom", attData.getMapZoom());
                        intent.putExtra("readonly", false);
                        startActivityForResult(intent, MAP_OPR);
                    }
                });

        img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                editImage();
            }
        });
        findViewById(R.id.back)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                        startActivity(new Intent(getApplicationContext(),
                                DisplayContact.class).putExtra("id",
                                attData.getId()));
                    }
                });
        findViewById(R.id.save)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new EditTask().execute();
                    }
                });

        findViewById(R.id.delete)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        confirmDelete();

                    }
                });

        findViewById(R.id.spin_site)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new GetSitesTask().execute();
                    }
                });

        findViewById(R.id.spin_street)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new GetStreetsTask().execute();
                    }
                });
        findViewById(R.id.spin_class_year)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new GetClassYearsTask().execute();

                    }
                });
        findViewById(R.id.spin_priest)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new GetPriestsTask().execute();

                    }
                });
        findViewById(R.id.spin_study_work)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new GetStudyWorkTask().execute();

                    }
                });
        findViewById(R.id.btn_connections)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getApplicationContext(),
                                EditConnections.class).putExtra("id",
                                attData.getId()).putExtra("name",
                                attData.getName()));

                    }
                });
    }

    private void setFields() {

        edit_name.setText(attData.getName());
        edit_address.setText(attData.getAddress());
        edit_comm.setText(attData.getComm());
        edit_email.setText(attData.getEmail());
        edit_lphone.setText(attData.getLandPhone());

        edit_mobile1.setText(attData.getMobile1());
        edit_mobile2.setText(attData.getMobile2());
        edit_mobile3.setText(attData.getMobile3());

        edit_priest.setText(attData.getPriest());

        edit_class_year.setText(attData.getClassYear());
        edit_study_work.setText(attData.getStudyWork());
        edit_street.setText(attData.getStreet());
        edit_site.setText(attData.getSite());

        dis_last_attend.setText(attData.getLastAttend());

        edit_bday.setText(attData.getBirthDay());

        edit_last_visit.setText(attData.getLastVisit());

        if (attData.getPicDir().length() == 0
                || !new File(attData.getPicDir()).exists())
            img.setImageResource(R.mipmap.def);
        else {

            img.setImageBitmap(ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(attData.getPicDir()), 250, 250));
        }

    }

    private void editImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditContact.this);
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

            } else if (requestCode == MAP_OPR) {
                attData.setMapLng(data.getDoubleExtra("lon",
                        attData.getMapLng()));
                attData.setMapLat(data.getDoubleExtra("lat",
                        attData.getMapLat()));
                attData.setMapZoom(data.getFloatExtra("zoom",
                        attData.getMapZoom()));
            }
        }
    }

    private void confirmDelete() {
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View chooseView = li.inflate(R.layout.delete_attendant, null);
        final AlertDialog ad = new AlertDialog.Builder(EditContact.this)
                .setCancelable(true).create();
        ad.setView(chooseView, 0, 0, 0, 0);
        ad.show();

        chooseView.findViewById(R.id.back)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.cancel();
                    }
                });
        chooseView.findViewById(R.id.del)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DeleteTask().execute();
                        ad.cancel();
                    }
                });
    }

}