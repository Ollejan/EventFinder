package se.mah.ad0025.apiprojekt;

import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Fragment som visar Google-kartan och hanterar Markers i denna.
 *  @author Jonas Dahlström, Sebastian Aspegren, Thomas Andersson
 */
public class MainMapFragment extends Fragment {
    private GoogleMap map;
    private static View view;
    private Controller controller;
    private ArrayList<MapMarker> mapMarkers = new ArrayList<MapMarker>();
    private HashMap<String, EventfulEvent> eventHashMap = new HashMap<String, EventfulEvent>();
    private ArrayList<EventfulEvent> events = new ArrayList<EventfulEvent>();
    private EventfulEvent event;
    private Resources res;

    public MainMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view == null)
            view = inflater.inflate(R.layout.fragment_main_map, container, false);
        res = getResources();
        FragmentManager fm = getFragmentManager();
        MapFragment mf = (MapFragment)fm.findFragmentById(R.id.myGoogleMap);
        map = mf.getMap();
        map.setMyLocationEnabled(true);
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                event = eventHashMap.get(marker.getId());
                String description = event.getDescription();
                Spanned newDescription = Html.fromHtml(description);
                String allInfo = res.getString(R.string.lvTitleText)+" "+event.getTitle()+"\n"+
                        res.getString(R.string.lvCityText)+" "+event.getCity()+"\n"+
                        res.getString(R.string.lvCountryText)+" "+event.getCountry()+"\n"+
                        res.getString(R.string.lvDateText)+" "+event.getStartTime()+"\n"+
                        res.getString(R.string.lvVenueName)+" "+event.getVenueName()+"\n"+
                        res.getString(R.string.lvVenueAddress)+" "+event.getVenueAddress();
                controller.showAlertDialog(res.getString(R.string.eventInfoText), allInfo + "\n\n" + newDescription);
            }
        });
        return view;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMarkers();
    }

    public void addMarker(String latitude, String longitude, String title, String snippet) {
        double lat = Double.parseDouble(latitude);
        double longi = Double.parseDouble(longitude);
        mapMarkers.add(new MapMarker(lat, longi, title, snippet));
    }

    public void updateMarkers() {
        map.clear();
        for(int i = 0; i < mapMarkers.size(); i++) {
            Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(mapMarkers.get(i).getLatitude(), mapMarkers.get(i).getLongitude())).title(mapMarkers.get(i).getTitle()).snippet(mapMarkers.get(i).getSnippet()));
            eventHashMap.put(marker.getId(), events.get(i));
        }
    }

    public void setEvents(ArrayList<EventfulEvent> events) {
        this.events = events;
    }

    @Override
    public void onPause() {
        eventHashMap.clear();
        mapMarkers.clear();
        super.onPause();
    }
}
