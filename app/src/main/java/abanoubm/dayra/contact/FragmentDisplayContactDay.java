package abanoubm.dayra.contact;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;

public class FragmentDisplayContactDay extends Fragment {
    private String id;
    private ArrayAdapter mAdpterYears;
    private ArrayAdapter mAdpterMonths;
    private ArrayAdapter mAdpterDays;
    private static final String ARG_ID = "id";
    private ArrayList<String> dates;
    private DB db;
    private int dayType = 0;
    private String choosenMonth, choosenYear;
    private TextView max, min, attend_count, absent_count;
    private ListView yearList, monthList;

    private class GetContactStatisticsTask extends AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            min.setText(result.get(0));
            max.setText(result.get(1));
            attend_count.setText(result.get(2));
            absent_count.setText(result.get(3));
            pBar.dismiss();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> result = db.getContactAttendanceStatistics(id, dayType + "");
            ArrayList<String> absences = db.getAbsences(id, dayType + "", result.get(0) == null ? "" : result.get(0));
            result.add(3, absences.size() + "");
            return result;
        }
    }

    private class GetDatesTask extends AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();

            mAdpterDays.clear();
            mAdpterMonths.clear();
            mAdpterYears.clear();

        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            mAdpterYears.addAll(result);
            if (result.size() != 0)
                yearList.performItemClick(yearList.findViewWithTag(mAdpterYears.getItem(0)),
                        0, mAdpterYears.getItemId(0));

            pBar.dismiss();

        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            dates = db.getAttendances(id, dayType + "");
            ArrayList<String> temp = new ArrayList<>(dates.size());
            String last = null, check;
            for (String str : dates) {
                check = str.substring(0, 4);
                if (!check.equals(last)) {
                    last = check;
                    temp.add(last);
                }
            }
            pBar.dismiss();
            return temp;
        }
    }

    private class GetMonthsTask extends AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            mAdpterMonths.addAll(result);
            if (result.size() != 0)
                monthList.performItemClick(monthList.findViewWithTag(mAdpterMonths.getItem(0)),
                        0, mAdpterMonths.getItemId(0));
            pBar.dismiss();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> temp = new ArrayList<>(dates.size());

            String last = null, check;
            for (String str : dates) {
                if (str.startsWith(choosenYear)) {
                    check = str.substring(5, 7);
                    if (!check.equals(last)) {
                        last = check;
                        temp.add(last);
                    }
                }
            }

            pBar.dismiss();
            return temp;
        }
    }

    private class GetDaysTask extends AsyncTask<String, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            mAdpterDays.addAll(result);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> temp = new ArrayList<>(dates.size());

            String last = null, check;
            for (String str : dates) {
                if (str.startsWith(choosenYear + "-" + choosenMonth)) {
                    check = str.substring(8, 10);
                    if (!check.equals(last)) {
                        last = check;
                        temp.add(last);
                    }
                }
            }

            pBar.dismiss();
            return temp;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_display_contact_day, container, false);

        ListView dayList;

        max = (TextView) root.findViewById(R.id.max);
        min = (TextView) root.findViewById(R.id.min);
        attend_count = (TextView) root.findViewById(R.id.attend_count);
        absent_count = (TextView) root.findViewById(R.id.absent_count);

        dayList = (ListView) root.findViewById(R.id.dayList);
        monthList = (ListView) root.findViewById(R.id.monthList);
        yearList = (ListView) root.findViewById(R.id.yearList);

        db = DB.getInstant(getContext());

        mAdpterDays = new ArrayAdapter(getContext(), R.layout.item_string,
                R.id.item, new ArrayList<String>(0));
        mAdpterMonths = new ArrayAdapter(getContext(), R.layout.item_string,
                R.id.item, new ArrayList<String>(0));
        mAdpterYears = new ArrayAdapter(getContext(), R.layout.item_string,
                R.id.item, new ArrayList<String>(0));

        dayList.setAdapter(mAdpterDays);
        monthList.setAdapter(mAdpterMonths);
        yearList.setAdapter(mAdpterYears);

        yearList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                mAdpterMonths.clear();
                mAdpterDays.clear();
                choosenYear = (String) mAdpterYears.getItem(position);
                new GetMonthsTask().execute();

            }
        });
        monthList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                mAdpterDays.clear();
                choosenMonth = (String) mAdpterMonths.getItem(position);
                new GetDaysTask().execute();
            }
        });

        Spinner spin = (Spinner) root.findViewById(R.id.spin);
        spin.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.item_string, getResources().getTextArray(R.array.attendance_type)));
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                dayType = position;
                new GetContactStatisticsTask().execute();
                new GetDatesTask().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new GetContactStatisticsTask().execute();
        new GetDatesTask().execute();
        return root;

    }


}