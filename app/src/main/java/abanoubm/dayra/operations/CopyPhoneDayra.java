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
import abanoubm.dayra.adapters.GContactsInAdapter;
import abanoubm.dayra.display.DisplayContact;
import abanoubm.dayra.main.ContactHelper;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactMobile;
import abanoubm.dayra.model.GoogleContact;

public class CopyPhoneDayra extends Activity {
    private CheckBox check;
    private GContactsInAdapter mAdapter;
    private int previousPosition = 0;
    private ListView lv;

    private class CheckAllContactsTask extends AsyncTask<Boolean, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(CopyPhoneDayra.this);
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
            GoogleContact temp;
            for (int i = 0; i < count; i++) {
                temp = mAdapter.getItem(i);
                if (!temp.isExisted())
                    temp.setSelected(params[0]);
            }
            return null;
        }

    }

    private class GetContactsMobileTask extends
            AsyncTask<Void, Void, ArrayList<GoogleContact>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(CopyPhoneDayra.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<GoogleContact> doInBackground(Void... params) {
            return ContactHelper.getGContacts(getContentResolver(),
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
                if (previousPosition < result.size())
                    lv.setSelection(previousPosition);
                previousPosition = 0;
            }

        }

    }

    private class CopyContactsMobileTask extends AsyncTask<Void, Void, Integer> {
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
            DB dbm = DB.getInstance(getApplicationContext(), Utility.getDayraName(getApplicationContext()));
            for (int i = 0; i < count; i++) {
                temp = mAdapter.getItem(i);
                if (temp.isSelected()) {
                    if (dbm.addAttendant(temp.getName(), temp.getMobile()).equals("-1"))
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
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_transfer);

        TextView CopyBtn = (TextView) findViewById(R.id.btn);
        CopyBtn.setText(R.string.btn_copy_selected);
        check = (CheckBox) findViewById(R.id.check_all);
        check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                new CheckAllContactsTask().execute(isChecked);
            }
        });

        lv = (ListView) findViewById(R.id.contacts_list);
        mAdapter = new GContactsInAdapter(getApplicationContext(),
                new ArrayList<GoogleContact>());
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                GoogleContact temp = mAdapter.getItem(position);
                if (!temp.isExisted()) {
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
        CopyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new CopyContactsMobileTask().execute();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        check.setChecked(false);
        new GetContactsMobileTask().execute();
    }
}
