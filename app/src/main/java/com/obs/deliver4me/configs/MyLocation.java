package com.obs.deliver4me.configs;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class can be used to find out the status of the network connection and
 * the gps mode. The status can be checked every 5 seconds. If the GPS is on,
 * the location can be calculated from the GPS, otherwise the location can be
 * calculated from network location.
 */
public class MyLocation extends Service implements LocationListener {

    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    boolean gps_enabled = false;
    boolean network_enabled = false;
    Context context;
    boolean result = true;
    public static MyLocation myLocation = null;


    public static MyLocation defaultHandler() {
        if (myLocation == null) {
            myLocation = new MyLocation();
        }

        return myLocation;
    }

    public boolean getLocation(Context context, LocationResult result) {
        // I use LocationResult callback class to pass location value from
        // MyLocation to user code.
        locationResult = result;
        this.context = context;
        if (lm == null)
            lm = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);

        // exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        // don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled)
            return false;

        try {
            int hasLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission_group.LOCATION);
            if (hasLocationPermission == PackageManager.PERMISSION_GRANTED || hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                if (gps_enabled) {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                            locationListenerGps);
                }
                if (network_enabled) {
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                            locationListenerNetwork);
                }
                timer1 = new Timer();
                timer1.schedule(new GetLastLocation(), 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Checks the Gps status
     *
     * @return boolean value of location status
     */
    public boolean isLocationAvailable(Context context) {
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (gps_enabled || network_enabled) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    LocationListener locationListenerGps = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            try {
                int hasLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission_group.LOCATION);
                if (hasLocationPermission == PackageManager.PERMISSION_GRANTED || hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                    lm.removeUpdates(this);
                    lm.removeUpdates(locationListenerNetwork);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Called when the provider is disabled by the user. If
         * requestLocationUpdates is called on an already disabled provider,
         * this method is called immediately.
         *
         * @param provider
         *            the name of the location provider associated with this
         *            update.
         */
        @Override
        public void onProviderDisabled(String provider) {
        }

        /**
         * Called when the provider is enabled by the user.
         *
         * @param provider
         *            the name of the location provider associated with this
         *            update.
         */
        @Override
        public void onProviderEnabled(String provider) {
        }

        /**
         * Called when the provider status changes. This method is called when a
         * provider is unable to fetch a location or if the provider has
         * recently become available after a period of unavailability.
         *
         * @param provider
         *            the name of the location provider associated with this
         *            update.
         * @param status
         *            OUT_OF_SERVICE if the provider is out of service, and this
         *            is not expected to change in the near future;
         *            TEMPORARILY_UNAVAILABLE if the provider is temporarily
         *            unavailable but is expected to be available shortly; and
         *            AVAILABLE if the provider is currently available.
         * @param extras
         *            an optional Bundle which will contain provider specific
         *            status variables. A number of common key/value pairs for
         *            the extras Bundle are listed below. Providers that use any
         *            of the keys on this list must provide the corresponding
         *            value as described below. satellites - the number of
         *            satellites used to derive the fix
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;

    }

    LocationListener locationListenerNetwork = new LocationListener() {
        /**
         * Called when the location has changed. There are no restrictions on
         * the use of the supplied Location object.
         *
         * @param location
         *            The new location, as a Location object.
         */
        @Override
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            try {
                int hasLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission_group.LOCATION);
                if (hasLocationPermission == PackageManager.PERMISSION_GRANTED || hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                    lm.removeUpdates(this);
                    lm.removeUpdates(locationListenerGps);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Called when the provider is disabled by the user. If
         * requestLocationUpdates is called on an already disabled provider,
         * this method is called immediately.
         *
         * @param provider
         *            the name of the location provider associated with this
         *            update.
         */
        @Override
        public void onProviderDisabled(String provider) {
        }

        /**
         * Called when the provider is enabled by the user.
         *
         * @param provider
         *            the name of the location provider associated with this
         *            update.
         */
        @Override
        public void onProviderEnabled(String provider) {
        }

        /**
         * Called when the provider status changes. This method is called when a
         * provider is unable to fetch a location or if the provider has
         * recently become available after a period of unavailability.
         *
         * @param provider
         *            the name of the location provider associated with this
         *            update.
         * @param status
         *            OUT_OF_SERVICE if the provider is out of service, and this
         *            is not expected to change in the near future;
         *            TEMPORARILY_UNAVAILABLE if the provider is temporarily
         *            unavailable but is expected to be available shortly; and
         *            AVAILABLE if the provider is currently available.
         * @param extras
         *            an optional Bundle which will contain provider specific
         *            status variables. A number of common key/value pairs for
         *            the extras Bundle are listed below. Providers that use any
         *            of the keys on this list must provide the corresponding
         *            value as described below. satellites - the number of
         *            satellites used to derive the fix
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    /**
     * The GPS location and Network location to be calculated in every 5 seconds
     * with the help of this class
     */
    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            try {
                int hasLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission_group.LOCATION);
                if (hasLocationPermission == PackageManager.PERMISSION_GRANTED || hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                    lm.removeUpdates(locationListenerGps);
                    lm.removeUpdates(locationListenerNetwork);

                    Location net_loc = null, gps_loc = null;
                    if (gps_enabled)
                        gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (network_enabled)
                        net_loc = lm
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    // if there are both values use the latest one
                    if (gps_loc != null && net_loc != null) {
                        if (gps_loc.getTime() > net_loc.getTime())
                            locationResult.gotLocation(gps_loc);
                        else
                            locationResult.gotLocation(net_loc);
                        return;
                    }

                    if (gps_loc != null) {
                        locationResult.gotLocation(gps_loc);
                        return;
                    }
                    if (net_loc != null) {
                        locationResult.gotLocation(net_loc);
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            locationResult.gotLocation(null);
        }
    }

    /**
     * This abstract class is used to get the location from other class.
     */
    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }
}
