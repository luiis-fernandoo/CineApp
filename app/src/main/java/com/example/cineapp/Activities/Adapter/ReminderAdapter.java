package com.example.cineapp.Activities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cineapp.Activities.DAO.FilmDao;
import com.example.cineapp.Activities.DAO.LembreteDao;
import com.example.cineapp.Activities.Models.Film;
import com.example.cineapp.Activities.Models.Reminder;
import com.example.cineapp.R;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    private List<Reminder> reminders;
    private Context context;
    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
        notifyDataSetChanged();
    }

    public ReminderAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ReminderAdapter.ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminders_item, parent, false);
        return new ReminderAdapter.ReminderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ReminderViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);
        holder.bind(reminder, this.context);
    }

    @Override
    public int getItemCount() {
        return reminders == null ? 0 : reminders.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        public TextView reminder;
        public ImageButton remove_reminder;

        public ReminderViewHolder(View view) {
            super(view);
            reminder = view.findViewById(R.id.reminder);
            remove_reminder = view.findViewById(R.id.remove_reminder);
        }
        public void bind(Reminder reminderModel, Context context) {
            try {
                Film film = new Film();
                FilmDao filmDao = new FilmDao(context, film);
                Film filmName = filmDao.getFilmById(reminderModel.getFilm_id());
                reminder.setText("Data: " + reminderModel.getDate() + " Filme: " + filmName.getTitle());
                remove_reminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LembreteDao lembreteDao = new LembreteDao(context, reminderModel);
                        if(lembreteDao.deleteReminders(reminderModel.getId())){
                            Toast.makeText(context, "Lembrete apagado com sucesso", Toast.LENGTH_SHORT).show();
                        };
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
