package com.sporlif.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sporlif.R;

import java.util.ArrayList;

import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * Created by MAURICIO on 24/05/2017.
 */

public class UtilsForViews {

    private Toolbar toolbar;

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void createToolbar(View view, AppCompatActivity activity, String title, boolean upEnabled){

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(upEnabled);

    }

    public static void setListItems(Context context, JsonArray data, ListView control) {

        ArrayList<String> items = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            JsonObject obj = data.getJsonObject(i);
            items.add(obj.getString("d2"));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);
        control.setAdapter(arrayAdapter);

    }

}
