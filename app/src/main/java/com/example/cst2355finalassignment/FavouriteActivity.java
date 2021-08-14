package com.example.cst2355finalassignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static androidx.appcompat.app.AlertDialog.Builder;
import static androidx.appcompat.app.AlertDialog.OnClickListener;

public class FavouriteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String ACTIVITY_NAME = "FAVOURITE_ACTIVITY";
    private List<SearchResult> faveList = new ArrayList<>();
    private NabberDB nabberDB;
    private ListView list;
    private FavouriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(ACTIVITY_NAME, "In onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

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
        nabberDB = new NabberDB(this);
        nabberDB.getWritableDatabase();
        faveList = nabberDB.getAll();

        ArticleQuery req = new FavouriteActivity.ArticleQuery();
        req.execute();
    }

    private class FavouriteAdapter extends BaseAdapter {

        public int getCount() {
            return faveList.size();
        }

        public Object getItem(int position) {
            return faveList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView;
            TextView tView;
            newView = inflater.inflate(R.layout.row_layout, parent, false);
            tView = newView.findViewById(R.id.rowTitle);
            tView.setText(getItem(position).toString());
            newView.setBackgroundColor(Color.BLACK);
            tView.setTextColor(Color.WHITE);
            return newView;
        }
    }

    class ArticleQuery extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... args) {
            return "Done";
        }

        @Override
        protected void onPostExecute(String result) {
            list = findViewById(R.id.faveArticles);
            list.setAdapter(adapter = new FavouriteAdapter());

            list.setOnItemClickListener((parent, view, position, id) -> {
                Bundle dataToPass = new Bundle();
                dataToPass.putSerializable("article", faveList.get(position));
            });

            list.setOnItemLongClickListener((parent, view, position, id) -> {
                new AlertDialog.Builder(FavouriteActivity.this)
                        .setTitle(getResources().getString(R.string.favourite_deleteheader))
                        .setMessage(getResources().getString(R.string.favourite_deletemsg) + " " + faveList.get(position).getTitle() + "?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            if (nabberDB.deleteArticle(faveList.get(position))) {
                                Snackbar.make(list, R.string.favourite_deletesuccess, Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                            faveList = nabberDB.getAll();
                            adapter.notifyDataSetChanged();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;
            });

            list.setOnItemClickListener((parent, view, position, id) -> {
                Intent gotoFDetails = new Intent(FavouriteActivity.this, FavouriteResultsActivity.class);
                gotoFDetails.putExtra("article", faveList.get(position));
                startActivity(gotoFDetails);
            });
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
                Intent goToHome = new Intent(FavouriteActivity.this, HelpActivity.class);
                startActivity(goToHome);
                break;
            case R.id.favePage:
                Intent goToFave = new Intent(FavouriteActivity.this, FavouriteActivity.class);
                startActivity(goToFave);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

}
