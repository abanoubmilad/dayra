package abanoubm.dayra.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
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
import abanoubm.dayra.model.ContactStatistics;

public class ContactStatisticsAdapter extends ArrayAdapter<ContactStatistics> {

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
            holder.name= (TextView) convertView.findViewById(R.id.name);
            holder.min=(TextView) convertView.findViewById(R.id.min);
            holder.max=(TextView) convertView.findViewById(R.id.max);
            holder.count=(TextView) convertView.findViewById(R.id.count);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.name.setText(contact.getName());
        holder.min.setText(contact.getMinDay());
        holder.max.setText(contact.getMaxDay());
        holder.count.setText(contact.getDaysCount()+"");


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
        TextView name,min,max,count;
        ImageView img;
    }
}