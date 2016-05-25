package abanoubm.dayra.adapt;

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
import abanoubm.dayra.obj.ContactStatis;

public class ContactStatisAdapter extends ArrayAdapter<ContactStatis> {

    public ContactStatisAdapter(Context context,
                                ArrayList<ContactStatis> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactStatis contact = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.contact_day_item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.attendantName);
        TextView day = (TextView) convertView.findViewById(R.id.attendantday);

        name.setText(contact.getName());
        day.setText(contact.getDays() + " % ");

        ImageView img = (ImageView) convertView.findViewById(R.id.attendantImg);

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