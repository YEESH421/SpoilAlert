package com.dfreez3.spoilalert;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class AddFoodActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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
            Date purchaseDate = new Date();

            //TODO actually calculate this from the UI
            // 5 days in milliseconds
            long expirationPeriod = 24 * 60 * 60 * 1000 * 5;
            FoodModel model = new FoodModel(name, purchaseDate, expirationPeriod);

            StorageService.addItemToJson(model);

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
}
