package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;
import android.content.Context;
import androidx.room.Database;
import androidx.room.PrimaryKey;
import androidx.room.*;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.*;
import com.squareup.picasso.Picasso;
// Song.java
@Entity
public class Song {
    @PrimaryKey(autoGenerate = true)
    private long id;
    // Getter for id
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }




    @ColumnInfo(name = "title")
    private String title;

    public Song(String title, String duration, String albumName, String albumCoverUrl) {
    }

    public String getTitle() {
        return title;
    }
    @ColumnInfo(name = "duration")
    private String duration;
    public String getDuration() {
        return duration;
    }
    @ColumnInfo(name = "albumName")
    private String albumName;
    public String getAlbumName() {
        return albumName;
    }
    
    @ColumnInfo(name = "albumCoverUrl")
    private String albumCoverUrl;

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }




    // Setter for title
    public void setTitle(String title) {
        this.title = title;
    }

    // Setter for duration
    public void setDuration(String duration) {
        this.duration = duration;
    }

    // Setter for albumName
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    // Setter for albumCoverUrl
    public void setAlbumCoverUrl(String albumCoverUrl) {
        this.albumCoverUrl = albumCoverUrl;
    }




// Constructor, getters, and setters

    // You can use annotations to customize the column names, indexes, etc.
}

// AppDatabase.java



