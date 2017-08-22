package abanoubm.dayra.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import abanoubm.dayra.R;

public class MenuItemAdapter extends Adapter<String> {
    private final TypedArray icons;

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
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_menu, parent, false);
            holder = new ViewHolder();
            holder.item = (TextView) convertView.findViewById(R.id.item);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.item.setText(getItem(position));

        holder.img.setImageResource(icons.getResourceId(position, -1));

        return convertView;
    }

    private static class ViewHolder {
        TextView item;
        ImageView img;
    }
}