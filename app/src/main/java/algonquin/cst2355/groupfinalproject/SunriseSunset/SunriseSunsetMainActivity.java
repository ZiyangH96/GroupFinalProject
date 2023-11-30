package algonquin.cst2355.groupfinalproject.SunriseSunset;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import algonquin.cst2355.groupfinalproject.R;

public class SunriseSunsetMainActivity extends AppCompatActivity {

    private EditText editTextLatitude, editTextLongitude;
    private Button buttonLookup, buttonSaveToFavorites, buttonViewFavorites, buttonDeleteFavorite;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String SEARCH_TERM_KEY = "searchTerm";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunrise_sunset);

        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);
        buttonLookup = findViewById(R.id.buttonLookup);
        buttonSaveToFavorites = findViewById(R.id.buttonSaveToFavorites);
        buttonViewFavorites = findViewById(R.id.buttonViewFavorites);
        buttonDeleteFavorite = findViewById(R.id.buttonDeleteFavorite);

        // Load previous search term from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String previousSearchTerm = prefs.getString(SEARCH_TERM_KEY, "");
        editTextLatitude.setText(previousSearchTerm);

        buttonLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform sunrise-sunset API lookup
                new SunLookupTask().execute(editTextLatitude.getText().toString(),
                        editTextLongitude.getText().toString());
            }
        });

        // Add onClickListeners for other buttons as needed
    }

    private class SunLookupTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // Perform API request and return the JSON response
            String latitude = params[0];
            String longitude = params[1];
            String apiUrl = "https://api.sunrise-sunset.io/json?lat=" + latitude +
                    "&lng=" + longitude + "&timezone=UTC&date=today";

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();
                Scanner scanner = new Scanner(stream).useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // Parse JSON response and display results
            try {
                JSONObject jsonResult = new JSONObject(result);
                String sunrise = jsonResult.getJSONObject("results").getString("sunrise");
                String sunset = jsonResult.getJSONObject("results").getString("sunset");

                // Display sunrise and sunset or handle as needed
                Toast.makeText(SunriseSunsetMainActivity.this, "Sunrise: " + sunrise + "\nSunset: " + sunset, Toast.LENGTH_LONG).show();

                // Save the current search term to SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putString(SEARCH_TERM_KEY, editTextLatitude.getText().toString());
                editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Implement other button functionalities as needed
}