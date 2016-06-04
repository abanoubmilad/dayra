package abanoubm.dayra.display;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactConnectionAdapter;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactConnection;

public class FragmentEditContactConnection extends Fragment {
    private static final String ARG_PARAM1 = "id";

    private String mParam1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_contact_connection, container, false);


        sname = (EditText) root.findViewById(R.id.sname_edittext);

        lv = (ListView) root.findViewById(R.id.sname_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                flag = (TextView) arg1.findViewById(R.id.flag);
                chosenAtt = (ContactConnection) parent
                        .getItemAtPosition(position);
                if (chosenAtt.isCon())
                    new DeConnectTask().execute();
                else
                    new ConnectTask().execute();

            }
        });
        sname.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                new GetAllConnectionsTask().execute(s.toString().trim());
            }

        });

        pBar = new ProgressDialog(getActivity());
        pBar.setCancelable(false);

        dbm = DB.getInstant(getActivity());

        return root;
    }

    private EditText sname;
    private ListView lv;
    private TextView flag;
    private ContactConnection chosenAtt;
    private ProgressDialog pBar;
    private DB dbm;
    private String hostID;

    private int previousPosition = 0;

    private class ConnectTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            chosenAtt.setCon(true);
            flag.setBackgroundColor(Utility.update);
            pBar.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbm.addConnection(hostID, chosenAtt.getId());
            return null;
        }

    }

    private class DeConnectTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbm.removeConnection(hostID, chosenAtt.getId());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            chosenAtt.setCon(false);
            flag.setBackgroundColor(Color.WHITE);
            pBar.dismiss();
        }
    }

    private class GetAllConnectionsTask extends
            AsyncTask<String, Void, ArrayList<ContactConnection>> {
        private String name;

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected ArrayList<ContactConnection> doInBackground(String... params) {
            name = params[0];
            return dbm.getAttendantConnections(hostID, name);
        }

        @Override
        protected void onPostExecute(ArrayList<ContactConnection> result) {
            pBar.dismiss();
            if (result.size() == 0) {
           //     finish();
                Toast.makeText(getActivity(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            } else {
                lv.setAdapter(new ContactConnectionAdapter(
                        getActivity(), result));
                if (name.length() == 0) {
                    if (previousPosition < result.size())
                        lv.setSelection(previousPosition);
                    previousPosition = 0;
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        new GetAllConnectionsTask().execute(sname.getText().toString().trim());
    }
}

