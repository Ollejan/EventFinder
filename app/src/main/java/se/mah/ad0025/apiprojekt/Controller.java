package se.mah.ad0025.apiprojekt;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Controller-klass som hanterar logiken i applikationen. Innehåller metoder som
 * hanterar knapptryck etc.
 * @author Jonas Dahlström, Sebastian Aspegren, Thomas Andersson on 2014-10-30.
 */
public class Controller {
    private MainActivity activity;
    private MenuFragment menuFragment;
    private MainMapFragment mainMapFragment;
    private SearchArtistFragment searchArtistFragment;
    private SearchCityFragment searchCityFragment;
    private HttpEventful httpEventful;
    private Resources res;
    private Calendar myCalendar;
    private double latitude, longitude;
    private LocationManager locationManager;

    public Controller(MainActivity activity, LocationManager locationManager) {
        this.activity = activity;
        this.locationManager = locationManager;
        res = activity.getResources();
        menuFragment = new MenuFragment();
        mainMapFragment = new MainMapFragment();
        searchArtistFragment = new SearchArtistFragment();
        searchCityFragment = new SearchCityFragment();
        httpEventful = new HttpEventful(this, activity);
        menuFragment.setController(this);
        searchArtistFragment.setController(this);
        searchCityFragment.setController(this);
        mainMapFragment.setController(this);
        activity.swapFragment(menuFragment, false);
    }

    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    };

    public void startLocationUpdates() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    public void stopLocationUpdates() {
        locationManager.removeUpdates(locationListener);
    }


    public void btnSearchByArtistClicked() {
        activity.swapFragment(searchArtistFragment, true);
    }

    public void btnShowNearbyEventsClicked() {
        activity.swapFragment(mainMapFragment, true);
        httpEventful.searchNearbyEvents(latitude, longitude);
    }

    public void btnSearchCityClicked() {
        activity.swapFragment(searchCityFragment, true);
        httpEventful.getAllCategories();
    }

    public void btnSearchArtistClicked() {
        String artistName = searchArtistFragment.getEtArtistName();
        if(artistName.equals("")) {
            Toast.makeText(activity, res.getString(R.string.toastEnterArtistName), Toast.LENGTH_SHORT).show();
        } else {
            httpEventful.searchArtist(artistName);
        }
    }

    public void updateListViewArtists(ArrayList<EventfulEvent> events) {
        searchArtistFragment.addEventsToList(events);
    }

    public void btnShowOnMapClicked() {
        ArrayList<EventfulEvent> events = searchArtistFragment.getEvents();
        activity.swapFragment(mainMapFragment, true);
        if (mainMapFragment != null && events != null) {
            mainMapFragment.setEvents(events);
            for (EventfulEvent event : events) {
                mainMapFragment.addMarker(event.getLatitude(), event.getLongitude(), event.getVenueAddress(), event.getStartTime());
            }
        }
    }

    public void lvArtistsClicked(int i) {
        ArrayList<EventfulEvent> events = searchArtistFragment.getEvents();
        String description = events.get(i).getDescription();
        Spanned newDescription = Html.fromHtml(description);
        String allInfo = res.getString(R.string.lvTitleText)+" "+events.get(i).getTitle()+"\n"+
                res.getString(R.string.lvCityText)+" "+events.get(i).getCity()+"\n"+
                res.getString(R.string.lvCountryText)+" "+events.get(i).getCountry()+"\n"+
                res.getString(R.string.lvDateText)+" "+events.get(i).getStartTime()+"\n"+
                res.getString(R.string.lvVenueName)+" "+events.get(i).getVenueName()+"\n"+
                res.getString(R.string.lvVenueAddress)+" "+events.get(i).getVenueAddress();

        showAlertDialog(res.getString(R.string.eventInfoText), allInfo + "\n\n" + newDescription);
    }

    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setPositiveButton(res.getString(R.string.alertDialogClose),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.setMessage(message);
        builder.create().show();
    }

    public void btnSearchCityDateCategoryClicked() {
        httpEventful.searchCityDateCategory(searchCityFragment.getEtCity(), searchCityFragment.getEtDate(), searchCityFragment.getSpinnerCategory());
    }

    public void etDateClicked() {
        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateText();
            }
        };

        new DatePickerDialog(menuFragment.getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateText() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        searchCityFragment.setEtDate(sdf.format(myCalendar.getTime()));
    }

    public void updateSpinnerCategories(ArrayList<EventfulEvent> events) {
        searchCityFragment.addSpinnerCategories(events);
    }

    public void updateListViewEvents(ArrayList<EventfulEvent> events) {
        searchCityFragment.addEventsToList(events);
    }

    public void addNearbyMarkers(ArrayList<EventfulEvent> events) {
        if (mainMapFragment != null) {
            mainMapFragment.setEvents(events);
            for (EventfulEvent event : events) {
                mainMapFragment.addMarker(event.getLatitude(), event.getLongitude(), event.getTitle(), event.getStartTime());
            }
        }
    }

    public void lvCityDateCategoryClicked(EventfulEvent event) {
        String description = event.getDescription();
        Spanned newDescription = Html.fromHtml(description);
        String allInfo = res.getString(R.string.lvTitleText)+" "+event.getTitle()+"\n"+
                res.getString(R.string.lvCityText)+" "+event.getCity()+"\n"+
                res.getString(R.string.lvCountryText)+" "+event.getCountry()+"\n"+
                res.getString(R.string.lvDateText)+" "+event.getStartTime()+"\n"+
                res.getString(R.string.lvVenueName)+" "+event.getVenueName()+"\n"+
                res.getString(R.string.lvVenueAddress)+" "+event.getVenueAddress();
        showAlertDialog(res.getString(R.string.eventInfoText), allInfo + "\n\n" + newDescription);
    }

    public void updateMarkersOnMap() {
        mainMapFragment.updateMarkers();
    }
}
