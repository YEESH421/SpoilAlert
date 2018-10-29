package com.dfreez3.spoilalert;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class FridgeActivity extends Activity implements View.OnClickListener {

    private ImageView addButton;

    private ArrayList<FoodModel> listContents;
    private FoodAdapter arrayAdapter;

    private void createList() {
        listContents = new ArrayList<>();
        listContents.add(new FoodModel("Apple", new Date(), 432000000L));
        Date bananaDate = new Date(new Date().getTime() - 300000000L);
        listContents.add(new FoodModel("Banana", bananaDate, 432000000L));
        listContents.add(new FoodModel("Eggs", new Date(), 432000000L));

        arrayAdapter = new FoodAdapter(listContents, this);

        ListView listView = findViewById(R.id.food_list);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == addButton.getId()) {
            Intent addFoodIntent = new Intent(
                    FridgeActivity.this,
                    AddFoodActivity.class
            );
            startActivity(addFoodIntent);
        }
    }

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

        /*
         * Create the view from layout.
         */
        setContentView(R.layout.activity_fridge);

        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(this);

        createList();
    }
}
