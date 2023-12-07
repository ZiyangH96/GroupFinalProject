package algonquin.cst2355.groupfinalproject.SunriseSunset;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2355.groupfinalproject.Dictionary.DictionaryMainActivity;
import algonquin.cst2355.groupfinalproject.R;

public class SunriseSunsetMainActivity extends AppCompatActivity {

    private EditText editTextLatitude, editTextLongitude;
    private Button buttonLookup, buttonSaveToFavorites, buttonViewFavorites;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String SEARCH_TERM_KEY = "searchTerm";
    private static final String FAVORITE_LOCATIONS_KEY = "favoriteLocations";

    private RequestQueue requestQueue;
    private SunriseSunsetDatabase sunriseSunsetDatabase;
    private TextView textViewSunInfo;
    private RecyclerView recyclerViewFavorites;
    private FavoriteLocationsAdapter favoritesAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunrise_sunset);

        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);
        buttonLookup = findViewById(R.id.buttonLookup);
        buttonSaveToFavorites = findViewById(R.id.buttonSaveToFavorites);
        buttonViewFavorites = findViewById(R.id.buttonViewFavorites);
        textViewSunInfo = findViewById(R.id.textViewSunInfo);

        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites);
        favoritesAdapter = new FavoriteLocationsAdapter(this, new ArrayList<>());
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFavorites.setAdapter(favoritesAdapter);

        requestQueue = Volley.newRequestQueue(this);
        sunriseSunsetDatabase = new SunriseSunsetDatabase(this);

        // Load previous search term from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String previousLatitude = prefs.getString("latitude", "");
        String previousLongitude = prefs.getString("longitude", "");

        editTextLatitude.setText(previousLatitude);
        editTextLongitude.setText(previousLongitude);

        buttonLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitude = editTextLatitude.getText().toString();
                String longitude = editTextLongitude.getText().toString();

                if (!latitude.isEmpty() && !longitude.isEmpty()) {
                    // Perform sunrise-sunset API lookup using Volley
                    performSunLookup(latitude, longitude);
                } else {
                    Toast.makeText(SunriseSunsetMainActivity.this, "Please enter latitude and longitude", Toast.LENGTH_SHORT).show();
                }
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
                loadFavoriteLocations();
                recyclerViewFavorites.setVisibility(View.VISIBLE);
            }
        });

        favoritesAdapter.setOnItemClickListener(new FavoriteLocationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Handle item click, e.g., perform a new query for the selected location
                LocationItem selectedLocation = favoritesAdapter.getItem(position);
                performSunLookup(selectedLocation.getLatitude(), selectedLocation.getLongitude());
            }
        });

        favoritesAdapter.setOnItemLongClickListener(new FavoriteLocationsAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                // Handle long click, e.g., delete the favorite location
                deleteFavoriteLocation(position);
            }
        });

        favoritesAdapter.setOnItemLongClickListener(new FavoriteLocationsAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                // Show an AlertDialog for confirmation before deleting
                showDeleteConfirmationDialog(position);
            }
        });






        // Add onClickListeners for other buttons as needed
    }

    private void performSunLookup(String latitude, String longitude) {
        String apiUrl = "https://api.sunrisesunset.io/json?lat=" + latitude +
                "&lng=" + longitude + "&timezone=UTC&date=today";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleSunLookupResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleSunLookupError(error);
                    }
                });

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);
    }

    private void handleSunLookupResponse(JSONObject response) {
        try {
            String sunrise = response.getJSONObject("results").getString("sunrise");
            String sunset = response.getJSONObject("results").getString("sunset");
            String date = response.getJSONObject("results").getString("date");

            // Display sunrise, sunset, and date in textViewSunInfo
            String sunInfo = "Date: " + date + "\nSunrise: " + sunrise + "\nSunset: " + sunset;
            textViewSunInfo.setText(sunInfo);

            // Save the current search term, latitude, longitude, and date to SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.putString(SEARCH_TERM_KEY, editTextLatitude.getText().toString());
            editor.putString("latitude", editTextLatitude.getText().toString()); // Save latitude
            editor.putString("longitude", editTextLongitude.getText().toString()); // Save longitude
            editor.putString("date", date);
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    private void handleSunLookupError(VolleyError error) {
        // Handle errors (e.g., show an error message to the user)
        Toast.makeText(SunriseSunsetMainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
    }

    private void saveToFavorites() {
        String latitude = editTextLatitude.getText().toString();
        String longitude = editTextLongitude.getText().toString();

        if (!latitude.isEmpty() && !longitude.isEmpty()) {
            LocationItem locationItem = new LocationItem(latitude, longitude);
            sunriseSunsetDatabase.saveLocation(locationItem);
            Toast.makeText(SunriseSunsetMainActivity.this, "Location saved to favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SunriseSunsetMainActivity.this, "Please enter latitude and longitude", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFavoriteLocations() {
        List<LocationItem> favoriteLocations = sunriseSunsetDatabase.getFavoriteLocations();
        favoritesAdapter.setLocations(favoriteLocations);
    }

    private void deleteFavoriteLocation(int position) {
        LocationItem deletedLocation = favoritesAdapter.getItem(position);
        sunriseSunsetDatabase.deleteLocation(deletedLocation);
        loadFavoriteLocations(); // Refresh the RecyclerView
    }


    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Favorite Location");
        builder.setMessage("Are you sure you want to delete this favorite location?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the selected favorite location
                deleteFavoriteLocation(position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            showHelpDialog();
            if (item.getItemId() == R.id.dictionary_MainActivity) {
                // Open the OtherActivity when the menu item is clicked
                Intent intent = new Intent(this, DictionaryMainActivity.class);
                startActivity(intent);
                return true;
            }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sunrise_sunset, menu);
        return true;
    }





    // Other methods...
}

