package com.example.cineapp.Activities.Service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.cineapp.Activities.DAO.LembreteDao;
import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.Fragments.HomeFragment;
import com.example.cineapp.Activities.Models.Reminder;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    private static final long INTERVALO_VERIFICACAO = 30 * 1000; // Verificar a cada minuto
    private Timer timer;
    private Handler handler = new Handler();
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel_id";
    private static final String CHANNEL_NAME = "Channel Name";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        iniciarVerificacaoHora();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void iniciarVerificacaoHora() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
                String savedEmail = sp.getString("email", "");

                User user = new User();
                UserDao userDao = new UserDao(getApplicationContext(), user);
                User userID = userDao.getUserNameID(savedEmail);
                Reminder reminder = new Reminder();
                LembreteDao lembreteDao = new LembreteDao(getApplicationContext(), reminder);
                List<Reminder> reminders = lembreteDao.getAllLembretes(userID);

                @SuppressLint("SimpleDateFormat") SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Log.d("Hora", "Fora do for"+reminders.get(0));
                try {
                    for (int i=0;i<reminders.size();i++){
                        Date dataHora = formato.parse(String.valueOf(reminders.get(i)));
                        Calendar calendarString = Calendar.getInstance();
                        assert dataHora != null;
                        calendarString.setTime(dataHora);

                        // Obtém a hora atual do dispositivo
                        Calendar calendarAtual = Calendar.getInstance();
                        Log.d("Hora", "Calendario Da hora atual "+calendarAtual.get(Calendar.HOUR_OF_DAY));
                        Log.d("Hora", "Minha Hora "+calendarString.get(Calendar.HOUR_OF_DAY));
                        // Verifica se a hora, o minuto e o segundo do dispositivo são iguais aos da string de data
                        if(calendarAtual.get(Calendar.HOUR_OF_DAY) == calendarString.get(Calendar.HOUR_OF_DAY)
                                && calendarAtual.get(Calendar.MINUTE) == calendarString.get(Calendar.MINUTE)){
                            Log.d("Hora", "A hora do dispositivo é igual à hora da string de data."+ calendarString);
                            createNotificationChannel();
                            sendNotification(getApplicationContext());
                        }else {
                            Log.d("Hora", "A hora do dispositivo não é igual à hora da string de data."+ calendarString);
                        }
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NotificationService.this, "Verificando hora...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 0, INTERVALO_VERIFICACAO);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        Toast.makeText(this, "Serviço de notificação encerrado.", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        // Verificar a versão do Android para criar o canal de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(Context context) {
        // Crie uma notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.cineapp)
                .setContentTitle("CineApp")
                .setContentText("Hora de assistir seu filme")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Crie uma intenção para abrir quando a notificação for clicada
        Intent resultIntent = new Intent(context, HomeFragment.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeFragment.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(resultPendingIntent);

        // Emita a notificação
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
