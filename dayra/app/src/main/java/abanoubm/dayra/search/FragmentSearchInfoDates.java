package abanoubm.dayra.search;

import android.app.DatePickerDialog;
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
import android.widget.EditText;
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

public class FragmentSearchInfoDates extends Fragment {
    private boolean searchForAbsence = true;
    private int dayType = 0;
    private String targetDay;


    private EditText input;
    private ContactFieldAdapter mAdapter;
    private int currentTag = 0;
    private final String[] searchTags = {
            DB.CONTACT_NAME
            , DB.CONTACT_CLASS_YEAR
            , DB.CONTACT_STUDY_WORK
            , DB.CONTACT_MOB1
            , DB.CONTACT_MOB2
            , DB.CONTACT_MOB3
            , DB.CONTACT_LPHONE
            , DB.CONTACT_EMAIL
            , DB.CONTACT_SITE
            , DB.CONTACT_ST
            , DB.CONTACT_HOME
            , DB.CONTACT_ADDR
            , DB.CONTACT_NOTES
            , DB.CONTACT_SUPERVISOR
    };
    private DB mDB;

    private class SearchTask extends
            AsyncTask<String, Void, ArrayList<ContactField>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<ContactField> doInBackground(String... params) {
            if (mDB == null)
                mDB = DB.getInstant(getActivity());
            return mDB.searchInfoDates(params[0], searchTags[currentTag], targetDay, dayType + "", searchForAbsence);
        }

        @Override
        protected void onPostExecute(ArrayList<ContactField> result) {
            mAdapter.clearThenAddAll(result);
            if (result.size() == 0)
                Toast.makeText(getActivity(),
                        R.string.msg_no_results, Toast.LENGTH_SHORT).show();
            pBar.dismiss();

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_info, container, false);

        input = (EditText) root.findViewById(R.id.input);

        Spinner spin = (Spinner) root.findViewById(R.id.spin);
        spin.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.item_string, getResources().getTextArray(R.array.search_menu)));

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (currentTag != position) {

                    currentTag = position;
                    input.setHint((String) parent.getItemAtPosition(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mAdapter = new ContactFieldAdapter(getActivity(), new ArrayList<ContactField>());
        ListView lv = (ListView) root.findViewById(R.id.list);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {

                Intent intent = new Intent(getActivity(),
                        DisplayContact.class);
                intent.putExtra("id", mAdapter.getItem(position).getId());
                startActivity(intent);

            }
        });
        root.findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = input.getText().toString().trim();
                if (str.length() > 0)
                    new SearchTask().execute(str);
            }
        });


        final TextView date = (TextView) root.findViewById(R.id.date);

        final DatePickerDialog picker;

        Calendar cal = Calendar.getInstance();
        targetDay = Utility.produceDate(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR));
        date.setText(targetDay);

        picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
                R.layout.item_string, getResources().getTextArray(R.array.search_dates_type)));

        spin_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                searchForAbsence = position == 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner spin_type = (Spinner) root.findViewById(R.id.spin_type);
        spin_type.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.item_string, Utility.getAttendanceTypes(getContext())));
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
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DB.getInstant(getActivity()).isDirty()) {
            new SearchTask().execute(input.getText().toString().trim());
            DB.getInstant(getActivity()).clearDirty();
        }
    }

}
