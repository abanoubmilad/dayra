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

    private String day, month, year;
    private Spinner spin_year;

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
            spin_year.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, result));
            pBar.dismiss();

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
                    Utility.produceDate(day, month, year), getResources().getStringArray(R.array.attendace_report_header),
                    findViewById(R.id.english_layout) != null);

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
                .setText(R.string.subhead_export_pdf);


        final Spinner spin_day, spin_month;
        spin_day = (Spinner) findViewById(R.id.spin_day);
        spin_month = (Spinner) findViewById(R.id.spin_month);
        spin_year = (Spinner) findViewById(R.id.spin_year);

        spin_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 0)
                    day = "--";
                else
                    day = position + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (position == 0)
                    month = "--";
                else
                    month = position + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (position == 0)
                    year = "----";
                else
                    year = position + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.export_btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new ExportTask().execute();

            }

        });
        new GetYearsTask().execute();
    }
}
