package com.example.cst2355finalassignment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FavouriteResultsActivity extends AppCompatActivity {

    private List<SearchResult> articleList;
    private SearchResult article;
    private NabberDB nabberDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_results);

        articleList = new ArrayList<>();

        Intent fromSearch = getIntent();
        article = (SearchResult) fromSearch.getSerializableExtra("article");

        TextView title = findViewById(R.id.articleTitle);
        title.setText(String.format(article.getTitle()));

        TextView sectionName = findViewById(R.id.articleSectionName);
        sectionName.setText(String.format(article.getSectionName()));

        TextView URL = findViewById(R.id.articleURL);
        URL.setText(String.format(article.getURL()));

        nabberDB = new NabberDB(this);
        nabberDB.getWritableDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}