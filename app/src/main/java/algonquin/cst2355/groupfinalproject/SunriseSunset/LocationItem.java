package algonquin.cst2355.groupfinalproject.SunriseSunset;
public class LocationItem {
    private String latitude;
    private String longitude;

    public LocationItem(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}

