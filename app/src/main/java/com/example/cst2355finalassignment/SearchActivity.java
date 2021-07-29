package com.example.cst2355finalassignment;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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

public class SearchActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "SEARCH_ACTIVITY";
    private List<SearchResult> resultList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(ACTIVITY_NAME, "In onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        resultList = new ArrayList<>();
        Intent fromGuardian = getIntent();
        String search = fromGuardian.getStringExtra("search");
        String url = String.format("https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=", search);

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
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");

                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    Log.e(ACTIVITY_NAME, "In While Loop");
                    if (eventType == XmlPullParser.START_TAG) {
                        Log.e(ACTIVITY_NAME, "In If Statement");
                        switch (xpp.getName()) {
                            case "id":
                                article = new SearchResult();
                                break;
                            case "sectionName":
                                eventType = xpp.next();
                                if (eventType == XmlPullParser.TEXT)
                                    article.setSectionName(xpp.getText());
                                break;
                            case "webTitle":
                                eventType = xpp.next();
                                if (eventType == XmlPullParser.TEXT)
                                    article.setTitle(xpp.getText());
                                break;
                            case "webURL":
                                eventType = xpp.next();
                                if (eventType == XmlPullParser.TEXT)
                                    article.setURL(xpp.getText());
                                resultList.add(article);
                                publishProgress((progress + 5));
                                break;
                        }
                    }
                    eventType = xpp.next();
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
            progressBar.setVisibility(View.INVISIBLE);
            if (resultList.isEmpty()) {
                TextView text = findViewById(R.id.notFound);
                text.setVisibility(View.VISIBLE);
            }

        }
    }


}