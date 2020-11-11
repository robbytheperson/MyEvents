package com.example.myevents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFERNECES_KEY = "shared_preferences";
    private static final String EVENT_LIST_KEY = "event_list_key";

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Event> events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        events = new ArrayList<>();
        getEvents();
        recyclerView = (RecyclerView)findViewById(R.id.eventList);
        recyclerView.setHasFixedSize(true);

        loadEvents();

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EventListAdapter(events);
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(decoration);

    }

    private void loadEvents() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERNECES_KEY, Context.MODE_PRIVATE);
        String eventListJson = sharedPreferences.getString(EVENT_LIST_KEY, null);
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        Gson gson = new Gson();
        events = gson.fromJson(eventListJson, type);
        if(events == null) {
            events = new ArrayList<Event>();
        }
    }

    public void addEvent(View view){
        TextInputEditText dateEditText = findViewById(R.id.date_input_editText);
        TextInputEditText nameEditText = findViewById(R.id.name_input_editText);
        String date = dateEditText.getText().toString();
        String name = nameEditText.getText().toString();
        events.add(new Event(date, name));
        adapter.notifyDataSetChanged();
    }
    private void saveEvents(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERNECES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String eventListJson = gson.toJson(events);
        editor.putString(EVENT_LIST_KEY, eventListJson);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveEvents();
    }

    public void getEvents(){
        for(int i = 0; i < EventDB.dates.length; i++) {
            events.add(new Event(EventDB.dates[i], EventDB.descriptions[i]));
        }
    }
}