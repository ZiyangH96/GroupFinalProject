package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;

// DeezerSearchActivity.java

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import algonquin.cst2355.groupfinalproject.R;

public class DeezerSearchActivity extends AppCompatActivity {

    private Toolbar toolbarDeezerSearch;
    private TextView textViewTitle;
    private EditText editTextSearch;
    private Button buttonSearch;
    private Button buttonReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer_search);

        // Initialize views
        toolbarDeezerSearch = findViewById(R.id.toolbarDeezerSearch);
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonReset = findViewById(R.id.buttonReset);

        setSupportActionBar(toolbarDeezerSearch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set click listeners
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle search button click
                // Add your logic to initiate the search
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle reset button click
                editTextSearch.setText("");
            }
        });
    }
}

