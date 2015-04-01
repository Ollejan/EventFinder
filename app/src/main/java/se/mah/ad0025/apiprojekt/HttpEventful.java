package se.mah.ad0025.apiprojekt;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Klass som skickar och tar emot information till/från Eventfuls API.
 * AsyncTask-trådar hanterar kommunikationen mellan applikation och API.
 * @author Jonas Dahlström on 2014-10-30.
 */
public class HttpEventful {
    private Controller controller;
    private URL url;
    private InputStream input = null;
    private String apiKey = "JwLLMcQtwnSz3Nmg";
    public static final int SEARCHARTIST = 0, SEARCHCITY = 1, SEARCHNEARBYEVENTS = 2, GETCATEGORIES = 3;
    private int currentSearch;
    private MainActivity activity;
    private Resources res;

    public HttpEventful(Controller controller, MainActivity activity) {
        this.controller = controller;
        this.activity = activity;
        this.res = activity.getResources();
    }

    public void searchArtist(String artistName) {
        currentSearch = SEARCHARTIST;
        String correctString = replaceChars(artistName, " ", "+");
        try {
            url = new URL("http://api.eventful.com/rest/events/search?app_key=" + apiKey + "&keywords=" + correctString + "&date=Future&page_size=50");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new GetXMLFromURL().execute(url);
    }

    public void getAllCategories() {
        currentSearch = GETCATEGORIES;
        try {
            url = new URL("http://api.eventful.com/rest/categories/list?app_key=" + apiKey);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new GetXMLFromURL().execute(url);
    }

    public void searchCityDateCategory(String city, String date, String category) {
        currentSearch = SEARCHCITY;
        String correctString = replaceChars(city, " ", "+");
        try {
            url = new URL("http://api.eventful.com/rest/events/search?app_key=" + apiKey + "&location=" + correctString + "&date=" + date + "-" + date + "&category=" + category + "&page_size=50");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new GetXMLFromURL().execute(url);
    }

    public void searchNearbyEvents(double latitude, double longitude) {
        currentSearch = SEARCHNEARBYEVENTS;
        try {
            url = new URL("http://api.eventful.com/rest/events/search?app_key=" + apiKey + "&location=" + latitude + "," + longitude + "&date=Future" + "&within=100&units=km&page_size=50");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new GetXMLFromURL().execute(url);
    }

    private String replaceChars(String source, String from, String to) {
        StringBuilder dest = new StringBuilder(source);
        for ( int i = 0; i < source.length(); i++ ) {
            int foundAt = from.indexOf(source.charAt(i));
            if ( foundAt >= 0 )
                dest.setCharAt(i,to.charAt(foundAt));
        }
        return ""+dest;
    }

    private ArrayList<EventfulEvent> readTransactionsEvents(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<EventfulEvent> list = new ArrayList<EventfulEvent>();
        String name="", title="", description="", startTime="", venueName="", venueAddress="", city="", country="", latitude="", longitude;
        int eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                name = parser.getName();
            } else if(eventType == XmlPullParser.END_TAG) {
                name = "";
            } else if(eventType == XmlPullParser.TEXT) {
                if(name.equals("title")) {
                    title = parser.getText();
                } else if(name.equals("description")) {
                    description = parser.getText();
                } else if(name.equals("start_time")) {
                    startTime = parser.getText();
                } else if(name.equals("venue_name")) {
                    venueName = parser.getText();
                } else if(name.equals("venue_address")) {
                    venueAddress = parser.getText();
                } else if(name.equals("city_name")) {
                    city = parser.getText();
                } else if(name.equals("country_name")) {
                    country = parser.getText();
                } else if(name.equals("latitude")) {
                    latitude = parser.getText();
                } else if(name.equals("longitude")) {
                    longitude = parser.getText();
                    list.add(new EventfulEvent(title, description, startTime, venueName, venueAddress, city, country, latitude, longitude));
                    description = res.getString(R.string.noDescriptionFound);
                }
            }

            eventType = parser.next();
        }
        input.close();
        return list;
    }

    private ArrayList<EventfulEvent> readTransactionsCategories(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<EventfulEvent> list = new ArrayList<EventfulEvent>();
        String name="", category, categoryID="";
        int eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                name = parser.getName();
            } else if(eventType == XmlPullParser.END_TAG) {
                name = "";
            } else if(eventType == XmlPullParser.TEXT) {
                if(name.equals("id")) {
                    categoryID = parser.getText();
                } else if(name.equals("name")) {
                    category = parser.getText();
                    category = category.replace("&amp;", "&");
                    list.add(new EventfulEvent(categoryID, category));
                }
            }
            eventType = parser.next();
        }
        input.close();
        return list;
    }

    private class GetXMLFromURL extends AsyncTask<URL, Void, ArrayList<EventfulEvent>> {
        ArrayList<EventfulEvent> list = new ArrayList<EventfulEvent>();
        ProgressDialog progDailog;
        Resources res = activity.getResources();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(activity);
            progDailog.setMessage(res.getString(R.string.progDialogLoading));
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected ArrayList<EventfulEvent> doInBackground(URL... urls) {
            HttpURLConnection connection;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                input = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            XmlPullParser parser = Xml.newPullParser();
            try {
                parser.setInput(new InputStreamReader(input, "UTF-8"));
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                if(currentSearch == SEARCHARTIST || currentSearch == SEARCHCITY || currentSearch == SEARCHNEARBYEVENTS) {
                    list = readTransactionsEvents(parser);
                } else if(currentSearch == GETCATEGORIES) {
                    list = readTransactionsCategories(parser);
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }

        protected void onPostExecute(ArrayList<EventfulEvent> events) {
            if(currentSearch == SEARCHARTIST) {
                controller.updateListViewArtists(events);
            } else if(currentSearch == GETCATEGORIES) {
                Collections.sort(events, new Comparator<EventfulEvent>() {
                    @Override
                    public int compare(EventfulEvent eventfulEvent, EventfulEvent eventfulEvent2) {
                        return eventfulEvent.getCategory().compareTo(eventfulEvent2.getCategory());
                    }
                });
                controller.updateSpinnerCategories(events);
            } else if(currentSearch == SEARCHCITY) {
                controller.updateListViewEvents(events);
            } else if(currentSearch == SEARCHNEARBYEVENTS) {
                controller.addNearbyMarkers(events);
                controller.updateMarkersOnMap();
            }
            progDailog.dismiss();
        }
    }
}
