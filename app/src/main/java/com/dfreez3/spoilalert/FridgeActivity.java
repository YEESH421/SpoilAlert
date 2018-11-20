package com.dfreez3.spoilalert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FridgeActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "FRIDGE_DEBUG -> ";


    private Timer autoUpdate;

    private ImageView addButton;
    private ImageView search;
    private EditText editText;

    private ArrayList<FoodModel> listContents;
    private FoodAdapter arrayAdapter;


    private String getItemFileContents() {
        try {
            return StorageService.getOrCreateJSON();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            return StorageService.BLANK_JSON;
        }
    }

    private void loadJSONData() {
        String in = getItemFileContents();

        try {
            JSONObject reader = new JSONObject(in);
            JSONArray items = reader.getJSONArray("items");

            for(int i = 0; i < items.length(); i++) {
                listContents.add(new FoodModel(
                        items.getJSONObject(i).getString("name"),
                        new Date(items.getJSONObject(i).getLong("purchaseDate")),
                        items.getJSONObject(i).getLong("expirationPeriod"))
                );
            }


        } catch (org.json.JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }

    private void createList() {
        listContents = new ArrayList<>();
        loadJSONData();

        arrayAdapter = new FoodAdapter(listContents, this);

        ListView listView = findViewById(R.id.food_list);
        listView.setAdapter(arrayAdapter);
    }
    private void updateList(String s) {
        ArrayList temp = new ArrayList<>();
        for (FoodModel f : listContents) {
            if (f.getName().equals(s)) {
                temp.add(f);
            }
        }
        listContents.clear();
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == addButton.getId()) {
            Intent addFoodIntent = new Intent(
                    FridgeActivity.this,
                    AddFoodActivity.class
            );
            startActivity(addFoodIntent);
        } else if (v.getId() == search.getId()) {
            String value = editText.getText().toString();
            updateList(value);
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
        search = findViewById(R.id.search_button);
        search.setOnClickListener(this);
        editText = findViewById(R.id.editText);
        editText.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, 0, 40000);
        createList();
    }





}
