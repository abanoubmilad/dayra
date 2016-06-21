package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactCheckAdapter;
import abanoubm.dayra.display.AddContactDetails;
import abanoubm.dayra.display.DisplayContact;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactCheck;
import abanoubm.dayra.model.IntWrapper;

public class RegisterAttendance extends Activity {
    private EditText sname;
    private ListView lv;
    private TextView addBtn, subhead2;
    private ProgressDialog pBar;
    private ContactCheck contact;
    private DB dbm;
    private int totalCount;
    private int dayType = 0;
    private int updatedCount;
    private ContactCheckAdapter mAdapter;

    private int previousPosition = 0;
    private String targetDay;

    private class AddDateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
            contact.setChecked(true);
            mAdapter.notifyDataSetChanged();
            subhead2.setText(++updatedCount + " / " + totalCount);
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbm.addDay(contact.getId(), dayType + "", targetDay);
            return null;
        }

    }

    private class RemoveDateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbm.removeDay(contact.getId(), dayType + "", targetDay);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            contact.setChecked(false);
            mAdapter.notifyDataSetChanged();
            subhead2.setText(--updatedCount + " / " + totalCount);
        }
    }

    private class GetAllUpdateDifTask extends
            AsyncTask<String, Void, ArrayList<ContactCheck>> {
        private String name;
        private IntWrapper temp = new IntWrapper();

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected ArrayList<ContactCheck> doInBackground(String... params) {
            name = params[0];
            return dbm.getDayAttendance(dayType + "", targetDay, params[0], temp);
        }

        @Override
        protected void onPostExecute(ArrayList<ContactCheck> result) {
            mAdapter.clear();
            mAdapter.addAll(result);

            if (result.size() > 0 || name.length() == 0) {
                if (previousPosition < result.size())
                    lv.setSelection(previousPosition);
                previousPosition = 0;
                addBtn.setVisibility(View.GONE);
            } else
                addBtn.setVisibility(View.VISIBLE);

            totalCount = result.size();
            updatedCount = temp.getCounter();
            subhead2.setText(updatedCount + " / " + totalCount);
            pBar.dismiss();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);

        ((TextView) findViewById(R.id.subhead1))
                .setText(R.string.subhead_register_attendance);
        subhead2 = (TextView) findViewById(R.id.subhead2);

        final TextView date = (TextView) findViewById(R.id.date);
        sname = (EditText) findViewById(R.id.sname_edittext);
        addBtn = (TextView) findViewById(R.id.sname_btn);
        lv = (ListView) findViewById(R.id.sname_list);

        Calendar cal = Calendar.getInstance();
        targetDay = Utility.produceDate(cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR));

        date.setText(targetDay);
        mAdapter = new ContactCheckAdapter(getApplicationContext(), new ArrayList<ContactCheck>());
        lv.setAdapter(mAdapter);

        final DatePickerDialog picker_date = new DatePickerDialog(this,
                new OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        targetDay = Utility.produceDate(dayOfMonth, monthOfYear + 1, year);
                        date.setText(targetDay);
                        sname.setVisibility(View.VISIBLE);
                        new GetAllUpdateDifTask().execute(sname.getText()
                                .toString().trim());
                    }

                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        findViewById(R.id.pick_date)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        picker_date.show();

                    }
                });

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                contact = mAdapter.getItem(position);
                if (contact.isChecked())
                    new RemoveDateTask().execute();
                else
                    new AddDateTask().execute();

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

        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        AddContactDetails.class).putExtra("name", sname.getText().toString().trim());
                startActivity(intent);
            }
        });

        pBar = new ProgressDialog(RegisterAttendance.this);
        pBar.setCancelable(false);

        dbm = DB.getInstant(getApplicationContext());

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
                new GetAllUpdateDifTask().execute(s.toString().trim());
            }

        });

        Spinner spin = (Spinner) findViewById(R.id.spin);
        spin.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                R.layout.item_string, getResources().getTextArray(R.array.attendance_type)));
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (dayType != position) {
                    dayType = position;
                    new GetAllUpdateDifTask()
                            .execute(sname.getText().toString().trim());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetAllUpdateDifTask()
                .execute(sname.getText().toString().trim());
    }
}
