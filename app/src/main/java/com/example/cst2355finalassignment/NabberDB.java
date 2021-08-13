package com.example.cst2355finalassignment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.appcompat.app.AlertDialog.Builder;
import static androidx.appcompat.app.AlertDialog.OnClickListener;

public class NabberDB extends SQLiteOpenHelper {
        protected final static String DATABASE_NAME = "NabberDB";
        protected final static int VERSION_NUM = 2;
        public final static String TABLE_NAME = "FAVS";
        public final static String COL_TITLE = "TITLE";
        public final static String COL_LINK = "LINK";
        public final static String COL_SECTIONNAME = "SECTIONNAME";
        public final static String COL_ID = "_id";
        public final String[] columns = {COL_ID, COL_TITLE, COL_LINK, COL_SECTIONNAME};

        public NabberDB(Context ctx) {
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
            db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_TITLE + " TEXT, "
                    + COL_LINK + " TEXT,"
                    + COL_SECTIONNAME + " TEXT);");
        }

        public boolean addArticle(SearchResult article) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_TITLE, article.getTitle());
            values.put(COL_LINK, article.getURL());
            values.put(COL_SECTIONNAME, article.getSectionName());

            return db.insert(TABLE_NAME, null, values) > 0;
        }

        public boolean deleteArticle(SearchResult sResult) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_TITLE, sResult.getTitle());
            values.put(COL_LINK, sResult.getURL());
            values.put(COL_SECTIONNAME, sResult.getSectionName());

            return db.delete(TABLE_NAME, COL_ID + "=" + sResult.getId(), null) > 0;

        }

        public List<SearchResult> getAll() {
            SQLiteDatabase db = this.getWritableDatabase();
            List<SearchResult> articleList = new ArrayList<>();
            Cursor c = db.query(TABLE_NAME, columns, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                articleList.add(new SearchResult(c.getInt(0), c.getString(1), c.getString(2), c.getString(3)));
                c.moveToNext();
            }
            printCursor(c, VERSION_NUM);
            return articleList;
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        private void printCursor(Cursor c, int version) {
            Log.i("printCursor", "Database version: " + version
                    + "\nNo. Columns: " + c.getColumnCount()
                    + "\nColumn Names: " + Arrays.toString(c.getColumnNames())
                    + "\nNo. Rows: " + c.getCount());
        }

    }