package abanoubm.dayra.opr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapt.GoogleContactAdapter;
import abanoubm.dayra.main.ContactHelper;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.obj.GoogleContact;

public class CopyPhoneDayra extends Activity {
    private ListView lv;
    private View chosenView;
    private GoogleContact chosenAtt;
    private GoogleContactAdapter mAdapter;

    private ProgressDialog pBar;
    private DB dbm;

    private class ImportContactTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                chosenAtt.setSelected(true);
                chosenView.setBackgroundColor(Utility.update);
            }
            pBar.dismiss();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return dbm.addAttendant(chosenAtt.getName(), chosenAtt.getMobile()) != -1;
        }

    }

    private class ImportAllContactsTask extends AsyncTask<Void, Void, Integer> {
        private ProgressDialog pBar;
        private int copied;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(CopyPhoneDayra.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int count = mAdapter.getCount();
            GoogleContact temp;
            int failureCounter = 0;
            copied = 0;
            for (int i = 0; i < count; i++) {
                temp = mAdapter.getItem(i);
                if (!temp.isSelected()) {
                    if (dbm.addAttendant(temp.getName(), temp.getMobile()) == -1)
                        failureCounter++;
                    else {
                        temp.setSelected(true);
                        copied++;
                    }
                }
            }
            return failureCounter;
        }

        @Override
        protected void onPostExecute(Integer result) {
            mAdapter.notifyDataSetChanged();

            if (result.intValue() != 0)
                Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(R.string.err_copy_contacts)
                                + " ( " + result.intValue() + " )",
                        Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(
                        getApplicationContext(),
                        " ( "
                                + copied
                                + " ) "
                                + getResources().getString(
                                R.string.msg_copy_success),
                        Toast.LENGTH_SHORT).show();

            pBar.dismiss();
        }

    }

    private class GetGoogleContactsTask extends
            AsyncTask<String, Void, ArrayList<GoogleContact>> {

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected ArrayList<GoogleContact> doInBackground(String... params) {
            return ContactHelper.getContacts(getContentResolver(),
                    getApplicationContext());
        }

        @Override
        protected void onPostExecute(ArrayList<GoogleContact> result) {
            pBar.dismiss();
            if (result.size() == 0) {
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            } else {
                mAdapter.clear();
                mAdapter.addAll(result);
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_import);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_import_contacts);


        ((TextView) findViewById(R.id.copy_btn))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new ImportAllContactsTask().execute();

                    }
                });

        lv = (ListView) findViewById(R.id.contacts_list);
        mAdapter = new GoogleContactAdapter(getApplicationContext(),
                new ArrayList<GoogleContact>());
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                chosenView = arg1;
                chosenAtt = (GoogleContact) parent.getItemAtPosition(position);
                if (!chosenAtt.isSelected())
                    new ImportContactTask().execute();

            }
        });

        pBar = new ProgressDialog(CopyPhoneDayra.this);
        pBar.setCancelable(false);

        dbm = DB.getInstance(
                getApplicationContext(),
                getSharedPreferences("login", Context.MODE_PRIVATE).getString(
                        "dbname", ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetGoogleContactsTask().execute();
    }
}
