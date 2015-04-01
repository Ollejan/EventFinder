package se.mah.ad0025.apiprojekt;

/**
 * Klass som har hand om Markers information. En datastruktursklass.
 * @author Jonas Dahlström, Sebastian Aspegren, Thomas Andersson on 2014-11-07.
 */
public class MapMarker {
    private double latitude, longitude;
    private String title, snippet;

    public MapMarker(double latitude, double longitude, String title, String snippet) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.snippet = snippet;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }
}
