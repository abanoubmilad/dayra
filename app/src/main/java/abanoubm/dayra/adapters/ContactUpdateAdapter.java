package abanoubm.dayra.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactUpdate;

public class ContactUpdateAdapter extends ArrayAdapter<ContactUpdate> {

    public ContactUpdateAdapter(Context context,
                                ArrayList<ContactUpdate> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        ContactUpdate contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_contact_update, parent, false);
            holder = new ViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.name);
            holder.day=(TextView) convertView.findViewById(R.id.day);
            holder.flag=(TextView) convertView.findViewById(R.id.flag);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.name.setText(contact.getName());
        holder.day.setText(contact.getDay());
        if (contact.isSelected()) {
           holder.flag.setBackgroundColor(Utility.update);
        } else {
            holder.flag.setBackgroundColor(Color.WHITE);
        }

        if (contact.getPicDir().length() != 0
                && new File(contact.getPicDir()).exists()) {
            holder.img.setImageBitmap(ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(contact.getPicDir()), 100, 100));
        } else {
            holder.img.setImageResource(R.mipmap.def);
        }
        return convertView;
    }
    private static class ViewHolder {
        TextView name,day,flag;
        ImageView img;
    }
}