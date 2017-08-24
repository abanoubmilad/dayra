package abanoubm.dayra.contact;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
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
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;
import abanoubm.dayra.model.ContactLocation;

public class FragmentEditContactMap extends Fragment implements OnMapReadyCallback, LocationListener {
    private double lon, lat;
    private float zoom;
    private Marker mLocationMarker, mDayraMarker;
    private static final String ARG_ID = "id";
    private String id;
    private TextView site, st, addr, home;
    private GoogleMap mMap;
    private final int MAP_REQUEST_CODE = 600;
    private View buttonSearchAddress;

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
        View root = inflater.inflate(R.layout.fragment_edit_contact_map, container, false);


        site = (TextView) root.findViewById(R.id.site);
        addr = (TextView) root.findViewById(R.id.addr);
        st = (TextView) root.findViewById(R.id.st);
        home = (TextView) root.findViewById(R.id.home);

        buttonSearchAddress= root.findViewById(R.id.search_address);

        buttonSearchAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchAddressTask().execute(site.getText().toString()+" "+st.getText().toString());
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        root.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateTask().execute();
            }
        });

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

        map.setOnMarkerDragListener(new OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                lat = marker.getPosition().latitude;
                lon = marker.getPosition().longitude;
                zoom = map.getCameraPosition().zoom;
            }
        });
        map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if (mLocationMarker != null)
                    mLocationMarker.remove();
                mLocationMarker = map
                        .addMarker(new MarkerOptions()
                                .position(
                                        new LatLng(point.latitude,
                                                point.longitude))
                                .title(getResources().getString(R.string.label_map_location_edit))
                                .draggable(true) .icon(BitmapDescriptorFactory
                                        .fromResource(R.mipmap.ic_map_user)));
                mLocationMarker.showInfoWindow();

                lat = point.latitude;
                lon = point.longitude;
                zoom = map.getCameraPosition().zoom;
            }
        });


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

    private class UpdateTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            getActivity().finish();
            pBar.dismiss();

            Toast.makeText(getActivity(), R.string.msg_updated,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            DB dbm = DB.getInstant(getActivity());
            ContentValues values = new ContentValues();
            values.put(DB.CONTACT_MAPLAT, lat);
            values.put(DB.CONTACT_MAPLNG, lon);
            values.put(DB.CONTACT_MAPZOM, zoom);
            dbm.updateContact(values, id);
            return null;
        }
    }

    private class GetTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                if (!result[0].equals("")) {
                    site.setVisibility(View.VISIBLE);
                    site.setText(result[0]);
                    buttonSearchAddress.setVisibility(View.VISIBLE);

                }
                if (!result[1].equals("")) {
                    st.setVisibility(View.VISIBLE);
                    st.setText(result[1]);
                    buttonSearchAddress.setVisibility(View.VISIBLE);

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
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(contact.getMapLat(), contact.getMapLng()), contact.getZoom()));
            mLocationMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(contact.getMapLat(), contact.getMapLng())).title(getResources().getString(R.string.label_map_location_edit))
                    .draggable(false).icon(BitmapDescriptorFactory
                    .fromResource(R.mipmap.ic_map_user)));
            mLocationMarker.showInfoWindow();
        }

        @Override
        protected ContactLocation doInBackground(Void... params) {
            return DB.getInstant(getActivity()).getContactLocation(id);
        }

    }
    private class SearchAddressTask extends AsyncTask<String, Void,ArrayList<LatLng>> {
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            if (getActivity() == null)
                return;
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();

        }

        @Override
        protected void onPostExecute(ArrayList<LatLng> result) {
            if (getActivity() == null)
                return;
            if (result == null){
                Toast.makeText(getActivity(),
                        R.string.map_search_err, Toast.LENGTH_SHORT).show();
            } else if(result.size()==0) {
                Toast.makeText(getActivity(),
                        R.string.map_search_noresult, Toast.LENGTH_SHORT).show();
            }else{
                for (LatLng latLng :result) {
                    mMap.addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(latLng.latitude, latLng.longitude
                                    ))
                            .draggable(false)
                            .title(site.getText().toString()+" "+st.getText().toString())
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.mipmap.ic_map_search)));
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(result.get(0).latitude, result.get(0).longitude), 15));
                buttonSearchAddress.setVisibility(View.GONE);
            }
            pBar.dismiss();

        }

        @Override
        protected ArrayList<LatLng> doInBackground(String... params) {
            if(getActivity()==null)
                return null;
            ArrayList<LatLng> result = new ArrayList<>();
            Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
            try
            {
                List<Address> addresses = geoCoder.getFromLocationName(params[0], 5);
                for (Address address :addresses) {
                    result.add(new LatLng(address.getLatitude(), address.getLongitude()));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
            return result;
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