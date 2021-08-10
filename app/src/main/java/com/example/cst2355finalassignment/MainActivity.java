package com.example.cst2355finalassignment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "MAIN_ACTIVITY";
    SharedPreferences preferences;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(ACTIVITY_NAME, "In onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText searchText = (EditText)findViewById(R.id.searchQuery);

        String searchToast = getString(R.string.toast_1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        // using toolbar as ActionBar
        setSupportActionBar(toolbar);


        // Display application icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // assigning ID of textView2 to a variable
        ImageView menuIcon = (ImageView) findViewById(R.id.imageView2);


        // "on click" operations to be performed
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override

            // incrementing the value of textView
            public void onClick( View view ) {
                System.out.println("hello");
            }
        });



        search = findViewById(R.id.searchQuery);

        preferences = getSharedPreferences("Search",Context.MODE_PRIVATE);
        String searchQuery = preferences.getString("Search","");
        search.setText(searchQuery);

        Fragment fragment = new UpdateFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();



        Button launchButton = findViewById(R.id.launchButton);
        launchButton.setOnClickListener(click -> {
            Toast toast= Toast.makeText(getApplicationContext(),searchToast,Toast.LENGTH_SHORT);
            toast.show();
            String searchTerm = searchText.getText().toString();
            Intent goToSearch = new Intent(MainActivity.this, SearchActivity.class);

            goToSearch.putExtra("SearchTerm", searchTerm.toString().replaceAll(" ", "_"));
            startActivity(goToSearch);

        });
    }



    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences(search.getText().toString());
    }

    private void SharedPreferences(String search){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Search",search);
        editor.commit();
    }

}