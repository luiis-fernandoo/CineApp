package com.example.cineapp.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    UserDao uDao;
    private TextView text_cadastro;
    private TextView bt_entrar;
    private TextView edit_email;
    private TextView edit_senha;
    private TextView textView;
    private GoogleSignInClient client;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        IniciarComponentes();

        sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        String savedEmail = sp.getString("email", "");

        if (!savedEmail.isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, MainActivityMenu.class);
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


            // Autenticar o usuário no Firebase
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Usuário autenticado com sucesso
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                // Salvando os dados do usuário no SharedPreferences
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("email", email);

                                //recupera o nome durante a verificação do login
                                String nome = uDao.getUserName(email);
                                editor.putString("nome", nome);
                                editor.apply();

                                Log.d("LoginActivity", "Email salvo: " + email);
                                Log.d("LoginActivity", "Nome salvo: " + nome);

                                Intent intent = new Intent(LoginActivity.this, MainActivityMenu.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Erro ao autenticar o usuário
                            Toast.makeText(LoginActivity.this, "Erro ao fazer login tente usar um email e senha valida! ", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("574191769330-do881iive497aemim01pgol0a5nko6qo.apps.googleusercontent.com")
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(this,options);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = client.getSignInIntent();
                startActivityForResult(i,1234);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = sp.edit();

                                    String email = account.getEmail(); // Obtém o email da conta do Google
                                    editor.putString("email", email);

                                    String nome = account.getDisplayName(); // Obtém o nome do usuário do Google
                                    editor.putString("nome", nome);

                                    String photoUrl = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "";

                                    // Armazena a URL da foto do perfil do usuário
                                    editor.putString("photoUrl", photoUrl);

                                    // Inicialize o uDao antes de usá-lo
                                    uDao = new UserDao(getApplicationContext(), new User(email, ""));

                                    // Verifique se uDao não é nulo antes de chamar métodos nele
                                    if (uDao != null) {
                                        // Remova esta linha para evitar a chamada duplicada do nome
                                        // String nome = uDao.getUserName(email);
                                        editor.putString("nome", nome); // Use o nome do Google em vez do nome do UserDao
                                        editor.putString("photoUrl", photoUrl);

                                        editor.apply();

                                        Log.d("LoginActivity", "Email salvo: " + email);
                                        Log.d("LoginActivity", "Nome salvo: " + nome);
                                        Log.d("LoginActivity", "URL da foto do perfil salva: " + photoUrl);

                                    } else {
                                        Log.e("LoginActivity", "UserDao não foi inicializado corretamente.");
                                    }

                                    Intent intent = new Intent(getApplicationContext(), MainActivityMenu.class);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            } catch (ApiException e) {
                e.printStackTrace();
            }

        }

    }



    private void IniciarComponentes() {
        text_cadastro = findViewById(R.id.text_cadastro);
        bt_entrar = findViewById(R.id.bt_entrar);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        textView = findViewById(R.id.signInWithGoogle);
    }
}
