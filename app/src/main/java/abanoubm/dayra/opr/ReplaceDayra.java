package abanoubm.dayra.opr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.UpdaterDB;
import abanoubm.dayra.main.Utility;

public class ReplaceDayra extends Activity {
    private static final int UPDATE_DB = 1;
    private TextView updateBtn, browseBtn;
    private String extr_path, extr_dbname;

    private CheckBox cb_selectall, cb_mobile1, cb_mobile2, cb_mobile3,
            cb_lphone, cb_addr, cb_lattend, cb_bday, cb_priest, cb_comm,
            cb_email, cb_lvisit, cb_study_work, cb_class_year, cb_site,
            cb_street, cb_dates;

    private ArrayList<String> dataTag;

    private class UpdateDBTask extends AsyncTask<Void, Void, Integer> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(ReplaceDayra.this);
            pBar.setMessage(getResources().getString(R.string.label_loading));
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

            UpdaterDB updater = new UpdaterDB(getApplicationContext(),
                    extr_dbname, extr_path);
            if (updater.checkDB()) {
                DB.getInstance(
                        getApplicationContext(),
                        getSharedPreferences("login", Context.MODE_PRIVATE)
                                .getString("dbname", "")).externalUpdater(
                        updater.getAttendantsData(), dataTag);
                updater.close();
                return R.string.msg_dayra_replaced;
            } else
                return R.string.err_msg_invalid_file;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_replace_dayra);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2))
                .setText(R.string.subhead_dayra_update);

        updateBtn = (TextView) findViewById(R.id.update_btn);
        browseBtn = (TextView) findViewById(R.id.browsebtn);

        cb_selectall = (CheckBox) findViewById(R.id.cb_selectall);

        cb_mobile1 = (CheckBox) findViewById(R.id.cb_mobile1);
        cb_mobile2 = (CheckBox) findViewById(R.id.cb_mobile2);
        cb_mobile3 = (CheckBox) findViewById(R.id.cb_mobile3);
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

        cb_selectall.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                cb_mobile1.setChecked(isChecked);
                cb_mobile2.setChecked(isChecked);
                cb_mobile3.setChecked(isChecked);
                cb_lphone.setChecked(isChecked);
                cb_addr.setChecked(isChecked);
                cb_lattend.setChecked(isChecked);
                cb_bday.setChecked(isChecked);
                cb_priest.setChecked(isChecked);
                cb_comm.setChecked(isChecked);
                cb_email.setChecked(isChecked);
                cb_lvisit.setChecked(isChecked);
                cb_study_work.setChecked(isChecked);
                cb_class_year.setChecked(isChecked);
                cb_street.setChecked(isChecked);
                cb_site.setChecked(isChecked);
                cb_dates.setChecked(isChecked);
            }
        });
        cb_dates.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                cb_lattend.setChecked(isChecked);
            }
        });
        cb_lattend.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                cb_dates.setChecked(isChecked);
            }
        });
        updateBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (extr_path != null && extr_dbname != null) {
                    dataTag = new ArrayList<>();

                    if (cb_mobile1.isChecked()) {
                        dataTag.add(DB.CONTACT_MOB1);

                    }
                    if (cb_class_year.isChecked()) {
                        dataTag.add(DB.CONTACT_CLASS_YEAR);

                    }
                    if (cb_study_work.isChecked()) {
                        dataTag.add(DB.CONTACT_STUDY_WORK);

                    }
                    if (cb_lattend.isChecked()) {
                        dataTag.add(DB.CONTACT_LAST_ATTEND);

                    }
                    if (cb_lvisit.isChecked()) {
                        dataTag.add(DB.CONTACT_LAST_VISIT);

                    }

                    if (cb_site.isChecked()) {
                        dataTag.add(DB.CONTACT_SITE);

                    }
                    if (cb_street.isChecked()) {
                        dataTag.add(DB.CONTACT_ST);

                    }
                    if (cb_addr.isChecked()) {
                        dataTag.add(DB.CONTACT_ADDR);

                    }
                    if (cb_mobile2.isChecked()) {
                        dataTag.add(DB.CONTACT_MOB2);

                    }
                    if (cb_mobile3.isChecked()) {
                        dataTag.add(DB.CONTACT_MOB3);

                    }
                    if (cb_lphone.isChecked()) {
                        dataTag.add(DB.CONTACT_LPHONE);

                    }

                    if (cb_email.isChecked()) {
                        dataTag.add(DB.CONTACT_EMAIL);

                    }
                    if (cb_comm.isChecked()) {
                        dataTag.add(DB.CONTACT_NOTES);

                    }
                    if (cb_bday.isChecked()) {
                        dataTag.add(DB.CONTACT_BDAY);

                    }

                    if (cb_priest.isChecked()) {
                        dataTag.add(DB.CONTACT_PRIEST);

                    }
                    if (cb_dates.isChecked()) {
                        dataTag.add(DB.CONTACT_ATTEND_DATES);
                    }

                    if (dataTag.size() == 0)
                        Toast.makeText(getApplicationContext(),
                                R.string.msg_choose_field, Toast.LENGTH_SHORT)
                                .show();
                    else
                        new UpdateDBTask().execute();
                } else
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_browse, Toast.LENGTH_SHORT).show();

            }
        });
        browseBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intentImport = new Intent(Intent.ACTION_GET_CONTENT);
                intentImport.setType("file/*");
                PackageManager manager = getApplicationContext()
                        .getPackageManager();
                List<ResolveInfo> infos = manager.queryIntentActivities(
                        intentImport, 0);

                if (infos.size() > 0) {
                    startActivityForResult(intentImport, UPDATE_DB);
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_explorer, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == UPDATE_DB) {
                Uri uri = data.getData();
                if (uri != null && new File(uri.getPath()).exists()) {
                    String path = uri.getPath();
                    String dbname = path.substring(path.lastIndexOf("/") + 1);
                    if (!Utility.isDBName(dbname)) {
                        Toast.makeText(getApplicationContext(),
                                R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        extr_dbname = dbname;
                        extr_path = path;
                    }

                }

            }
        }
    }
}
