package com.example.cineapp.Activities.Broadcast;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.cineapp.Activities.Service.NotificationService;

public class BroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Intent serviceIntent = new Intent(context, NotificationService.class);
            ComponentName componentName = context.startService(serviceIntent);
            if (componentName != null) {
                // O serviço foi iniciado com sucesso
                Log.d("BroadCastActivity", "Service started successfully.");
            } else {
                // Não foi possível iniciar o serviço
                Log.d("MainActivity", "Failed to start service.");
            }
        }
    }
}


