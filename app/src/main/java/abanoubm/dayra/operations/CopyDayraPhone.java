package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.GContactsOutAdapter;
import abanoubm.dayra.display.DisplayContactDetails;
import abanoubm.dayra.main.ContactHelper;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactMobile;

public class CopyDayraPhone extends Activity {
    private CheckBox check;
    private GContactsOutAdapter mAdapter;
    private int previousPosition = 0;
    private ListView lv;

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
            ContactMobile temp;
            for (int i = 0; i < count; i++) {
                temp = mAdapter.getItem(i);
                if (!temp.isExisted())
                    temp.setSelected(params[0]);
            }
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
            return DB.getInstant(getApplicationContext()).getGContactsMobile(getContentResolver());
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
                    byte[] photo = null;
                    if (temp.getPicDir().length() != 0
                            && new File(temp.getPicDir()).exists()) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ThumbnailUtils.extractThumbnail(
                                BitmapFactory.decodeFile(temp.getPicDir()), 250, 250).compress(Bitmap.CompressFormat.PNG, 90, baos);
                        photo = baos.toByteArray();
                    }
                    if (!ContactHelper.insertContact(getContentResolver(),
                            temp.getName(), temp.getMobile(), photo))
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
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_transfer);

        TextView CopyBtn = (TextView) findViewById(R.id.btn);
        CopyBtn.setText(R.string.subhead_select_out);
        check = (CheckBox) findViewById(R.id.check_all);
        check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                new CheckAllContactsTask().execute(isChecked);
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
                ContactMobile temp = mAdapter.getItem(position);
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
                        DisplayContactDetails.class);
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
