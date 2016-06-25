package abanoubm.dayra.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.model.ContactMobile;

public class GContactsOutAdapter extends Adapter<ContactMobile> {

    public GContactsOutAdapter(Context context,
                               ArrayList<ContactMobile> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        ContactMobile contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_gcontact_out, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.mobile = (TextView) convertView.findViewById(R.id.mobile);
            holder.flag1 = (TextView) convertView.findViewById(R.id.flag1);
            holder.flag2 = (ImageView) convertView.findViewById(R.id.flag2);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.name.setText(contact.getName());
        holder.mobile.setText(contact.getMobile());

        holder.flag1.setVisibility(contact.isExisted() ? View.VISIBLE : View.INVISIBLE);

        holder.flag2.setImageResource(contact.isSelected() ? R.mipmap.ic_check : R.mipmap.ic_uncheck);


        if (contact.getPhoto() != null)
            holder.img.setImageBitmap(contact.getPhoto());
        else
            holder.img.setImageResource(R.mipmap.def);

        return convertView;
    }

    private static class ViewHolder {
        TextView name, flag1, mobile;
        ImageView img, flag2;
    }
}