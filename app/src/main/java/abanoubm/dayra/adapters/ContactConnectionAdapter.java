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
import abanoubm.dayra.model.ContactConnection;

public class ContactConnectionAdapter extends ArrayAdapter<ContactConnection> {

    public ContactConnectionAdapter(Context context,
                                    ArrayList<ContactConnection> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ContactConnection contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_contact_connection, parent, false);
        }


        ((TextView) convertView.findViewById(R.id.name)).setText(contact.getName());
        TextView flag = (TextView) convertView.findViewById(R.id.flag);

        if (contact.isCon()) {
            flag.setBackgroundColor(Utility.update);
        } else {
            flag.setBackgroundColor(Color.WHITE);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.img);

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