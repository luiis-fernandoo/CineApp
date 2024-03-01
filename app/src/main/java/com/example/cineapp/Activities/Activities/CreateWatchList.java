//package com.example.cineapp.Activities.Activities;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.cineapp.Activities.DAO.UserDao;
//import com.example.cineapp.Activities.DAO.WatchlistDao;
//import com.example.cineapp.Activities.Fragments.HomeFragment;
//import com.example.cineapp.Activities.Models.User;
//import com.example.cineapp.Activities.Models.WatchList;
//import com.example.cineapp.R;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class CreateWatchList extends AppCompatActivity {
//
//    Button btnWatchList;
//    TextView watchListName;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_watch_list);
//
//        btnWatchList = findViewById(R.id.btnWatchList);
//        watchListName = findViewById(R.id.watchListName);
//
//        btnWatchList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String nameWatchList = watchListName.getText().toString();
//                SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
//                String savedEmail = sp.getString("email", "");
//
//                User user = new User();
//                UserDao userDao = new UserDao(getApplicationContext(), user);
//                user = userDao.getUserNameID(savedEmail);
//                int userId = user.getId();
//
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference tableWatchList = database.getReference("WatchList");
//                DatabaseReference newItem = tableWatchList.push();
//
//                newItem.child("name").setValue(nameWatchList);
//                newItem.child("user_id").setValue(userId);
//                newItem.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        // Verifique se os dados foram salvos com sucesso
//                        Log.d("Firebase", "Dados salvos com sucesso!");
//
//                        WatchList watchList = new WatchList(nameWatchList, userId);
//                        WatchlistDao watchlistDao = new WatchlistDao(getApplicationContext(), watchList);
//
//                        if (watchlistDao.insertNewWatchList()) {
//                            // Sucesso ao salvar no Firebase e no banco de dados local
//                            Toast.makeText(CreateWatchList.this, "WatchList criada com sucesso!", Toast.LENGTH_SHORT).show();
//                            Intent it = new Intent(CreateWatchList.this, HomeFragment.class);
//                            startActivity(it);
//                        } else {
//                            // Erro ao salvar no banco de dados local
//                            Toast.makeText(CreateWatchList.this, "Erro ao criar WatchList.", Toast.LENGTH_SHORT).show();
//                        }
//                        Intent it = new Intent(CreateWatchList.this, HomeFragment.class);
//                        startActivity(it);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        // Ocorreu um erro ao salvar no Firebase
//                        Log.d("Firebase", "Erro ao salvar dados: " + error.getMessage());
//                        Toast.makeText(CreateWatchList.this, "Erro ao salvar dados no Firebase.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }
//}