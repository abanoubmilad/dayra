package abanoubm.dayra.search;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactFieldAdapter;
import abanoubm.dayra.contact.DisplayContact;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactField;

public class FragmentSearchDates extends Fragment {
    private ContactFieldAdapter adapter;
    private int currentTag = 0;
    private int dayType = 0;
    private String targetDay;
    private final String[] searchTags = {
            DB.ATTEND_DAY,
            " MIN(" + DB.ATTEND_DAY + ")",
            " MAX(" + DB.ATTEND_DAY + ")"
    };

    private DB mDB;
    private class SearchDatesTask extends
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
            if(mDB==null)
                mDB =DB.getInstant(getActivity());
            return mDB
                    .searchDates(targetDay, dayType + "", searchTags[currentTag]);


        }

        @Override
        protected void onPostExecute(ArrayList<ContactField> att) {
            adapter.clearThenAddAll(att);
            pBar.dismiss();
            if (att.size() == 0)
                Toast.makeText(getActivity(),
                        R.string.msg_no_results, Toast.LENGTH_SHORT).show();


        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search_dates, container, false);


        ListView lv = (ListView) root.findViewById(R.id.list);
        adapter = new ContactFieldAdapter(getActivity(), new ArrayList<ContactField>(0));
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

        final TextView date = (TextView) root.findViewById(R.id.date);

        final DatePickerDialog picker;

        Calendar cal = Calendar.getInstance();
        targetDay = Utility.produceDate(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR));
        date.setText(targetDay);

        picker = new DatePickerDialog(getActivity(), new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                targetDay = Utility.produceDate(dayOfMonth, monthOfYear + 1, year);
                date.setText(targetDay);
            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
                .get(Calendar.DAY_OF_MONTH));

        root.findViewById(R.id.pick_date)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        picker.show();

                    }
                });

        Spinner spin_search = (Spinner) root.findViewById(R.id.spin_search);
        spin_search.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.item_string, getResources().getTextArray(R.array.search_dates_menu)));

        spin_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                currentTag = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner spin_type = (Spinner) root.findViewById(R.id.spin_type);
        spin_type.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.item_string, getResources().getTextArray(R.array.attendance_type)));
        spin_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                dayType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        root.findViewById(R.id.btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new SearchDatesTask().execute();

            }

        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DB.getInstant(getActivity()).isDirty()) {
            new SearchDatesTask().execute();
            DB.getInstant(getActivity()).clearDirty();
        }
    }
}
