package net.sylvek.itracing2.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import net.sylvek.itracing2.R;

/**
 * Created by sylvek on 12/06/2015.
 */
public class CapturePosition extends BroadcastReceiver {

    static final int NOTIFICATION_ID = 453436;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // because some customers don't like Google Play Services…
        Location bestLocation = null;
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        for (final String provider : locationManager.getAllProviders()) {
            final Location location = locationManager.getLastKnownLocation(provider);
            if (location != null && (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy())) {
                bestLocation = location;
            }
        }

        if (bestLocation != null) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            final String position = bestLocation.getLatitude() + "," + bestLocation.getLongitude();
            final Uri uri = Uri.parse("geo:" + position + "?z=17&q=" + position);
            final Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);

            final Notification notification = new Notification.Builder(context)
                    .setContentText(context.getString(R.string.display_last_position))
                    .setContentTitle(context.getString(R.string.app_name))
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(context, 0, mapIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .build();
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }
}
