package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;

public class ExportContacts extends Activity {

    private ArrayList<String> dataHeader;
    private ArrayList<String> dataTag;

    private class ExportTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(ExportContacts.this);
            pBar.setCancelable(false);
            pBar.setMessage(getResources().getString(R.string.label_loading));
            pBar.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String path;
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED)) {
                path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/";
            } else {
                path = android.os.Environment.getDataDirectory()
                        .getAbsolutePath() + "/";
            }
            path += "dayra folder";
            new File(path).mkdirs();
            path += "/";


            path += "dayra_"
                    + getSharedPreferences("login", Context.MODE_PRIVATE)
                    .getString("dbname", "data") + ".pdf";
            if (android.os.Build.VERSION.SDK_INT >= 8) {

                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{path}, null, null);
            }
            return DB.getInstant(getApplicationContext()).exportContactsPdf(
                    dataTag, dataHeader, path);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            pBar.dismiss();
            if (result)

                Toast.makeText(getApplicationContext(), R.string.msg_exported,
                        Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),
                        R.string.err_msg_export, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_export_contacts);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2))
                .setText(R.string.subhead_export_pdf);


        final TextView exportBtn;
        final CheckBox cb_numbering, cb_name, cb_mobile1, cb_mobile2, cb_mobile3,
                cb_lphone, cb_addr, cb_lattend, cb_bday, cb_priest, cb_comm,
                cb_email, cb_lvisit, cb_study_work, cb_class_year, cb_site,
                cb_street, cb_dates;


        exportBtn = (TextView) findViewById(R.id.export_btn);

        cb_name = (CheckBox) findViewById(R.id.cb_name);
        cb_mobile1 = (CheckBox) findViewById(R.id.cb_mobile1);
        cb_mobile2 = (CheckBox) findViewById(R.id.cb_mobile2);
        cb_mobile3 = (CheckBox) findViewById(R.id.cb_mobile3);
        cb_numbering = (CheckBox) findViewById(R.id.cb_numbering);
        cb_lphone = (CheckBox) findViewById(R.id.cb_lphone);
        cb_addr = (CheckBox) findViewById(R.id.cb_addr);
        cb_lattend = (CheckBox) findViewById(R.id.cb_lattend);
        cb_bday = (CheckBox) findViewById(R.id.cb_bday);
        cb_priest = (CheckBox) findViewById(R.id.cb_priest);
        cb_comm = (CheckBox) findViewById(R.id.cb_comm);
        cb_email = (CheckBox) findViewById(R.id.cb_email);
        cb_lvisit = (CheckBox) findViewById(R.id.cb_lvisit);
        cb_study_work = (CheckBox) findViewById(R.id.cb_study_work);
        cb_class_year = (CheckBox) findViewById(R.id.cb_classyear);
        cb_street = (CheckBox) findViewById(R.id.cb_street);
        cb_site = (CheckBox) findViewById(R.id.cb_site);
        cb_dates = (CheckBox) findViewById(R.id.cb_dates);

        exportBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Resources temp = getResources();
                dataTag = new ArrayList<>();
                dataHeader = new ArrayList<>();
                if (cb_numbering.isChecked()) {
                    dataTag.add(" ");
                    dataHeader.add(" ");
                }
                if (cb_name.isChecked()) {
                    dataTag.add(DB.CONTACT_NAME);
                    dataHeader.add(temp.getString(R.string.label_name));
                }
                if (cb_mobile1.isChecked()) {
                    dataTag.add(DB.CONTACT_MOB1);
                    dataHeader.add(temp.getString(R.string.label_mobile1));

                }
                if (cb_class_year.isChecked()) {
                    dataTag.add(DB.CONTACT_CLASS_YEAR);
                    dataHeader.add(temp.getString(R.string.label_class_year));

                }
                if (cb_study_work.isChecked()) {
                    dataTag.add(DB.CONTACT_STUDY_WORK);
                    dataHeader.add(temp.getString(R.string.label_study_work));

                }
                if (cb_lattend.isChecked()) {
                    dataTag.add(DB.CONTACT_LAST_ATTEND);
                    dataHeader.add(temp
                            .getString(R.string.label_last_attendance));

                }
                if (cb_lvisit.isChecked()) {
                    dataTag.add(DB.CONTACT_LAST_VISIT);
                    dataHeader.add(temp.getString(R.string.label_last_visit));

                }

                if (cb_site.isChecked()) {
                    dataTag.add(DB.CONTACT_SITE);
                    dataHeader.add(temp.getString(R.string.label_site));

                }
                if (cb_street.isChecked()) {
                    dataTag.add(DB.CONTACT_ST);
                    dataHeader.add(temp.getString(R.string.label_street));

                }
                if (cb_addr.isChecked()) {
                    dataTag.add(DB.CONTACT_ADDR);
                    dataHeader.add(temp.getString(R.string.label_address));

                }
                if (cb_mobile2.isChecked()) {
                    dataTag.add(DB.CONTACT_MOB2);
                    dataHeader.add(temp.getString(R.string.label_mobile2));

                }
                if (cb_mobile3.isChecked()) {
                    dataTag.add(DB.CONTACT_MOB3);
                    dataHeader.add(temp.getString(R.string.label_mobile3));

                }
                if (cb_lphone.isChecked()) {
                    dataTag.add(DB.CONTACT_LPHONE);
                    dataHeader.add(temp.getString(R.string.label_lphone));

                }

                if (cb_email.isChecked()) {
                    dataTag.add(DB.CONTACT_EMAIL);
                    dataHeader.add(temp.getString(R.string.label_email));

                }
                if (cb_comm.isChecked()) {
                    dataTag.add(DB.CONTACT_NOTES);
                    dataHeader.add(temp.getString(R.string.label_notes));

                }
                if (cb_bday.isChecked()) {
                    dataTag.add(DB.CONTACT_BDAY);
                    dataHeader.add(temp.getString(R.string.label_bday));

                }

                if (cb_priest.isChecked()) {
                    dataTag.add(DB.CONTACT_PRIEST);
                    dataHeader.add(temp.getString(R.string.label_conf_father));

                }
                if (cb_dates.isChecked()) {
                    dataTag.add(DB.CONTACT_ATTEND_DATES);
                    dataHeader.add(temp
                            .getString(R.string.label_attendance_dates));

                }

                if (dataTag.size() == 0)
                    Toast.makeText(getApplicationContext(),
                            R.string.msg_choose_field, Toast.LENGTH_SHORT)
                            .show();
                else
                    new ExportTask().execute();

            }

        });
    }
}
