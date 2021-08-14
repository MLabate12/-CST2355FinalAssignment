package com.example.cst2355finalassignment;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static androidx.appcompat.app.AlertDialog.*;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String ACTIVITY_NAME = "SEARCH_ACTIVITY";
    private List<SearchResult> titleList;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(ACTIVITY_NAME, "In onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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

        titleList = new ArrayList<>();
        Intent fromGuardian = getIntent();
        String search = fromGuardian.getStringExtra("SearchTerm");
        String url = String.format("https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=" + search);

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        SearchQuery req = new SearchQuery();
        req.execute(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    class SearchQuery extends AsyncTask<String, Integer, String> {
        SearchResult article;

        @Override
        protected String doInBackground(String... args) {
            Log.e(ACTIVITY_NAME, "In doInBackground");
            int progress = 0;
            try {
                //start connection
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                String result = sb.toString(); //result into a string
                int index = result.indexOf("results");
                result = "{" + result.substring(index - 1);

                if (result != null) {
                    try {
                        JSONObject articleString = new JSONObject(result);
                        JSONArray jArray = articleString.getJSONArray("results");

                        //loop through all results
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject c = jArray.getJSONObject(i);
                            article = new SearchResult(); //set new article

                            article.setSectionName(c.getString("sectionName"));
                            article.setTitle(c.getString("webTitle"));
                            article.setURL(c.getString("webUrl"));

                            titleList.add(article);

                            publishProgress((progress + 25)); //increase progress bar
                        }
                    } catch (final JSONException e) {
                        Log.e(ACTIVITY_NAME, "Json parsing error: " + e.getMessage());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            publishProgress(100);
            return "Done";
        }

        @Override
        public void onProgressUpdate(Integer... args) {
            progressBar.setProgress(args[0]);
        }

        @Override
        public void onPostExecute(String fromDoInBackground) {
            Log.e(ACTIVITY_NAME, "In onPostExecute");
            super.onPostExecute(fromDoInBackground);
            ListView list = findViewById(R.id.resultsView);
            progressBar.setVisibility(View.INVISIBLE);

            if (titleList.isEmpty()) {
                TextView text = findViewById(R.id.notFound);
                text.setVisibility(View.VISIBLE);
            } else {
                list.setAdapter(new ResultListAdapter());
            }

            list.setOnItemClickListener((parent, view, position, id) -> {
                Intent gotoDetails = new Intent(SearchActivity.this, SearchResultsActivity.class);
                gotoDetails.putExtra("article", titleList.get(position));
                startActivity(gotoDetails);
            });
        }
    }

        private class ResultListAdapter extends BaseAdapter {

            public int getCount() {
                return titleList.size();
            }

            public Object getItem(int position) {
                return titleList.get(position);
            }

            public Object getTitleList(int position) {
                return titleList.get(position);
            }

            public long getItemId(int position) {
                return position;
            }

            public View getView(int position, View old, ViewGroup parent) {
                LayoutInflater inflater = getLayoutInflater();
                View newView;
                newView = inflater.inflate(R.layout.row_layout, parent, false);

                //display article titles
                TextView titleView;
                titleView = newView.findViewById(R.id.rowTitle);
                newView.setBackgroundColor(Color.BLACK);
                titleView.setTextColor(Color.WHITE);
                titleView.setText(getTitleList(position).toString());

                return newView;
            }
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
                message = "HELP GOES HERE";
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
                Intent goToHome = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(goToHome);
                break;
            case R.id.favePage:
                Intent goToFave = new Intent(SearchActivity.this, FavouriteActivity.class);
                startActivity(goToFave);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }
}
