package abanoubm.dayra.operations;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactIDAdapter;
import abanoubm.dayra.display.DisplayContact;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.model.ContactID;

public class FragmentSearchInfo extends Fragment {
    private EditText input;
    private ContactIDAdapter mAdapter;
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
            , DB.CONTACT_ADDR
            , DB.CONTACT_NOTES
            , DB.CONTACT_SUPERVISOR
    };

    private class SearchTask extends
            AsyncTask<String, Void, ArrayList<ContactID>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<ContactID> doInBackground(String... params) {
            return DB.getInstant(getActivity()).search(params[0], searchTags[currentTag]);
        }

        @Override
        protected void onPostExecute(ArrayList<ContactID> result) {
            mAdapter.clear();
            mAdapter.addAll(result);
            if (result.size() == 0)
                Toast.makeText(getActivity(),
                        R.string.msg_no_results, Toast.LENGTH_SHORT).show();
            pBar.dismiss();

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

                currentTag = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mAdapter = new ContactIDAdapter(getActivity(), new ArrayList<ContactID>());
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
        return root;
    }

}
