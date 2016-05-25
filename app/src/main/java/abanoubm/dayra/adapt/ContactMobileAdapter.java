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
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.obj.ContactMobile;

public class ContactMobileAdapter extends ArrayAdapter<ContactMobile> {

    public ContactMobileAdapter(Context context,
                                ArrayList<ContactMobile> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ContactMobile contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.contact_mobile_item, parent, false);
        }


        ((TextView) convertView.findViewById(R.id.contact_name)).setText(contact.getName());
        ((TextView) convertView.findViewById(R.id.contact_mob)).setText(contact.getMobile());

        ImageView img = (ImageView) convertView.findViewById(R.id.contact_img);

        if (contact.isSelected()) {
            convertView.setBackgroundColor(Utility.update);
        } else {
            convertView.setBackgroundColor(Utility.deupdate);
        }

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