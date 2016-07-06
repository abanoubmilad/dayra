package abanoubm.dayra.contact;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import abanoubm.dayra.R;
import abanoubm.dayra.main.DB;

public class FragmentEditContactMap extends Fragment implements OnMapReadyCallback {
    private double lon, lat;
    private float zoom;
    private Marker addressMarker, dayraMarker;
    private static final String ARG_LAT = "lat";
    private static final String ARG_LNG = "lon";
    private static final String ARG_ZOM = "zoom";
    private static final String ARG_ID = "id";
    private String id;
    private TextView site, st, addr,home;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lon = getArguments().getDouble(ARG_LNG, 0);
            lat = getArguments().getDouble(ARG_LAT, 0);
            zoom = getArguments().getFloat(ARG_ZOM, 0);
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

        new GetTask().execute();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

              root.findViewById(R.id.deleteImage).setVisibility(View.GONE);

        root.findViewById(R.id.saveImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateTask().execute();
            }
        });

        return root;

    }

    @Override
    public void onMapReady(final GoogleMap map) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat, lon), zoom));
        map.setMyLocationEnabled(true);

        Location myLocation = map.getMyLocation();
        if (myLocation != null) {
            dayraMarker = map.addMarker(new MarkerOptions()
                    .position(
                            new LatLng(myLocation.getLatitude(), myLocation
                                    .getLongitude()))
                    .draggable(false)
                    .title("you are here")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        }
        map.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (dayraMarker != null)
                    dayraMarker.remove();
                dayraMarker = map.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(location.getLatitude(), location
                                        .getLongitude()))
                        .draggable(false)
                        .title("you are here")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

            }
        });


        addressMarker = map.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title("current location, tap or drag me to reposition")
                .draggable(true));

        addressMarker.showInfoWindow();

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
                if (addressMarker != null)
                    addressMarker.remove();
                addressMarker = map
                        .addMarker(new MarkerOptions()
                                .position(
                                        new LatLng(point.latitude,
                                                point.longitude))
                                .title("current location, tap or drag me to reposition")
                                .draggable(true));
                addressMarker.showInfoWindow();

                lat = point.latitude;
                lon = point.longitude;
                zoom = map.getCameraPosition().zoom;
            }
        });

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
        private ProgressDialog pBar;

        @Override
        protected void onPreExecute() {
            pBar = new ProgressDialog(getActivity());
            pBar.setCancelable(false);
            pBar.show();
        }

        @Override
        protected void onPostExecute(String[] result) {
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
            pBar.dismiss();
        }

        @Override
        protected String[] doInBackground(Void... params) {
            return DB.getInstant(getActivity()).getContactFullAddress(id);
        }
    }
}