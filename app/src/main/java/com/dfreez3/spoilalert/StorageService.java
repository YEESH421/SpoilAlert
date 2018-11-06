package com.dfreez3.spoilalert;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StorageService {

    public static final String BLANK_JSON = "{\n\t\"items\": [\n\t]\n}";
    private static final String ITEM_FILENAME = "items.json";
    private static final String TAG = "StorageService";


    public static void addItemToJson(FoodModel foodModel){

        try {
            addItemToJson(foodModel, getOrCreateJSON(ITEM_FILENAME), ITEM_FILENAME);
        } catch (IOException e) {
            Log.e(TAG, "Error writing to json: ", e);
        }
    }

    public static void addItemToJson(FoodModel foodModel, String jsonContents, String filename) {
        try {
            JSONObject jsonFile = new JSONObject(jsonContents);
            JSONArray items = jsonFile.getJSONArray("items");
            JSONObject newItem = new JSONObject();

            newItem.put("name", foodModel.getName());
            newItem.put("expirationPeriod", foodModel.getExpirationPeriod());
            newItem.put("purchaseDate", foodModel.getPurchaseDate().getTime());

            items.put(newItem);
            JSONObject newJsonFile = new JSONObject();
            newJsonFile.put("items", items);

            File root = new File(Environment.getDataDirectory(), "data/com.dfreez3.spoilalert/config");
            FileWriter jsonWrite = new FileWriter(new File(root, filename));
            jsonWrite.write(newJsonFile.toString());
            jsonWrite.flush();
            jsonWrite.close();
        } catch (Exception e) {
            Log.d("", e.getMessage());
        }
    }

    public static String getOrCreateJSON() throws IOException {
        return getOrCreateJSON(ITEM_FILENAME);
    }

    public static String getOrCreateJSON(String filename) throws IOException {
        File root = new File(Environment.getDataDirectory(), "data/com.dfreez3.spoilalert/config");
        if (!root.exists()) {
            root.setReadable(true);
            root.setWritable(true);
            if(!root.mkdirs()) {
                Log.d("", "Unable to make directory: " + root.getAbsolutePath());
            }
        }

        File jsonFile = new File(root, filename);
        if(!jsonFile.exists()) {
            jsonFile.setWritable(true);
            jsonFile.setReadable(true);
            jsonFile.createNewFile();
            FileWriter writer = new FileWriter(jsonFile);
            writer.append(BLANK_JSON);
            writer.flush();
            writer.close();
        }

        return readFile(jsonFile);
    }

    public static String readFile(File file) throws IOException {
        StringBuilder contents = new StringBuilder();

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;

        while((line = reader.readLine()) != null) {
            contents.append(line);
            contents.append('\n');
        }
        reader.close();

        return contents.toString();
    }

}
