package algonquin.cst2355.groupfinalproject.SunriseSunset;
/**
 * The {@code LocationItem} class represents a geographic location
 * with latitude and longitude coordinates.
 */
public class LocationItem {

    /**
     * The latitude coordinate of the location.
     */
    private String latitude;

    /**
     * The longitude coordinate of the location.
     */
    private String longitude;

    /**
     * Constructs a new {@code LocationItem} with the specified latitude and longitude.
     *
     * @param latitude  The latitude coordinate of the location.
     * @param longitude The longitude coordinate of the location.
     */
    public LocationItem(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the latitude coordinate of the location.
     *
     * @return The latitude coordinate as a string.
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude coordinate of the location.
     *
     * @return The longitude coordinate as a string.
     */
    public String getLongitude() {
        return longitude;
    }
}


