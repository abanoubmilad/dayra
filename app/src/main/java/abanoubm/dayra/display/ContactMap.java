package abanoubm.dayra.display;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

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
import abanoubm.dayra.main.Utility;

public class ContactMap extends FragmentActivity implements OnMapReadyCallback {
    private double lon, lat;
    private double lon_new, lat_new;
    private float zoom;
    private float zoom_new;
    private boolean readonly;
    private Marker addressMarker, dayraMarker;
    private TextView right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_map);
        ((TextView) findViewById(R.id.subhead1)).setText(Utility.getDayraName(this));

        right = (TextView) findViewById(R.id.btnright);

        findViewById(R.id.back)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_CANCELED, new Intent());
                        finish();
                    }
                });

        lon = getIntent().getDoubleExtra("lon", 0);
        lat = getIntent().getDoubleExtra("lat", 0);
        zoom = getIntent().getFloatExtra("zoom", 0);
        readonly = getIntent().getBooleanExtra("readonly", false);
        if (readonly) {
            ((TextView) findViewById(R.id.subhead2)).setText(R.string.btn_map_display);
        } else {
            lon_new = lon;
            lat_new = lat;
            zoom_new = zoom;
            right.setVisibility(View.VISIBLE);
            right.setText(R.string.btn_save);
            ((TextView) findViewById(R.id.subhead2)).setText(R.string.btn_map_edit);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        if (readonly) {
            addressMarker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lon)).title("current location")
                    .draggable(false));
        } else {
            addressMarker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .title("current location, tap or drag me to reposition")
                    .draggable(true));
        }
        addressMarker.showInfoWindow();

        if (!readonly)
            map.setOnMarkerDragListener(new OnMarkerDragListener() {

                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    lat_new = marker.getPosition().latitude;
                    lon_new = marker.getPosition().longitude;
                    zoom_new = map.getCameraPosition().zoom;
                }
            });
        if (!readonly)
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

                    lat_new = point.latitude;
                    lon_new = point.longitude;
                    zoom_new = map.getCameraPosition().zoom;
                }
            });

        right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("lat", lat_new);
                returnIntent.putExtra("lon", lon_new);
                returnIntent.putExtra("zoom", zoom_new);
                setResult(RESULT_OK, returnIntent);
                finish();

            }
        });

    }
}