package abanoubm.dayra.contact;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import abanoubm.dayra.R;

public class DisplayContact extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_display_contact);

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
