package com.example.cineapp.Activities.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cineapp.Activities.DAO.FilmDao;
import com.example.cineapp.Activities.DAO.SaveListDAO;
import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.DAO.WatchlistDao;
import com.example.cineapp.Activities.Helpers.MyAsyncTask;
import com.example.cineapp.Activities.Models.Film;
import com.example.cineapp.Activities.Models.SaveList;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.Activities.Models.WatchList;
import com.example.cineapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class detailsFilmActivity extends AppCompatActivity implements MyAsyncTask.AsyncTaskListener {
    ImageView imagePrincipal;
    TextView titleDetails, overview, genres, production, release_date, tagline;
    ConstraintLayout constraintLayout;
    Button addWatchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_film);

        imagePrincipal  = findViewById(R.id.imagePrincipal);
        constraintLayout = findViewById(R.id.constraintLayout);
        titleDetails = findViewById(R.id.titleDetails);
        overview = findViewById(R.id.overview);
        genres = findViewById(R.id.genres);
        production = findViewById(R.id.production);
        release_date = findViewById(R.id.release_date);
        tagline = findViewById(R.id.tagline);
        addWatchList = findViewById(R.id.addWatchList);

        Intent it = getIntent();
        String tag = it.getStringExtra("tag");

        MyAsyncTask myAsyncTask = new MyAsyncTask(this, tag);
        myAsyncTask.execute();

    }

    public void onTaskComplete(JSONObject result){
        try {
            if (result.length() > 0) {
                try {
                    String backdrop_path = "https://image.tmdb.org/t/p/original/" + result.getString("backdrop_path");
                    Glide.with(this)
                            .load(backdrop_path)
                            .into(imagePrincipal);
                    if (imagePrincipal.getParent() != null) {
                        ((ViewGroup) imagePrincipal.getParent()).removeView(imagePrincipal);
                    }
                    constraintLayout.addView(imagePrincipal);

                    titleDetails.setText(result.getString("title"));

                    JSONArray genresArray = result.getJSONArray("genres");
                    List<String> genreNames = new ArrayList<>();
                    for (int i = 0; i < genresArray.length(); i++) {
                        JSONObject genreObject = genresArray.getJSONObject(i);
                        String name = genreObject.getString("name");
                        genreNames.add(name);
                    }
                    StringBuilder genreBuilder = new StringBuilder();
                    for (String genreName : genreNames) {
                        genreBuilder.append(genreName).append(", ");
                    }
                    String allGenres = genreBuilder.toString();

                    // Remove a última vírgula e espaço em branco da string
                    if (allGenres.length() > 0) {
                        allGenres = allGenres.substring(0, allGenres.length() - 2);
                    }
                    genres.setText(allGenres);

                    overview.setText(result.getString("overview"));

                    JSONArray productionCompany = result.getJSONArray("production_companies");
                    List<String> productionNames = new ArrayList<>();
                    for (int i = 0; i < productionCompany.length(); i++) {
                        JSONObject productionObject = productionCompany.getJSONObject(i);
                        String name = productionObject.getString("name");
                        productionNames.add(name);
                    }
                    StringBuilder productionBuilder = new StringBuilder();
                    for (String productionName : productionNames) {
                        productionBuilder.append(productionName).append(", ");
                    }
                    String allProduction = productionBuilder.toString();
                    // Remove a última vírgula e espaço em branco da string
                    if (allProduction.length() > 0) {
                        allProduction = allProduction.substring(0, allProduction.length() - 2);
                    }
                    production.setText(allProduction);

                    release_date.setText(result.getString("release_date"));

                    tagline.setText(result.getString("tagline"));

                    addWatchList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            exibirPopup(view, result);
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else {
                Toast.makeText(this, "Filme não encontrado", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e){
            // Lidar com erros de análise JSON, se necessário
            e.printStackTrace();
        }
    }

    public void onTaskError(String error) {

    }

    private void exibirPopup(View view, JSONObject film) {
        view = LayoutInflater.from(this).inflate(R.layout.activity_popup_view, null);

        // Encontrar o Spinner
        Spinner spinner = view.findViewById(R.id.watchlist_spinner);

        // Carregar as watchlists do banco
        new LoadWatchlistsTask(getApplicationContext(), spinner).execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obter a watchlist selecionada
                String watchlistName = (String) parent.getItemAtPosition(position);
               //Log.d("Selected Watchlist", ""+watchlistName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nenhuma watchlist foi selecionada
            }
        });

        // Criar um AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Selecionar Watchlist")
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String watchlistName = (String) spinner.getSelectedItem();

                        WatchList watchList = new WatchList();
                        WatchlistDao watchlistDao = new WatchlistDao(getApplicationContext(), watchList);
                        WatchList ObjwatchList = watchlistDao.getWatchlist(watchlistName);
                        SaveList saveList = new SaveList();
                        SaveListDAO saveListDAO = new SaveListDAO(getApplicationContext(), saveList);
                        try {
                            if(saveListDAO.insertNewSaveList(film.getString("id"), ObjwatchList)){
                                Film filme = new Film();
                                FilmDao filmDao = new FilmDao(getApplicationContext(), filme);
                                if(filmDao.insertFilm(film)){
                                    Log.d("Filme", "Filme cadastrado");
                                }else{
                                    Log.d("Filme", "Erro");
                                }
                                Toast.makeText(detailsFilmActivity.this, "Filme salvo na watchlist " + ObjwatchList.getName(), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(detailsFilmActivity.this, "Não foi possível salvar o filme", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .create();

        dialog.show();
    }
    public class LoadWatchlistsTask extends AsyncTask<Void, Void, List<String>> {
        private Context context;
        private Spinner spinner;

        public LoadWatchlistsTask(Context context, Spinner spinner) {
            this.context = context;
            this.spinner = spinner;
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
            String savedEmail = sp.getString("email", "");

            User user = new User();
            UserDao userDao = new UserDao(getApplicationContext(), user);
            User userPass = userDao.getUserNameID(savedEmail);

            WatchList watchList = new WatchList();
            WatchlistDao watchlistDao = new WatchlistDao(getApplicationContext(), watchList);
            List<WatchList> watchlists = watchlistDao.getAllWatchList(userPass);

            List<String> watchlistNames = new ArrayList<>();
            for (WatchList watchlist : watchlists) {
                watchlistNames.add(watchlist.getName());
            }

            return watchlistNames;

        }

        protected void onPostExecute(List<String> watchlistNames) {
            // Atualizar o Spinner com a lista de watchlists
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_spinner_item,
                    watchlistNames
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

    }
}