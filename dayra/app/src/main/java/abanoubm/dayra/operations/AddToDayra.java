package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.DBAdder;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.IntWrapper;

public class AddToDayra extends Activity {
    private static final int IMPORT_FILE = 1;
    private static final int IMPORT_EXCEL = 2;

    private String extr_path;
    private ArrayList<Integer> tags;


    private CheckBox photo, bday, map_location;
    private TextView expressXls;
    private boolean dayraTypeFile = true;

    private boolean photoSelected = false;

    private class AddFromDayraFileTask extends AsyncTask<Void, Void, Integer> {
        private ProgressDialog pBar;
        private IntWrapper addedCounter = new IntWrapper(),
                updatedCounter = new IntWrapper(),
                totalCounter = new IntWrapper();

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(AddToDayra.this);
            pBar.setMessage(getResources().getString(R.string.label_loading));
            pBar.setCancelable(false);
            pBar.show();

        }

        @Override
        protected void onPostExecute(Integer result) {
            pBar.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                    .show();
            if (result != R.string.msg_dayra_added)
                return;

            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            View View = li.inflate(R.layout.dialogue_add_data, null, false);
            final AlertDialog ad = new AlertDialog.Builder(AddToDayra.this)
                    .setCancelable(true).create();
            ad.setView(View, 0, 0, 0, 0);
            ad.show();

            ((TextView) View.findViewById(R.id.path)).setText(extr_path);
            ((TextView) View.findViewById(R.id.total)).setText(totalCounter.getCounter() + "");
            ((TextView) View.findViewById(R.id.added)).setText(addedCounter.getCounter() + "");
            ((TextView) View.findViewById(R.id.updated)).setText(updatedCounter.getCounter() + "");

            View.findViewById(R.id.yesBtn).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                }
            });

        }

        @Override
        protected Integer doInBackground(Void... params) {

            DBAdder adder = new DBAdder(getApplicationContext(),
                    extr_path);
            if (adder.checkDB()) {
                DB.getInstant(getApplicationContext()).AddFromDayraFile(adder
                        , tags, photoSelected, totalCounter, addedCounter, updatedCounter);
                adder.close();
                return R.string.msg_dayra_added;
            } else
                return R.string.err_msg_invalid_file;

        }
    }

    private class AddFromDayraExcelTask extends AsyncTask<Void, Void, Integer> {
        private ProgressDialog pBar;
        private IntWrapper addedCounter = new IntWrapper(),
                updatedCounter = new IntWrapper(),
                totalCounter = new IntWrapper();

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(AddToDayra.this);
            pBar.setMessage(getResources().getString(R.string.label_loading));
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Integer result) {
            pBar.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                    .show();
            if (result != R.string.msg_dayra_added)
                return;
            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            View View = li.inflate(R.layout.dialogue_add_data, null, false);
            final AlertDialog ad = new AlertDialog.Builder(AddToDayra.this)
                    .setCancelable(true).create();
            ad.setView(View, 0, 0, 0, 0);
            ad.show();

            ((TextView) View.findViewById(R.id.path)).setText(extr_path);
            ((TextView) View.findViewById(R.id.total)).setText(totalCounter.getCounter() + "");
            ((TextView) View.findViewById(R.id.added)).setText(addedCounter.getCounter() + "");
            ((TextView) View.findViewById(R.id.updated)).setText(updatedCounter.getCounter() + "");

            View.findViewById(R.id.yesBtn).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                }
            });

        }

        @Override
        protected Integer doInBackground(Void... params) {

            if ((DB.getInstant(getApplicationContext())).AddFromDayraExcel(
                    tags, extr_path, totalCounter, addedCounter, updatedCounter))
                return R.string.msg_dayra_added;
            return R.string.err_msg_invalid_file;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_dayra);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2))
                .setText(R.string.subhead_add_data);
        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        final CheckBox selectall, mobile1, mobile2, mobile3,
                lphone, addr, supervisor, comm,
                email, study_work, class_year, site,
                street, home;

        selectall = (CheckBox) findViewById(R.id.selectall);

        mobile1 = (CheckBox) findViewById(R.id.mobile1);
        mobile2 = (CheckBox) findViewById(R.id.mobile2);
        mobile3 = (CheckBox) findViewById(R.id.mobile3);
        lphone = (CheckBox) findViewById(R.id.lphone);
        addr = (CheckBox) findViewById(R.id.addr);
        supervisor = (CheckBox) findViewById(R.id.supervisor);
        comm = (CheckBox) findViewById(R.id.comm);
        email = (CheckBox) findViewById(R.id.email);
        study_work = (CheckBox) findViewById(R.id.study_work);
        class_year = (CheckBox) findViewById(R.id.classyear);
        street = (CheckBox) findViewById(R.id.street);
        site = (CheckBox) findViewById(R.id.site);
        home = (CheckBox) findViewById(R.id.home);

        photo = (CheckBox) findViewById(R.id.photo);
        map_location = (CheckBox) findViewById(R.id.map_location);
        bday = (CheckBox) findViewById(R.id.bday);
        expressXls = (TextView) findViewById(R.id.xls_express);

        selectall.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mobile1.setChecked(isChecked);
                mobile2.setChecked(isChecked);
                mobile3.setChecked(isChecked);
                lphone.setChecked(isChecked);
                addr.setChecked(isChecked);
                supervisor.setChecked(isChecked);
                comm.setChecked(isChecked);
                email.setChecked(isChecked);
                study_work.setChecked(isChecked);
                class_year.setChecked(isChecked);
                street.setChecked(isChecked);
                site.setChecked(isChecked);
                home.setChecked(isChecked);
                photo.setChecked(isChecked);
                map_location.setChecked(isChecked);
                bday.setChecked(isChecked);
            }
        });
        final RadioGroup radio = (RadioGroup) findViewById(R.id.radio);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectall.setChecked(false);
                if (checkedId == R.id.dayra_file) {
                    dayraTypeFile = true;
                    photo.setVisibility(View.VISIBLE);
                    map_location.setVisibility(View.VISIBLE);
                    bday.setVisibility(View.VISIBLE);
                    expressXls.setVisibility(View.GONE);
                } else if (checkedId == R.id.dayra_xls) {
                    dayraTypeFile = false;
                    photo.setVisibility(View.GONE);
                    map_location.setVisibility(View.GONE);
                    bday.setVisibility(View.GONE);
                    expressXls.setVisibility(View.VISIBLE);
                }

            }
        });

        findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tags = new ArrayList<>();

                if (class_year.isChecked())
                    tags.add(1);
                if (study_work.isChecked())
                    tags.add(2);
                if (mobile1.isChecked())
                    tags.add(3);
                if (mobile2.isChecked())
                    tags.add(4);
                if (mobile3.isChecked())
                    tags.add(5);
                if (lphone.isChecked())
                    tags.add(6);
                if (email.isChecked())
                    tags.add(7);
                if (site.isChecked())
                    tags.add(8);
                if (street.isChecked())
                    tags.add(9);
                if (home.isChecked())
                    tags.add(10);
                if (addr.isChecked())
                    tags.add(11);
                if (comm.isChecked())
                    tags.add(12);
                if (supervisor.isChecked())
                    tags.add(13);

                if (dayraTypeFile) {
                    if (bday.isChecked())
                        tags.add(14);
                    if (map_location.isChecked()) {
                        tags.add(15);
                        tags.add(16);
                        tags.add(17);
                    }
                    photoSelected = photo.isChecked();

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
                } else {
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

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMPORT_FILE) {
                String path = Utility.getRealPath(getApplicationContext(),data.getData());
                if(path==null) {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_invalid_path, Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                String dbname = path.substring(path.lastIndexOf("/") + 1);

                if (Utility.isInvlaidDBName(dbname)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    extr_path = path;
                    new AddFromDayraFileTask().execute();

                }

            } else if (requestCode == IMPORT_EXCEL) {
                String path = Utility.getRealPath(getApplicationContext(),data.getData());
                if(path==null) {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_invalid_path, Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                String dbname = path.substring(path.lastIndexOf("/") + 1)
                        .replace(".xls", "");
                if (Utility.isInvlaidDBName(dbname)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.err_msg_dayra_name, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    extr_path = path;
                    new AddFromDayraExcelTask().execute();

                }


            }
        }
    }
}
