package abanoubm.dayra.contact;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import abanoubm.dayra.R;

public class DisplayContact extends AppCompatActivity {
    private static final String ARG_ID = "id";
    private static final String ARG_DUAL_MODE = "dual";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_display_contact);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(ARG_ID, getIntent().getStringExtra(ARG_ID));
            arguments.putBoolean(ARG_DUAL_MODE, false);

            FragmentDisplayContact fragment = new FragmentDisplayContact();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.display_contact_fragment, fragment)
                    .commit();

        }
    }
}
