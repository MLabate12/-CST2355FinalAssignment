package com.example.cst2355finalassignment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class FavouriteResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
        title.setText(String.format("Article Title: " + article.getTitle()));
        title.setBackgroundColor(Color.BLACK);
        title.setTextColor(Color.WHITE);

        TextView sectionName = findViewById(R.id.articleSectionName);
        sectionName.setText(String.format("Section Name: " + article.getSectionName()));
        sectionName.setBackgroundColor(Color.BLACK);
        sectionName.setTextColor(Color.WHITE);

        TextView URL = findViewById(R.id.articleURL);
        URL.setText(String.format("URL: " + article.getURL()));
        URL.setBackgroundColor(Color.BLACK);
        URL.setTextColor(Color.WHITE);

        nabberDB = new NabberDB(this);
        nabberDB.getWritableDatabase();

        //This gets the toolbar from the layout:
        Toolbar tBar = findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

        //start Navigation Bar
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Display application icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.help_item:
                message = "To delete press on the item you want to delete./Pour supprimer, appuyez sur l'élément que vous souhaitez supprimer.";
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message = null;

        switch(item.getItemId())
        {
            case R.id.homePage:
                Intent goToHome = new Intent(FavouriteResultsActivity.this, HelpActivity.class);
                startActivity(goToHome);
                break;
            case R.id.favePage:
                Intent goToFave = new Intent(FavouriteResultsActivity.this, FavouriteActivity.class);
                startActivity(goToFave);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
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