package com.example.cineapp.Activities.Broadcast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.cineapp.Activities.Activities.DetailsWatchlistActivity;
import com.example.cineapp.Activities.DAO.FilmDao;
import com.example.cineapp.Activities.DAO.LembreteDao;
import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.Models.Film;
import com.example.cineapp.Activities.Models.Reminder;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyWorker extends Worker {

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Lógica para verificar e enviar notificações
        verificarLembretes();

        return Result.success(); // Retorna Result.success() se o trabalho for concluído com sucesso
    }

    private void verificarLembretes() {
        while (true){
            Context context = getApplicationContext();

            SharedPreferences sp = context.getSharedPreferences("app", Context.MODE_PRIVATE);
            String savedEmail = sp.getString("email", "");

            User user = new User();
            UserDao userDao = new UserDao(context, user);
            User userID = userDao.getUserNameID(savedEmail);
            Reminder reminder = new Reminder();
            LembreteDao lembreteDao = new LembreteDao(context, reminder);
            List<Reminder> reminders = lembreteDao.getAllLembretes(userID);

            for (Reminder remind : reminders) {
                Date currentDate = new Date(); // Data atual
                Date reminderDate = convertStringToDate(remind.getDate());// Data do lembrete do banco de dados
                Log.d("", "Current " + currentDate);
                Log.d("", "reminder " + reminderDate);
                Film film = new Film();
                FilmDao filmDao = new FilmDao(context, film);
                Film filmNotification = filmDao.getFilmById(remind.getFilm_id());
                if (reminderDate != null && currentDate.compareTo(reminderDate) >= 0) {
                    createNotificationChannel(context);
                    sendNotification(context, userID, filmNotification);
                    lembreteDao.deleteReminders(remind.getId());
                    Log.d("", "Enviar notificação");
                } else {
                    Log.d("", "Não há lembretes nesse horario");
                }
            }
            try{
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createNotificationChannel(Context context) {
        // Verificar a versão do Android para criar o canal de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(Context context, User user, Film film) {
        // Crie uma notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
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
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        builder.setContentIntent(resultPendingIntent);
        // Emita a notificação
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
