package algonquin.cst2355.groupfinalproject.Dictionary;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class DictionaryItem {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name="id")
    public int id;

    @ColumnInfo(name="timeAdded")
    String timeAdded;

    @ColumnInfo(name="word")
    protected String word;

    @ColumnInfo(name="definition")
    protected String definition;

    public DictionaryItem(String timeAdded, String word, String definition) {
        this.timeAdded = timeAdded;
        this.word = word;
        this.definition = definition;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
