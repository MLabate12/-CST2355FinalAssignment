package com.example.cst2355finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "SEARCH_ACTIVITY";
    public TextView title;
    public TextView URL2;
    public TextView sectionName;
    public TextView query;
    //public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        title = findViewById(R.id.title);
        URL2 = findViewById(R.id.URL);
        sectionName = findViewById(R.id.sectionName);
        query = findViewById(R.id.searchTerm);
        String searchTerm = getIntent().getStringExtra("SearchTerm");
        query.setText(getResources().getString(R.string.searchTerm) + searchTerm);

        SearchQuery searchNow = new SearchQuery();
        searchNow.execute("https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q="+getResources().getString(R.string.searchTerm));
    }

    class SearchQuery extends AsyncTask<String, Integer, String> {

        private String titleString;
        private String URLString;
        private String sectionNameString;

        @Override
        protected String doInBackground(String... args) {
            Log.e(ACTIVITY_NAME, "In doInBackground");
            try {
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                String iconName = null;

                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        Log.e(ACTIVITY_NAME, "Inside If");
                        sectionNameString = xpp.getAttributeValue(null, "sectionName");
                        //publishProgress(25);
                        titleString = xpp.getAttributeValue(null, "webTitle");
                        //publishProgress(50);
                        URLString = xpp.getAttributeValue(null, "URL");
                        //publishProgress(75);
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

            } catch (Exception e) {

            }

            return "Done";
        }


        public void onProgressUpdate(Integer... args) {

        }

        public void onPostExecute(String fromDoInBackground) {
            Log.e(ACTIVITY_NAME, "In onPostExeute");
            title.setText(getResources().getString(R.string.title) + titleString);
            URL2.setText(getResources().getString(R.string.URL) + URLString);
            sectionName.setText(getResources().getString(R.string.sectionName) + sectionNameString);

            //progressBar.setVisibility(View.INVISIBLE);

        }
    }
}