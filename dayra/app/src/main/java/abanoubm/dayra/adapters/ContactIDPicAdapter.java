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
import abanoubm.dayra.model.ContactID;

public class ContactIDPicAdapter extends Adapter<ContactID> {

    private int selected = -1;

    public ContactIDPicAdapter(Context context, ArrayList<ContactID> verses) {
        super(context, 0, verses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ContactID contact = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_pic_contact, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.root = convertView.findViewById(R.id.root);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(contact.getName());

        if (holder.bitmap != null)
            holder.bitmap.recycle();
        holder.bitmap = Utility.getBitmap(contact.getPhoto());
        if (holder.bitmap != null)
            holder.img.setImageBitmap(holder.bitmap);
        else
            holder.img.setImageResource(R.mipmap.def);


        if (selected == position)
            holder.root.setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color.colorAccent));
        else
            holder.root.setBackgroundResource(R.drawable.dynamic_bg);
        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        ImageView img;
        Bitmap bitmap;
        View root;

    }
    public void setSelectedIndex(int pos) {
        selected = pos;
        notifyDataSetChanged();
    }
}