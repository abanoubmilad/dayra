package abanoubm.dayra.display;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import abanoubm.dayra.R;
import abanoubm.dayra.main.Utility;

public class DisplayContacts extends ActionBarActivity implements CallBack {

    private boolean dualMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_display_contacts);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView)  findViewById(R.id.subhead2)).setText(R.string.subhead_display_contacts);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.display_contacts_fragment, new FragmentDisplayContacts())
                    .commit();
        }

        if (findViewById(R.id.display_contacts_fragment_dual) != null)
            dualMode = true;
        else
            dualMode = false;

    }

    @Override
    public void onItemSelected(String id) {
        if (dualMode) {
            Bundle args = new Bundle();
            args.putString("id", id);
            args.putBoolean("isdual", dualMode);

            FragmentDisplayContact fragment = new FragmentDisplayContact();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.display_contact_fragment, fragment)
                    .commit();

        } else {
            startActivity(new Intent(this, DisplayContact.class).putExtra("id", id));
        }

    }
}
