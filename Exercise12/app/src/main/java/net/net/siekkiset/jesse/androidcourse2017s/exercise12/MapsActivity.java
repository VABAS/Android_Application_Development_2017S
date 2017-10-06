package net.net.siekkiset.jesse.androidcourse2017s.exercise12;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    ArrayList<String[]> stops = new ArrayList<>();
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataLoader task = new DataLoader();
        task.execute("http://student.labranet.jamk.fi/~K1661/android/stops.json");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (stops.size() > 0) {
            for (int i = 0; i < stops.size(); i++) {
                String id = stops.get(i)[0];
                String name = stops.get(i)[1];
                float lat = Float.parseFloat(stops.get(i)[2]);
                float lon = Float.parseFloat(stops.get(i)[3]);
                int zone = Integer.parseInt(stops.get(i)[4]);
                LatLng loc = new LatLng(lat, lon);
                float color;
                // Color variation based on zone (vyöhyke).
                switch (zone) {
                    case 1:
                        color = BitmapDescriptorFactory.HUE_RED;
                        break;
                    case 2:
                        color = BitmapDescriptorFactory.HUE_GREEN;
                        break;

                    case 3:
                        color = BitmapDescriptorFactory.HUE_BLUE;
                        break;

                    case 4:
                        color = BitmapDescriptorFactory.HUE_YELLOW;
                        break;

                    default:
                        color = BitmapDescriptorFactory.HUE_ORANGE;
                        break;
                }
                mMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .title(name + " (" + id + ")")
                        .icon(BitmapDescriptorFactory.defaultMarker(color))
                );
            }
            LatLng keskusta = new LatLng(62.2416223, 25.7597309);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(keskusta, 14));
        }
        else {
            Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_LONG);
        }
    }

    class DataLoader extends AsyncTask<String, Void, String> {

        protected  void onPreExecute() {
            Toast.makeText(getBaseContext(), "Loading data...", Toast.LENGTH_LONG);
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
            return null;
        }

        protected void onPostExecute(String response) {
            try {
                JSONArray jsonArray = new JSONObject(response).getJSONArray("stops");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject stop = jsonArray.getJSONObject(i);
                    String[] asStr = {
                            stop.getString("stop_id"),
                            stop.getString("stop_name"),
                            stop.getString("stop_lat"),
                            stop.getString("stop_lon"),
                            stop.getString("zone_id"),
                    };
                    stops.add(asStr);
                }
            }
            catch (JSONException e) {
                Toast.makeText(getBaseContext(), "JSON parsing failed: " + e.getMessage(), Toast.LENGTH_LONG);
                Log.e("JSON", e.getMessage());
            }
        }
    }
}
