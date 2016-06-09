package abanoubm.dayra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.GoogleContact;

public class GContactsInAdapter extends ArrayAdapter<GoogleContact> {
    public GContactsInAdapter(Context context,
                              ArrayList<GoogleContact> contacts) {
        super(context, 0, contacts);
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
            holder.root = convertView.findViewById(R.id.root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.name.setText(contact.getName());
        holder.mobile.setText(contact.getMobile());

        holder.flag1.setVisibility(contact.isExisted() ? View.VISIBLE : View.INVISIBLE);


        if (contact.isSelected())
            holder.root.setBackgroundColor(Utility.update);
        else
            holder.root.setBackgroundColor(Utility.deupdate);

        return convertView;
    }

    private static class ViewHolder {
        TextView name, flag1, mobile;
        View root;
    }
}