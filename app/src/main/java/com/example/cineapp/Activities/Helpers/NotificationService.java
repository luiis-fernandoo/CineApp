//package com.example.cineapp.Activities.Helpers;
//
//import android.app.Notification;
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//
//import com.example.cineapp.R;
//
//import java.util.concurrent.TimeUnit;
//
//import javax.xml.transform.Result;
//
//import kotlinx.coroutines.scheduling.CoroutineScheduler;
//
//public class NotificationService extends NotificationCompat.Builder {
//
//    public static final String CHANNEL_ID = "notificacoes_app";
//    public NotificationService(@NonNull Context context, @NonNull Notification notification) {
//        super(context, notification);
//    }
//    public NotificationService(Context context) {
//        super(context, CHANNEL_ID);
//        setContentTitle("Data limite!");
//        setContentText("Sua tarefa vence hoje!");
//        setSmallIcon(R.drawable.cineapp);
//    }
//}
//
//public class MyWorker extends CoroutineScheduler.Worker {
//
//    public MyWorker(Context context, WorkerParameters workerParams) {
//        super(context, workerParams);
//    }
//
//    @Override
//    public Result doWork() {
//        // Obter data da tarefa
//        String data = ...;
//
//        // Converter para timestamp
//        long timestamp = ...;
//
//        // Criar solicitação de trabalho
//        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .setInitialDelay(timestamp - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
//                .build();
//
//        // Agendar trabalho
//        WorkManager.getInstance(getApplicationContext()).enqueue(request);
//
//        return Result.success();
//    }
//
//}
//
