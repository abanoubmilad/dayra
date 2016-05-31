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
        ContactStatistics contact = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_contact_day, parent, false);
        }
        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView min = (TextView) convertView.findViewById(R.id.min);
        TextView max = (TextView) convertView.findViewById(R.id.max);
        TextView count = (TextView) convertView.findViewById(R.id.count);

        name.setText(contact.getName());
        min.setText(contact.getMinDay());
        max.setText(contact.getMaxDay());
        count.setText(contact.getDaysCount());


        if (contact.getPicDir().length() != 0
                && new File(contact.getPicDir()).exists()) {
            img.setImageBitmap(ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(contact.getPicDir()), 100, 100));
        } else {
            img.setImageResource(R.mipmap.def);
        }

        return convertView;
    }
}