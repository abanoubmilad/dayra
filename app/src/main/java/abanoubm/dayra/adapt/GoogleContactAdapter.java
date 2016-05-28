package abanoubm.dayra.adapt;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.obj.GoogleContact;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GoogleContactAdapter extends ArrayAdapter<GoogleContact> {

	public GoogleContactAdapter(Context context,
			ArrayList<GoogleContact> contacts) {
		super(context, 0, contacts);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		GoogleContact contact = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.google_contact_item, parent, false);
		}


		((TextView) convertView.findViewById(R.id.name)).setText(contact.getName());
		((TextView) convertView.findViewById(R.id.mobile)).setText(contact.getMobile());
		TextView flag = (TextView) convertView.findViewById(R.id.flag);

		if (contact.isSelected()) {
			flag.setBackgroundColor(Utility.update);
		} else {
			flag.setBackgroundColor(Color.WHITE);
		}

		return convertView;
	}
}