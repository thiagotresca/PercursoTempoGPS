package br.usjt.percursoetempogps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    public boolean gpsStatus ;
    private Chronometer chronometer;
    public boolean running;
    public Location startLocation;
    public Location stopLocation;
    private TextView distanceTraveled;
    private double latitudeAtual;
    private double longitudeAtual;
    private EditText searchItem;



   public void askForGpsPermission (View view) {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        }
   }

    public void startGps (View view) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, locationListener);
            Toast.makeText(this, getString(R.string.gps_activated), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, getString(R.string.no_gps_no_app), Toast.LENGTH_SHORT).show();
        }

    }

   public void stopGps (View view){
       locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
       assert locationManager != null;
       gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
       if(gpsStatus) {
           locationManager.removeUpdates(locationListener);
           Toast.makeText(this, getString(R.string.gps_deactivated), Toast.LENGTH_SHORT).show();
       }else{
           Toast.makeText(this, getString(R.string.gps_already_deactivated), Toast.LENGTH_SHORT).show();
       }
   }

   public void startRoute (View view){
       if(!running){
           chronometer.setBase(SystemClock.elapsedRealtime());
           chronometer.start();
           running = true;
           if (ActivityCompat.checkSelfPermission(this,
                   Manifest.permission.ACCESS_FINE_LOCATION) ==
                   PackageManager.PERMISSION_GRANTED){
               //startLocation.setLatitude(latitudeAtual);
               //startLocation.setLongitude(longitudeAtual);
           }
       }
   }

   public void endRoute (View view){
       if(running){
           chronometer.stop();
           running = false;
           if (ActivityCompat.checkSelfPermission(this,
                   Manifest.permission.ACCESS_FINE_LOCATION) ==
                   PackageManager.PERMISSION_GRANTED){
               //stopLocation.setLatitude(latitudeAtual);
               //stopLocation.setLongitude(longitudeAtual);
           }
           //float distance = startLocation.distanceTo(stopLocation);
           //distanceTraveled.setText(String.format("%f",distance));
       }
   }

    public void search (View view) {
        Uri gmmIntentUri =
                Uri.parse(String.format("geo:%f,%f?q=%s",
                        latitudeAtual, longitudeAtual, searchItem.getText()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void limpaEditText (View view){
       searchItem.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chronometer = findViewById(R.id.chronometer);

        distanceTraveled = findViewById(R.id.DistanceTraveledTextView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        searchItem = findViewById(R.id.searchEditText);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                latitudeAtual = lat;
                longitudeAtual = lon;
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
        };
    }
}