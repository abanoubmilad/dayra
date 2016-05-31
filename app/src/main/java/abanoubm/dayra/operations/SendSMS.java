package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactMobileAdapter;
import abanoubm.dayra.display.DisplayContact;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactMobile;

public class SendSMS extends Activity {
    private ListView lv;
    private ProgressDialog pBar;
    private DB dbm;
    private ContactMobileAdapter mAdapter;
    private CheckBox check;
    private int previousPosition = 0;

    private class CheckAllContactsTask extends AsyncTask<Boolean, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(SendSMS.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();
            pBar.dismiss();
        }

        @Override
        protected Void doInBackground(Boolean... params) {
            int count = mAdapter.getCount();
            ContactMobile temp;
            for (int i = 0; i < count; i++) {
                temp = mAdapter.getItem(i);
                if (temp.isExisted())
                    temp.setSelected(params[0]);
            }
            return null;
        }

    }

    private class SendTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(SendSMS.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.length() != 0)
                startActivity(new Intent(Intent.ACTION_SENDTO,
                        Uri.parse("smsto:" + result)));
            else
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_mobiles, Toast.LENGTH_SHORT).show();
            pBar.dismiss();
        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder phoneNumbers = new StringBuilder("");
            String sep = "; ";
            if (android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung"))
                sep = ", ";

            int count = mAdapter.getCount();
            ContactMobile temp;
            count--;
            for (int i = 0; i < count; i++) {
                temp = mAdapter.getItem(i);
                if (temp.isSelected()) {
                    phoneNumbers.append(temp.getMobile());
                    phoneNumbers.append(sep);
                }
            }
            if (count >= 0) {
                temp = mAdapter.getItem(count);
                if (temp.isSelected())
                    phoneNumbers.append(temp.getMobile());
            }

            return phoneNumbers.toString();
        }

    }

    private class GetContactsMobileTask extends
            AsyncTask<Void, Void, ArrayList<ContactMobile>> {
        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected ArrayList<ContactMobile> doInBackground(Void... params) {
            return dbm.getContactsMobile();
        }

        @Override
        protected void onPostExecute(ArrayList<ContactMobile> result) {
            pBar.dismiss();

            if (result.size() == 0) {
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            } else {
                mAdapter.clear();
                mAdapter.addAll(result);
                if (previousPosition < result.size())
                    lv.setSelection(previousPosition);
                previousPosition = 0;
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mobiles);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_sms);

        check = (CheckBox) findViewById(R.id.check_all);
        check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                new CheckAllContactsTask().execute(isChecked);
            }
        });

        lv = (ListView) findViewById(R.id.contacts_list);
        mAdapter = new ContactMobileAdapter(getApplicationContext(),
                new ArrayList<ContactMobile>());
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                ContactMobile temp = (ContactMobile) parent
                        .getItemAtPosition(position);
                if (temp.isExisted()) {
                    check.setChecked(false);
                    temp.invertSelected();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                previousPosition = lv.getFirstVisiblePosition();

                ContactMobile temp = (ContactMobile) parent
                        .getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(),
                        DisplayContact.class);
                intent.putExtra("id", temp.getId());
                startActivity(intent);
                return true;
            }
        });
        findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendTask().execute();
            }
        });

        pBar = new ProgressDialog(SendSMS.this);
        pBar.setCancelable(false);

        dbm = DB.getInstant(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        check.setChecked(false);
        new GetContactsMobileTask().execute();
    }
}
