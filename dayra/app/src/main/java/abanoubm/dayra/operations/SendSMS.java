package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactMobileAdapter;
import abanoubm.dayra.contact.DisplayContact;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactMobile;

public class SendSMS extends Activity {
    private ListView lv;
    private ProgressDialog pBar;
    private DB mDB;
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
            if(mDB==null)
                mDB =DB.getInstant(getApplicationContext());
            return mDB.getContactsMobile();
        }

        @Override
        protected void onPostExecute(ArrayList<ContactMobile> result) {
            pBar.dismiss();
            if (previousPosition >= result.size())
                previousPosition = 0;
            if (result.size() == 0) {
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            } else {
                mAdapter.clearThenAddAll(result);
                if (previousPosition < result.size())
                    lv.setSelection(previousPosition);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mobiles);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_sms);
        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        check = (CheckBox) findViewById(R.id.check_all);

        check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckAllContactsTask().execute(check.isChecked());

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
                ContactMobile temp = mAdapter.getItem(position);
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

                Intent intent = new Intent(getApplicationContext(),
                        DisplayContact.class).putExtra("id", mAdapter.getItem(position).getId());
                startActivity(intent);
                return true;
            }
        });
        FloatingActionButton sendBtn = (FloatingActionButton) findViewById(R.id.btn);
        sendBtn.setImageResource(R.mipmap.ic_btn_sms);
        sendBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendTask().execute();
            }
        });

        pBar = new ProgressDialog(SendSMS.this);
        pBar.setCancelable(false);

        new GetContactsMobileTask().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DB.getInstant(getApplicationContext()).isDirty()) {
            check.setChecked(false);
            new GetContactsMobileTask().execute();
            DB.getInstant(getApplicationContext()).clearDirty();
        }
    }
}
