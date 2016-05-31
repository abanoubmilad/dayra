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
import abanoubm.dayra.model.ContactDay;

public class ContactDayAdapter extends ArrayAdapter<ContactDay> {

    public ContactDayAdapter(Context context, ArrayList<ContactDay> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactDay contact = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_contact_day, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.attendantName);
        TextView day = (TextView) convertView.findViewById(R.id.attendantday);

        name.setText(contact.getName());
        day.setText(contact.getDay());

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