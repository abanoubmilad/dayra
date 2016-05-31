package abanoubm.dayra.operations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactIDAdapter;
import abanoubm.dayra.display.DisplayContact;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactID;

public class SearchName extends Activity {
    private EditText sname;
    private TextView addBtn;
    private ListView lv;

    private class SearchNameTask extends
            AsyncTask<String, Void, ArrayList<ContactID>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(SearchName.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected ArrayList<ContactID> doInBackground(String... params) {
            return DB.getInstance(
                    getApplicationContext(),
                    getSharedPreferences("login", Context.MODE_PRIVATE)
                            .getString("dbname", "")).searchName(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<ContactID> att) {
            lv.setAdapter(new ContactIDAdapter(getApplicationContext(), att));
            if (att.size() > 0) {
                addBtn.setVisibility(View.GONE);
            } else {
                addBtn.setVisibility(View.VISIBLE);
            }
            pBar.dismiss();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search_name);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_search_name);

        lv = (ListView) findViewById(R.id.sname_list);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {

                ContactID att = (ContactID) parent
                        .getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(),
                        DisplayContact.class);
                intent.putExtra("id", att.getId());
                startActivity(intent);

            }
        });
        sname = (EditText) findViewById(R.id.sname_edittext);
        addBtn = (TextView) findViewById(R.id.sname_btn);
        addBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        AddContact.class);
                intent.putExtra("name", sname.getText().toString()
                        .trim());
                startActivity(intent);
            }
        });
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
                String str = sname.getText().toString().trim();
                if (str.length() > 0)
                    new SearchNameTask().execute(str);

            }
        });
    }
}
