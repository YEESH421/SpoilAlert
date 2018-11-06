package com.dfreez3.spoilalert;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddFoodActivity extends Activity implements View.OnClickListener {

    private EditText mFoodName;

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
    }

    @Override
    public void onClick(View v) {
        
    }
}
