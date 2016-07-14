package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;

public class DivideDayra extends Activity {
    private String chosenTag = DB.CONTACT_SITE;

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
            String path = Utility.getDayraFolder() + "/"
                    + Utility.getDayraName(getApplicationContext()) + "_divided/";
            File file = new File(path);
            if (file.exists()) {
                File[] contents = file.listFiles();
                if (contents != null) {
                    for (File f : contents)
                        f.delete();

                }
            } else {
                file.mkdirs();
            }
            DB db = DB.getInstant(getApplicationContext());

            ArrayList<String> dividerList = db.getDividerList(chosenTag);
            if (dividerList.size() < 2)
                return false;

            try {
                DB.getInstant(getApplicationContext()).divideDayra(chosenTag, dividerList,
                        getApplicationContext(), path);
                return true;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pBar.dismiss();
            if (result == null)
                Toast.makeText(getApplicationContext(),
                        R.string.err_dayra_divide, Toast.LENGTH_SHORT).show();
            else if (result)
                Toast.makeText(getApplicationContext(), R.string.msg_dayra_divided,
                        Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), R.string.msg_dayra_no_divider,
                        Toast.LENGTH_SHORT).show();


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_divide_dayra);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_divide);

        final RadioGroup radio = (RadioGroup) findViewById(R.id.radio);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.class_year)
                    chosenTag = DB.CONTACT_CLASS_YEAR;
                else if (checkedId == R.id.study_work)
                    chosenTag = DB.CONTACT_STUDY_WORK;
                else if (checkedId == R.id.site)
                    chosenTag = DB.CONTACT_SITE;
                else if (checkedId == R.id.street)
                    chosenTag = DB.CONTACT_ST;
                else if (checkedId == R.id.supervisor)
                    chosenTag = DB.CONTACT_SUPERVISOR;
            }
        });

        findViewById(R.id.divide_btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                new DivideTask().execute();

            }

        });
    }
}
