package com.example.cineapp.Activities.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainUserActivity extends AppCompatActivity {

    private TextView textNomeUser, textNomeEmail;
    private Button bt_deslogar, bt_apagar, bt_Editar;
    private ImageView imgUser;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        textNomeUser = findViewById(R.id.textNomeUser);
        textNomeEmail = findViewById(R.id.textNomeEmail);
        bt_deslogar = findViewById(R.id.bt_deslogar);
        bt_apagar = findViewById(R.id.bt_apagar);
        bt_Editar = findViewById(R.id.bt_Editar);
        imgUser = findViewById(R.id.imgUser);

        // Inicialize o SharedPreferences
        sp = getSharedPreferences("app", MODE_PRIVATE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String nome = user.getDisplayName();
            String email = user.getEmail();

            // Verifica se o nome e o e-mail não estão vazios antes de exibi-los
            if (nome != null && !nome.isEmpty()) {
                textNomeUser.setText("Nome: " + nome);
            } else {
                textNomeUser.setText("Nome: N/A");
            }
            if (email != null && !email.isEmpty()) {
                textNomeEmail.setText("Email: " + email);
            } else {
                textNomeEmail.setText("Email: N/A");
            }
            // Recupere a URL da foto do perfil após inicializar o SharedPreferences
            String photoUrl = user.getPhotoUrl().toString();
            if (photoUrl != null && !photoUrl.isEmpty()) {
                // Use o Glide para carregar a imagem na View
                Glide.with(this)
                        .load(photoUrl)
                        .into(imgUser);
            }
        } else {
            Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
        }

        String savedEmail = sp.getString("email", "");
        String savedNome = sp.getString("nome", "");
        String savedPhotoUrl = sp.getString("photoUrl", "");


        Log.d("MainUserActivity", "Email recuperado: " + savedEmail);
        Log.d("MainUserActivity", "Nome recuperado: " + savedNome);
        Log.d("MainUserActivity", "URL da foto do perfil recuperada: " + savedPhotoUrl);


        textNomeUser.setText("Nome: " + savedNome);
        textNomeEmail.setText("Email: " + savedEmail);

        bt_deslogar.setOnClickListener(v -> {
            logoutUser();
        });

        bt_apagar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmação");
            builder.setMessage("Deseja realmente apagar o usuário?");

            builder.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Ação a ser realizada ao clicar em "Apagar"
                    String emailToBeDeleted = sp.getString("email", "");

                    UserDao userDAO = new UserDao(getApplicationContext(), null); // Passe o objeto User, se necessário

                    if (userDAO.DeleteUser(emailToBeDeleted)) {
                        logoutUser();
                    } else {
                        Toast.makeText(MainUserActivity.this, "Falha ao apagar o usuário", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Ação a ser realizada ao clicar em "Cancelar"
                    dialog.dismiss(); // Fecha o diálogo
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        bt_Editar.setOnClickListener(v -> {
            Intent intent = new Intent(MainUserActivity.this, EditActivity.class);
            startActivity(intent);
        });
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sp.edit();
        FirebaseAuth.getInstance().signOut();

        // Remove os dados do SharedPreferences
        editor.remove("email");
        editor.remove("nome");
        editor.remove("photoUrl"); // Remova a URL da foto do perfil ao deslogar

        editor.apply();

        // Após deslogar, redireciona para a tela de login
        Intent intent = new Intent(MainUserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String savedNome = sp.getString("nome", "");
        String savedEmail = sp.getString("email", "");
        String savedPhotoUrl = sp.getString("photoUrl", "");



        textNomeUser.setText("Nome: " + savedNome);
        textNomeEmail.setText("Email: " + savedEmail);
        Log.d("MainUserActivity", "URL da foto do perfil recuperada onResume: " + savedPhotoUrl);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Atualiza os dados na tela principal antes de destruir a atividade
        String savedNome = sp.getString("nome", "");
        String savedEmail = sp.getString("email", "");

        textNomeUser.setText("Nome: " + savedNome);
        textNomeEmail.setText("Email: " + savedEmail);
    }
}
