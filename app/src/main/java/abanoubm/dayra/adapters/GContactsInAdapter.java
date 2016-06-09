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
    private final int white, highlight;

    public GContactsInAdapter(Context context,
                              ArrayList<GoogleContact> contacts) {
        super(context, 0, contacts);
        white = ContextCompat.getColor(context, R.color.white);
        highlight = ContextCompat.getColor(context, R.color.red);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        GoogleContact contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_gcontact_in, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.mobile = (TextView) convertView.findViewById(R.id.mobile);
            holder.flag1 = (TextView) convertView.findViewById(R.id.flag1);
            holder.flag2 = (CheckBox) convertView.findViewById(R.id.flag2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.name.setText(contact.getName());
        holder.mobile.setText(contact.getMobile());

        if (contact.isExisted())
            holder.flag1.setBackgroundColor(highlight);
        else
            holder.flag1.setBackgroundColor(white);

        holder.flag2.setChecked(contact.isSelected());

        return convertView;
    }

    private static class ViewHolder {
        TextView name, flag1, mobile;
        CheckBox flag2;
    }
}