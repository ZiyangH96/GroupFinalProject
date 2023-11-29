package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;
import algonquin.cst2355.groupfinalproject.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;

public class SongDetailsFragment extends Fragment {

    private Song selectedSong;

    public SongDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_details, container, false);

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView durationTextView = view.findViewById(R.id.durationTextView);
        TextView albumNameTextView = view.findViewById(R.id.albumNameTextView);
        ImageView albumCoverImageView = view.findViewById(R.id.albumCoverImageView);
        Button saveButton = view.findViewById(R.id.saveButton);

        if (getArguments() != null) {
            selectedSong = getArguments().getParcelable("selectedSong");
            if (selectedSong != null) {
                titleTextView.setText(selectedSong.getTitle());
                durationTextView.setText(selectedSong.getDuration());
                albumNameTextView.setText(selectedSong.getAlbumName());
                Picasso.get().load(selectedSong.getAlbumCoverUrl()).into(albumCoverImageView);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Implement save functionality here
                        // You may want to save the selected song to the database
                        // Example: SongDatabase.getInstance(requireContext()).getSongDao().insert(selectedSong);
                    }
                });
            }
        }

        return view;
    }
}

