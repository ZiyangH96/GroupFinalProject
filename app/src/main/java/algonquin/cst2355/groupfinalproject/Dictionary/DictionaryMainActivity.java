package algonquin.cst2355.groupfinalproject.Dictionary;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2355.groupfinalproject.MainActivity;
import algonquin.cst2355.groupfinalproject.R;
import algonquin.cst2355.groupfinalproject.databinding.ActivityMainBinding;
import algonquin.cst2355.groupfinalproject.databinding.DictionaryActivityMainBinding;

public class DictionaryMainActivity extends AppCompatActivity {

    private DictionaryActivityMainBinding binding;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);
        return true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DictionaryActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        setSupportActionBar(binding.dictionaryToolBar);


        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("Word","");
        SharedPreferences.Editor editor = prefs.edit();
        EditText et = binding.enterWord;
        Button searchBtn = binding.searchButton;
        Button resetBtn = binding.helpButton;

        searchBtn.setOnClickListener(v->{
            CharSequence text = "Searching";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            // reset input
            binding.enterWord.setText("");
        });
        resetBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Reset")
                    .setMessage("Do you want to reset?")
                    .setPositiveButton("Yes",(dialog,cl)->{
                        Snackbar.make(et, "reset",Snackbar.LENGTH_SHORT)
                                .setAction("Undo", clk ->{
                                // TODO implement method to get the definition back
                                })
                                .show();
                        binding.enterWord.setText("");
                    })
                    .setNegativeButton("No", (dialog, cl)->{} )
                    .create().show();
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch( item.getItemId() )
        {
            // this case allows going to dictionary by clicking the icon
            case R.id.dictionary_icon:

            case R.id.help:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Help")
                        .setMessage("Enter a word then click the Search button. The app will help you find" +
                                "the dictionary definition for the word.")
                        .setPositiveButton("Thanks",(dialog,cl)->{
                        })
                        .create().show();
                //put other go-to icons here
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
