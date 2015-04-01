package se.mah.ad0025.apiprojekt;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;


public class MainActivity extends Activity {
    private FragmentManager fragmentManager;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        controller = new Controller(this, lm);
        controller.startLocationUpdates();
    }

    public void swapFragment(Fragment fragment, boolean backstack) {
        if(backstack) {
            fragmentManager.beginTransaction().replace(R.id.masterLayout, fragment).addToBackStack(null).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.masterLayout, fragment).commit();
        }
    }

    @Override
    protected void onDestroy() {
        controller.stopLocationUpdates();
        super.onDestroy();
    }

}
