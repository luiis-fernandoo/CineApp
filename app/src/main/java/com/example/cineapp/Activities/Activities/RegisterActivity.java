package com.example.cineapp.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private UserDao userDAO;
    private FirebaseAuth firebaseAuth;
    private EditText edit_email, edit_senha, edit_nome, edit_CPF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        edit_nome = findViewById(R.id.watchListName);
        edit_CPF = findViewById(R.id.edit_CPF);
        Button bt_cadastro = findViewById(R.id.bt_cadastro);

        bt_cadastro.setOnClickListener(v -> {
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();
            String nome = edit_nome.getText().toString();
            String cpf = edit_CPF.getText().toString();

            // Cria um novo usuário com os dados fornecidos
            User newUser = new User(email, nome, senha, cpf);

            // Tenta registrar o novo usuário no Firebase
            registerUserOnFirebase(email, senha, newUser);
        });
    }

    private void registerUserOnFirebase(String email, String senha, User newUser) {
        firebaseAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        // Se o registro no Firebase foi bem-sucedido, salva localmente e no banco
                        if (firebaseUser != null) {
                            // Salva o nome no SharedPreferences após o registro bem-sucedido
                            saveNameInSharedPreferences(email, senha, newUser.getNome(), newUser.getCpf());

                            // Inicia o UserDAO com o contexto e o novo usuário
                            userDAO = new UserDao(getApplicationContext(), newUser);
                            // Tenta inserir o novo usuário no banco de dados local
                            if (userDAO.insertNewUser()) {
                                Toast.makeText(RegisterActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(it);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Falha ao cadastrar o usuário localmente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        // Se o registro no Firebase falhou
                        Toast.makeText(RegisterActivity.this, "Falha ao cadastrar o usuário no Firebase", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para salvar o nome no SharedPreferences (fora do método onCreate)
    private void saveNameInSharedPreferences(String email, String senha, String nome, String cpf) {
        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", email);
        editor.putString("senha", senha);
        editor.putString("nome", nome);
        editor.putString("cpf", cpf);
        editor.apply();
    }
}
