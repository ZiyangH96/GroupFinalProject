package algonquin.cst2355.groupfinalproject.Dictionary;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import algonquin.cst2355.groupfinalproject.DeezerSongSearcher.DeezerSearchActivity;
import algonquin.cst2355.groupfinalproject.R;
import algonquin.cst2355.groupfinalproject.RecipeSearch.RecipeSearchMainActivity;
import algonquin.cst2355.groupfinalproject.SunriseSunset.SunriseSunsetMainActivity;
import algonquin.cst2355.groupfinalproject.databinding.DefinitionBinding;
import algonquin.cst2355.groupfinalproject.databinding.DictionaryActivityMainBinding;

public class DictionaryMainActivity extends AppCompatActivity {

    private DictionaryActivityMainBinding binding;
    private RecyclerView.Adapter myAdapter = null;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);
        return true;
    }
    ArrayList<DictionaryItem> messages = new ArrayList<>();
    DictionaryItemDAO dDAO;
    DictionaryMainViewModel dictionaryModel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DictionaryActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.dictionaryToolBar);
        EditText et = binding.enterWord;
        Button searchBtn = binding.searchButton;
        Button recordBtn = binding.helpButton;
        Button resetBtn = binding.resetButton;
        TextView title = binding.dictionaryTitle;

        // define queue for api connection
        RequestQueue queue;
        queue = Volley.newRequestQueue(this);

        // create database
        DictionaryDB db = Room.databaseBuilder(getApplicationContext(), DictionaryDB.class, "database-name").build();

        dDAO = db.diDAO();

        // use shared preference to store and restore inputs
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String userInput = prefs.getString("inputText","");
        et.setText(userInput);
        // get everything from db
        dictionaryModel = new ViewModelProvider(this).get(DictionaryMainViewModel.class);
        messages = dictionaryModel.messages.getValue();

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            List<DictionaryItem> fromDatabase = dDAO.getAllItems();
            messages.addAll(fromDatabase);
        });
        if (messages == null) {
            dictionaryModel.messages.postValue(messages = new ArrayList<>());
        }
        dictionaryModel.selectedMessage.observe(this, (newValue) -> {
            MessageDetailsFragment wordFragment = new MessageDetailsFragment(newValue);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation,wordFragment)
                    .addToBackStack("")
                    .commit();
        });

        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                DefinitionBinding binding = DefinitionBinding.inflate(getLayoutInflater(), parent, false);
                return  new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.definitionText.setText(messages.get(position).getDefinition());
                holder.timeText.setText("Search time: "+messages.get(position).getTimeAdded());
                holder.wordText.setText(messages.get(position).getWord());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
        };
        binding.myRecyclerView.setAdapter( myAdapter );

        // Allow scrolling
        binding.myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<MeaningDetails> meaningList = new ArrayList<>();
        recordBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.reset)
                    .setMessage("Do you want to check past records?")
                    .setPositiveButton("Yes",(dialog,cl)->{
                        binding.helpButton.setVisibility(View.INVISIBLE);
                        binding.resetButton.setVisibility(View.VISIBLE);
                        binding.myRecyclerView.setVisibility(View.VISIBLE);
                        binding.dictionaryTitle.setVisibility(View.INVISIBLE);
                        binding.defaultTextView.setVisibility(View.INVISIBLE);
                    })
                    .setNegativeButton("No", (dialog, cl)->{} )
                    .create().show();
        });
        resetBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.reset)
                    .setMessage("Do you want to go back to word search?")
                    .setPositiveButton("Yes",(dialog,cl)->{
                        binding.helpButton.setVisibility(View.VISIBLE);
                        binding.resetButton.setVisibility(View.INVISIBLE);
                        binding.myRecyclerView.setVisibility(View.INVISIBLE);
                        binding.dictionaryTitle.setVisibility(View.VISIBLE);
                        binding.defaultTextView.setVisibility(View.VISIBLE);
                    })
                    .setNegativeButton("No", (dialog, cl)->{} )
                    .create().show();
        });

        final Boolean[] wordExistsInDB = {false};
        searchBtn.setOnClickListener(view -> {
                    CharSequence text = "Searching";
                    // display a short notice
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(this, text, duration);
                    toast.show();

                    // store user input
                    editor.putString("inputText", et.getText().toString());
                    editor.apply();
                    final Executor[] thread2 = {Executors.newSingleThreadExecutor()};
                    thread2[0].execute(() -> {
                        List<DictionaryItem> fromDatabase = dDAO.getAllItems();
                        // Check if there is a record that matches the user input
                        for (DictionaryItem item : fromDatabase) {
                            if (item.getWord().equalsIgnoreCase(et.getText().toString())) {
                                // Display the matching record directly
                                runOnUiThread(() -> {
                                    binding.dictionaryTitle.setText(item.getWord());
                                    messages.add(item);
                                    myAdapter.notifyItemChanged(messages.size() - 1);
                                    binding.defaultTextView.setText(item.definition.toString());
                                });
                                wordExistsInDB[0] = true;
                                return; // Exit the loop if a match is found
                            }
                        }
                    });
                    if (wordExistsInDB[0] == false) {
                        try {
                            String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + et.getText().toString();

                            StringBuilder displayText = new StringBuilder();
                            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                                    (response) -> {
                                        try {
                                            JSONObject content = response.getJSONObject(0);
                                            String word = content.getString("word");
                                            runOnUiThread(() -> binding.dictionaryTitle.setText(word));
                                            displayText.append("Word: ").append(word).append("\n");
                                            JSONArray meaningArray = content.getJSONArray("meanings");
                                            for (int i = 0; i < meaningArray.length(); i++) {
                                                JSONObject thisPartofSpeech = meaningArray.getJSONObject(i);
                                                String partOfSpeech = thisPartofSpeech.getString("partOfSpeech");
                                                displayText.append("\n \nPart of Speech: ").append(partOfSpeech).append("\n");
                                                JSONArray definitionArray = thisPartofSpeech.getJSONArray("definitions");
                                                // get at most 3 definitions in case there are too many to display
                                                for (int j = 0; j < Math.min(3, definitionArray.length()); j++) {
                                                    JSONObject currentDefinition = definitionArray.getJSONObject(j);
                                                    String currentDefinitionText = currentDefinition.getString("definition");
                                                    int defNum = j+1;
                                                    displayText.append("definition"+defNum+": \n").append(currentDefinitionText).append("\n");
                                                }
                                                JSONArray synonymArray = thisPartofSpeech.getJSONArray("synonyms");
                                                displayText.append(" \nsynonym:");
                                                for (int k = 0; k < synonymArray.length(); k++) {
                                                    displayText.append(synonymArray.getString(k)).append("  ");
                                                }
                                                displayText.append(" \nantonym: ");
                                                JSONArray antonymArray = thisPartofSpeech.getJSONArray("antonyms");
                                                for (int k = 0; k < antonymArray.length(); k++) {
                                                    displayText.append(antonymArray.getString(k)).append("  ");
                                                }
                                                MeaningDetails meaning = new MeaningDetails(partOfSpeech, definitionArray, synonymArray, antonymArray);
                                                meaningList.add(meaning);
                                            }

                                            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                                            String currentDateandTime = sdf.format(new Date());
                                            DictionaryItem newItem = new DictionaryItem(currentDateandTime, word, displayText.toString());
                                            // add to database
                                            Executor thread3 = Executors.newSingleThreadExecutor();
                                            thread3.execute(() -> {
                                                dDAO.insertDefinition(newItem);
                                                messages.clear();
                                                List<DictionaryItem> fromDatabase = dDAO.getAllItems();
                                                messages.addAll(fromDatabase);
                                            });
                                            myAdapter.notifyItemInserted(messages.size()-1);
                                            binding.defaultTextView.setText(displayText.toString());

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    },
                                    (error) -> {
                                        // Do something with loaded bitmap...
                                        Log.d("Error", "No word found");
                                    });
                            queue.add(request);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                        .setMessage("Do you want to check past records?")
                        .setPositiveButton("Yes",(dialog,cl)->{
                            binding.dictionaryTitle.setText(R.string.dictionary);
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
                            Intent goBackToMain = new Intent(DictionaryMainActivity.this, DeezerSearchActivity.class);
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

    class MyRowHolder extends RecyclerView.ViewHolder{

        TextView definitionText;
        TextView timeText;
        TextView wordText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            definitionText = itemView.findViewById(R.id.definitionText);
            timeText = itemView.findViewById(R.id.timeText);
            wordText = itemView.findViewById(R.id.wordText);
            itemView.setOnClickListener(clk -> {
                int position =  getAdapterPosition();
                DictionaryItem d = messages.get(position);
                DictionaryItem selected = messages.get(position);
                dictionaryModel.selectedMessage.postValue(selected);
                AlertDialog.Builder builder = new AlertDialog.Builder( DictionaryMainActivity.this );
                builder.setMessage("Do you want to delete the record" + wordText.getText());
                builder.setTitle("Warning:");
                builder.setNegativeButton("No",(dialog,cl)->{ });
                builder.setPositiveButton("Yes",(dialog,cl)->{
                    Executor thread5 = Executors.newSingleThreadExecutor();
                    thread5.execute(new Runnable() {
                        @Override
                        public void run() {
                            dDAO.deleteItem(d);
                            messages.clear();
                            List<DictionaryItem> fromDatabase = dDAO.getAllItems();
                            messages.addAll(fromDatabase);
                        }
                    });

                    myAdapter.notifyItemRemoved(position);
                    Snackbar.make(definitionText, "You deleted message" + position, Snackbar.LENGTH_SHORT).setAction("undo", click -> {
                        messages.add(position,d);
                        myAdapter.notifyItemInserted(position);
                        Executor thread6 = Executors.newSingleThreadExecutor();
                        thread6.execute(new Runnable() {
                            @Override
                            public void run() {
                                dDAO.insertDefinition(d);
                            }
                        });
                    }).show();
                });
                builder.create().show();
        });
    }
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
