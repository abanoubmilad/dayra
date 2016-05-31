package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactDayAdapter;
import abanoubm.dayra.display.DisplayContact;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactDay;

public class SearchDate extends Activity {
    private ContactDayAdapter adapter;
    private int searchFlag;
    private String dbName;

    private class SearchDateTask extends
            AsyncTask<String, Void, ArrayList<ContactDay>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(SearchDate.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<ContactDay> doInBackground(String... params) {
            if (searchFlag < 3)
                return DB.getInstance(
                        getApplicationContext(), dbName).searchLastDate(
                        params[0], searchFlag == 1);
            else {
                String[] arr = params[0].split("-");
                return DB.getInstance(
                        getApplicationContext(),
                        dbName)
                        .searchLastDateAbsence(Integer.parseInt(arr[0]),
                                Integer.parseInt(arr[1]),
                                Integer.parseInt(arr[2]), searchFlag == 3);
            }

        }

        @Override
        protected void onPostExecute(ArrayList<ContactDay> att) {
            adapter.clear();

            if (att != null) {
                adapter.addAll(att);
                if (att.size() == 0)
                    Toast.makeText(getApplicationContext(),
                            R.string.msg_no_results, Toast.LENGTH_SHORT).show();
            }
            pBar.dismiss();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search_date);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));

        dbName = getSharedPreferences("login", Context.MODE_PRIVATE).getString(
                "dbname", "");

        searchFlag = getIntent().getIntExtra("sf", 1);

        if (searchFlag == 1)
            ((TextView) findViewById(R.id.subhead2))
                    .setText(R.string.subhead_search_ldate);
        else if (searchFlag == 2)

            ((TextView) findViewById(R.id.subhead2))
                    .setText(R.string.subhead_search_vlast);
        else if (searchFlag == 3)

            ((TextView) findViewById(R.id.subhead2))
                    .setText(R.string.subhead_search_attend_absence);
        else if (searchFlag == 4)
            ((TextView) findViewById(R.id.subhead2))
                    .setText(R.string.subhead_search_visit_absence);

        ListView lv = (ListView) findViewById(R.id.list);
        adapter = new ContactDayAdapter(this, new ArrayList<ContactDay>(0));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {

                ContactDay att = (ContactDay) parent
                        .getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(),
                        DisplayContact.class);
                intent.putExtra("id", att.getId());
                startActivity(intent);

            }
        });

        final EditText date = (EditText) findViewById(R.id.edit_date);

        final DatePickerDialog picker;
        Calendar cal = Calendar.getInstance();
        picker = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                new SearchDateTask().execute(dayOfMonth + "-"
                        + (monthOfYear + 1) + "-" + year);
            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
                .get(Calendar.DAY_OF_MONTH));

        findViewById(R.id.pick_date)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        picker.show();

                    }
                });
    }
}
