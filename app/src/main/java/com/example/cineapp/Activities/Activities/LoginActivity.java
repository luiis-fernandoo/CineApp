package com.example.cineapp.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.R;

public class LoginActivity extends AppCompatActivity {
    UserDao uDao;
    private TextView text_cadastro;
    private TextView bt_entrar;
    private TextView edit_email;
    private TextView edit_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        IniciarComponentes();

        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        String savedEmail = sp.getString("email", "");

        if (!savedEmail.isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        text_cadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        bt_entrar.setOnClickListener(v -> {
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();

            uDao = new UserDao(getApplicationContext(), new User(email, senha));

            if (uDao.verifyEmailAndPassword()) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("email", email);

                //recupera o nome durante a verificação do login
                String nome = uDao.getUserName(email);
                editor.putString("nome", nome);
                editor.apply();

                Log.d("LoginActivity", "Email salvo: " + email);
                Log.d("LoginActivity", "Nome salvo: " + nome);

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();


            } else {
                Toast.makeText(LoginActivity.this, "Dados incorretos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void IniciarComponentes() {
        text_cadastro = findViewById(R.id.text_cadastro);
        bt_entrar = findViewById(R.id.bt_entrar);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
    }
}
