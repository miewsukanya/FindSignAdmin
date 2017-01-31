package miewsukanya.com.findsignadmin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.List;

public class InsertActivity extends AppCompatActivity implements OnMapReadyCallback {
    //Explicit
    GoogleMap mGoogleMap;
    EditText edtSignName,edtSearch,edt_lat,edt_lng;
    String lngString, latString,signString;
    ImageView imgInsert;
    TextView edt_adId;
    //RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()) {
            // Toast.makeText(this, "Perfect!!", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_insert);

            GetMap getMap = new GetMap(InsertActivity.this);
            getMap.execute();
            initMap();

        } else {
            //No google map layout
        }
        //test intent data from mainActivity 29/01/17
        TextView textView = (TextView) findViewById(R.id.textView6);
        Intent intent = getIntent();
        String adminID = intent.getStringExtra("adminID");
        textView.setText(adminID);
        textView.setTextSize(20);
        Log.d("adminID", "ID :" + adminID);

        edtSignName = (EditText) findViewById(R.id.edtSignName);
        edt_lat = (EditText) findViewById(R.id.edt_lat);
        edt_lng = (EditText) findViewById(R.id.edt_lng);
        edt_adId = (TextView) findViewById(R.id.textView6);
        //requestQueue = Volley.newRequestQueue(getApplicationContext());

    }//Main Method

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (mGoogleMap != null){
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
                    Geocoder gc = new Geocoder(InsertActivity.this);
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
                    marker.showInfoWindow();
                }
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
        }
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
                    if (strSignName.equals("sign45")) {
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(strLat), Double.parseDouble(strLng)))
                                .title(strSignName)
                                .snippet(strSignID))
                                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sign45_ss));

                    } else if (strSignName.equals("sign60")) {
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
                    LatLng coordinate = new LatLng (Double.parseDouble(strLat), Double.parseDouble(strLng));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));
                    goToLocationZoom(Double.parseDouble(strLat), Double.parseDouble(strLng));
                    //  Log.d("Data", strIcon);
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

    //Show map
    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }//initMap

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

        //Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
        double lnt = address.getLatitude();
        double lng = address.getLongitude();
        goToLocationZoom(lnt, lng, 15);
        //ปักหมุดสถานที่
        setMarker(locality, lnt, lng);
    }//geoLocate onclick

    private void setMarker(String locality, double lnt, double lng) {
        if (marker != null){
            marker.remove();
        }
        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .position(new LatLng(lnt,lng))
                //can move
                .draggable(true);
                //.snippet("I am here");
        marker = mGoogleMap.addMarker(options);
    }//setMarker

    //insert lat lng
    public void insert (View view){
        String str_signname = edtSignName.getText().toString();
        String str_latitude = edt_lat.getText().toString();
        String str_longitude = edt_lng.getText().toString();
        String str_adId = edt_adId.getText().toString();

        String type = "insert";
        InsertBackground insertBackground = new InsertBackground(this);
        insertBackground.execute(type,str_signname,str_latitude,str_longitude,str_adId);

        //alert msg
        MyAlert myAlert = new MyAlert(InsertActivity.this, R.drawable.sign45_ss,
                getResources().getString(R.string.title_insert),
                getResources().getString(R.string.message_insert));
        myAlert.myDialog();
    }//on click insert
}//Main Class
