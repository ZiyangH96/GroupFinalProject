package algonquin.cst2355.groupfinalproject.SunriseSunset;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class SunriseSunsetDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SunriseSunsetDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public SunriseSunsetDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LATITUDE + " TEXT, " +
                COLUMN_LONGITUDE + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    public void saveLocation(LocationItem locationItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LATITUDE, locationItem.getLatitude());
        values.put(COLUMN_LONGITUDE, locationItem.getLongitude());
        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

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

    public void deleteLocation(LocationItem locationItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES,
                COLUMN_LATITUDE + " = ? AND " + COLUMN_LONGITUDE + " = ?",
                new String[]{locationItem.getLatitude(), locationItem.getLongitude()});
        db.close();
    }
}

