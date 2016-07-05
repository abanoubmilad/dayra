package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import abanoubm.dayra.adapters.GContactsOutAdapter;
import abanoubm.dayra.contact.DisplayContact;
import abanoubm.dayra.main.ContactHelper;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactMobile;

public class CopyDayraPhone extends Activity {
    private CheckBox check;
    private GContactsOutAdapter mAdapter;
    private int previousPosition = 0;
    private ListView lv;
    private  DB mDB;

    private class CheckAllContactsTask extends AsyncTask<Boolean, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(CopyDayraPhone.this);
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
            //     ContactMobile temp;
            for (int i = 0; i < count; i++)
                //   temp =
                mAdapter.getItem(i).setSelected(params[0]);
            //   if (!temp.isExisted())
            //  temp.

            return null;
        }

    }

    private class GetContactsMobileTask extends
            AsyncTask<Void, Void, ArrayList<ContactMobile>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(CopyDayraPhone.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<ContactMobile> doInBackground(Void... params) {
            if(mDB==null)
                mDB =DB.getInstant(getApplicationContext());
            return mDB.getGContactsMobile(getContentResolver());
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

    private class CopyContactsMobileTask extends AsyncTask<Void, Void, Integer> {
        private ProgressDialog pBar;
        private int copied;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(CopyDayraPhone.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int count = mAdapter.getCount();
            ContactMobile temp;
            int failureCounter = 0;
            copied = 0;
            for (int i = 0; i < count; i++) {
                temp = mAdapter.getItem(i);
                if (temp.isSelected()) {
                    if (!ContactHelper.insertContact(getContentResolver(),
                            temp.getName(), temp.getMobile(), Utility.getBytes(temp.getPhoto())))
                        failureCounter++;
                    else
                        copied++;
                }
            }
            return failureCounter;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result != 0)
                Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(R.string.err_copy_contacts)
                                + " ( " + result + " )",
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mobiles);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_select_out);

        TextView CopyBtn = (TextView) findViewById(R.id.btn);
        CopyBtn.setText(R.string.subhead_select_out);

        check = (CheckBox) findViewById(R.id.check_all);
        check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckAllContactsTask().execute(check.isChecked());

            }
        });

        lv = (ListView) findViewById(R.id.contacts_list);
        mAdapter = new GContactsOutAdapter(getApplicationContext(),
                new ArrayList<ContactMobile>());
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                check.setChecked(false);
                mAdapter.getItem(position).invertSelected();
                mAdapter.notifyDataSetChanged();
            }
        });
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                previousPosition = lv.getFirstVisiblePosition();
                Intent intent = new Intent(getApplicationContext(),
                        DisplayContact.class);
                intent.putExtra("id", mAdapter.getItem(position).getId());
                startActivity(intent);
                return true;
            }
        });
        CopyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new CopyContactsMobileTask().execute();
            }
        });

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
