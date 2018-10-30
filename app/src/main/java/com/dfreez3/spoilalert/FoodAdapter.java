package com.dfreez3.spoilalert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class FoodAdapter extends ArrayAdapter<FoodModel> {

    private final long DAY_IN_MILLISECONDS = 86400000;

    private Context context;

    private static class ViewHolder {
        TextView txtName;
        TextView txtDate;
        RelativeLayout progressBar;
    }

    private Comparator<FoodModel> foodModelComparator = new Comparator<FoodModel>() {
        @Override
        public int compare(FoodModel m1, FoodModel m2) {
            long timeLeft1 = m1.getPurchaseDate().getTime() + m1.getExpirationPeriod()
                    - new Date().getTime();
            long timeLeft2 = m2.getPurchaseDate().getTime() + m2.getExpirationPeriod()
                    - new Date().getTime();
            return Long.compare(timeLeft1, timeLeft2);
        }
    };

    public FoodAdapter(ArrayList<FoodModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.sort(this.foodModelComparator);
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

        long timeLeft = foodModel.getPurchaseDate().getTime() + foodModel.getExpirationPeriod()
                - new Date().getTime();

        if (timeLeft > DAY_IN_MILLISECONDS) {
            int daysLeft = (int)(timeLeft / DAY_IN_MILLISECONDS) + 1;
            viewHolder.txtDate.setText(daysLeft + " days left");
        } else {
            viewHolder.txtDate.setText("Less than a day");
        }

        RelativeLayout background = convertView.findViewById(R.id.item_background);

        setProgress(foodModel, viewHolder.progressBar, background);

        return convertView;
    }

    private void setProgress(FoodModel foodModel, final RelativeLayout progressLayout, final RelativeLayout bgLayout) {
        final ViewGroup.LayoutParams progressParams = progressLayout.getLayoutParams();

        /*
         * Create constant aliases.
         */
        final Date currentTime = new Date();
        final FoodModel foodModelF = foodModel;
        final RelativeLayout progressLayoutF = progressLayout;

        bgLayout.post(new Runnable() {
            @Override
            public void run() {
                double expirationPercent =
                        1.0 - (double) (currentTime.getTime() - foodModelF.getPurchaseDate().getTime())
                                / (double) foodModelF.getExpirationPeriod();
                progressParams.width = (int) (expirationPercent * (double) bgLayout.getWidth());

                if (expirationPercent < 0.25) {
                    progressLayoutF.setBackground(
                            context.getResources().getDrawable(R.drawable.pb_red, null)
                    );
                } else if (expirationPercent < 0.5) {
                    progressLayoutF.setBackground(
                            context.getResources().getDrawable(R.drawable.pb_orange, null)
                    );
                } else if (expirationPercent < 0.75) {
                    progressLayoutF.setBackground(
                            context.getResources().getDrawable(R.drawable.pb_limegreen, null)
                    );
                } else {
                    progressLayoutF.setBackground(
                            context.getResources().getDrawable(R.drawable.pb_green, null)
                    );
                }

                progressLayoutF.setLayoutParams(progressParams);
            }
        });
    }

}
