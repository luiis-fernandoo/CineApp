package com.example.cineapp.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.AsyncTask;
import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button button_id;
    TextView hello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_id = findViewById(R.id.button_id);
        hello = findViewById(R.id.hello);

        button_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyAsyncTask().execute();
            }
        });

        //Conexão com o database realtime do firebase:
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("User");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verifique se o dataSnapshot não é nulo
                if (dataSnapshot.exists()) {
                    // Imprima os dados para depuração
                    Log.d("Firebase", "DataSnapshot: " + dataSnapshot.toString());

                    // Faça algo com os dados
                    String nome = dataSnapshot.child("Name").getValue(String.class);
                    String username = dataSnapshot.child("Username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    Log.d("Firebase", "Nome: " + nome);
                    Log.d("Firebase", "Username: " + username);
                    Log.d("Firebase", "Email: " + email);

                    hello.setText("Nome: " + nome + ", Username: " + username + ", Email: " + email);
                } else {
                    Log.d("Firebase", "DataSnapshot não existe ou está vazio.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Trate erros de leitura
                Log.e("Firebase", "Erro ao ler do banco de dados", databaseError.toException());
            }
        });
    }


    //conexão com a api.. Está tudo aqui para conectarmos tudo ao seus devidos lugares mais tarde
    public class MyAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/572802")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhNTc0YmRkOGM5ZTg4MTQxNGM0NzI2YmRlMTgxYTEyNyIsInN1YiI6IjY1OWZkZDhkOGRlMGFlMDEyNThjMmVkZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.VG61EHZUYapdl2lk_rnun2DQK7y25nz5C8Q1WqJFyhY")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();

            }catch (IOException e) {
            // Trate a exceção de maneira apropriada
            return "Erro de rede: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                // Acessar a chave "title"
                String title = json.getString("original_title");
                // Atualize a interface do usuário com o resultado
                hello.setText("original_title:" + title);
            }catch (JSONException e) {
                // Lidar com erros de análise JSON
                e.printStackTrace();
                hello.setText("Erro ao processar resposta JSON");
            }

        }
    }
}

