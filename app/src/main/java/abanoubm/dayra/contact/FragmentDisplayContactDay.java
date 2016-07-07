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
import java.util.Collections;
import java.util.Comparator;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.DayCheckAdapter;
import abanoubm.dayra.adapters.StringAdapter;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.model.DayCheck;

public class FragmentDisplayContactDay extends Fragment {
    private String id;
    private StringAdapter mAdapterYears;
    private StringAdapter mAdapterMonths;
    private DayCheckAdapter mAdapterDays;
    private static final String ARG_ID = "id";
    private ArrayList<DayCheck> days;
    private int dayType = 0;
    private String chosenMonth, chosenYear;
    private TextView max, min, attend_count, absent_count;
    private ListView yearList, monthList;
    private ProgressDialog pBar;

    private class GetAttendanceAbsenceTask extends AsyncTask<Void, Void, ArrayList<String>> {
        private ArrayList<String> yearsStr;

        @Override
        protected void onPreExecute() {

            mAdapterDays.clear();
            mAdapterMonths.clear();
            mAdapterYears.clear();

            pBar.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            min.setText(result.get(0));
            max.setText(result.get(1));
            attend_count.setText(result.get(2));
            absent_count.setText(result.get(3));


            mAdapterYears.addAll(yearsStr);

            if (yearsStr.size() != 0)
                yearList.performItemClick(yearList.findViewWithTag(mAdapterYears.getItem(0)),
                        0, mAdapterYears.getItemId(0));

            pBar.dismiss();

        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            DB db = DB.getInstant(getActivity());
            ArrayList<String> result = db.getContactAttendanceStatistics(id, dayType + "");
            days = db.getAttendanceAbsence(id, dayType + "", result.get(0) == null ? "9999-99-99" : result.get(0));

            Collections.sort(days, new Comparator<DayCheck>() {
                @Override
                public int compare(DayCheck lhs, DayCheck rhs) {
                    return rhs.getDay().compareTo(lhs.getDay());
                }
            });

            result.add(3, days.size() - Integer.parseInt(result.get(2)) + "");


            yearsStr = new ArrayList<>(days.size());
            String last = null, check;
            for (DayCheck day : days) {
                check = day.getDay().substring(0, 4);
                if (!check.equals(last)) {
                    last = check;
                    yearsStr.add(last);
                }
            }
            return result;
        }
    }

    private class GetMonthsTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            mAdapterMonths.addAll(result);
            if (result.size() != 0)
                monthList.performItemClick(monthList.findViewWithTag(mAdapterMonths.getItem(0)),
                        0, mAdapterMonths.getItemId(0));
            pBar.dismiss();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> temp = new ArrayList<>(days.size());

            String last = null, check;
            for (DayCheck day : days) {
                if (day.getDay().startsWith(chosenYear)) {
                    check = day.getDay().substring(5, 7);
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

    private class GetDaysTask extends AsyncTask<String, Void, ArrayList<DayCheck>> {

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected void onPostExecute(ArrayList<DayCheck> result) {
            mAdapterDays.addAll(result);
            pBar.dismiss();
        }

        @Override
        protected ArrayList<DayCheck> doInBackground(String... params) {
            ArrayList<DayCheck> temp = new ArrayList<>(days.size());

            String last = null, check;
            for (DayCheck day : days) {
                if (day.getDay().startsWith(chosenYear + "-" + chosenMonth)) {
                    check = day.getDay().substring(8, 10);
                    if (!check.equals(last)) {
                        last = check;
                        temp.add(day);
                    }
                }
            }

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

        final ListView dayList;

        max = (TextView) root.findViewById(R.id.max);
        min = (TextView) root.findViewById(R.id.min);
        attend_count = (TextView) root.findViewById(R.id.attend_count);
        absent_count = (TextView) root.findViewById(R.id.absent_count);

        dayList = (ListView) root.findViewById(R.id.dayList);
        monthList = (ListView) root.findViewById(R.id.monthList);
        yearList = (ListView) root.findViewById(R.id.yearList);

        mAdapterDays = new DayCheckAdapter(getActivity(), new ArrayList<DayCheck>(0));

        mAdapterMonths = new StringAdapter(getContext(), new ArrayList<String>(0));
        mAdapterYears = new StringAdapter(getContext(), new ArrayList<String>(0));

        dayList.setAdapter(mAdapterDays);
        monthList.setAdapter(mAdapterMonths);
        yearList.setAdapter(mAdapterYears);

        yearList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                String temp = mAdapterYears.getItem(position);
                if (!temp.equals(chosenYear)) {
                    mAdapterMonths.clear();
                    mAdapterDays.clear();
                    chosenYear = temp;
                    mAdapterYears.setSelectedIndex(position);
                    new GetMonthsTask().execute();
                }

            }
        });
        monthList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                String temp = mAdapterMonths.getItem(position);
                if (!temp.equals(chosenMonth)) {
                    mAdapterDays.clear();
                    chosenMonth = temp;
                    mAdapterMonths.setSelectedIndex(position);
                    new GetDaysTask().execute();
                }
            }
        });

        Spinner spin = (Spinner) root.findViewById(R.id.spin);
        spin.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.item_string, getResources().getTextArray(R.array.attendance_type)));
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (dayType != position) {
                    dayType = position;
                    new GetAttendanceAbsenceTask().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pBar = new ProgressDialog(getActivity());
        pBar.setCancelable(false);
        new GetAttendanceAbsenceTask().execute();
        return root;

    }


}
