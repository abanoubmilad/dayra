package abanoubm.dayra.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactMobile;

public class ContactMobileAdapter extends Adapter<ContactMobile> {

    public ContactMobileAdapter(Context context,
                                ArrayList<ContactMobile> contacts) {
        super(context, 0, contacts);
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
            holder.flag2 = (ImageView) convertView.findViewById(R.id.flag2);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder.bitmap != null)
            holder.bitmap.recycle();
        holder.bitmap = Utility.getBitmap(contact.getPhoto());
        if (holder.bitmap != null)
            holder.img.setImageBitmap(holder.bitmap);
        else
            holder.img.setImageResource(R.mipmap.def);

        holder.name.setText(contact.getName());
        holder.mobile.setText(contact.getMobile());

        holder.flag2.setImageResource(contact.isSelected() ? R.mipmap.ic_check : R.mipmap.ic_uncheck);


        return convertView;
    }

    private static class ViewHolder {
        TextView name, mobile;
        ImageView img;
        ImageView flag2;
        Bitmap bitmap;

    }
}