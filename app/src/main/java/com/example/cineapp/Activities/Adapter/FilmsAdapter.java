package com.example.cineapp.Activities.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cineapp.Activities.Activities.DetailsWatchlistActivity;
import com.example.cineapp.Activities.Activities.detailsFilmActivity;
import com.example.cineapp.Activities.DAO.SaveListDAO;
import com.example.cineapp.Activities.Models.Film;
import com.example.cineapp.Activities.Models.SaveList;
import com.example.cineapp.R;

import org.w3c.dom.Text;

import java.util.List;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.FilmsViewHolder> {
    private List<Film> films;
    private Context context;
    private List<SaveList> saveLists;
    private TextView film_name;
    private Button remove_film;
    public void setFilms(List<Film> films, List<SaveList> saveLists) {
        this.films = films;
        this.saveLists = saveLists;
    }

    public FilmsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FilmsAdapter.FilmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.films_item, parent, false);
        return new FilmsAdapter.FilmsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmsAdapter.FilmsViewHolder holder, int position) {
        Film film = films.get(position);
        SaveList saveList = saveLists.get(position);
        holder.bind(film, saveList, this.context, position);
    }

    @Override
    public int getItemCount() {
        return films == null ? 0 : films.size();
    }

    public class FilmsViewHolder extends RecyclerView.ViewHolder {
        public Button remove_film;

        public FilmsViewHolder(@NonNull View itemView) {
            super(itemView);
            film_name = itemView.findViewById(R.id.film_name);
            remove_film = itemView.findViewById(R.id.remove_film);
        }

        public void bind(Film film, SaveList saveList, Context context, int position) {
            try {
                film_name.setText(film.getTitle());
                film_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent it = new Intent(context, detailsFilmActivity.class);
                        it.putExtra("tag", film.getId());
                        context.startActivity(it);
                    }
                });
                remove_film.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("", "Posição filme" + film.getTitle() + position);
                        Log.d("", "Posição saveList" + saveList.getId() + saveList.getFilm_id() + position);
                        SaveListDAO saveListDAO = new SaveListDAO(context, saveList);
                        if(saveListDAO.deleteSaveListByIdAndFilm(saveList.getId())){
                            Toast.makeText(context, "Filme removido da sua watchlist", Toast.LENGTH_SHORT).show();
                        };
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
