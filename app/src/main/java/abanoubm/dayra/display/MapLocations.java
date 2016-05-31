package abanoubm.dayra.display;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
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
import abanoubm.dayra.model.ContactLoc;

public class MapLocations extends FragmentActivity implements
        OnMapReadyCallback {
    private Marker dayraMarker;
    private GoogleMap dmap;

    private class DisplayTask extends
            AsyncTask<Void, Void, ArrayList<ContactLoc>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(MapLocations.this);
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(ArrayList<ContactLoc> result) {
            pBar.dismiss();
            if (result.size() == 0) {
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.msg_no_contacts, Toast.LENGTH_SHORT).show();
            } else {
                for (ContactLoc attLoc : result) {
                    dmap.addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(attLoc.getMapLat(), attLoc
                                            .getMapLng()))
                            .title("\u200e" + attLoc.getName())
                            .draggable(false));
                }
            }
        }

        @Override
        protected ArrayList<ContactLoc> doInBackground(Void... params) {
            return DB.getInstance(
                    getApplicationContext(),
                    getSharedPreferences("login", Context.MODE_PRIVATE)
                            .getString("dbname", "")).getAttendantsLoc();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_map);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_display_locs);

        ((ImageView) findViewById(R.id.back))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        dmap = map;
        dmap.setMyLocationEnabled(true);

        Location myLocation = dmap.getMyLocation();
        if (myLocation != null) {
            dayraMarker = dmap.addMarker(new MarkerOptions()
                    .position(
                            new LatLng(myLocation.getLatitude(), myLocation
                                    .getLongitude()))
                    .draggable(false)
                    .title("you are here")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        }

        dmap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (dayraMarker != null)
                    dayraMarker.remove();
                dayraMarker = dmap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(location.getLatitude(), location
                                        .getLongitude()))
                        .draggable(false)
                        .title("you are here")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

            }
        });

        new DisplayTask().execute();

    }
}