package se.mah.ad0025.apiprojekt;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Fragment som visar komponenter för att söka på en artist och visa informationen
 * om denna artist.
 *  @author Jonas Dahlström, Sebastian Aspegren, Thomas Andersson
 */
public class SearchArtistFragment extends Fragment {
    private EditText etArtistName;
    private ListView lvArtists;
    private Controller controller;
    private ArrayList<String> content = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<EventfulEvent> events;

    public SearchArtistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_artist, container, false);
        etArtistName = (EditText)view.findViewById(R.id.etArtistName);
        Button btnSearchArtist = (Button)view.findViewById(R.id.btnSearchArtist);
        Button btnShowOnMap = (Button)view.findViewById(R.id.btnShowOnMap);
        lvArtists = (ListView)view.findViewById(R.id.lvArtists);
        arrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, content);
        btnSearchArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.btnSearchArtistClicked();
            }
        });
        btnShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.btnShowOnMapClicked();
            }
        });
        lvArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                controller.lvArtistsClicked(i);
            }
        });
        return view;
    }

    public String getEtArtistName() {
        return etArtistName.getText().toString();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateList();
    }

    public void addEventsToList(ArrayList<EventfulEvent> events) {
        this.events = events;
        content.clear();
        for (EventfulEvent event : events) {
            content.add(event.getTitle() + " - " + event.getCity() + " - " + event.getCountry() + " - " + event.getStartTime());
        }
        if(lvArtists != null) {
            populateList();
        }
    }

    private void populateList() {
        lvArtists.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    public ArrayList<EventfulEvent> getEvents() {
        return events;
    }
}
