package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> {

    private List<Song> songList;

    public SongListAdapter(List<Song> songList) {
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);

        // Bind the data to the views
        holder.titleTextView.setText(song.getTitle());
        holder.albumNameTextView.setText(song.getAlbumName());

        // Load album cover using Picasso library
        Picasso.get().load(song.getAlbumCoverUrl()).into(holder.albumCoverImageView);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView albumNameTextView;
        ImageView albumCoverImageView;

        SongViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            albumNameTextView = itemView.findViewById(R.id.albumNameTextView);
            albumCoverImageView = itemView.findViewById(R.id.albumCoverImageView);
        }
    }
}
