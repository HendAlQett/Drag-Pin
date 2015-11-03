package com.hendalqett.dragpin;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    @Bind(R.id.locationMarkerText)
    TextView locationMarkerText;
    @Bind(R.id.ivMarker)
    ImageView ivMarker;
    @Bind(R.id.locationMarker)
    LinearLayout locationMarker;
    @Bind(R.id.addressText)
    TextView addressText;
    @Bind(R.id.parentLayout)
    FrameLayout parentLayout;
    private GoogleMap mMap;


    // public static String ShopLat;
    public static String ShopPlaceId;
    // public static String ShopLong;
    private LocationRequest mLocationRequest;
    // Stores the current instantiation of the location client in this object
//    private LocationClient mLocationClient; //Deprecated
    private GoogleApiClient mGoogleApiClient;

    boolean mUpdatesRequested = false;

    private LatLng center;

    private Geocoder geocoder;
    private List<Address> addresses;
    int mStatus;
    Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //This part is related to onMapReady
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);


        mStatus = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());

        if (mStatus != ConnectionResult.SUCCESS) { // Google Play Services are
            // not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(mStatus, this,
                    requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment
            // Create a new global location parameters object
            mLocationRequest = LocationRequest.create();

            /*
             * Set the update interval
             */
            mLocationRequest.setInterval(GData.UPDATE_INTERVAL_IN_MILLISECONDS);

            // Use high accuracy
            mLocationRequest
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            // Set the interval ceiling to one minute
            mLocationRequest
                    .setFastestInterval(GData.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

            // Note that location updates are off until the user turns them on
            mUpdatesRequested = false;

            /*
             * Create a new location client, using the enclosing class to handle
             * callbacks.
             */
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();


        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        if (mStatus != ConnectionResult.SUCCESS) {

        } else {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        if (mStatus != ConnectionResult.SUCCESS) {

        } else {
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng tahrir = new LatLng(30.0444, 31.2357);
//
//        mMap.addMarker(new MarkerOptions().position(tahrir).title("Marker in Tahrir square").draggable(true));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tahrir, 17));
//
//
//        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//            @Override
//            public void onMarkerDragStart(Marker marker) {
//
//            }
//
//            @Override
//            public void onMarkerDrag(Marker marker) {
//
//            }
//
//            @Override
//            public void onMarkerDragEnd(Marker marker) {
//                LatLng newPosition = marker.getPosition();
//                Toast.makeText(MapsActivity.this, "Latitude: " + Double.toString(newPosition.latitude) + " Longitude: " + Double.toString(newPosition.longitude), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
    private void setupMap() {
        try {
            LatLng tahrir;
            String lat = "", lng = "";

            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // Enabling MyLocation in Google Map
            mMap.setMyLocationEnabled(true);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                tahrir = new LatLng(mLastLocation
                        .getLatitude(), mLastLocation
                        .getLongitude());
                lat = Double.toString(mLastLocation.getLatitude());
                lng = Double.toString(mLastLocation.getLongitude());


            } else {
                tahrir = new LatLng(30.0444, 31.2357);
            }

//            LatLng tahrir = new LatLng(30.0444, 31.2357);

//            mMap.addMarker(new MarkerOptions().position(tahrir).title("Marker in Tahrir square").draggable(true));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tahrir, 17));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(tahrir).zoom(19f).tilt(70).build();

            mMap.setMyLocationEnabled(true);
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // Clears all the existing markers
            mMap.clear();

            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                @Override
                public void onCameraChange(CameraPosition arg0) {

                    center = mMap.getCameraPosition().target;

                    locationMarkerText.setText(" Set your Location ");
                    mMap.clear();
                    locationMarker.setVisibility(View.VISIBLE);

                    Snackbar
                            .make(parentLayout, "Latitude: " + center.latitude + " Longitude: " + center.longitude, Snackbar.LENGTH_LONG)

                            .show(); // Don’t forget to show!


                    try {
                        new GetLocationAsync(center.latitude, center.longitude)
                                .execute();

                    } catch (Exception e) {
                    }
                }
            });

            locationMarker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    try {

                        LatLng latLng1 = new LatLng(center.latitude,
                                center.longitude);

                        Marker m = mMap.addMarker(new MarkerOptions()
                                .position(latLng1)
                                .title(" Set your Location ")
                                .snippet("")
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.ic_marker_flag)));
                        m.setDraggable(true);

                        locationMarker.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        setupMap();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        // boolean duplicateResponse;
        double x, y;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {

            x = latitude;
            y = longitude;
        }

        @Override
        protected void onPreExecute() {
            addressText.setText(" Getting location ");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                geocoder = new Geocoder(MapsActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (geocoder.isPresent()) {
                    Address returnAddress = addresses.get(0);

                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();

                    str.append(localityString + "");
                    str.append(city + "" + region_code + "");
                    str.append(zipcode + "");

                } else {
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                addressText.setText(addresses.get(0).getAddressLine(0) + " - "
                        + addresses.get(0).getAddressLine(1) + " ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

}
