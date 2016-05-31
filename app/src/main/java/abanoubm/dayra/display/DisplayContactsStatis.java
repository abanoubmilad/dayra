package abanoubm.dayra.display;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import java.util.Collections;
import java.util.Comparator;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactStatisAdapter;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactStatis;

public class DisplayContactsStatis extends Activity {
    private ListView lv;
    private int previousPosition = 0;

    private class GetAllTask extends
            AsyncTask<Void, Void, ArrayList<ContactStatis>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(DisplayContactsStatis.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<ContactStatis> doInBackground(Void... params) {
            ArrayList<ContactStatis> atts = DB.getInstance(
                    getApplicationContext(),
                    getSharedPreferences("login", Context.MODE_PRIVATE)
                            .getString("dbname", "")).getAttendantsStatis();
            Collections.sort(atts, new Comparator<ContactStatis>() {
                @Override
                public int compare(ContactStatis lhs, ContactStatis rhs) {
                    return lhs.getDays() - rhs.getDays();
                }
            });
            return atts;
        }

        @Override
        protected void onPostExecute(ArrayList<ContactStatis> att) {
            pBar.dismiss();
            if (att.size() == 0) {
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            } else {
                lv.setAdapter(new ContactStatisAdapter(getApplicationContext(), att));
                if (previousPosition < att.size())
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
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                previousPosition = lv.getFirstVisiblePosition();

                ContactStatis att = (ContactStatis) parent
                        .getItemAtPosition(position);
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
