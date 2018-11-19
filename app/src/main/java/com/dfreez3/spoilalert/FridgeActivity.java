package com.dfreez3.spoilalert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FridgeActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "FRIDGE_DEBUG -> ";

    private Timer autoUpdate;

    private ImageView addButton;
    private ImageView searchButton;
    private ImageView filterButton;
    private ImageView deleteButton;
    private ImageView settingsButton;
    private ImageView cancelButton;

    private ArrayList<FoodModel> listContents;
    private FoodAdapter arrayAdapter;

    public void setSelectionMode() {
        addButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        filterButton.setVisibility(View.GONE);
        settingsButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
    }

    private void unsetSelectionMode() {
        addButton.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.VISIBLE);
        filterButton.setVisibility(View.VISIBLE);
        settingsButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
    }

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
                listContents.add(new FoodModel(i,
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

    @Override
    public void onClick(View v) {
        if(v.getId() == addButton.getId()) {
            Intent addFoodIntent = new Intent(
                    FridgeActivity.this,
                    AddFoodActivity.class
            );
            startActivity(addFoodIntent);
        } else if(v.getId() == cancelButton.getId()) {
            unsetSelectionMode();
            createList();
        } else if(v.getId() == deleteButton.getId()) {
            List<Integer> deleteList = this.arrayAdapter.getSelectionContent();

            ArrayList<Integer> ids = new ArrayList<>();
            for(FoodModel model : this.listContents) {
                if(deleteList.contains(model.getId())) {
                    ids.add(model.getId());
                }
            }
            if(ids.size() > 0) {
                StorageService.deleteItemsFromJson(ids);
            }
            createList();

            unsetSelectionMode();
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
        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
        deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(this);
        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);

        filterButton = findViewById(R.id.filter_button);
        settingsButton = findViewById(R.id.settings_button);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(this.autoUpdate == null) {
            this.autoUpdate = new Timer();
            this.autoUpdate.schedule(new TimerTask() {
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
        }

        createList();
    }
}
