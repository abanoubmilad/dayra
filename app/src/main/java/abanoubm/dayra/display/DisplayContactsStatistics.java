package abanoubm.dayra.display;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactStatisticsAdapter;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactStatistics;

public class DisplayContactsStatistics extends Activity {
    private ListView lv;
    private int previousPosition = 0;
    private int dayType = 0;
    private ContactStatisticsAdapter mAdapter;

    private class GetAllTask extends
            AsyncTask<Void, Void, ArrayList<ContactStatistics>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(DisplayContactsStatistics.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<ContactStatistics> doInBackground(Void... params) {
            return DB.getInstant(getApplicationContext()).getContactsAttendanceStatistics(dayType + "");
        }

        @Override
        protected void onPostExecute(ArrayList<ContactStatistics> result) {
            mAdapter.clear();
            ;
            mAdapter.addAll(result);
            pBar.dismiss();
            if (result.size() == 0) {
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            } else {
                if (previousPosition < result.size())
                    lv.setSelection(previousPosition);
                previousPosition = 0;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_display_search_results);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_percentage);

        lv = (ListView) findViewById(R.id.dsr_list);
        mAdapter = new ContactStatisticsAdapter(getApplicationContext(), new ArrayList<ContactStatistics>(0));
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                previousPosition = lv.getFirstVisiblePosition();

                ContactStatistics att = mAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(),
                        DisplayContact.class);
                intent.putExtra("id", att.getId());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetAllTask().execute();
    }
}
