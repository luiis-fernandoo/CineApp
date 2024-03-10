package com.example.cineapp.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cineapp.Activities.DAO.FilmDao;
import com.example.cineapp.Activities.DAO.LembreteDao;
import com.example.cineapp.Activities.DAO.SaveListDAO;
import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.DAO.WatchlistDao;
import com.example.cineapp.Activities.Models.Film;
import com.example.cineapp.Activities.Models.Reminder;
import com.example.cineapp.Activities.Models.SaveList;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.Activities.Models.WatchList;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    UserDao uDao;
    private TextView text_cadastro;
    private TextView bt_entrar;
    private TextView edit_email;
    private TextView edit_senha;
    private TextView textView;
    private GoogleSignInClient client;
    private SharedPreferences sp;
    private String dateString;
    private Date release_date_convert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        IniciarComponentes();

        sp = getSharedPreferences("app", Context.MODE_PRIVATE);

        text_cadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        bt_entrar.setOnClickListener(v -> {
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();

            uDao = new UserDao(getApplicationContext(), new User(email, senha));

            loginFirebase(email, senha, uDao);
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

                                    uDao = new UserDao(getApplicationContext(), new User(email, ""));

                                    if (uDao != null) {

                                        editor.putString("nome", nome);
                                        editor.putString("photoUrl", photoUrl);

                                        editor.apply();

                                        String senha = "default";
                                        String cpf = "default";
                                        User userLocal = new User(email, nome, senha, cpf);
                                        User userVerify = uDao.getUserNameID(email);
                                        if (userVerify != null && userVerify.getEmail() != null) {
                                            Log.d("LoginActivity", "Usuário já existe");
                                        } else {
                                            if (uDao.insertNewUser(userLocal)) {
                                                Log.d("LoginActivity", "user salvo do google no bd local: ");
                                            } else {
                                                Log.d("LoginActivity", "Erro google: " + email + nome);
                                            }
                                        }
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

    private void loginFirebase(String email, String senha, UserDao uDao){
        if (!isValidEmail(email)) {
            Toast.makeText(LoginActivity.this, "O endereço de e-mail é inválido.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (senha.length() < 6) {
            Toast.makeText(LoginActivity.this, "A senha deve ter pelo menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
            Toast.makeText(LoginActivity.this, "O endereço de e-mail e a senha não podem estar vazios.", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Usuário autenticado com sucesso
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            // Salvando os dados do usuário no SharedPreferences
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("email", email);
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("nome");
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String nome = snapshot.getValue(String.class);
                                        editor.putString("nome", nome);
                                        editor.apply();
                                        String cpf = "Default";
                                        User userLocal = new User(email, nome, senha, cpf);
                                        if(uDao.verifyEmailAndPassword()){
                                            Intent intent = new Intent(LoginActivity.this, MainActivityMenu.class);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            if(inserUserByFirebase(userLocal)){
                                                insertWatchlistByFirebase(user);
                                                insertSaveListByFirebase(user);
                                                insertReminderByFirebase(user);
                                                insertFilmByFirebase(user);
                                                Intent intent = new Intent(LoginActivity.this, MainActivityMenu.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    } else {
                                        Log.d("NOME", "O campo 'nome' não existe no nó do usuário.");
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("NOME", "Erro ao recuperar o nome do usuário: " + error.getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Erro ao autenticar o usuário
                        Toast.makeText(LoginActivity.this, "Erro ao fazer login tente usar um email e senha valida!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean inserUserByFirebase(User user){
        if(uDao.insertNewUser(user)){
            Log.d("LoginActivity", "user salvo no bd local" );
            return true;
        }else{
            Log.d("LoginActivity", "erro ao salvar no banco Local ");
            return false;
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void insertWatchlistByFirebase(FirebaseUser user){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("Watchlists");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot watchlistSnapshot : snapshot.getChildren()) {
                    Long userIdLong = (Long) watchlistSnapshot.child("user_id").getValue();
                    Integer user_id = userIdLong != null ? userIdLong.intValue() : null;
                    String name = (String) watchlistSnapshot.child("name").getValue();

                    if (user_id != null && name != null) {
                        WatchlistDao watchlistDao = new WatchlistDao(getApplicationContext(), new WatchList(name, user_id));
                        watchlistDao.insertNewWatchList();
                        Log.d("FirebaseData", "UserID: " + user_id + ", Nome: " + name);
                    } else {
                        Log.e("FirebaseData", "user_id ou name é nulo");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void insertSaveListByFirebase(FirebaseUser user){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("Savelists");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot savelistSnapshot : snapshot.getChildren()) {
                    Long userIdLong = (Long) savelistSnapshot.child("user_id").getValue();
                    Integer user_id = userIdLong != null ? userIdLong.intValue() : null;
                    String film_id = (String) savelistSnapshot.child("film_id").getValue();
                    Long watchlistId = (Long) savelistSnapshot.child("watchlist_id").getValue();
                    String watchlist_id = watchlistId !=null ? watchlistId.toString() : null;

                    if (user_id != null && film_id != null) {
                        WatchlistDao watchlistDao = new WatchlistDao(getApplicationContext(), new WatchList());
                        WatchList watchList = watchlistDao.getWatchlistID(watchlist_id);
                        SaveListDAO saveListDAO = new SaveListDAO(getApplicationContext(), new SaveList());
                        saveListDAO.insertNewSaveList(film_id, watchList);
                        Log.d("", "UserID: " + user_id + ", Watchlist_id: " + watchlist_id + film_id);
                    } else {
                        Log.e("FirebaseData", "user_id ou film_id é nulo");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void insertReminderByFirebase(FirebaseUser user){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("Reminders");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot reminderSnapshot : snapshot.getChildren()) {
                    Long userIdLong = (Long) reminderSnapshot.child("user_id").getValue();
                    Integer user_id = userIdLong != null ? userIdLong.intValue() : null;
                    String film_id = (String) reminderSnapshot.child("film_id").getValue();
                    Object dateObject = reminderSnapshot.child("date").getValue();
                    if (dateObject instanceof Map) {
                        Map<String, Object> dateMap = (Map<String, Object>) dateObject;
                        String dateString = dateMap.toString();
                    } else {
                        if (dateObject instanceof String) {
                            String dateString = (String) dateObject;
                        } else {}
                    }

                    if (user_id != null && film_id != null) {
                        LembreteDao lembreteDao = new LembreteDao(getApplicationContext(), new Reminder());

                        lembreteDao.insertLembrete(dateString, Integer.parseInt(film_id), user_id);
                        Log.d("", "date" + dateString);
                    } else {
                        Log.e("FirebaseData", "user_id ou film_id é nulo");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void insertFilmByFirebase(FirebaseUser user){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("Films");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot filmSnapshot : snapshot.getChildren()) {
                    String film_id = (String) filmSnapshot.child("film_id").getValue();
                    String title = (String) filmSnapshot.child("title").getValue();
                    String backdrop_path = (String) filmSnapshot.child("backdrop_path").getValue();
                    boolean falser = false;
                    String original_language = (String) filmSnapshot.child("original_language").getValue();
                    String original_title = (String) filmSnapshot.child("original_title").getValue();
                    String overview = (String) filmSnapshot.child("overview").getValue();
                    String poster_path = (String) filmSnapshot.child("poster_path").getValue();
                    String release_date = (String) filmSnapshot.child("release_date").getValue();
                    String vote_average = (String) filmSnapshot.child("vote_average").getValue();
                    if (release_date != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            release_date_convert = dateFormat.parse(release_date);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    } else {}
                    if (film_id != null) {
                        FilmDao filmDao = new FilmDao(getApplicationContext(), new Film(falser, backdrop_path, original_language, original_title, poster_path, release_date_convert, title, vote_average, overview, Integer.parseInt(film_id)));
                        if (filmDao.insertFilmByFirebase()) {
                            Log.d("", "Filme cadastrado no local");
                        }
                    } else {
                        Log.e("FirebaseData", "user_id ou film_id é nulo");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

}
