package se.mah.ad0025.apiprojekt;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Fragment som visar huvudmenyn och hanterar knapptryck. Detta fragment är det som visas
 * först, när applikationen startar.
 *  @author Jonas Dahlström, Sebastian Aspegren, Thomas Andersson
 */
public class MenuFragment extends Fragment {
    private Controller controller;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        Button btnSearchArtist = (Button)view.findViewById(R.id.btnSearchByArtist);
        Button btnShowNearbyEvents = (Button)view.findViewById(R.id.btnShowNearbyEvents);
        Button btnSearchCity = (Button)view.findViewById(R.id.btnSearchCity);
        btnSearchArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.btnSearchByArtistClicked();
            }
        });
        btnShowNearbyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.btnShowNearbyEventsClicked();
            }
        });
        btnSearchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.btnSearchCityClicked();
            }
        });
        return view;
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }
}
