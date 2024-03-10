package com.example.cineapp.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cineapp.Activities.Adapter.FilmsAdapter;
import com.example.cineapp.Activities.DAO.FilmDao;
import com.example.cineapp.Activities.DAO.SaveListDAO;
import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.DAO.WatchlistDao;
import com.example.cineapp.Activities.Models.Film;
import com.example.cineapp.Activities.Models.SaveList;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.Activities.Models.WatchList;
import com.example.cineapp.R;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class DetailsWatchlistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout buttonOk;
    private FilmsAdapter adapter;
    private TextView watchlist_name, watchlist_title;
    private LinearLayout edit_watchlist, delete_watchlist;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_watchlist);

        watchlist_name = findViewById(R.id.reminder);
        edit_watchlist = findViewById(R.id.edit_watchlist);
        delete_watchlist = findViewById(R.id.delete_watchlist);

        Intent it = getIntent();
        String watchlist_id = it.getStringExtra("watchlist_id");
        Log.d("" ,"Watchlist_id"+watchlist_id);
        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        String savedEmail = sp.getString("email", "");

        User user = new User();
        UserDao userDao = new UserDao(getApplicationContext(), user);
        User userID = userDao.getUserNameID(savedEmail);

        WatchList watchList = new WatchList();
        WatchlistDao watchlistDao = new WatchlistDao(getApplicationContext(), watchList);
        watchList = watchlistDao.getWatchlistID(watchlist_id);
        SaveList saveList = new SaveList();
        SaveListDAO saveListDAO = new SaveListDAO(getApplicationContext(), saveList);
        List<SaveList> saveLists = saveListDAO.getSaveListByUserAndWatchlist(userID.getId(), watchList.getId());
        Film film = new Film();
        FilmDao filmDao = new FilmDao(getApplicationContext(), film);
        List<Film> films = filmDao.getFilmBySaveList(saveLists);
        Log.d("" ,"Watchlist "+watchList.getName());
        recyclerView = findViewById(R.id.recycleViewFilm);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new FilmsAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.setFilms(films, saveLists);

        WatchList finalWatchList = watchList;
        delete_watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(watchlistDao.deleteWatchList(finalWatchList.getId(), getApplicationContext())){
                    Toast.makeText(DetailsWatchlistActivity.this, "Deletado com sucesso", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(DetailsWatchlistActivity.this, "Falha para deletar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edit_watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_edit(finalWatchList);
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void popup_edit(WatchList watchlist){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View popupView = inflater.inflate(R.layout.popup_add_watchlist, null);
        EditText editText_input = popupView.findViewById(R.id.editText_input);
        editText_input.setText(watchlist.getName());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailsWatchlistActivity.this);
        alertDialogBuilder.setView(popupView);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        buttonOk = popupView.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextInput = popupView.findViewById(R.id.editText_input);
                String nameWatchList = editTextInput.getText().toString();

//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference tableWatchList = database.getReference("WatchList");
//                DatabaseReference newItem = tableWatchList.push();
//
//                newItem.child("name").setValue(nameWatchList);
//                newItem.child("user_id").setValue(user);
//                newItem.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        // Verifique se os dados foram salvos com sucesso
//                        Log.d("Firebase", "Dados salvos com sucesso!");

                        WatchList watchList = new WatchList(nameWatchList, watchlist.getId());
                        WatchlistDao watchlistDao = new WatchlistDao(getApplicationContext(), watchList);
                        if (watchlistDao.updateWatchlist(watchlist.getId(), nameWatchList)) {
                            // Sucesso ao salvar no Firebase e no banco de dados local
                            Toast.makeText(DetailsWatchlistActivity.this, "WatchList editada com sucesso!", Toast.LENGTH_SHORT).show();

                        } else {
                            // Erro ao salvar no banco de dados local
                            Toast.makeText(DetailsWatchlistActivity.this, "Erro ao alterar WatchList.", Toast.LENGTH_SHORT).show();
                        }
                        alertDialog.dismiss();
                    }
                    //@Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Firebase", "Erro ao salvar dados: " + error.getMessage());
                        Toast.makeText(DetailsWatchlistActivity.this, "Erro ao salvar dados no Firebase.", Toast.LENGTH_SHORT).show();
                    }
                //});
            });
        }
    //}
}