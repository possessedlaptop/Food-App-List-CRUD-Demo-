package com.awakesloth.desarrollo_apps_final_lista_comidas.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.awakesloth.desarrollo_apps_final_lista_comidas.R;
import com.awakesloth.desarrollo_apps_final_lista_comidas.entities.Plate;

import java.util.ArrayList;
import java.util.List;

public class PlateAdapter extends BaseAdapter {
    private Context context;
    private List<Plate> plateList;
    private List<Plate> selectedPlates;

    public PlateAdapter(Context context, List<Plate> plateList) {
        this.context = context;
        this.plateList = plateList;
        this.selectedPlates = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return plateList.size();
    }

    @Override
    public Object getItem(int position) {
        return plateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_plate, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.tvPlateName);
            holder.priceTextView = convertView.findViewById(R.id.tvPlatePrice);
            holder.ingredientsTextView = convertView.findViewById(R.id.tvPlateIngredients);
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Plate plate = plateList.get(position);
        holder.nameTextView.setText(plate.getName());
        holder.priceTextView.setText(context.getString(R.string.currency_format, (int) plate.getPrice()));
        holder.ingredientsTextView.setText(plate.getIngredients());

        // Set a listener for the checkbox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedPlates.add(plate);
            } else {
                selectedPlates.remove(plate);
            }
        });

        // Set the initial state of the checkbox based on plate selection
        holder.checkBox.setChecked(selectedPlates.contains(plate));

        return convertView;
    }

    public List<Plate> getSelectedPlates() {
        return selectedPlates;
    }

    public void clearSelection() {
        selectedPlates.clear();
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView ingredientsTextView;
        CheckBox checkBox;
    }
}
