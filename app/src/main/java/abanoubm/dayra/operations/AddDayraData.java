package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.DBUpdater;
import abanoubm.dayra.main.Utility;

public class AddDayraData extends Activity {
    private static final int IMPORT_FILE = 1;
    private static final int IMPORT_EXCEL = 2;

    private String extr_path, extr_dbname;
    private ArrayList<String> dataTag;

    private class AddDayraFileTask extends AsyncTask<Void, Void, Integer> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(AddDayraData.this);
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

            DBUpdater updater = new DBUpdater(getApplicationContext(),
                    extr_dbname, extr_path);
            if (updater.checkDB()) {
                DB.getInstant(getApplicationContext()).externalUpdater(
                        updater.getAttendantsData(), dataTag);
                updater.close();
                return R.string.msg_dayra_replaced;
            } else
                return R.string.err_msg_invalid_file;

        }
    }

    private class AddDayraExcelTask extends AsyncTask<String, Void, Integer> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(AddDayraData.this);
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
        protected Integer doInBackground(String... params) {

            if (DB.getInstant(getApplicationContext()).AddDayraExcel(extr_path))
                return R.string.msg_dayra_imported;
            return R.string.err_msg_invalid_file;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_dayra_data);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2))
                .setText(R.string.subhead_add_data);


        final CheckBox selectall, mobile1, mobile2, mobile3,
                lphone, addr, bday, supervisor, comm,
                email, study_work, class_year, site,
                street;
        selectall = (CheckBox) findViewById(R.id.selectall);

        mobile1 = (CheckBox) findViewById(R.id.mobile1);
        mobile2 = (CheckBox) findViewById(R.id.mobile2);
        mobile3 = (CheckBox) findViewById(R.id.mobile3);
        lphone = (CheckBox) findViewById(R.id.lphone);
        addr = (CheckBox) findViewById(R.id.addr);
        bday = (CheckBox) findViewById(R.id.bday);
        supervisor = (CheckBox) findViewById(R.id.supervisor);
        comm = (CheckBox) findViewById(R.id.comm);
        email = (CheckBox) findViewById(R.id.email);
        study_work = (CheckBox) findViewById(R.id.study_work);
        class_year = (CheckBox) findViewById(R.id.classyear);
        street = (CheckBox) findViewById(R.id.street);
        site = (CheckBox) findViewById(R.id.site);

        selectall.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mobile1.setChecked(isChecked);
                mobile2.setChecked(isChecked);
                mobile3.setChecked(isChecked);
                lphone.setChecked(isChecked);
                addr.setChecked(isChecked);
                bday.setChecked(isChecked);
                supervisor.setChecked(isChecked);
                comm.setChecked(isChecked);
                email.setChecked(isChecked);
                study_work.setChecked(isChecked);
                class_year.setChecked(isChecked);
                street.setChecked(isChecked);
                site.setChecked(isChecked);
            }
        });

        findViewById(R.id.btn_file).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dataTag = new ArrayList<>();
                if (mobile1.isChecked())
                    dataTag.add(DB.CONTACT_MOB1);
                if (class_year.isChecked())
                    dataTag.add(DB.CONTACT_CLASS_YEAR);
                if (study_work.isChecked())
                    dataTag.add(DB.CONTACT_STUDY_WORK);
                if (site.isChecked())
                    dataTag.add(DB.CONTACT_SITE);
                if (street.isChecked())
                    dataTag.add(DB.CONTACT_ST);
                if (addr.isChecked())
                    dataTag.add(DB.CONTACT_ADDR);
                if (mobile2.isChecked())
                    dataTag.add(DB.CONTACT_MOB2);
                if (mobile3.isChecked())
                    dataTag.add(DB.CONTACT_MOB3);
                if (lphone.isChecked())
                    dataTag.add(DB.CONTACT_LPHONE);
                if (email.isChecked())
                    dataTag.add(DB.CONTACT_EMAIL);
                if (comm.isChecked())
                    dataTag.add(DB.CONTACT_NOTES);
                if (bday.isChecked())
                    dataTag.add(DB.CONTACT_BDAY);
                if (supervisor.isChecked())
                    dataTag.add(DB.CONTACT_SUPERVISOR);

                Intent intentImport = new Intent(Intent.ACTION_GET_CONTENT);
                intentImport.setDataAndType(Uri.fromFile(new File(Utility.getDayraFolder())),
                        "application/octet-stream");

                if (getApplicationContext().getPackageManager()
                        .queryIntentActivities(intentImport, 0).size() > 0) {
                    startActivityForResult(intentImport, IMPORT_FILE);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.err_msg_explorer,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.btn_xls).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dataTag = new ArrayList<>();
                if (mobile1.isChecked())
                    dataTag.add(DB.CONTACT_MOB1);
                if (class_year.isChecked())
                    dataTag.add(DB.CONTACT_CLASS_YEAR);
                if (study_work.isChecked())
                    dataTag.add(DB.CONTACT_STUDY_WORK);
                if (site.isChecked())
                    dataTag.add(DB.CONTACT_SITE);
                if (street.isChecked())
                    dataTag.add(DB.CONTACT_ST);
                if (addr.isChecked())
                    dataTag.add(DB.CONTACT_ADDR);
                if (mobile2.isChecked())
                    dataTag.add(DB.CONTACT_MOB2);
                if (mobile3.isChecked())
                    dataTag.add(DB.CONTACT_MOB3);
                if (lphone.isChecked())
                    dataTag.add(DB.CONTACT_LPHONE);
                if (email.isChecked())
                    dataTag.add(DB.CONTACT_EMAIL);
                if (comm.isChecked())
                    dataTag.add(DB.CONTACT_NOTES);
                if (bday.isChecked())
                    dataTag.add(DB.CONTACT_BDAY);
                if (supervisor.isChecked())
                    dataTag.add(DB.CONTACT_SUPERVISOR);
                ;

                Intent intentImport = new Intent(Intent.ACTION_GET_CONTENT);
                intentImport.setDataAndType(Uri.fromFile(new File(Utility.getDayraFolder())),
                        "application/vnd.ms-excel");

                if (getApplicationContext().getPackageManager()
                        .queryIntentActivities(intentImport, 0).size() > 0) {
                    startActivityForResult(intentImport, IMPORT_EXCEL);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.err_msg_explorer,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMPORT_FILE) {
                String path = Utility.getRealPath(data.getData(), getApplicationContext());
                String dbname = path.substring(path.lastIndexOf("/") + 1);

                if (!Utility.isDBName(dbname)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    extr_dbname = dbname;
                    extr_path = path;
                    if (dataTag.size() == 0) {
                        Toast.makeText(getApplicationContext(),
                                R.string.msg_choose_field, Toast.LENGTH_SHORT)
                                .show();
                    } else {
                    }
                }

            } else if (requestCode == IMPORT_EXCEL) {
                String path = Utility.getRealPath(data.getData(), getApplicationContext());
                String dbname = path.substring(path.lastIndexOf("/") + 1)
                        .replace(".xls", "");
                if (!Utility.isDBName(dbname)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    extr_dbname = dbname;
                    extr_path = path;
                    if (dataTag.size() == 0) {
                        Toast.makeText(getApplicationContext(),
                                R.string.msg_choose_field, Toast.LENGTH_SHORT)
                                .show();
                    } else {
                    }
                }


            }
        }
    }
}
