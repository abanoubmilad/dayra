package abanoubm.dayra.contact;

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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

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
    private TextView date1, date2, flag;
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
            flag.setText(getResources().getText(R.string.flag_contact_attend));
            flag.setBackgroundColor(
                    getContext().getResources().getColor(
                            R.color.hotgreen));
            pBar.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbm.addDay(id, dayType + "", targetDay);
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
            flag.setText("");
            flag.setBackgroundColor(
                    getContext().getResources().getColor(
                            R.color.colorAccent));
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
            date2.setText(targetDay);

            if (result) {
                flag.setText(getResources().getText(R.string.flag_contact_attend));
                flag.setBackgroundColor(
                        getContext().getResources().getColor(
                                R.color.hotgreen));
            } else {
                flag.setText("");
                flag.setBackgroundColor(
                        getContext().getResources().getColor(
                                R.color.colorAccent));
            }
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

        date1 = (TextView) root.findViewById(R.id.date1);
        date2 = (TextView) root.findViewById(R.id.date2);
        flag = (TextView) root.findViewById(R.id.flag);

        Calendar cal = Calendar.getInstance();
        targetDay = Utility.produceDate(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR));

        date1.setText(targetDay);

        final DatePickerDialog picker_date = new DatePickerDialog(getActivity(),
                new OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        targetDay = Utility.produceDate(dayOfMonth, monthOfYear + 1, year);
                        date1.setText(targetDay);
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


        Spinner spin = (Spinner) root.findViewById(R.id.spin);
        spin.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.item_string, getResources().getTextArray(R.array.attendance_type)));
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (dayType != position) {
                    dayType = position;
                    new GetUpdateTask()
                            .execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        root.findViewById(R.id.root).setOnClickListener(new OnClickListener() {
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

        root.findViewById(R.id.backImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        root.findViewById(R.id.deleteImage).setVisibility(View.GONE);
        root.findViewById(R.id.saveImage).setVisibility(View.GONE);

        return root;
    }
}
