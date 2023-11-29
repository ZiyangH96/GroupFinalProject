package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;
import android.content.Context;
import androidx.room.Database;
import androidx.room.PrimaryKey;
import androidx.room.*;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.*;

// Song.java
@Entity
public class Song {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "duration")
    private String duration;

    @ColumnInfo(name = "albumName")
    private String albumName;

    @ColumnInfo(name = "albumCoverUrl")
    private String albumCoverUrl;

    // Constructor, getters, and setters

    // You can use annotations to customize the column names, indexes, etc.
}

// SongDao.java
@Dao
public interface SongDao {
    @Insert
    void insert(Song song);

    @Delete
    void delete(Song song);

    @Query("SELECT * FROM Song")
    List<Song> getAllSongs();
}

// AppDatabase.java



@Database(entities = {Song.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract SongDao songDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "song_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }

