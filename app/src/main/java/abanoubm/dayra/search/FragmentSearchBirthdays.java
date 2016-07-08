package abanoubm.dayra.search;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactFieldAdapter;
import abanoubm.dayra.contact.DisplayContact;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactField;

public class FragmentSearchBirthdays extends Fragment {
    private ContactFieldAdapter adapter;
    private String month = "0", day = "0";
    private DB mDB;

    private class SearchBDayTask extends
            AsyncTask<Void, Void, ArrayList<ContactField>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<ContactField> doInBackground(Void... params) {
            if (mDB == null)
                mDB = DB.getInstant(getActivity());
            return mDB.searchBirthdays(Utility.produceDateRegex(day, month));
        }

        @Override
        protected void onPostExecute(ArrayList<ContactField> att) {
            adapter.clearThenAddAll(att);
            if (att.size() == 0)
                Toast.makeText(getActivity(),
                        R.string.msg_no_results, Toast.LENGTH_SHORT).show();
            pBar.dismiss();

        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search_birthdays, container, false);

        ListView lv = (ListView) root.findViewById(R.id.list);
        adapter = new ContactFieldAdapter(getActivity(),
                new ArrayList<ContactField>(0));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                Intent intent = new Intent(getActivity(),
                        DisplayContact.class);
                intent.putExtra("id", adapter.getItem(position).getId());
                startActivity(intent);

            }
        });

        root.findViewById(R.id.btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new SearchBDayTask().execute();
            }

        });

        Spinner spin_day = (Spinner) root.findViewById(R.id.spin_day);
        spin_day.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.item_string, getResources().getTextArray(R.array.days)));

        Spinner spin_month = (Spinner) root.findViewById(R.id.spin_month);
        spin_month.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.item_string, getResources().getTextArray(R.array.months)));

        spin_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 0)
                    day = "__";
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
                    month = "__";
                else
                    month = position + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Calendar cal = Calendar.getInstance();
        spin_day.setSelection(cal.get(Calendar.DAY_OF_MONTH), true);
        spin_month.setSelection(cal.get(Calendar.MONTH) + 1, true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DB.getInstant(getActivity()).isDirty()) {
            new SearchBDayTask().execute();
            DB.getInstant(getActivity()).clearDirty();
        }
    }
}
