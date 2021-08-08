package com.example.cst2355finalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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