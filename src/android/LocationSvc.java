package com.location.yhck;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
/**
 * 获取定位服务
 * Created by YH_CK on 2017/05/19.
 */

public class LocationSvc extends Service implements LocationListener {

    private static final String TAG = "LocationSvc";
    private LocationManager locationManager;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if(isMarshmallow()){
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
              && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               return;
            }else{
              if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
              }else if(locationManager.getProvider(LocationManager.GPS_PROVIDER) != null){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
              }
            }

        }else{
          if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
          }else if(locationManager.getProvider(LocationManager.GPS_PROVIDER) != null){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
          }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Get the current position \n" + location);
        //通知Activity
        Intent intent = new Intent();
        intent.setAction(LocationPlugin.LOCATION_ACTION);
        intent.putExtra(LocationPlugin.LOCATION_TYPE, location.getProvider());
        intent.putExtra(LocationPlugin.LONFITUDE, String.valueOf(location.getLongitude()));
        intent.putExtra(LocationPlugin.LATITUDE, String.valueOf(location.getLatitude()));
        sendBroadcast(intent);
        // 如果只是需要定位一次，这里就移除监听，停掉服务。如果要进行实时定位，可以在退出应用或者其他时刻停掉定位服务。
        locationManager.removeUpdates(this);
        stopSelf();
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

  private static boolean isMarshmallow() {
    return Build.VERSION.SDK_INT >= 23;
  }
}
