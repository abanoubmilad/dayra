package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;

public class DivideDayra extends Activity {
    private TextView divideBtn, tv_class_year;
    private CheckBox cb_class_year;
    private ImageView spin_class_year;

    private class GetClassYearsTask extends AsyncTask<Void, Void, String[]> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(DivideDayra.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected String[] doInBackground(Void... params) {
            int temp = DB.getInstant(getApplicationContext()).getClassYearsCount();
            if (temp == 0)
                return null;
            String[] arr = new String[temp];
            temp++;
            for (int i = 1; i < temp; i++)
                arr[i - 1] = i + "";
            return arr;
        }

        @Override
        protected void onPostExecute(final String[] result) {
            pBar.dismiss();
            if (result == null)
                cb_class_year.setChecked(false);
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        DivideDayra.this);
                builder.setTitle(getResources().getString(R.string.label_tolerance));
                builder.setItems(result,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                tv_class_year.setText(which + 1 + "");
                            }

                        });
                builder.create().show();
            }
        }
    }

    private class DivideTask extends AsyncTask<Boolean, Void, Boolean> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(DivideDayra.this);
            pBar.setMessage(getResources().getString(R.string.label_loading));
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
            String path;
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED)) {
                path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/";
            } else {
                path = android.os.Environment.getDataDirectory()
                        .getAbsolutePath() + "/";
            }
            path += "dayra folder/"
                    + getSharedPreferences("login", Context.MODE_PRIVATE)
                    .getString("dbname", "") + "_divided/";
            new File(path).mkdirs();
            String class_year = tv_class_year.getText().toString().trim();
            try {
                if (class_year.equals(""))
                    DB.getInstant(getApplicationContext()).divideDayra(
                            getApplicationContext(), path);
                else
                    DB.getInstant(getApplicationContext()).divideDayra(
                            getApplicationContext(),
                            Integer.parseInt(class_year), path);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pBar.dismiss();
            if (result)
                Toast.makeText(getApplicationContext(), R.string.msg_dayra_divided,
                        Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),
                        R.string.err_dayra_divide, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_divide_dayra);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_divide);

        divideBtn = (TextView) findViewById(R.id.divide_btn);
        tv_class_year = (TextView) findViewById(R.id.tv_classyear);
        spin_class_year = (ImageView) findViewById(R.id.spin_class_year);
        cb_class_year = (CheckBox) findViewById(R.id.cb_classyear);
        spin_class_year.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cb_class_year.isChecked())
                    new GetClassYearsTask().execute();

            }
        });
        cb_class_year.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked)
                    new GetClassYearsTask().execute();
            }
        });
        divideBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                new DivideTask().execute();

            }

        });
    }
}
