package algonquin.cst2355.groupfinalproject.Dictionary;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import algonquin.cst2355.groupfinalproject.DeezerSongSearcher.DeezerSongMainActivity;
import algonquin.cst2355.groupfinalproject.MainActivity;
import algonquin.cst2355.groupfinalproject.R;
import algonquin.cst2355.groupfinalproject.RecipeSearch.RecipeSearchMainActivity;
import algonquin.cst2355.groupfinalproject.SunriseSunset.SunriseSunsetMainActivity;
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
        SharedPreferences.Editor editor = prefs.edit();
        EditText et = binding.enterWord;
        Button searchBtn = binding.searchButton;
        Button resetBtn = binding.helpButton;
        TextView title = binding.dictionaryTitle;

        // queue
        RequestQueue queue;
        queue = Volley.newRequestQueue(this);


        ArrayList<MeaningDetails> meaningList = new ArrayList<>();
        resetBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.reset)
                    .setMessage(getMessage())
                    .setPositiveButton("Yes",(dialog,cl)->{
                        meaningList.clear();
                        binding.dictionaryTitle.setText(R.string.dictionary);
                        binding.definition.setText(R.string.definition);
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
        searchBtn.setOnClickListener(view ->{

            CharSequence text = "Searching";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(this, text, duration);
                    toast.show();
            try {
                String url = "https://api.dictionaryapi.dev/api/v2/entries/en/"+et.getText().toString();

                StringBuilder displayText = new StringBuilder();
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                        (response) -> {
                            try {

                                    JSONObject content = response.getJSONObject(0);
                                    String word = content.getString("word");
                                    runOnUiThread( (  )  -> {
                                        binding.dictionaryTitle.setText(word);
                                            });
                                        displayText.append("Word: ").append(word).append("\n");
                                        JSONArray meaningArray = content.getJSONArray("meanings");
                                        for (int i=0;i<meaningArray.length();i++){
                                            JSONObject thisPartofSpeech = meaningArray.getJSONObject(i);
                                            String partOfSpeech = thisPartofSpeech.getString("partOfSpeech");
                                            displayText.append("Part of Speech: ").append(partOfSpeech).append("\n");
                                            JSONArray definitionArray = thisPartofSpeech.getJSONArray("definitions");
                                            JSONObject firstDefinition = definitionArray.getJSONObject(0);
                                            String firstDefinitionText = firstDefinition.getString("definition");
                                            displayText.append("definitions: \n").append(firstDefinitionText).append("\n");
                                            JSONArray synonymArray = thisPartofSpeech.getJSONArray("synonyms");
                                            displayText.append("synonym: \n");
                                            for (int k=0;k<synonymArray.length();k++){
                                                displayText.append(synonymArray.getString(k)).append("\n");
                                            }
                                            displayText.append("antonym: \n");
                                            JSONArray antonymArray = thisPartofSpeech.getJSONArray("antonyms");
                                            for (int k=0;k<antonymArray.length();k++){
                                                displayText.append(antonymArray.getString(k)).append("\n");
                                            }
                                            MeaningDetails meaning = new MeaningDetails(partOfSpeech,definitionArray,synonymArray,antonymArray);
                                            meaningList.add(meaning);
                                            runOnUiThread( (  )  -> {
                                                binding.definition.setText(displayText.toString());
                                            });

                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        },
                        (error) -> {
                            // Do something with loaded bitmap...
                            Log.d("Error","No word found");
                        });
                queue.add(request);
            }catch (Exception e) {
                e.printStackTrace();
            }
                }
                );

        // reset input
        binding.enterWord.setText("");
    }

    @NonNull
    private static String getMessage() {
        return "Do you want to reset?";
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch( item.getItemId() )
        {
            // this case allows going to dictionary by clicking the icon
            case R.id.dictionary_icon:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.reset)
                        .setMessage("Do you want to reset?")
                        .setPositiveButton("Yes",(dialog,cl)->{
                            binding.dictionaryTitle.setText(R.string.dictionary);
                            binding.definition.setText(R.string.definition);
                            binding.enterWord.setText("");
                        })
                        .setNegativeButton("No", (dialog, cl)->{} )
                        .create().show();
                break ;
            case R.id.help:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle(R.string.help)
                        .setMessage(R.string.help_text)
                        .setPositiveButton(R.string.sure,(dialog,cl)->{
                        })
                        .create().show();
                //put other go-to icons here
                break;
            case R.id.deezer_icon:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setTitle(R.string.deezer)
                        .setPositiveButton(R.string.sure,(dialog,cl)->{
                            Intent goBackToMain = new Intent(DictionaryMainActivity.this, DeezerSongMainActivity.class);
                            startActivity(goBackToMain);
                        })
                        .setNegativeButton(R.string.nothx,(dialog,cl)->{
                        })
                        .create().show();
                break;
            case R.id.sunrise_icon:
                AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
                builder4.setTitle(R.string.sunrise)
                        .setPositiveButton(R.string.sure,(dialog,cl)->{
                            Intent goBackToMain = new Intent(DictionaryMainActivity.this, SunriseSunsetMainActivity.class);
                            startActivity(goBackToMain);
                        })
                        .setNegativeButton(R.string.nothx,(dialog,cl)->{
                        })
                        .create().show();
                break;
            case R.id.recipe_icon:
                AlertDialog.Builder builder5 = new AlertDialog.Builder(this);
                builder5.setTitle(R.string.recipe)
                        .setPositiveButton(R.string.sure,(dialog,cl)->{
                            Intent goBackToMain = new Intent(DictionaryMainActivity.this, RecipeSearchMainActivity.class);
                            startActivity(goBackToMain);
                        })
                        .setNegativeButton(R.string.nothx,(dialog,cl)->{
                        })
                        .create().show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    static class MeaningDetails {
        private String partOfSpeech;
        private JSONArray definitionArray;
        private JSONArray synonymArray;
        private JSONArray antonymArray;

        public MeaningDetails(String partOfSpeech, JSONArray definitionArray, JSONArray synonymArray, JSONArray antonymArray) {
            this.partOfSpeech = partOfSpeech;
            this.definitionArray = definitionArray;
            this.synonymArray = synonymArray;
            this.antonymArray = antonymArray;
        }

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public void setPartOfSpeech(String partOfSpeech) {
            this.partOfSpeech = partOfSpeech;
        }

        public JSONArray getDefinitionArray() {
            return definitionArray;
        }

        public void setDefinitionArray(JSONArray definitionArray) {
            this.definitionArray = definitionArray;
        }

        public JSONArray getSynonymArray() {
            return synonymArray;
        }

        public void setSynonymArray(JSONArray synonymArray) {
            this.synonymArray = synonymArray;
        }

        public JSONArray getAntonymArray() {
            return antonymArray;
        }

        public void setAntonymArray(JSONArray antonymArray) {
            this.antonymArray = antonymArray;
        }
    }
}
