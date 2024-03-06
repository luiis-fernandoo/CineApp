package com.example.cineapp.Activities.Service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
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

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.cineapp.Activities.Activities.DetailsWatchlistActivity;
import com.example.cineapp.Activities.DAO.FilmDao;
import com.example.cineapp.Activities.DAO.LembreteDao;
import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.Fragments.HomeFragment;
import com.example.cineapp.Activities.Models.Film;
import com.example.cineapp.Activities.Models.Reminder;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    private static final long INTERVALO_VERIFICACAO = 60 * 1000; // Verificar a cada minuto
    private Timer timer;
    private Handler handler = new Handler();
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel_id";
    private static final String CHANNEL_NAME = "Channel Notification";

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

                for(Reminder remind : reminders){
                    Date currentDate = new Date(); // Data atual
                    Date reminderDate = convertStringToDate(remind.getDate());// Data do lembrete do banco de dados
                    Log.d("", "Current "+ currentDate);
                    Log.d("", "reminder "+ reminderDate);
                    Film film = new Film();
                    FilmDao filmDao = new FilmDao(getApplicationContext(), film);
                    Film filmNotification = filmDao.getFilmById(remind.getFilm_id());
                    if (reminderDate != null && currentDate.compareTo(reminderDate) >= 0) {
                        createNotificationChannel();
                        sendNotification(getApplicationContext(), userID, filmNotification);
                        lembreteDao.deleteReminders(remind.getId());
                        Log.d("", "Enviar notificação");
                    } else {
                        Log.d("", "Não há lembretes nesse horario");
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(NotificationService.this, "Verificando hora...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        },0, INTERVALO_VERIFICACAO);
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

    private void sendNotification(Context context, User user, Film film) {
        // Crie uma notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.cineapp)
                .setContentTitle("CineApp")
                .setContentText("Hora de assistir " + (film.getTitle() != null ? film.getTitle() : "O filme Agendado")  + " " + user.getNome())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Crie uma intenção para abrir quando a notificação for clicada
        Intent resultIntent = new Intent(context, DetailsWatchlistActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(DetailsWatchlistActivity.class);
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

    private Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Ou trate o erro de acordo com sua lógica de negócio
        }
    }
}
