package abanoubm.dayra.display;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Calendar;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;

public class FragmentEditContactDay extends Fragment {
    private static final String ARG_ID = "id";
    private String id;
    private ProgressDialog pBar;
    private DB dbm;
    private int dayType = 0;
    private LinearLayout layout;
    private String targetDay;
    private boolean isAttendant;

    private class AddDateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            isAttendant = true;
            layout.setBackgroundColor(Utility.update);
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbm.addDay(id, dayType + "", targetDay);
            pBar.dismiss();
            return null;
        }

    }

    private class RemoveDateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            pBar.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            dbm.removeDay(id, dayType + "", targetDay);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            isAttendant = false;
            layout.setBackgroundColor(Utility.deupdate);
            pBar.dismiss();

        }
    }

    private class GetUpdateTask extends
            AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return dbm.isContactDayAttendance(id, targetDay, dayType + "");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            isAttendant = result;
            if (result)
                layout.setBackgroundColor(Utility.update);
            else
                layout.setBackgroundColor(Utility.deupdate);

            pBar.dismiss();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (targetDay.length() > 0)
            new GetUpdateTask()
                    .execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_contact_day, container, false);

        layout = (LinearLayout) root.findViewById(R.id.root);
        final EditText edit_date = (EditText) root.findViewById(R.id.edit_date);

        Calendar cal = Calendar.getInstance();
        targetDay = Utility.produceDate(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR));

        edit_date.setText(targetDay);

        final DatePickerDialog picker_date = new DatePickerDialog(getActivity(),
                new OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        targetDay = Utility.produceDate(dayOfMonth, monthOfYear + 1, year);
                        edit_date.setText(targetDay);
                        new GetUpdateTask().execute();
                    }

                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        root.findViewById(R.id.pick_date)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        picker_date.show();

                    }
                });


        pBar = new ProgressDialog(getActivity());
        pBar.setCancelable(false);

        dbm = DB.getInstant(getActivity());


        ((Spinner) root.findViewById(R.id.spin)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (dayType != position) {
                    dayType = position;
                    if (targetDay.length() > 0)
                        new GetUpdateTask()
                                .execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAttendant)
                    new RemoveDateTask().execute();
                else
                    new AddDateTask().execute();

            }
        });

        new GetUpdateTask()
                .execute();

        return root;
    }
}
