package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactUpdateAdapter;
import abanoubm.dayra.display.DisplayContact;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactUpdate;
import abanoubm.dayra.model.IntWrapper;

public class RegisterAttendance extends Activity {
    private EditText sname;
    private ListView lv;
    private TextView addBtn, rateView;
    private ProgressDialog pBar;
    private TextView flag;
    private ContactUpdate chosenAtt;
    private DB dbm;
    private int totalCount;
    private String dayType = "";
    private int updatedCount;

    private EditText edit_date;
    private int previousPosition = 0;
    private String targetDay = Calendar.getInstance().get(Calendar.YEAR) +
            "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) +
            "-" + Calendar.getInstance().get(
            Calendar.DAY_OF_MONTH);

    private class AddDateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
            chosenAtt.setDay("");
            chosenAtt.setSelected(true);
            flag.setBackgroundColor(Utility.update);
            rateView.setText(++updatedCount + " / " + totalCount);
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbm.addDay(chosenAtt.getId(), dayType, targetDay);
            return null;
        }

    }

    private class RemoveDateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbm.removeDay(chosenAtt.getId(), dayType, targetDay);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            chosenAtt.setDay("");
            chosenAtt.setSelected(false);
            flag.setBackgroundColor(Color.WHITE);
            rateView.setText(--updatedCount + " / " + totalCount);
        }
    }

    private class GetAllUpdateDifTask extends
            AsyncTask<String, Void, ArrayList<ContactUpdate>> {
        private String name;
        private IntWrapper temp = new IntWrapper();

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected ArrayList<ContactUpdate> doInBackground(String... params) {
            name = params[0];
            return dbm.getDayAttendance(dayType, targetDay, params[0], temp);
        }

        @Override
        protected void onPostExecute(ArrayList<ContactUpdate> result) {
            lv.setAdapter(new ContactUpdateAdapter(getApplicationContext(),
                    result));
            if (result.size() > 0 || name.length() == 0) {
                if (previousPosition < result.size())
                    lv.setSelection(previousPosition);
                previousPosition = 0;
                addBtn.setVisibility(View.GONE);
            } else
                addBtn.setVisibility(View.VISIBLE);

            totalCount = result.size();
            updatedCount = temp.getCounter();
            rateView.setText(updatedCount + " / " + totalCount);
            pBar.dismiss();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);
        ((TextView) findViewById(R.id.subhead1))
                .setText(R.string.subhead_register_attendance);
        rateView = (TextView) findViewById(R.id.subhead2);

        edit_date = (EditText) findViewById(R.id.edit_date);

        sname = (EditText) findViewById(R.id.sname_edittext);
        addBtn = (TextView) findViewById(R.id.sname_btn);
        lv = (ListView) findViewById(R.id.sname_list);

        Calendar cal = Calendar.getInstance();
        final DatePickerDialog picker_date = new DatePickerDialog(this,
                new OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        targetDay = dayOfMonth + "-" + (monthOfYear + 1) + "-"
                                + year;
                        edit_date.setText(targetDay);
                        sname.setVisibility(View.VISIBLE);
                        new GetAllUpdateDifTask().execute(sname.getText()
                                .toString().trim());
                    }

                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        ((ImageView) findViewById(R.id.pick_date))
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
                flag = (TextView) arg1.findViewById(R.id.flag);
                chosenAtt = (ContactUpdate) parent.getItemAtPosition(position);
                if (chosenAtt.isSelected())
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

                ContactUpdate att = (ContactUpdate) parent
                        .getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(),
                        DisplayContact.class);
                intent.putExtra("id", att.getId());
                startActivity(intent);
                return true;
            }
        });

        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        AddContact.class);
                intent.putExtra("name", sname.getText().toString().trim());
                startActivity(intent);
            }
        });

        pBar = new ProgressDialog(RegisterAttendance.this);
        pBar.setCancelable(false);

        dbm = DB.getInstance(
                getApplicationContext(),
                getSharedPreferences("login", Context.MODE_PRIVATE).getString(
                        "dbname", ""));

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
        edit_date.setText(targetDay);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (targetDay.length() > 0)
            new GetAllUpdateDifTask()
                    .execute(sname.getText().toString().trim());
    }
}
