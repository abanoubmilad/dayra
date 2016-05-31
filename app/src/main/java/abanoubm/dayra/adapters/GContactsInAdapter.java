package abanoubm.dayra.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.model.GoogleContact;

public class GContactsInAdapter extends ArrayAdapter<GoogleContact> {
    private final int  white, highlight;

    public GContactsInAdapter(Context context,
                              ArrayList<GoogleContact> contacts) {
        super(context, 0, contacts);
        white = ContextCompat.getColor(context, R.color.white);
        highlight = ContextCompat.getColor(context, R.color.red);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GoogleContact contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_gcontact_in, parent, false);
        }


        ((TextView) convertView.findViewById(R.id.name)).setText(contact.getName());
        ((TextView) convertView.findViewById(R.id.mobile)).setText(contact.getMobile());

        if (contact.isExisted())
            convertView.findViewById(R.id.flag1).setBackgroundColor(highlight);
        else
            convertView.findViewById(R.id.flag1).setBackgroundColor(white);
        CheckBox flag = (CheckBox) convertView.findViewById(R.id.flag2);
        flag.setChecked(contact.isSelected());

        return convertView;
    }
}