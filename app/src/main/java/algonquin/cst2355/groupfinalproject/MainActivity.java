package algonquin.cst2355.groupfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import algonquin.cst2355.groupfinalproject.Dictionary.DictionaryMainActivity;
import algonquin.cst2355.groupfinalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

    }
}