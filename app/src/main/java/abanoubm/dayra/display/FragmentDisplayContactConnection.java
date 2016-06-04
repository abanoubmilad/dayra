package abanoubm.dayra.display;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactIDAdapter;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.model.ContactID;

public class FragmentDisplayContactConnection extends Fragment {

    private ListView lv;
    private String id = "-1";
    private int previousPosition = 0;
    private ContactIDAdapter mAdapter;

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
        View root = inflater.inflate(R.layout.fragment_display_contact_connection, container, false);


        lv = (ListView) root.findViewById(R.id.sname_list);
        mAdapter = new ContactIDAdapter(getContext(), new ArrayList<ContactID>(0));
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                previousPosition = lv.getFirstVisiblePosition();
                Intent intent = new Intent(getActivity(),
                        DisplayContactDetails.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id", mAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });
        return root;

    }


    private class GetAllConnectionsTask extends
            AsyncTask<Void, Void, ArrayList<ContactID>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);

            pBar.show();
        }

        @Override
        protected ArrayList<ContactID> doInBackground(Void... params) {
            return DB.getInstant(getActivity()).getAttendantConnections(
                    id);
        }

        @Override
        protected void onPostExecute(ArrayList<ContactID> result) {
            mAdapter.clear();
            mAdapter.addAll(result);
            pBar.dismiss();

            if (result.size() > 0) {
                if (previousPosition < result.size())
                    lv.setSelection(previousPosition);
                previousPosition = 0;
            } else {
                Toast.makeText(getActivity(),
                        R.string.msg_no_connections, Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        new GetAllConnectionsTask().execute();
    }

}
