package abanoubm.dayra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.model.ContactCheck;

public class ContactCheckAdapter extends ArrayAdapter<ContactCheck> {
    private String flagStr;

    public ContactCheckAdapter(Context context,
                               ArrayList<ContactCheck> contacts, int type) {
        super(context, 0, contacts);
        flagStr = context.getResources().getText(
                type == 0 ? R.string.flag_contact_attend : R.string.flag_contact_con).toString();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_contact_check, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.flag = (TextView) convertView.findViewById(R.id.flag);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ContactCheck contact = getItem(position);

        holder.name.setText(contact.getName());


        if (contact.isChecked()) {
            holder.flag.setText(flagStr);
            holder.flag.setBackgroundColor(
                    getContext().getResources().getColor(
                            R.color.hotgreen));
        } else {
            holder.flag.setText("");
            holder.flag.setBackgroundColor(
                    getContext().getResources().getColor(
                            R.color.colorAccent));
        }


        if (contact.getPhoto() != null)
            holder.img.setImageBitmap(contact.getPhoto());
        else
            holder.img.setImageResource(R.mipmap.def);

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        TextView flag;
        ImageView img;
    }
}