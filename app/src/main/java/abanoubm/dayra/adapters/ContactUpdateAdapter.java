package abanoubm.dayra.adapters;

import java.io.File;
import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactUpdate;
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

public class ContactUpdateAdapter extends ArrayAdapter<ContactUpdate> {

	public ContactUpdateAdapter(Context context,
			ArrayList<ContactUpdate> contacts) {
		super(context, 0, contacts);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ContactUpdate contact = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_contact_update, parent, false);
		}

		TextView name = (TextView) convertView.findViewById(R.id.attendantName);
		TextView day = (TextView) convertView.findViewById(R.id.attendantday);
		TextView flag = (TextView) convertView.findViewById(R.id.flag);

		name.setText(contact.getName());
		day.setText(contact.getDay());
		if (contact.isSelected()) {
			flag.setBackgroundColor(Utility.update);
		} else {
			flag.setBackgroundColor(Color.WHITE);
		}

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