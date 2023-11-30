// FavoriteListFragment.java
package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import algonquin.cst2355.groupfinalproject.R;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteListAdapter favoriteListAdapter;
    private List<Song> favoriteSongs;

    public FavoriteListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);

        recyclerView = view.findViewById(R.id.favoriteRecyclerView);
        favoriteSongs = // Retrieve your favorite songs from the database or another source
                (List<Song>) (favoriteListAdapter = new FavoriteListAdapter(favoriteSongs));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(favoriteListAdapter);

        // You can add click listeners, item decorations, or other setup code for the RecyclerView here

        return view;
    }
}
