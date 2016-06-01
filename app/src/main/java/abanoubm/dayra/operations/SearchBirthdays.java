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

public class SearchBirthdays extends Activity {
    private EditText month, day;
    private ContactDayAdapter adapter;

    private class SearchBDayTask extends
            AsyncTask<String, Void, ArrayList<ContactDay>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(SearchBirthdays.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<ContactDay> doInBackground(String... params) {
            return DB.getInstant(getApplicationContext()).searchBirthdays(params[0]);
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

        ListView lv = (ListView) findViewById(R.id.list);
        adapter = new ContactDayAdapter(getApplicationContext(),
                new ArrayList<ContactDay>(0));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                Intent intent = new Intent(getApplicationContext(),
                        DisplayContactDetails.class);
                intent.putExtra("id", adapter.getItem(position).getId());
                startActivity(intent);

            }
        });
        month = (EditText) findViewById(R.id.month);
        day = (EditText) findViewById(R.id.day);

        findViewById(R.id.btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String d = day.getText().toString().trim();
                String m = month.getText().toString().trim();
                if (d.length() == 0)
                    new SearchBDayTask().execute(Utility.produceDate(m));
                else
                    new SearchBDayTask().execute(Utility.produceDate(d, m));

            }

        });

    }
}
