package com.example.brzostek.broadcastreceiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.brzostek.common.model.Product;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private final String channelID = "channel1";
    private int id = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Sprawdzam", "Działa");

        createNotificationChannel(context);
        Product product = (Product) intent.getSerializableExtra(Product.KEY);

        Intent intentEdit = new Intent();
        intentEdit.putExtra(Product.KEY, product);
        intentEdit.setComponent(new ComponentName("com.example.brzostek.project1", "com.example.brzostek.project1.EditActivity"));

        PendingIntent pendint = PendingIntent.getActivity(context, 0, intentEdit, PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notif = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(product.getName())
                .setContentText(product.getPrice() + "zł\n" + product.getQuantity() + "szt.")
                .setContentIntent(pendint)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id++, notif.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Default";
            String description = "Default";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, name, importance);
            channel.setDescription(description);
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
    }
}


