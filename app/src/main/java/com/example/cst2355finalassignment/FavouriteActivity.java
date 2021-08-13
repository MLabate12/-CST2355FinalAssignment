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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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

public class FavouriteActivity extends AppCompatActivity {

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

}
