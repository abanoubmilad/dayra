package abanoubm.dayra.display;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import abanoubm.dayra.R;
import abanoubm.dayra.main.Utility;

public class DisplayContact extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_display_contact);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_display_contact);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString("id", getIntent().getStringExtra("id"));

            FragmentDisplayContact fragment = new FragmentDisplayContact();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.display_contact_fragment, fragment)
                    .commit();

        }
    }
}
