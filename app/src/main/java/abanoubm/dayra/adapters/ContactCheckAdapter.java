package abanoubm.dayra.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactCheck;

public class ContactCheckAdapter extends Adapter<ContactCheck> {
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
            holder.flag.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hotgreen));
        } else {
            holder.flag.setText("");
            holder.flag.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.darkred));
        }


        if (holder.bitmap != null)
            holder.bitmap.recycle();
        holder.bitmap = Utility.getBitmap(contact.getPhoto());
        if (holder.bitmap != null)
            holder.img.setImageBitmap(holder.bitmap);
        else
            holder.img.setImageResource(R.mipmap.def);

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        TextView flag;
        ImageView img;
        Bitmap bitmap;

    }
}