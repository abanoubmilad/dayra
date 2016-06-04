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
import abanoubm.dayra.model.ContactCheck;

public class ContactCheckAdapter extends ArrayAdapter<ContactCheck> {

    public ContactCheckAdapter(Context context,
                               ArrayList<ContactCheck> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_contact_check, parent, false);
            holder = new ViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.name);
            holder.flag=(TextView) convertView.findViewById(R.id.flag);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        ContactCheck contact = getItem(position);

        holder.name.setText(contact.getName());
        if (contact.isChecked()) {
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
        TextView name;
        TextView flag;
        ImageView img;
    }
}