package abanoubm.dayra.display;


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
    private String choosenMonth, choosenYear;

    private class GetDatesTask extends AsyncTask<Void, Void, ArrayList<String>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            pBar.dismiss();
            mAdpterYears.addAll(result);

        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            dates = db.getAttendances(id);
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

        final ListView monthList, dayList, yearList;

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
                choosenMonth = (String) mAdpterMonths.getItem(position);
                mAdpterDays.clear();
                new GetDaysTask().execute();
            }
        });

        new GetDatesTask().execute();

        return root;

    }


}
