package com.dfreez3.spoilalert;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class AddFoodActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Date mExpirationDate;

    private EditText mFoodName;
    private Switch mManualDate;
    private TextView mExpirationDateText;
    private TextView mExpirationDateLabel;
    private Button mPickDate;
    private Button mSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Overscan screen layout into status bar.
         */
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        /*
         * Remove title bar.
         */
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_add_food);

        mFoodName = findViewById(R.id.food_name_textbox);
        mFoodName.setOnClickListener(this);
        mManualDate = findViewById(R.id.manual_expiration_switch);
        mManualDate.setOnCheckedChangeListener(this);
        mExpirationDateText = findViewById(R.id.expiration_date_text);
        mExpirationDateText.setOnClickListener(this);
        mExpirationDateLabel = findViewById(R.id.expiration_date_label);
        mPickDate = findViewById(R.id.set_date_button);
        mPickDate.setOnClickListener(this);
        mSave = findViewById(R.id.save_button);
        mSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mPickDate.getId()){
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        } else if (v.getId() == mSave.getId()){
            String name = mFoodName.getText().toString();

            // Calculate the expiration period in milliseconds.
            Calendar purchase = Calendar.getInstance();
            int year = purchase.get(Calendar.YEAR);
            int month = purchase.get(Calendar.MONTH);
            int day = purchase.get(Calendar.DATE);
            Date purchaseDate = new Date(year, month, day);
            long expirationPeriod = Math.abs(mExpirationDate.getTime() - purchaseDate.getTime());

            // Save the food item
            FoodModel model = new FoodModel(StorageService.getNextAvailableId(), name, purchaseDate, expirationPeriod);
            StorageService.addItemToJson(model);

            // Return to the fridge screen
            finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            mPickDate.setVisibility(View.VISIBLE);
            mExpirationDateText.setVisibility(View.VISIBLE);
            mExpirationDateLabel.setVisibility(View.VISIBLE);
        } else {
            mPickDate.setVisibility(View.GONE);
            mExpirationDateText.setVisibility(View.GONE);
            mExpirationDateLabel.setVisibility(View.GONE);
        }
    }

    public void setExpirationDate(int year, int month, int day){
        mExpirationDate = new Date(year, month, day);
        String date = Integer.toString(month + 1) + "/" + Integer.toString(day) + "/" + Integer.toString(year);
        mExpirationDateText.setText(date);
    }
}
