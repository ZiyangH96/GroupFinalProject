package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class DeezerSongMainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer_song_main);

        // Initialize UI elements
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        // Set click listener for the search button
        searchButton.setOnClickListener(view -> {
            String artistName = searchEditText.getText().toString().trim();
            if (!artistName.isEmpty()) {
                searchArtist(artistName);
            }
        });

        // Example: Initial fragment transaction
        SongListFragment initialFragment = new SongListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tracklistUrl", "https://api.deezer.com/artist/123/albums");
        initialFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, initialFragment);
        transaction.commit();
    }

    private void searchArtist(String artistName) {
        // Example: Constructing the Deezer API URL for artist search
        String deezerApiUrl = "https://api.deezer.com/search/artist/?q=" + artistName;

        // Example: Creating a fragment transaction
        SongListFragment searchResultFragment = new SongListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tracklistUrl", deezerApiUrl);
        searchResultFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, searchResultFragment);
        transaction.addToBackStack(null); // Optional: Adds the transaction to the back stack
        transaction.commit();
    }
}
