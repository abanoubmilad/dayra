package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactDayAdapter;
import abanoubm.dayra.display.DisplayContactDetails;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactDay;

public class SearchBDay extends Activity {
    private EditText month, day;
    private ContactDayAdapter adapter;
    private TextView tv;

    private class SearchMDBDayTask extends
            AsyncTask<String, Void, ArrayList<ContactDay>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(SearchBDay.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<ContactDay> doInBackground(String... params) {
            return DB.getInstant(getApplicationContext()).searchBirthDay(
                    Integer.parseInt(params[0]), Integer.parseInt(params[1]));
        }

        @Override
        protected void onPostExecute(ArrayList<ContactDay> att) {
            adapter.clear();
            adapter.addAll(att);
            if (att.size() == 0)
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_results, Toast.LENGTH_SHORT).show();
            pBar.dismiss();

        }

    }

    private class SearchMBDayTask extends
            AsyncTask<String, Void, ArrayList<ContactDay>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(SearchBDay.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<ContactDay> doInBackground(String... params) {
            return DB.getInstant(getApplicationContext()).searchBirthDay(
                    Integer.parseInt(params[0]));
        }

        @Override
        protected void onPostExecute(ArrayList<ContactDay> att) {
            adapter.clear();
            adapter.addAll(att);
            if (att.size() == 0)
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_results, Toast.LENGTH_SHORT).show();
            pBar.dismiss();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search_bday);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_search_bday);

        ListView lv = (ListView) findViewById(R.id.sbday_list);
        adapter = new ContactDayAdapter(getApplicationContext(),
                new ArrayList<ContactDay>(0));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {

                ContactDay att = (ContactDay) parent
                        .getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(),
                        DisplayContactDetails.class);
                intent.putExtra("id", att.getId());
                startActivity(intent);

            }
        });
        month = (EditText) findViewById(R.id.sbday_month);
        day = (EditText) findViewById(R.id.sbday_day);
        tv = (TextView) findViewById(R.id.sbday_btn);

        tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String d = day.getText().toString().trim();
                String m = month.getText().toString().trim();
                if (d.length() == 0) {
                    if (Utility.isMonth(m))
                        new SearchMBDayTask().execute(m);
                    else
                        Toast.makeText(getApplicationContext(),
                                R.string.err_msg_month, Toast.LENGTH_SHORT)
                                .show();

                } else {
                    if (!Utility.isDay(d))
                        Toast.makeText(getApplicationContext(),
                                R.string.err_msg_day, Toast.LENGTH_SHORT)
                                .show();
                    else if (!Utility.isMonth(m))
                        Toast.makeText(getApplicationContext(),
                                R.string.err_msg_month, Toast.LENGTH_SHORT)
                                .show();
                    else
                        new SearchMDBDayTask().execute(m, d);

                }
            }
        });

    }
}
