package abanoubm.dayra.display;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.model.ContactData;

public class FragmentDisplayContactDay extends Fragment {

    private TextView dis_last_visit, dis_last_attend;
    private ContactData attData;
    private String id = "-1";
    private ListView monthList, dayList, yearList;

    private class GetTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            dis_last_attend.setText(attData.getLastAttend());
            dis_last_visit.setText(attData.getLastVisit());
            pBar.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            attData = DB.getInstant(getActivity()).getAttendantData(id);
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_display_contact_day, container, false);

        dayList = (ListView) root.findViewById(R.id.dayList);
        monthList = (ListView) root.findViewById(R.id.monthList);
        yearList = (ListView) root.findViewById(R.id.yearList);

        dis_last_visit = (TextView) root.findViewById(R.id.dis_lvisit);
        dis_last_attend = (TextView) root.findViewById(R.id.dis_last_attend);

        new GetTask().execute();

        root.findViewById(R.id.dis_last_attend)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                getActivity());
                        builder.setTitle(R.string.label_attendance_dates);
                        builder.setItems(attData
                                .getAttendDates().split(";"), null);
                        builder.create().show();
                    }
                });


        return root;

    }


}
