package algonquin.cst2355.groupfinalproject.Dictionary;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DictionaryItem.class},version = 1)
public abstract class DictionaryDB extends RoomDatabase {
    public abstract DictionaryItemDAO diDAO();
}
