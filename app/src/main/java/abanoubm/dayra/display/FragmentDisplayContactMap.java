package abanoubm.dayra.display;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import abanoubm.dayra.R;

public class FragmentDisplayContactMap extends Fragment implements OnMapReadyCallback {
    private double lon, lat;
    private float zoom;
    private Marker addressMarker, dayraMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lon = getArguments().getDouble("lon", 0);
            lat = getArguments().getDouble("lat", 0);
            zoom = getArguments().getFloat("zoom", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_display_contact_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
                .position(new LatLng(lat, lon)).title("current location")
                .draggable(false));
        addressMarker.showInfoWindow();

    }
}
