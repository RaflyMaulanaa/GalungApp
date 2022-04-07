package id.example.galungapp.Maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.sweetalert.SweetAlertDialog;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import id.example.galungapp.MenuDrawer.DaftarSawahActivity;
import id.example.galungapp.MenuDrawer.EditSawahActivity;
import id.example.galungapp.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;

    private Geocoder geocoder;
    private LatLng latLng;

    private Button btn_find;
    private ImageView iv_buttonback;
    private String address, dataSawah;
    private Double datalatitude, datalongitude;
    private String data_getmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btn_find = (Button) findViewById(R.id.btn_find);
        iv_buttonback = (ImageView) findViewById(R.id.iv_buttonback);

        Bundle extras = getIntent().getExtras();

        datalatitude = getIntent().getDoubleExtra("data_latitude", -3.793071);
        datalongitude = getIntent().getDoubleExtra("data_longitude", 119.6408);
        if(extras != null) {
            data_getmap = extras.getString("data_getmap");

            if(extras.getString("data_sawah") != null) {
                dataSawah = extras.getString("data_sawah");
            }
        }

        if(data_getmap != null && data_getmap.equals("view")){
            btn_find.setVisibility(View.GONE);
        }


        iv_buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        configureCameraIdle();
//        setupAutoCompleteFragment();
    }


    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                latLng = mMap.getCameraPosition().target;
                geocoder = new Geocoder(MapsActivity.this);
                btn_find.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            if (addressList != null && addressList.size() > 0) {
                                address = addressList.get(0).getAddressLine(0);
                                if (!address.isEmpty() && !address.isEmpty()) {
                                    new SweetAlertDialog(MapsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Anda Yakin Ini Lokasi Sawah Anda?")
                                            .setContentText(address)
                                            .setConfirmText("Ya, Lokasi Sawah Saya!")
                                            .setCancelText("Bukan")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    if (data_getmap.equals("daftar")) {
                                                        Intent mIntent = new Intent(MapsActivity.this, DaftarSawahActivity.class);
                                                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        mIntent.putExtra("titik_koordinat", latLng.latitude + "," + latLng.longitude);
                                                        mIntent.putExtra("lokasi", address);
                                                        mIntent.putExtra("data_sawah", dataSawah);
                                                        startActivity(mIntent);
                                                    } else if (data_getmap.equals("edit")) {
                                                        Bundle extrasData = getIntent().getExtras();
                                                        Intent mIntent = new Intent(MapsActivity.this, EditSawahActivity.class);
                                                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        mIntent.putExtra("titik_koordinat", latLng.latitude + "," + latLng.longitude);
                                                        mIntent.putExtra("lokasi", address);
                                                        mIntent.putExtra("id_sawah",extrasData.getInt("id_sawah"));
                                                        mIntent.putExtra("kecamatan",extrasData.getString("kecamatan"));
                                                        mIntent.putExtra("kelurahan",extrasData.getString("kelurahan"));
                                                        mIntent.putExtra("nama_sawah",extrasData.getString("nama_sawah"));
                                                        mIntent.putExtra("luas_sawah",extrasData.getString("luas_sawah"));
                                                        startActivity(mIntent);
                                                    }
                                                }
                                            })
                                            .show();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
    }

//    private void setupAutoCompleteFragment() {
//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                Toast.makeText(MapsActivity.this, place.getLatLng().toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(Status status) {
//                Log.e("Error", status.getStatusMessage());
//            }
//        });
//    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(datalatitude, datalongitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnCameraIdleListener(onCameraIdleListener);
    }

}
