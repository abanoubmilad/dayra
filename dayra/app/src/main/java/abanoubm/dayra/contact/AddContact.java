package abanoubm.dayra.contact;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import abanoubm.dayra.R;
import abanoubm.dayra.contacts.CallBack;
import abanoubm.dayra.main.Utility;

public class AddContact extends AppCompatActivity implements CallBack {
    private ImageView[] buttons;
    private static final String ARG_ID = "id";
    private static final String ARG_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_contact);

        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_add_contact);
        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        buttons = new ImageView[]{
                (ImageView) findViewById(R.id.img1),
                (ImageView) findViewById(R.id.img2)
        };
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            String name = getIntent().getStringExtra(ARG_NAME);
            if (name != null)
                arguments.putString(ARG_NAME, name);

            FragmentAddContactInfo fragment = new FragmentAddContactInfo();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();

        }

    }

    @Override
    public void notify(String id) {
        buttons[0].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.grey));
        buttons[1].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.white));
        Bundle arguments = new Bundle();
        arguments.putString(ARG_ID, id);

        FragmentEditContactMap fragment = new FragmentEditContactMap();
        fragment.setArguments(arguments);

        ((InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();

    }
}

