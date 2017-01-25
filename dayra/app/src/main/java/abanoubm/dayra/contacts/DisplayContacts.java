package abanoubm.dayra.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import abanoubm.dayra.R;
import abanoubm.dayra.contact.DisplayContact;
import abanoubm.dayra.contact.FragmentDisplayContact;
import abanoubm.dayra.main.Utility;

public class DisplayContacts extends AppCompatActivity implements CallBack {

    private boolean dualMode;
    private static final String ARG_ID = "id";
    private static final String ARG_DUAL_MODE = "dual";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_display_contacts);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_display_contacts);
        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        dualMode = findViewById(R.id.display_contacts_fragment_dual) != null;

        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putBoolean(ARG_DUAL_MODE, dualMode);
            FragmentDisplayContacts fragment = new FragmentDisplayContacts();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.display_contacts_fragment, fragment)
                    .commit();
        }


    }

    @Override
    public void notify(String id) {
        if (dualMode) {
            Bundle args = new Bundle();
            args.putString(ARG_ID, id);
            args.putBoolean(ARG_DUAL_MODE, true);

            FragmentDisplayContact fragment = new FragmentDisplayContact();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.display_contact_fragment, fragment)
                    .commit();

        } else {
            startActivity(new Intent(this, DisplayContact.class).putExtra(ARG_ID, id));
        }

    }
}
