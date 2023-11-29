package algonquin.cst2355.groupfinalproject.DeezerSongSearcher;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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
        return null;
    }
}
