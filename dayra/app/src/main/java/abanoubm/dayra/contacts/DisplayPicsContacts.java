package abanoubm.dayra.contacts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactIDPicAdapter;
import abanoubm.dayra.contact.DisplayContact;
import abanoubm.dayra.contact.FragmentDisplayContact;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactID;

public class DisplayPicsContacts extends Activity {

    private static final String ARG_ID = "id";
    private GridView lv;
    private int previousPosition = 0;
    private ContactIDPicAdapter mAdapter;
    private DB mDB;

    private class GetAllTask extends AsyncTask<Void, Void, ArrayList<ContactID>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(DisplayPicsContacts.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<ContactID> doInBackground(Void... params) {
            if (mDB == null)
                mDB = DB.getInstant(getApplicationContext());
            return mDB.getPicsContactsDisplayList();
        }

        @Override
        protected void onPostExecute(ArrayList<ContactID> list) {
            mAdapter.clearThenAddAll(list);
            if (list.size() == 0) {
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            } else {
                if (previousPosition < list.size())
                    lv.setSelection(previousPosition);
            }
            pBar.dismiss();

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (DB.getInstant(getApplicationContext()).isDirty()) {
            new GetAllTask().execute();
            DB.getInstant(getApplicationContext()).clearDirty();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pics_display_contacts);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_display_contacts);
        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });


        lv = (GridView) findViewById(R.id.list);
        mAdapter = new ContactIDPicAdapter(getApplicationContext(), new ArrayList<ContactID>(0));
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                previousPosition = lv.getFirstVisiblePosition();
                startActivity(new Intent(getApplicationContext(), DisplayContact.class).putExtra(ARG_ID, ((ContactID) parent
                        .getItemAtPosition(position)).getId()));

            }
        });
        new GetAllTask().execute();

    }

}
