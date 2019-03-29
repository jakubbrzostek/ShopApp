package com.example.brzostek.project1;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceTransitionsIntentService extends IntentService {

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e("TEST", "geofencingEvent with error");
            return;
        }

        Log.d("TEST", "geofencingEvent success");

        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        Geofence triggeringGeofence = geofencingEvent.getTriggeringGeofences().get(0);
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            showNotification(triggeringGeofence.getRequestId(), "Hello, you entered shop. No special offers.");
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            showNotification(triggeringGeofence.getRequestId(), "You left shop, bye.");
        }
    }

    private void showNotification(String title, String description) {
        NotificationCompat.Builder notif = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(title.hashCode(), notif.build());
    }
}
