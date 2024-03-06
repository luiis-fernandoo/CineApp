package com.example.cineapp.Activities.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.R;

public class EditActivity extends AppCompatActivity {

    private EditText edit_email, edit_senha, edit_nome, edit_CPF;
    private UserDao userDAO;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        edit_nome = findViewById(R.id.watchListName);
        edit_CPF = findViewById(R.id.edit_CPF);
        Button bt_salvar = findViewById(R.id.btnWatchList);
        sp = getSharedPreferences("app", MODE_PRIVATE);

        String savedNome = sp.getString("nome", "");
        String savedCPF = sp.getString("cpf", "");
        String savedEmail = sp.getString("email", "");
        String savedSenha = sp.getString("senha", "");

        edit_nome.setText(savedNome);
        edit_CPF.setText(savedCPF);
        edit_email.setText(savedEmail);
        edit_senha.setText(savedSenha);

        bt_salvar.setOnClickListener(v -> {
            String nome = edit_nome.getText().toString();
            String cpf = edit_CPF.getText().toString();
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();

            if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(EditActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else if (senha.length() < 6) {
                Toast.makeText(EditActivity.this, "A senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
            } else {
                if (!email.equals(savedEmail)) {
                    Toast.makeText(EditActivity.this, "Não é possível alterar o e-mail", Toast.LENGTH_SHORT).show();
                    return; // Impede a execução adicional do código
                }

                // Atualize o usuário com os novos dados
                User user = new User(email, nome, senha, cpf);
                userDAO = new UserDao(getApplicationContext(), user);
                if (userDAO.UserUpdate(user)) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("nome", nome);
                    editor.putString("cpf", cpf);
                    editor.putString("senha", senha);
                    editor.apply();

                    Toast.makeText(EditActivity.this, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditActivity.this, "Falha ao atualizar o usuário", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
