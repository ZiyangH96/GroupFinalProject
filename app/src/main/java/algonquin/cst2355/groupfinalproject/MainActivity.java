package algonquin.cst2355.groupfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import algonquin.cst2355.groupfinalproject.Dictionary.DictionaryMainActivity;
import algonquin.cst2355.groupfinalproject.SunriseSunset.SunriseSunsetMainActivity;
import algonquin.cst2355.groupfinalproject.databinding.ActivityMainBinding;
import algonquin.cst2355.groupfinalproject.R;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolBar);

        // 4 buttons for each sub project
        Button deezerBtn = binding.deezerButton;
        Button recipeBtn = binding.recipeButton;
        Button sunriseBtn = binding.sunriseButton;
        Button dictionaryBtn = binding.dictionaryButton;

        // click the button to jump to the projects
        dictionaryBtn.setOnClickListener(v -> {
            Intent goToDictionary = new Intent(MainActivity.this, DictionaryMainActivity.class);
            startActivity(goToDictionary);
        });

        sunriseBtn.setOnClickListener(v -> {
            Intent goToSunrise = new Intent(MainActivity.this, SunriseSunsetMainActivity.class);
            startActivity(goToSunrise);
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch( item.getItemId() )
        {
            // this case allows going to dictionary by clicking the icon
            case R.id.dictionary_icon:
                Intent goToDictionary = new Intent(MainActivity.this, DictionaryMainActivity.class);
                startActivity(goToDictionary);

            //put other go-to icons here
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}