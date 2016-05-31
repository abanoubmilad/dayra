package abanoubm.dayra.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;

public class MenuItemAdapter extends ArrayAdapter<String> {
    private TypedArray icons;

    public MenuItemAdapter(Context context, ArrayList<String> arr, int menuType) {
        super(context, 0, arr);
        if (menuType == 1)
            icons = context.getResources().obtainTypedArray(R.array.sign_icons);
        else if (menuType == 2)
            icons = context.getResources().obtainTypedArray(R.array.home_icons);
        else if (menuType == 3)
            icons = context.getResources().obtainTypedArray(R.array.out_icons);
        else
            icons = context.getResources().obtainTypedArray(
                    R.array.settings_icons);

    }

    public void recycleIcons() {
        icons.recycle();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.menu_item, parent, false);
        }

        TextView t = (TextView) convertView.findViewById(R.id.item);
        t.setText(getItem(position));

        ImageView i = ((ImageView) convertView.findViewById(R.id.image));
        i.setImageResource(icons.getResourceId(position, -1));

        return convertView;
    }
}