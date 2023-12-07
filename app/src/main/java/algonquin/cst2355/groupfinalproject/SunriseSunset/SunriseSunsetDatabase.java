package algonquin.cst2355.groupfinalproject.SunriseSunset;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code SunriseSunsetDatabase} class provides database operations
 * for saving, retrieving, and deleting favorite locations.
 */
public class SunriseSunsetDatabase extends SQLiteOpenHelper {

    /**
     * The name of the database.
     */
    private static final String DATABASE_NAME = "SunriseSunsetDB";

    /**
     * The version of the database schema.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * The name of the table for storing favorite locations.
     */
    private static final String TABLE_FAVORITES = "favorites";

    /**
     * The name of the column for the unique identifier of each location.
     */
    private static final String COLUMN_ID = "id";

    /**
     * The name of the column for storing latitude information.
     */
    private static final String COLUMN_LATITUDE = "latitude";

    /**
     * The name of the column for storing longitude information.
     */
    private static final String COLUMN_LONGITUDE = "longitude";

    /**
     * Constructs a new {@code SunriseSunsetDatabase} instance.
     *
     * @param context The context in which the database is created.
     */
    public SunriseSunsetDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     *
     * @param db The SQLiteDatabase instance.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LATITUDE + " TEXT, " +
                COLUMN_LONGITUDE + " TEXT)";
        db.execSQL(createTableQuery);
    }

    /**
     * Called when the database needs to be upgraded.
     *
     * @param db         The SQLiteDatabase instance.
     * @param oldVersion The old version of the database.
     * @param newVersion The new version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    /**
     * Saves a new favorite location to the database.
     *
     * @param locationItem The LocationItem to be saved.
     */
    public void saveLocation(LocationItem locationItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LATITUDE, locationItem.getLatitude());
        values.put(COLUMN_LONGITUDE, locationItem.getLongitude());
        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    /**
     * Retrieves a list of all favorite locations from the database.
     *
     * @return A list of LocationItem representing favorite locations.
     */
    public List<LocationItem> getFavoriteLocations() {
        List<LocationItem> locationList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FAVORITES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String latitude = cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE));
                String longitude = cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE));

                LocationItem locationItem = new LocationItem(latitude, longitude);
                locationList.add(locationItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return locationList;
    }

    /**
     * Deletes a favorite location from the database.
     *
     * @param locationItem The LocationItem to be deleted.
     */
    public void deleteLocation(LocationItem locationItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES,
                COLUMN_LATITUDE + " = ? AND " + COLUMN_LONGITUDE + " = ?",
                new String[]{locationItem.getLatitude(), locationItem.getLongitude()});
        db.close();
    }
}

