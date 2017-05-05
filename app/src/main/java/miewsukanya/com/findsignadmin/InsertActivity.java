package miewsukanya.com.findsignadmin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InsertActivity extends AppCompatActivity implements OnMapReadyCallback {
    //Explicit
    GoogleMap mGoogleMap;
    //EditText edtSignName;
    EditText edtSearch,edt_lat,edt_lng;
    String lngString, latString,signString;
    ImageView imgInsert;
    TextView edt_adId;
    RadioGroup signnameRadioGroup;
    RadioButton signnameRadioButton;

    GPSTracker gps;
    private LocationManager locationManager;
    private LocationListener listener;
    //RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        /*if (googleServicesAvailable()) {
            // Toast.makeText(this, "Perfect!!", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_insert);

           // GetMap getMap = new GetMap(InsertActivity.this);
           // getMap.execute();

           // initMap();

        } else {
            //No google map layout
        }*/
        //intent data from mainActivity 29/01/17
        TextView textView = (TextView) findViewById(R.id.textView6);
        Intent intent = getIntent();
        String adminID = intent.getStringExtra("adminID");
        textView.setText(adminID);
        textView.setTextSize(20);
        Log.d("adminID", "ID :" + adminID);

        edt_lat = (EditText) findViewById(R.id.edt_lat);
        edt_lng = (EditText) findViewById(R.id.edt_lng);
        edt_adId = (TextView) findViewById(R.id.textView6);

        //get lat lng location device
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            @Override
            public void onProviderEnabled(String provider) {

            }
            @Override
            public void onProviderDisabled(String provider) {

            }
        };{
            gps = new GPSTracker(InsertActivity.this);

            if(gps.canGetLocation()){

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                edt_lat.setText(latitude+"");
                edt_lng.setText(longitude+"");

                Log.d("01FebV1", "Marker" + "Lat:" + latitude + "Lng:" + longitude);

            }else{
                // txtLocation.setText("อุปกรณ์์ของคุณ ปิด GPS");
            }
            configure_button();
        }//listener
        GetMap getMap = new GetMap(InsertActivity.this);
        getMap.execute();
        initMap();
    }//Main Method

    //Show map
    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }//initMap

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (mGoogleMap != null){

            //touch map set marker
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                    //remove marker
                    if (marker != null) {
                        marker.remove();
                    }
                    //touch map add marker
                    MarkerOptions options = new MarkerOptions()
                            .position(latLng);
                    marker = mGoogleMap.addMarker(options);
                    marker.showInfoWindow();
                }//on map click
            });
            //touch marker remove
            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                 //   marker.remove();
                    return false;
                }
            });
            //move icon place
            mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }
                @Override
                public void onMarkerDrag(Marker marker) {

                }
                @Override
                public void onMarkerDragEnd(Marker marker) {
                   /*Geocoder gc = new Geocoder(InsertActivity.this);
                    LatLng latLng = marker.getPosition();
                    double lat = latLng.latitude;
                    double lng = latLng.longitude;
                    List<android.location.Address> list =null;
                    try {
                        list = gc.getFromLocation(lat,lng,1);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    android.location.Address add = list.get(0);
                    marker.setTitle(add.getLocality());
                    marker.showInfoWindow();*/
                }//on drag end marker

            });
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }
                @Override
                public View getInfoContents(Marker marker) {
                    //blind widget
                    final EditText lat = (EditText) findViewById(R.id.edt_lat);
                    final EditText lng = (EditText) findViewById(R.id.edt_lng);
                    //final EditText signName = (EditText) findViewById(R.id.edtSignName);

                    View v = getLayoutInflater().inflate(R.layout.info_window,null);
                    TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
                    TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
                    TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
                    TextView tvSnippet = (TextView) v.findViewById(R.id.tv_snippet);

                    LatLng latLng = marker.getPosition();
                    tvLocality.setText(marker.getTitle());
                    tvLat.setText("Latitude: "+latLng.latitude);
                    tvLng.setText("Longitude: "+latLng.longitude);
                    tvSnippet.setText(marker.getSnippet());

                    //show lat long in edit text
                    lat.setText(latLng.latitude+"");
                    lng.setText(latLng.longitude+"");
                    return v;
                }
            });
        }//if

    }//onMapReady

    //Search Map All
    private class GetMap extends AsyncTask<Void, Void, String> {
        //Explicit
        private Context context;
        private static final String urlJSON = "http://202.28.94.32/2559/563020232-9/getlatlong.php";


        public GetMap(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(Void... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlJSON).build();
                com.squareup.okhttp.Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("26novV1", "e doIn==>" + e.toString());
                return null;
            }
            //return null;
        }//doInBack

        @Override
        protected void onPostExecute(String s) {

            Log.d("26novV1", "Json ==>" + s);
            try {

                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i += 1) {
                    //Get Json from Database
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String strSignID = jsonObject.getString("SignID");
                    String strSignName = jsonObject.getString("SignName");
                    String strLat = jsonObject.getString("Latitude");
                    String strLng = jsonObject.getString("Longitude");
                    //String strIcon = jsonObject.getString("IConID");

                    // MapIcon mapIcon = new MapIcon(context, Integer.parseInt(strIcon));
                    //Create Marker Sign
                    if (strSignName.equals("Sign45") || strSignName.equals("sign45")) {
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(strLat), Double.parseDouble(strLng)))
                                .title(strSignName)
                                .snippet(strSignID))
                                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sign45_ss));

                    } else if (strSignName.equals("Sign60") || strSignName.equals("sign60")) {
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(strLat), Double.parseDouble(strLng)))
                                .title(strSignName)
                                .snippet(strSignID))
                                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sign60_ss));
                    } else {
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(strLat), Double.parseDouble(strLng)))
                                .title(strSignName)
                                .snippet(strSignID))
                                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sign80_ss));
                    }

                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    goToLocationZoom(Double.parseDouble(strLat), Double.parseDouble(strLng),15);

                    //remove marker
                    if (marker != null) {
                        marker.remove();
                    }
                    //set marker from gps device
                    MarkerOptions options = new MarkerOptions()
                            .position(new LatLng(gps.getLatitude(), gps.getLongitude()));
                    marker = mGoogleMap.addMarker(options);
                    marker.showInfoWindow();
                    LatLng coordinate = new LatLng (gps.getLatitude(),gps.getLongitude());
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));
                    goToLocationZoom(gps.getLatitude(),gps.getLongitude(),15);
                    Log.d("01FebV2", "Marker" + "Lat:" + gps.getLatitude() + "Lng:" + gps.getLongitude());

                }// for
            } catch (Exception e) {
                e.printStackTrace();
            }
        }//onPost
    }//SnyUser

    //ZoomMap
    private void goToLocationZoom(double v, double v1) {
        LatLng latlng = new LatLng(v, v1);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng,15);
        mGoogleMap.moveCamera(update);
    }//goToLocationZoom

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }//googleServicesAvailable

    //class zoom map
    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng latlng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, zoom);
        mGoogleMap.moveCamera(update);
    }//goToLocationZoom

    Marker marker;
    //search location
    public void geoLocate(View view) throws IOException {
        //BindWidget
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        String location = edtSearch.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location, 1);
        android.location.Address address = list.get(0);
        //String locality = address.getLocality();
        //ค้นหาตำแหน่ง
       String locality = String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ?
                address.getAddressLine(0) : "",
                address.getCountryName());

        double lnt = address.getLatitude();
        double lng = address.getLongitude();
        goToLocationZoom(lnt, lng, 15);
        //ปักหมุดสถานที่
       setMarker(locality, lnt, lng);
        edtSearch.setText("");
    }//geoLocate onclick

    private void setMarker(String locality, double lnt, double lng) {
        //int n = 99;
        //for (int i = 0;i<=n;i++) {
            if (marker != null){
                marker.remove();
            }
       // }
        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .position(new LatLng(lnt, lng));
        //can move
        // .draggable(true);
        //.snippet("I am here");
        marker = mGoogleMap.addMarker(options);
    }//setMarker

    List<Marker >Markers = new ArrayList<Marker>() ;
    private void removeMarkers() {
        for (Marker marker : Markers) {
            marker.remove();
        }
        Markers.clear();
    }//remove marker

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }
    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
    }//onRequestPermissionsResult

    //insert lat lng
    public void insert (View view){

        // select radio button sign name
        signnameRadioGroup = (RadioGroup) findViewById(R.id.radg_signname);
        int selected_id = signnameRadioGroup.getCheckedRadioButtonId();
        signnameRadioButton = (RadioButton) findViewById(selected_id);
        signnameRadioButton.getText().toString();

        String str_latitude = edt_lat.getText().toString();
        String str_longitude = edt_lng.getText().toString();
        String str_adId = edt_adId.getText().toString();
        String str_signname=signnameRadioButton.getText().toString();

        String type = "insert";
        InsertBackground insertBackground = new InsertBackground(this);
        insertBackground.execute(type,str_signname,str_latitude,str_longitude,str_adId);

        //alert msg
        MyAlert myAlert = new MyAlert(InsertActivity.this, R.drawable.iconapp,
                getResources().getString(R.string.title_insert),
                getResources().getString(R.string.message_insert));
        myAlert.myDialog();

        //get map after insert sign
         GetMap getMap = new GetMap(InsertActivity.this);
         getMap.execute();
         initMap();
       // Intent intent = new Intent(getApplicationContext(), AdminPage.class);
       // startActivity(intent);



    }//on click insert
}//Main Class
