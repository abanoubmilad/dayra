package abanoubm.dayra.opr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapt.ContactConnectionAdapter;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.obj.ContactConnection;

public class EditConnections extends Activity {
    private EditText sname;
    private ListView lv;
    private View chosenView;
    private ContactConnection chosenAtt;
    private ProgressDialog pBar;
    private DB dbm;
    private int hostID;

    private int previousPosition = 0;

    private class ConnectTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            chosenAtt.setCon(true);
            chosenView.setBackgroundColor(Utility.update);
            pBar.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbm.addConnection(hostID, chosenAtt.getId());
            return null;
        }

    }

    private class DeConnectTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbm.removeConnection(hostID, chosenAtt.getId());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            chosenAtt.setCon(false);
            chosenView.setBackgroundColor(Utility.deupdate);
            pBar.dismiss();
        }
    }

    private class GetAllConnectionsTask extends
            AsyncTask<String, Void, ArrayList<ContactConnection>> {
        private String name;

        @Override
        protected void onPreExecute() {
            pBar.show();
        }

        @Override
        protected ArrayList<ContactConnection> doInBackground(String... params) {
            name = params[0];
            return dbm.getAttendantConnections(hostID, name);
        }

        @Override
        protected void onPostExecute(ArrayList<ContactConnection> result) {
            pBar.dismiss();
            if (result.size() == 0) {
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            } else {
                lv.setAdapter(new ContactConnectionAdapter(
                        getApplicationContext(), result));
                if (name.length() == 0) {
                    if (previousPosition < result.size())
                        lv.setSelection(previousPosition);
                    previousPosition = 0;
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_connections);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2))
                .setText(R.string.subhead_edit_connections);

        sname = (EditText) findViewById(R.id.sname_edittext);
        hostID = getIntent().getIntExtra("id", -1);

        ((TextView) findViewById(R.id.subhead2)).setText(getIntent()
                .getStringExtra("name"));

        lv = (ListView) findViewById(R.id.sname_list);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1,
                                    int position, long arg3) {
                chosenView = arg1;
                chosenAtt = (ContactConnection) parent
                        .getItemAtPosition(position);
                if (chosenAtt.isCon())
                    new DeConnectTask().execute();
                else
                    new ConnectTask().execute();

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
                new GetAllConnectionsTask().execute(s.toString().trim());
            }

        });

        pBar = new ProgressDialog(EditConnections.this);
        pBar.setCancelable(false);

        dbm = DB.getInstance(
                getApplicationContext(),
                getSharedPreferences("login", Context.MODE_PRIVATE).getString(
                        "dbname", ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetAllConnectionsTask().execute(sname.getText().toString().trim());
    }
}
