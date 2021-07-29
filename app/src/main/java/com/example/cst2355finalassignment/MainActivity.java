package com.example.cst2355finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText searchQuery = (EditText)findViewById(R.id.searchQuery);

        Button launchButton = findViewById(R.id.launchButton);
        launchButton.setOnClickListener(click -> {
            String searchTerm = searchQuery.getText().toString();

            Intent goToSearch = new Intent(MainActivity.this, SearchActivity.class);
            goToSearch.putExtra("SearchTerm", searchTerm);
            startActivity(goToSearch);

        });
    }

}