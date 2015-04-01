package se.mah.ad0025.apiprojekt;

/**
 * Klass som har hand om evenemangsinformationen. En datastruktursklass.
 * @author Jonas Dahlström on 2014-10-30.
 */
public class EventfulEvent {
    private String title, description, startTime, venueName, venueAddress, city, country, latitude, longitude;
    private String categoryID, category;

    public EventfulEvent(String title, String description, String startTime, String venueName, String venueAddress,
                         String city, String country, String latitude, String longitude) {

        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.venueName = venueName;
        this.venueAddress = venueAddress;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public EventfulEvent(String categoryID, String category) {
        this.categoryID = categoryID;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public String getCategory() {
        return category;
    }
}
