package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;
import algonquin.cst2355.groupfinalproject.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;





import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.squareup.picasso.Picasso;
import algonquin.cst2355.groupfinalproject.R;  // Assuming SongListAdapter and Song classes are in the R package




public class SongListFragment extends Fragment {
    private RecyclerView recyclerView;
    private SongListAdapter songListAdapter;
    private List<Song> songList;

    public SongListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        songList = new ArrayList<>();
        songListAdapter = new SongListAdapter(songList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(songListAdapter);

        // Get the artist's tracklist URL from arguments
        if (getArguments() != null) {
            String tracklistUrl = getArguments().getString("tracklistUrl");
            if (tracklistUrl != null) {
                getSongList(tracklistUrl);
            } else {
                Toast.makeText(getContext(), "Tracklist URL not available", Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    private void getSongList(String tracklistUrl) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, tracklistUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray tracklist = response.getJSONArray("data");
                            for (int i = 0; i < tracklist.length(); i++) {
                                JSONObject track = tracklist.getJSONObject(i);
                                String title = track.getString("title");
                                String duration = track.getString("duration");
                                String albumName = track.getJSONObject("album").getString("title");
                                String albumCoverUrl = track.getJSONObject("album").getString("cover_small");

                                Song song = new Song(title, duration, albumName, albumCoverUrl);
                                songList.add(song);
                            }
                            songListAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error fetching song list", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }
}
