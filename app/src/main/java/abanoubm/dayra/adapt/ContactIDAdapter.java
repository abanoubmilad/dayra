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
import abanoubm.dayra.obj.ContactID;

public class ContactIDAdapter extends ArrayAdapter<ContactID> {

    public ContactIDAdapter(Context context, ArrayList<ContactID> verses) {
        super(context, 0, verses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactID contact = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.contact_item, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.attendantName)).setText(contact.getName());

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