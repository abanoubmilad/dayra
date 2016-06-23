package abanoubm.dayra.contact;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import abanoubm.dayra.R;
import abanoubm.dayra.contact.FragmentAddContactInfo;
import abanoubm.dayra.contact.FragmentEditContactMap;
import abanoubm.dayra.display.CallBack;
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
        buttons[1].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
        Bundle arguments = new Bundle();
        arguments.putString(ARG_ID, id);

        FragmentEditContactMap fragment = new FragmentEditContactMap();
        fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();

    }
}

