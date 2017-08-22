package abanoubm.dayra.contacts;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.Map;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.main.Utility;
import abanoubm.dayra.model.ContactLocationList;

public class DisplayContactsMap extends FragmentActivity implements
        OnMapReadyCallback, LocationListener {

    private Marker mDayraMarker;
    private GoogleMap mMap;
    private Map<String, String> mMarkerInfoList = new HashMap<>();
    private TextView site, st, addr, home;
    private ImageView photo;
    private String chosenContactId = null;
    private final int MAP_REQUEST_CODE = 600;

    private class DisplayContactsTask extends
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
                    mMarkerInfoList.put(mMap.addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(attLoc.getMapLat(), attLoc
                                            .getMapLng()))
                            .title("\u200e" + attLoc.getName())
                            .draggable(false)).getId(), attLoc.getId());
                }
            }
        }

        @Override
        protected ArrayList<ContactLocationList> doInBackground(Void... params) {
            return DB.getInstant(getApplicationContext()).getContactsLocations();
        }

    }

    private class GetContactInfoTask extends AsyncTask<Void, Void, String[]> {
        byte [] bitmap = null;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                if (!result[0].equals("")) {
                    site.setVisibility(View.VISIBLE);
                    site.setText(result[0]);
                } else
                    site.setVisibility(View.GONE);
                if (!result[1].equals("")) {
                    st.setVisibility(View.VISIBLE);
                    st.setText(result[1]);
                } else
                    st.setVisibility(View.GONE);
                if (!result[2].equals("")) {
                    home.setVisibility(View.VISIBLE);
                    home.setText(result[2]);
                } else
                    home.setVisibility(View.GONE);
                if (!result[3].equals("")) {
                    addr.setVisibility(View.VISIBLE);
                    addr.setText(result[3]);
                } else
                    addr.setVisibility(View.GONE);
            }
            if (bitmap != null) {
                photo.setVisibility(View.VISIBLE);
                photo.setImageBitmap(Utility.getBitmap(bitmap));
            } else
                photo.setVisibility(View.GONE);
        }

        @Override
        protected String[] doInBackground(Void... params) {
            bitmap = DB.getInstant(getApplicationContext()).getContactPhoto(chosenContactId);
            return DB.getInstant(getApplicationContext()).getContactFullAddress(chosenContactId);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_map);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));
        ((TextView) findViewById(R.id.subhead2)).setText(R.string.subhead_display_locs);
        findViewById(R.id.nav_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        site = (TextView) findViewById(R.id.site);
        addr = (TextView) findViewById(R.id.addr);
        st = (TextView) findViewById(R.id.st);
        home = (TextView) findViewById(R.id.home);
        photo = (ImageView) findViewById(R.id.photo);

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
        new DisplayContactsTask().execute();

        if (Build.VERSION.SDK_INT < 23 ||
                ContextCompat.checkSelfPermission(DisplayContactsMap.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(DisplayContactsMap.this,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

            setDayralocation();
        } else {
            ActivityCompat.requestPermissions(DisplayContactsMap.this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MAP_REQUEST_CODE);
        }


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String id = mMarkerInfoList.get(marker.getId());
                if (id != null && !id.equals(chosenContactId)) {
                    chosenContactId = id;
                    new GetContactInfoTask().execute();
                }
                return false;

            }
        });

    }

    private void setDayralocation() {

        mMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(new Criteria(), false));
        if (myLocation != null) {
            mDayraMarker = mMap.addMarker(new MarkerOptions()
                    .position(
                            new LatLng(myLocation.getLatitude(), myLocation
                                    .getLongitude()))
                    .draggable(false)
                    .title(getResources().getString(R.string.label_map_here))
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MAP_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                setDayralocation();
        }
    }
}