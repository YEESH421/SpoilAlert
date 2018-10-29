package com.dfreez3.spoilalert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FoodAdapter extends ArrayAdapter<FoodModel> {

    private ArrayList<FoodModel> dataSet;
    private Context context;

    private static class ViewHolder {
        TextView txtName;
        TextView txtDate;
        RelativeLayout progressBar;
    }

    public FoodAdapter(ArrayList<FoodModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FoodModel foodModel = getItem(position);

        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.name);
            viewHolder.txtDate = convertView.findViewById(R.id.date);
            viewHolder.progressBar = convertView.findViewById(R.id.progress_bar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(foodModel.getName());
        viewHolder.txtDate.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(foodModel.getExpirationDate()));

        setProgress(viewHolder.progressBar);

        return convertView;
    }

    private void setProgress(RelativeLayout relativeLayout) {
        ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
        params.width = 200;
        relativeLayout.setLayoutParams(params);
    }

}
