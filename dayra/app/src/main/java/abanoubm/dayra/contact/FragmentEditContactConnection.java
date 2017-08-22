package abanoubm.dayra.contact;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactCheckAdapter;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.model.ContactCheck;

public class FragmentEditContactConnection extends Fragment {
    private static final String ARG_ID = "id";
    private ContactCheckAdapter mAdapter;
    private String id;
    private EditText sname;
    private ListView lv;
    private DB mDB;
    private int previousPosition = 0;
    private ContactCheck contact;
    private ProgressDialog pBar;

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
        View root = inflater.inflate(R.layout.fragment_edit_contact_connection, container, false);


        sname = (EditText) root.findViewById(R.id.input);

        lv = (ListView) root.findViewById(R.id.sname_list);
        mAdapter = new ContactCheckAdapter(getContext(), new ArrayList<ContactCheck>(0), 1);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                contact = mAdapter.getItem(position);
                if (contact.isChecked())
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
        return root;
    }


    private class ConnectTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
            contact.setChecked(true);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mDB.addConnection(id, contact.getId());
            return null;
        }

    }

    private class DeConnectTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            mDB.removeConnection(id, contact.getId());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            contact.setChecked(false);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class GetAllConnectionsTask extends
            AsyncTask<String, Void, ArrayList<ContactCheck>> {
        private String name;

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected ArrayList<ContactCheck> doInBackground(String... params) {
            if (mDB == null)
                mDB = DB.getInstant(getActivity());

            name = params[0];
            return mDB.getAttendantConnections(id, name);
        }

        @Override
        protected void onPostExecute(ArrayList<ContactCheck> result) {
            if (result.size() == 0) {
                Toast.makeText(getActivity(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            } else {
                mAdapter.clearThenAddAll(result);
                if (name.length() == 0) {
                    if (previousPosition < result.size())
                        lv.setSelection(previousPosition);
                    previousPosition = 0;
                }
            }
            pBar.dismiss();

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        new GetAllConnectionsTask().execute(sname.getText().toString().trim());
    }
}

