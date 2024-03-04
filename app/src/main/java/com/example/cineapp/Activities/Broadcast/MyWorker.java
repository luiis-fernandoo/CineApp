package com.example.cineapp.Activities.Broadcast;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, BroadCast.class);
        context.sendBroadcast(intent);

        return Result.success(); // Retorna Result.success() se o trabalho for concluído com sucesso
    }
}