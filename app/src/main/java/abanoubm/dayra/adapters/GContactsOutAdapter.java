package abanoubm.dayra.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.model.ContactMobile;

public class GContactsOutAdapter extends ArrayAdapter<ContactMobile> {
    private int white, highlight;

    public GContactsOutAdapter(Context context,
                               ArrayList<ContactMobile> contacts) {
        super(context, 0, contacts);
        white = ContextCompat.getColor(context, R.color.white);
        highlight = ContextCompat.getColor(context, R.color.red);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ContactMobile contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_gcontact_out, parent, false);
        }


        ((TextView) convertView.findViewById(R.id.contact_name)).setText(contact.getName());
        ((TextView) convertView.findViewById(R.id.contact_mob)).setText(contact.getMobile());

        ImageView img = (ImageView) convertView.findViewById(R.id.contact_img);

        if (contact.isExisted())
            convertView.findViewById(R.id.flag1).setBackgroundColor(highlight);
        else
            convertView.findViewById(R.id.flag1).setBackgroundColor(white);
        CheckBox flag = (CheckBox) convertView.findViewById(R.id.flag2);
        flag.setChecked(contact.isSelected());

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