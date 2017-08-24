package abanoubm.dayra.contact;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.model.ContactLocation;

public class FragmentDisplayContactMap extends Fragment implements OnMapReadyCallback, LocationListener {
    private Marker mDayraMarker;
    private GoogleMap mMap;

    private static final String ARG_ID = "id";
    private String id;
    private final int MAP_REQUEST_CODE = 600;

    private TextView site, st, addr, home;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_display_contact_map, container, false);

        site = (TextView) root.findViewById(R.id.site);
        addr = (TextView) root.findViewById(R.id.addr);
        st = (TextView) root.findViewById(R.id.st);
        home = (TextView) root.findViewById(R.id.home);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return root;

    }

    @Override
    public void onMapReady(final GoogleMap map) {
        mMap = map;

        new GetTask().execute();
        new DisplayTask().execute();

        if (Build.VERSION.SDK_INT < 23 ||
                ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

            setDayralocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MAP_REQUEST_CODE);
        }

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
                        .fromResource(R.mipmap.ic_map_current)));
    }

    private class GetTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String[] result) {
            if(getActivity()==null)
                return;
            if (result != null) {
                if (!result[0].equals("")) {
                    site.setVisibility(View.VISIBLE);
                    site.setText(result[0]);
                }
                if (!result[1].equals("")) {
                    st.setVisibility(View.VISIBLE);
                    st.setText(result[1]);
                }
                if (!result[2].equals("")) {
                    home.setVisibility(View.VISIBLE);
                    home.setText(result[2]);
                }
                if (!result[3].equals("")) {
                    addr.setVisibility(View.VISIBLE);
                    addr.setText(result[3]);
                }
            }
        }

        @Override
        protected String[] doInBackground(Void... params) {
            return DB.getInstant(getActivity()).getContactFullAddress(id);
        }
    }

    private class DisplayTask extends
            AsyncTask<Void, Void, ContactLocation> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(ContactLocation contact) {
            if(getActivity()==null)
                return;
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(contact.getMapLat(), contact.getMapLng())).title(getResources().getString(R.string.label_map_location))
                    .draggable(false) .icon(BitmapDescriptorFactory
                            .fromResource(R.mipmap.ic_map_user))).showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(contact.getMapLat(), contact.getMapLng()), contact.getZoom()));
        }

        @Override
        protected ContactLocation doInBackground(Void... params) {
            return DB.getInstant(getActivity()).getContactLocation(id);
        }

    }

    private void setDayralocation() {

        mMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
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
                            .fromResource(R.mipmap.ic_map_current)));

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
