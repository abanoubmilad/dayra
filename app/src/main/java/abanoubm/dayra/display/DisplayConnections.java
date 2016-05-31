package abanoubm.dayra.display;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.adapters.ContactIDAdapter;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.model.ContactID;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayConnections extends Activity {
	private ListView lv;
	private int hostID;
	private int previousPosition = 0;

	private class GetAllConnectionsTask extends
			AsyncTask<Void, Void, ArrayList<ContactID>> {
		private ProgressDialog pBar;

		@Override
		protected void onPreExecute() {
			pBar = new ProgressDialog(DisplayConnections.this);
			pBar.setCancelable(false);

			pBar.show();
		}

		@Override
		protected ArrayList<ContactID> doInBackground(Void... params) {
			return DB.getInstance(
					getApplicationContext(),
					getSharedPreferences("login", Context.MODE_PRIVATE)
							.getString("dbname", "")).getAttendantConnections(
					hostID);
		}

		@Override
		protected void onPostExecute(ArrayList<ContactID> result) {
			pBar.dismiss();
			if (result.size() > 0) {
				lv.setAdapter(new ContactIDAdapter(getApplicationContext(),
						result));
				if (previousPosition < result.size())
					lv.setSelection(previousPosition);
				previousPosition = 0;
			} else {

				finish();
				Toast.makeText(getApplicationContext(),
						R.string.msg_no_connections, Toast.LENGTH_SHORT).show();

			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_display_connections);

		((TextView) findViewById(R.id.subhead1))
				.setText(R.string.subhead_display_connections);
		((TextView) findViewById(R.id.subhead2)).setText(getIntent()
				.getStringExtra("name"));
		
		hostID = getIntent().getIntExtra("id", -1);



		lv = (ListView) findViewById(R.id.sname_list);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				previousPosition = lv.getFirstVisiblePosition();
				ContactID att = (ContactID) parent.getItemAtPosition(position);
				Intent intent = new Intent(getApplicationContext(),
						DisplayContact.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("id", att.getId());
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		new GetAllConnectionsTask().execute();
	}
}
