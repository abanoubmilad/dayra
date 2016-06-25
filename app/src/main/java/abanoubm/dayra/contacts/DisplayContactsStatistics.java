package abanoubm.dayra.contacts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactStatisticsAdapter;
import abanoubm.dayra.contact.DisplayContact;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactStatistics;

public class DisplayContactsStatistics extends Activity {
    private ListView lv;
    private int previousPosition = 0;
    private int dayType = 0;
    private ContactStatisticsAdapter mAdapter;
    private int sortType = 0;
    private ArrayList<ContactStatistics> list;

    private class SortTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(DisplayContactsStatistics.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            switch (sortType) {
                // name
                case 0:
                    Collections.sort(list, new Comparator<ContactStatistics>() {
                        @Override
                        public int compare(ContactStatistics lhs, ContactStatistics rhs) {
                            return lhs.getName().compareTo(rhs.getName());
                        }
                    });
                    // oldest date
                case 1:
                    Collections.sort(list, new Comparator<ContactStatistics>() {
                        @Override
                        public int compare(ContactStatistics lhs, ContactStatistics rhs) {
                            return lhs.getMinDay().compareTo(rhs.getMinDay());
                        }
                    });
                    break;
                // newest date
                case 2:
                    Collections.sort(list, new Comparator<ContactStatistics>() {
                        @Override
                        public int compare(ContactStatistics lhs, ContactStatistics rhs) {
                            return lhs.getMaxDay().compareTo(rhs.getMaxDay());
                        }
                    });
                    break;
                // times
                case 3:
                    Collections.sort(list, new Comparator<ContactStatistics>() {
                        @Override
                        public int compare(ContactStatistics lhs, ContactStatistics rhs) {
                            return lhs.getDaysCount() - rhs.getDaysCount();
                        }
                    });
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.clearThenAddAll(list);
            previousPosition = 0;
            if (list.size() == 0) {
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            }
            pBar.dismiss();

        }
    }

    private class GetAllTask extends
            AsyncTask<Void, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(DisplayContactsStatistics.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            list = DB.getInstant(getApplicationContext()).getContactsAttendanceStatistics(dayType + "");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.clearThenAddAll(list);
            pBar.dismiss();
            if (list.size() == 0) {
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            } else {
                if (previousPosition < list.size())
                    lv.setSelection(previousPosition);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_display_contacts_statistics);
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
                        DisplayContact.class).putExtra("id", att.getId());
                startActivity(intent);
            }
        });
        Spinner spin_type = (Spinner) findViewById(R.id.spin_type);
        spin_type.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                R.layout.item_string, getResources().getTextArray(R.array.attendance_type)));
        spin_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (dayType != position) {
                    dayType = position;
                    new GetAllTask().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner spin_sort = (Spinner) findViewById(R.id.spin_sort);
        spin_sort.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                R.layout.item_string, getResources().getTextArray(R.array.sort_statistics_menu)));

        spin_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (sortType != position) {
                    sortType = position;
                    new SortTask().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetAllTask().execute();
    }
}
