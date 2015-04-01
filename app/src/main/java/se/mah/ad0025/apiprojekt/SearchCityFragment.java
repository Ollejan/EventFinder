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
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Fragment som visar komponenter för att söka på evenemang i en viss stad,
 * ett visst datum och inom en viss kategori.
 *  @author Jonas Dahlström, Sebastian Aspegren, Thomas Andersson
 */
public class SearchCityFragment extends Fragment {
    private EditText etCity, etDate;
    private Spinner spinnerCategory;
    private ListView lvEvents;
    private Controller controller;
    private ArrayList<EventfulEvent> eventCategories = new ArrayList<EventfulEvent>();
    private ArrayList<EventfulEvent> events = new ArrayList<EventfulEvent>();
    private ArrayList<String> content = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;

    public SearchCityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_city, container, false);
        etCity = (EditText)view.findViewById(R.id.etCity);
        etDate = (EditText)view.findViewById(R.id.etDate);
        etDate.setFocusable(false);
        spinnerCategory = (Spinner)view.findViewById(R.id.spinnerCategory);
        Button btnSearchCityDateCategory = (Button)view.findViewById(R.id.btnSearchCityDateCategory);
        lvEvents = (ListView)view.findViewById(R.id.lvEvents);
        arrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, content);
        btnSearchCityDateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.btnSearchCityDateCategoryClicked();
            }
        });
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.etDateClicked();
            }
        });
        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                controller.lvCityDateCategoryClicked(events.get(i));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateList();
        etDate.setText("");
        etCity.setText("");
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setEtDate(String date) {
        etDate.setText(date);
    }

    public void addSpinnerCategories(ArrayList<EventfulEvent> events) {
        this.eventCategories = events;
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);
        for (EventfulEvent event : events) {
            spinnerAdapter.add(event.getCategory());
        }
    }

    public String getEtCity() {
        return etCity.getText().toString();
    }

    public String getEtDate() {
        String date = etDate.getText().toString();
        date = date.replace("-", "");
        date += "00";
        return date;
    }

    public String getSpinnerCategory() {
        return eventCategories.get(spinnerCategory.getSelectedItemPosition()).getCategoryID();
    }

    public void addEventsToList(ArrayList<EventfulEvent> events) {
        this.events = events;
        content.clear();
        for (EventfulEvent event : events) {
            content.add(event.getTitle() + " - " + event.getCity() + " - " + event.getCountry() + " - " + event.getStartTime());
        }
        if(lvEvents != null) {
            populateList();
        }
    }

    private void populateList() {
        lvEvents.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }
}
