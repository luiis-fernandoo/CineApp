package com.example.cineapp.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cineapp.Activities.DAO.FilmDao;
import com.example.cineapp.Activities.DAO.LembreteDao;
import com.example.cineapp.Activities.DAO.SaveListDAO;
import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.DAO.WatchlistDao;
import com.example.cineapp.Activities.Helpers.MyAsyncTask;
import com.example.cineapp.Activities.Models.Film;
import com.example.cineapp.Activities.Models.Reminder;
import com.example.cineapp.Activities.Models.SaveList;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.Activities.Models.WatchList;
import com.example.cineapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class detailsFilmActivity extends AppCompatActivity implements MyAsyncTask.AsyncTaskListener {
    ImageView imagePrincipal;
    TextView titleDetails, overview, genres, production, release_date, tagline;
    ConstraintLayout constraintLayout;
    ImageButton addWatchList, addLembrete;
    private FirebaseAuth firebaseAuth;

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
        addLembrete = findViewById(R.id.addLembrete);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent it = getIntent();
        int tag = Integer.parseInt(it.getStringExtra("tag"));

        Log.d("", "Tag " + tag);

        MyAsyncTask myAsyncTask = new MyAsyncTask(this, tag);
        myAsyncTask.execute();

    }

    public void onTaskComplete(JSONObject result, String reference){
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

                    SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
                    String savedEmail = sp.getString("email", "");

                    User user = new User();
                    UserDao userDao = new UserDao(getApplicationContext(), user);
                    User userPass = userDao.getUserNameID(savedEmail);

                    addWatchList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            exibirPopup(view, result);
                        }
                    });

                    addLembrete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            exibirAddLembrete(view,  result, userPass);
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

    @Override

    public void onTaskError(String error) {

    }

    private void exibirPopup(View view, JSONObject film) {
        view = LayoutInflater.from(this).inflate(R.layout.activity_popup_view, null);
        Spinner spinner = view.findViewById(R.id.watchlist_spinner);

        new LoadWatchlistsTask(getApplicationContext(), spinner).execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(detailsFilmActivity.this, "Nenhuma watchlist selecionada", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setView(view)
                .setTitle("Selecionar Watchlist")
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String watchlistName = (String) spinner.getSelectedItem();
                        WatchlistDao watchlistDao = new WatchlistDao(getApplicationContext(), new WatchList());
                        WatchList ObjwatchList = watchlistDao.getWatchlist(watchlistName);
                        try {
                            createSavelistFirebase(film, ObjwatchList);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .create();

        dialog.show();
    }

    public void exibirAddLembrete(View view,  JSONObject film, User userPass) {
        view = LayoutInflater.from(this).inflate(R.layout.popup_date_pick, null);

        DatePicker datePicker = view.findViewById(R.id.datePicker);

        Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Selecionar Data e Hora")
                .setPositiveButton("Próximo", null);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button nextButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();

                        dialog.dismiss();

                        exibirTimePicker(day, month, year,  film, userPass);
                    }
                });
            }
        });

        dialog.show();
    }

    private void exibirTimePicker(int day, int month, int year,  JSONObject film, User userPass) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                String date  = day + "/" + (month + 1) + "/" + year + " " + hourOfDay + ":" + minute;
                String format = "dd/MM/yyyy HH:mm";
                try {
                    @SuppressLint("SimpleDateFormat") Date dateObject = new SimpleDateFormat(format).parse(date);
                    createReminderFirebase(dateObject, date, film, userPass);
                }catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);

        timePickerDialog.show();
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

    private void createSavelistFirebase(JSONObject film, WatchList watchList) throws JSONException {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userID = currentUser.getUid();
            DatabaseReference savelistRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("Savelists");
            savelistRef.orderByChild("film_id").equalTo(film.getString("id")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        savelistRef.orderByChild("watchlist_id").equalTo(watchList.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Toast.makeText(detailsFilmActivity.this, "Esse filme já está em essa watchlist", Toast.LENGTH_SHORT).show();
                                }else{
                                    try {
                                        insertFirebaseSaveList(savelistRef, film, watchList);
                                        DatabaseReference filmRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("Films");
                                        filmRef.orderByChild("film_id").equalTo(film.getString("id")).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    Log.d("", "Filme já cadastrado no firebase");
                                                }else{
                                                    try {
                                                        insertFilmFirebase(filmRef, film);
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {}
                                        });
                                        Toast.makeText(detailsFilmActivity.this, "Filme adicionado à " + watchList.getName(), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                    }else{
                        try {
                            insertFirebaseSaveList(savelistRef, film, watchList);
                            DatabaseReference filmRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("Films");
                            filmRef.orderByChild("film_id").equalTo(film.getString("id")).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        Log.d("", "Filme já cadastrado no firebase");
                                    }else{
                                        try {
                                            insertFilmFirebase(filmRef, film);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                            });
                            Toast.makeText(detailsFilmActivity.this, "Filme adicionado à " + watchList.getName(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }

    private void insertFirebaseSaveList(DatabaseReference savelistRef, JSONObject film, WatchList watchlist) throws JSONException {

        SaveListDAO saveListDAO = new SaveListDAO(getApplicationContext(), new SaveList());
        DatabaseReference newItem = savelistRef.push();
        newItem.child("film_id").setValue(film.getString("id"));
        newItem.child("user_id").setValue(watchlist.getUser_id());
        newItem.child("watchlist_id").setValue(watchlist.getId());
        newItem.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    saveListDAO.insertNewSaveList(film.getString("id"), watchlist);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void insertFilmFirebase(DatabaseReference filmRef, JSONObject film) throws JSONException {
        FilmDao filmDao = new FilmDao(getApplicationContext(), new Film());
        filmDao.insertFilm(film);
        DatabaseReference newItem = filmRef.push();
        newItem.child("film_id").setValue(film.getString("id"));
        newItem.child("title").setValue(film.getString("title"));
        newItem.child("backdrop_path").setValue(film.getString("backdrop_path"));
        newItem.child("original_language").setValue(film.getString("original_language"));
        newItem.child("original_title").setValue(film.getString("original_title"));
        newItem.child("overview").setValue(film.getString("overview"));
        newItem.child("poster_path").setValue(film.getString("poster_path"));
        newItem.child("release_date").setValue(film.getString("release_date"));
        newItem.child("vote_average").setValue(film.getString("vote_average"));
        newItem.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(detailsFilmActivity.this, "Filme adicionado com sucesso", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void createReminderFirebase(Date date, String dateString, JSONObject film, User user) throws JSONException {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userID = currentUser.getUid();
            DatabaseReference reminderRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("Reminders");

            LembreteDao lembreteDao = new LembreteDao(getApplicationContext(), new Reminder());
            lembreteDao.insertLembrete(dateString, Integer.parseInt(film.getString("id")), user.getId());
            DatabaseReference newItem = reminderRef.push();
            newItem.child("date").setValue(date);
            newItem.child("film_id").setValue(film.getString("id"));
            newItem.child("user_id").setValue(user.getId());
            newItem.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                            DatabaseReference filmRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("Films");
                            filmRef.orderByChild("film_id").equalTo(film.getString("id")).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        Log.d("", "Filme já cadastrado no firebase");
                                    }else{
                                        try {
                                            insertFilmFirebase(filmRef, film);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                            });
                            Toast.makeText(detailsFilmActivity.this, "Lembrete Salvo com Sucesso", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }
}