package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;

public class CreateAttendanceReport extends Activity {

    private String month1 = "01", year1 = "", month2 = "01", year2 = "", day1 = "01", day2 = "01";
    private Spinner spin_year1, spin_year2;
    private TextView startingDate, endingDate;

    private class GetYearsTask extends AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(CreateAttendanceReport.this);
            pBar.setCancelable(false);
            pBar.setMessage(getResources().getString(R.string.label_loading));
            pBar.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            return DB.getInstant(getApplicationContext()).getExistingYears();

        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                    R.layout.item_string, result);
            spin_year1.setAdapter(adapter);
            spin_year2.setAdapter(adapter);

            pBar.dismiss();

            if (adapter.getCount() != 0)
                year1 = year2 = adapter.getItem(0);
            else {
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_attendance, Toast.LENGTH_SHORT).show();
            }


        }

    }

    private class ExportTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(CreateAttendanceReport.this);
            pBar.setCancelable(false);
            pBar.setMessage(getResources().getString(R.string.label_loading));
            pBar.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String path = Utility.getDayraFolder() +
                    "/" + Utility.getDayraName(getApplicationContext()) +
                    "_dayra_attendance_report_" +
                    new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss a", Locale.getDefault())
                            .format(new Date()) + ".pdf";
            if (android.os.Build.VERSION.SDK_INT >= 8) {

                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{path}, null, null);
            }
            return DB.getInstant(getApplicationContext()).exportAttendanceReport(path,
                    Utility.produceDate(day1, month1, year1),
                    Utility.produceDate(day2, month2, year2),
                    getResources().getStringArray(R.array.attendace_report_header),
                    Utility.getAttendanceTypes(getApplicationContext()),
                    findViewById(R.id.english_layout) != null, getApplicationContext());

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
        setContentView(R.layout.act_attendance_report);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2))
                .setText(R.string.subhead_attendance_report);
        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        startingDate = (TextView) findViewById(R.id.starting_date);
        endingDate = (TextView) findViewById(R.id.ending_date);

        final Spinner spin_month1, spin_month2, spin_day1, spin_day2;

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.item_string, getResources().getTextArray(R.array.pure_months));

        spin_month1 = (Spinner) findViewById(R.id.spin_month1);
        spin_month1.setAdapter(adapter);
        spin_month2 = (Spinner) findViewById(R.id.spin_month2);
        spin_month2.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter_days = new ArrayAdapter<>(getApplicationContext(),
                R.layout.item_string, getResources().getTextArray(R.array.pure_days));

        spin_day1 = (Spinner) findViewById(R.id.spin_day1);
        spin_day1.setAdapter(adapter_days);
        spin_day2 = (Spinner) findViewById(R.id.spin_day2);
        spin_day2.setAdapter(adapter_days);

        spin_year1 = (Spinner) findViewById(R.id.spin_year1);
        spin_year2 = (Spinner) findViewById(R.id.spin_year2);

        spin_month1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                month1 = position + 1 + "";
                startingDate.setText(Utility.produceDate(day1, month1, year1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_day1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                day1 = position + 1 + "";
                startingDate.setText(Utility.produceDate(day1, month1, year1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_month2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                month2 = position + 1 + "";
                endingDate.setText(Utility.produceDate(day2, month2, year2));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_day2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                day2 = position + 1 + "";
                endingDate.setText(Utility.produceDate(day2, month2, year2));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_year1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                year1 = ((ArrayAdapter<String>) spin_year1.getAdapter()).getItem(position);
                startingDate.setText(Utility.produceDate(day1, month1, year1));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_year2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                year2 = ((ArrayAdapter<String>) spin_year2.getAdapter()).getItem(position);
                endingDate.setText(Utility.produceDate(day2, month2, year2));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new ExportTask().execute();

            }

        });
        new GetYearsTask().execute();
    }
}
