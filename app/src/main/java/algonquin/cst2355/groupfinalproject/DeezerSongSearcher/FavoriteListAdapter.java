// FavoriteListAdapter.java
package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;
import algonquin.cst2355.groupfinalproject.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHolder> {

    private List<Song> favoriteSongs;

    public FavoriteListAdapter(List<Song> favoriteSongs) {
        this.favoriteSongs = favoriteSongs;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Song song = favoriteSongs.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return favoriteSongs.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView albumNameTextView;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.favorite_song_title);
            albumNameTextView = itemView.findViewById(R.id.favoriteAlbumName);
        }

        public void bind(Song song) {
            titleTextView.setText(song.getTitle());
            albumNameTextView.setText(song.getAlbumName());
            // You can bind more data or handle click events if needed
        }
    }
}
