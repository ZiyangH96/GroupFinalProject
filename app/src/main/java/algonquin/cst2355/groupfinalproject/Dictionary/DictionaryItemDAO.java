package algonquin.cst2355.groupfinalproject.Dictionary;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface DictionaryItemDAO {

    @Insert
    void insertDefinition (DictionaryItem d);

    @Query("Select * from DictionaryItem")
    List<DictionaryItem> getAllItems();

    @Delete
    void deleteItem(DictionaryItem d);

}
