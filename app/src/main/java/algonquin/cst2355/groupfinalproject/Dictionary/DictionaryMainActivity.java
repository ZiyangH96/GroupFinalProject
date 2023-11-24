package algonquin.cst2355.groupfinalproject.Dictionary;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2355.groupfinalproject.databinding.ActivityMainBinding;
import algonquin.cst2355.groupfinalproject.databinding.DictionaryActivityMainBinding;

public class DictionaryMainActivity extends AppCompatActivity {

    private DictionaryActivityMainBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DictionaryActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // test code
        //
    }
}
