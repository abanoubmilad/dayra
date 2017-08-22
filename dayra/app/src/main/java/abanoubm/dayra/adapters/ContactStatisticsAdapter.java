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
import abanoubm.dayra.model.ContactStatistics;

public class ContactStatisticsAdapter extends Adapter<ContactStatistics> {

    public ContactStatisticsAdapter(Context context,
                                    ArrayList<ContactStatistics> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        ContactStatistics contact = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_contact_statistics, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.min = (TextView) convertView.findViewById(R.id.min);
            holder.max = (TextView) convertView.findViewById(R.id.max);
            holder.count = (TextView) convertView.findViewById(R.id.count);
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
        holder.min.setText(contact.getMinDay());
        holder.max.setText(contact.getMaxDay());
        holder.count.setText(contact.getDaysCount() + "");


        return convertView;
    }

    private static class ViewHolder {
        TextView name, min, max, count;
        ImageView img;
        Bitmap bitmap;
    }
}