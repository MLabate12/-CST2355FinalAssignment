package com.example.cst2355finalassignment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private List<SearchResult> articleList;
    private SearchResult article;
    private NabberDB nabberDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        articleList = new ArrayList<>();

        Intent fromSearch = getIntent();
        article = (SearchResult) fromSearch.getSerializableExtra("article");

        TextView title = findViewById(R.id.articleTitle);
        title.setText("Article Title: " + String.format(article.getTitle()));
        title.setBackgroundColor(Color.BLACK);
        title.setTextColor(Color.WHITE);

        TextView sectionName = findViewById(R.id.articleSectionName);
        sectionName.setText("Section Name: " + String.format(article.getSectionName()));
        sectionName.setBackgroundColor(Color.BLACK);
        sectionName.setTextColor(Color.WHITE);

        TextView URL = findViewById(R.id.articleURL);
        URL.setText("URL: " + String.format(article.getURL()));
        URL.setBackgroundColor(Color.BLACK);
        URL.setTextColor(Color.WHITE);

        nabberDB = new NabberDB(this);
        nabberDB.getWritableDatabase();
        String faveToast = getString(R.string.toast_2);

        Button faveButton = findViewById(R.id.faveButton);
        faveButton.setOnClickListener(click -> {
            nabberDB.addArticle(article);

            Toast toast= Toast.makeText(getApplicationContext(),faveToast,Toast.LENGTH_SHORT);
            toast.show();
        });
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
                message = getString(R.string.resultHelp);
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
                Intent goToHome = new Intent(SearchResultsActivity.this, MainActivity.class);
                startActivity(goToHome);
                break;
            case R.id.favePage:
                Intent goToFave = new Intent(SearchResultsActivity.this, FavouriteActivity.class);
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