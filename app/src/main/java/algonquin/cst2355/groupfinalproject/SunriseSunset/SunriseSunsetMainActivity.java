package algonquin.cst2355.groupfinalproject.SunriseSunset;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import algonquin.cst2355.groupfinalproject.R;
import algonquin.cst2355.groupfinalproject.SunriseSunset.LocationItem;
import algonquin.cst2355.groupfinalproject.SunriseSunset.SunriseSunsetAdapter;

public class SunriseSunsetMainActivity extends AppCompatActivity implements SunriseSunsetAdapter.ItemClickListener {

    private EditText editTextLatitude, editTextLongitude;
    private Button buttonLookup, buttonSaveToFavorites, buttonViewFavorites, buttonDeleteFavorite;
    private TextView textViewResult;
    private RecyclerView recyclerView;
    private SunriseSunsetAdapter adapter;
    private DatabaseHelper databaseHelper;

    private List<LocationItem> favoriteLocations;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String SEARCH_TERM_KEY = "searchTerm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunrise_sunset);

        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);
        buttonLookup = findViewById(R.id.buttonLookup);
        buttonSaveToFavorites = findViewById(R.id.buttonSaveToFavorites);
        buttonViewFavorites = findViewById(R.id.buttonViewFavorites);
        buttonDeleteFavorite = findViewById(R.id.buttonDeleteFavorite);
        textViewResult = findViewById(R.id.textViewResult);
        recyclerView = findViewById(R.id.recyclerView);

        // Load previous search term from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String previousSearchTerm = prefs.getString(SEARCH_TERM_KEY, "");
        editTextLatitude.setText(previousSearchTerm);
        databaseHelper = new DatabaseHelper(this);

        // Initialize favorites list and adapter
        favoriteLocations = new ArrayList<>();
        adapter = new SunriseSunsetAdapter(this, favoriteLocations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform sunrise-sunset API lookup
                new SunLookupTask().execute(editTextLatitude.getText().toString(),
                        editTextLongitude.getText().toString());
            }
        });

        buttonSaveToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFavorites();
            }
        });

        buttonViewFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFavorites();
            }
        });

        buttonDeleteFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFavorite();
            }
        });
    }

    private class SunLookupTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String latitude = params[0];
            String longitude = params[1];
            String apiUrl = "https://api.sunrisesunset.io/json?lat=" + latitude +
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
            if (result != null && !result.isEmpty()) {
                handleSunLookupResponse(result);
            } else {
                Toast.makeText(SunriseSunsetMainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleSunLookupResponse(String jsonResponse) {
        try {
            JSONObject response = new JSONObject(jsonResponse);
            JSONObject results = response.getJSONObject("results");

            String date = results.getString("date");
            String sunrise = results.getString("sunrise");
            String sunset = results.getString("sunset");

            // Display sunrise, sunset, and date
            String resultText = "Date: " + date + "\nSunrise: " + sunrise + "\nSunset: " + sunset;
            textViewResult.setText(resultText);

            // Save the current search term to SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putString(SEARCH_TERM_KEY, editTextLatitude.getText().toString());
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveToFavorites() {
        String latitude = editTextLatitude.getText().toString();
        String longitude = editTextLongitude.getText().toString();

        if (!latitude.isEmpty() && !longitude.isEmpty()) {
            LocationItem locationItem = new LocationItem(latitude, longitude);
            databaseHelper.addFavoriteLocation(latitude, longitude);
            favoriteLocations.add(locationItem);
            adapter.notifyDataSetChanged();
            Toast.makeText(SunriseSunsetMainActivity.this, "Location saved to favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SunriseSunsetMainActivity.this, "Please enter latitude and longitude", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewFavorites() {
        favoriteLocations.clear();
        favoriteLocations.addAll(databaseHelper.getFavoriteLocations());

        if (favoriteLocations.isEmpty()) {
            Toast.makeText(SunriseSunsetMainActivity.this, "No favorite locations found", Toast.LENGTH_SHORT).show();
        } else {
            // Show favorite locations using RecyclerView
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    private void deleteFavorite() {
        if (favoriteLocations.isEmpty()) {
            Toast.makeText(SunriseSunsetMainActivity.this, "No favorite locations found", Toast.LENGTH_SHORT).show();
        } else {
            // Show AlertDialog to delete favorite location
            showDeleteFavoriteDialog();
        }
    }

    private void showDeleteFavoriteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Favorite Location");
        builder.setMessage("Are you sure you want to delete this favorite location?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = adapter.getSelectedPosition();
                LocationItem locationItem = favoriteLocations.get(position);
                databaseHelper.deleteFavoriteLocation(locationItem.getLatitude(), locationItem.getLongitude());
                refreshFavorites();
                Toast.makeText(SunriseSunsetMainActivity.this, "Favorite location deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.setSelectedPosition(-1);
                dialog.dismiss();
            }
        });
        builder.create().show();

    }
    @Override
    public void onItemClick(View view, int position) {
        // Handle item click event (e.g., perform a new query for sunrise and sunset)
        LocationItem selectedLocation = favoriteLocations.get(position);
        new SunLookupTask().execute(selectedLocation.getLatitude(), selectedLocation.getLongitude());
    }

    @Override
    public void onDeleteClick(int position) {
        // Handle delete click here
        deleteFavorite(position);
    }
    private void deleteFavorite(int position) {
        // Implement your logic to delete the favorite location
        if (favoriteLocations.isEmpty()) {
            Toast.makeText(SunriseSunsetMainActivity.this, "No favorite locations found", Toast.LENGTH_SHORT).show();
        } else {
            // Show AlertDialog to delete favorite location
            showDeleteFavoriteDialog(position);
        }
    }
    private void showDeleteFavoriteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Favorite Location");
        builder.setMessage("Are you sure you want to delete this favorite location?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocationItem locationItem = favoriteLocations.get(position);
                databaseHelper.deleteFavoriteLocation(locationItem.getLatitude(), locationItem.getLongitude());
                refreshFavorites();
                Toast.makeText(SunriseSunsetMainActivity.this, "Favorite location deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.setSelectedPosition(-1);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void refreshFavorites() {
        favoriteLocations.clear();
        favoriteLocations.addAll(databaseHelper.getFavoriteLocations());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sunrise_sunset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_help) {
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help");
        builder.setMessage("Instructions for using the Sunrise & Sunset Lookup app.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
