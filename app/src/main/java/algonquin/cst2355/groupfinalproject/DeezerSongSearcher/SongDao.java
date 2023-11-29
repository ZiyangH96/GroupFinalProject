package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

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
