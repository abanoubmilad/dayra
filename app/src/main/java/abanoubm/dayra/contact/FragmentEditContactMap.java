package abanoubm.dayra.contact;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private double lonTemp, latTemp;
    private float zoomTemp;
    private Marker addressMarker, dayraMarker;
    private static final String ARG_LAT = "lat";
    private static final String ARG_LNG = "lon";
    private static final String ARG_ZOM = "zoom";
    private static final String ARG_ID = "id";
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lonTemp = lon = getArguments().getDouble(ARG_LNG, 0);
            latTemp = lat = getArguments().getDouble(ARG_LAT, 0);
            zoomTemp = zoom = getArguments().getFloat(ARG_ZOM, 0);
            id = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_contact_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        root.findViewById(R.id.backImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                final View view = li.inflate(R.layout.dialogue_back, null, false);
                final AlertDialog ad = new AlertDialog.Builder(getActivity())
                        .setCancelable(true).create();
                ad.setView(view, 0, 0, 0, 0);
                ad.show();
                view.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();
                    }
                });
                view.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
//                        startActivity(new Intent(getActivity(),
//                                DisplayContact.class).putExtra("id",
//                                id));
                        ad.dismiss();

                    }
                });
            }
        });
        root.findViewById(R.id.deleteImage).setVisibility(View.GONE);
        root.findViewById(R.id.resetImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latTemp = lat;
                lonTemp = lon;
                zoomTemp = zoom;
            }
        });
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
                new LatLng(latTemp, lonTemp), zoomTemp));
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
                .position(new LatLng(latTemp, lonTemp))
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
                latTemp = marker.getPosition().latitude;
                lonTemp = marker.getPosition().longitude;
                zoomTemp = map.getCameraPosition().zoom;
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

                latTemp = point.latitude;
                lonTemp = point.longitude;
                zoomTemp = map.getCameraPosition().zoom;
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
//            startActivity(new Intent(getActivity(),
//                    DisplayContact.class).putExtra("id",
//                    id));
        }

        @Override
        protected Void doInBackground(Void... params) {
            DB dbm = DB.getInstant(getActivity());
            ContentValues values = new ContentValues();
            values.put(DB.CONTACT_MAPLAT, latTemp);
            values.put(DB.CONTACT_MAPLNG, lonTemp);
            values.put(DB.CONTACT_MAPZOM, zoomTemp);
            dbm.updateContact(values, id);
            return null;
        }
    }

}