package abanoubm.dayra.contacts;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactLocationList;

public class DisplayContactsMap extends FragmentActivity implements
        OnMapReadyCallback, LocationListener {

    private Marker mDayraMarker;
    private GoogleMap mMap;

    private class DisplayTask extends
            AsyncTask<Void, Void, ArrayList<ContactLocationList>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(ArrayList<ContactLocationList> result) {
            if (result.size() == 0) {
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_locations, Toast.LENGTH_SHORT).show();
            } else {
                for (ContactLocationList attLoc : result) {
                    mMap.addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(attLoc.getMapLat(), attLoc
                                            .getMapLng()))
                            .title("\u200e" + attLoc.getName())
                            .draggable(false));
                }
            }
        }

        @Override
        protected ArrayList<ContactLocationList> doInBackground(Void... params) {
            return DB.getInstant(getApplicationContext()).getContactsLocations();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_map);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_display_locs);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mMap == null)
            return;

        if (mDayraMarker != null)
            mDayraMarker.remove();
        mDayraMarker = mMap.addMarker(new MarkerOptions()
                .position(
                        new LatLng(location.getLatitude(), location
                                .getLongitude()))
                .draggable(false)
                .title(getResources().getString(R.string.label_map_here))
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        new DisplayTask().execute();

        map.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location myLocation = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));

        if (myLocation != null) {
            mDayraMarker = map.addMarker(new MarkerOptions()
                    .position(
                            new LatLng(myLocation.getLatitude(), myLocation
                                    .getLongitude()))
                    .draggable(false)
                    .title(getResources().getString(R.string.label_map_here))
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        }

    }
}