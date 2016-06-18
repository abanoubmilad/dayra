package abanoubm.dayra.display;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactLocation;

public class EditContact extends ActionBarActivity {
    private String id;
    private int current = 0;
    private ImageView[] buttons;

    private static final String ARG_LAT = "lat";
    private static final String ARG_LNG = "lon";
    private static final String ARG_ZOM = "zoom";
    private static final String ARG_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_contact);


        findViewById(R.id.subhead1).setVisibility(View.GONE);
        final TextView subhead2 = ((TextView) findViewById(R.id.subhead2));

        id = getIntent().getStringExtra(ARG_ID);

        buttons = new ImageView[]{
                (ImageView) findViewById(R.id.img1),
                (ImageView) findViewById(R.id.img2),
                (ImageView) findViewById(R.id.img3),
                (ImageView) findViewById(R.id.img4)
        };
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(ARG_ID, getIntent().getStringExtra(ARG_ID));

            subhead2.setText(R.string.subhead_edit_info);

            FragmentEditContactInfo fragment = new FragmentEditContactInfo();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();

        }

        buttons[0].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 0) {
                    buttons[current].setBackgroundColor(0);
                    current = 0;
                    buttons[0].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

                    subhead2.setText(R.string.subhead_edit_info);

                    Bundle arguments = new Bundle();
                    arguments.putString(ARG_ID, id);

                    FragmentEditContactInfo fragment = new FragmentEditContactInfo();
                    fragment.setArguments(arguments);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .commit();
                }

            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 1) {
                    buttons[current].setBackgroundColor(0);
                    current = 1;
                    buttons[1].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));

                    subhead2.setText(R.string.subhead_edit_day);

                    Bundle arguments = new Bundle();
                    arguments.putString(ARG_ID, id);

                    FragmentEditContactDay fragment = new FragmentEditContactDay();
                    fragment.setArguments(arguments);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .commit();
                }
            }
        });
        buttons[2].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current != 2) {
                    buttons[current].setBackgroundColor(0);
                    current = 2;
                    buttons[2].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

                    subhead2.setText(R.string.subhead_edit_connections);


                    Bundle arguments = new Bundle();
                    arguments.putString(ARG_ID, id);

                    FragmentEditContactConnection fragment = new FragmentEditContactConnection();
                    fragment.setArguments(arguments);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, fragment)
                            .commit();
                }

            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new GetLocationTask().execute();
                subhead2.setText(R.string.subhead_edit_map);
            }
        });

    }

    private class GetLocationTask extends AsyncTask<Void, Void, ContactLocation> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(EditContact.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(ContactLocation result) {
            if (current != 3) {
                buttons[current].setBackgroundColor(0);
                current = 3;
                buttons[3].setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

                Bundle arguments = new Bundle();
                arguments.putDouble(ARG_LAT, result.getMapLat());
                arguments.putDouble(ARG_LNG, result.getMapLng());
                arguments.putFloat(ARG_ZOM, result.getZoom());
                arguments.putString(ARG_ID, id);

                FragmentEditContactMap fragment = new FragmentEditContactMap();
                fragment.setArguments(arguments);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .commit();
            }
            pBar.dismiss();

        }

        @Override
        protected ContactLocation doInBackground(Void... params) {
            return DB.getInstant(getApplicationContext()).getContactLocation(id);
        }
    }
}
