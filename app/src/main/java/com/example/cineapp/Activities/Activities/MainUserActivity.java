package com.example.cineapp.Activities.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.R;

public class MainUserActivity extends AppCompatActivity {


    private TextView textNomeUser, textNomeEmail;
    private Button bt_deslogar, bt_apagar, bt_Editar;

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

        sp = getSharedPreferences("app", MODE_PRIVATE);
        String savedEmail = sp.getString("email", "");
        String savedNome = sp.getString("nome", "");

        Log.d("MainUserActivity", "Email recuperado: " + savedEmail);
        Log.d("MainUserActivity", "Nome recuperado: " + savedNome);

        textNomeUser.setText("Nome: "+savedNome);
        textNomeEmail.setText("Email: "+savedEmail);

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


        bt_deslogar.setOnClickListener(v -> {
            logoutUser();
        });

        bt_Editar.setOnClickListener(v -> {
            Intent intent = new Intent(MainUserActivity.this, EditActivity.class);
            startActivity(intent);
        });

    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sp.edit();

        // Remove os dados do SharedPreferences
        editor.remove("email");
        editor.remove("nome");
        // Adicione outras chaves para remover se necessário

        editor.apply();

        // Após deslogar, redireciona para a tela de login
        Intent intent = new Intent(MainUserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        String savedNome = sp.getString("nome", "");
        String savedEmail = sp.getString("email", "");

        textNomeUser.setText("Nome: " + savedNome);
        textNomeEmail.setText("Email: " + savedEmail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Atualiza os dados na tela principal antes de destruir a atividade
        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        String savedNome = sp.getString("nome", "");
        String savedEmail = sp.getString("email", "");

        textNomeUser.setText("Nome: " + savedNome);
        textNomeEmail.setText("Email: " + savedEmail);
    }
    // Menu metodo



}
