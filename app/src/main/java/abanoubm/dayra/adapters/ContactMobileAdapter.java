package abanoubm.dayra.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactMobile;

public class ContactMobileAdapter extends ArrayAdapter<ContactMobile> {
    private final int white, highlight;

    public ContactMobileAdapter(Context context,
                                ArrayList<ContactMobile> contacts) {
        super(context, 0, contacts);
        white = ContextCompat.getColor(context, R.color.white);
        highlight = ContextCompat.getColor(context, R.color.red);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        ContactMobile contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_contact_mobile, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.mobile = (TextView) convertView.findViewById(R.id.mobile);
            holder.flag1 = (TextView) convertView.findViewById(R.id.flag1);
            holder.root = convertView.findViewById(R.id.root);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
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


        if (contact.isSelected())
            holder.root.setBackgroundColor(Utility.update);
        else
            holder.root.setBackgroundColor(Utility.deupdate);

        if (contact.getPhoto() != null)
            holder.img.setImageBitmap(contact.getPhoto());
        else
            holder.img.setImageResource(R.mipmap.def);

        return convertView;
    }

    private static class ViewHolder {
        TextView name, flag1, mobile;
        ImageView img;
        View root;
    }
}