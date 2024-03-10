package com.example.cineapp.Activities.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private UserDao userDAO;
    private FirebaseAuth firebaseAuth;
    private EditText edit_email, edit_senha, edit_nome, edit_CPF;
    private ImageView imgUser;
    private Uri mSelectedUri;


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
        edit_CPF.addTextChangedListener(CPFMaskUtil.insert(edit_CPF));
        imgUser = findViewById(R.id.imgUser);



        bt_cadastro.setOnClickListener(v -> {
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();
            String nome = edit_nome.getText().toString();
            String cpf = CPFMaskUtil.unmask(edit_CPF.getText().toString());

            User newUser = new User(email, nome, senha, cpf);

            registerUserOnFirebase(email, senha, newUser);
        });

        imgUser.setOnClickListener(v -> {
            selectPhoto();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            mSelectedUri = data.getData();
            Picasso.get().load(mSelectedUri).transform(new CircleTransform()).into(imgUser);

        }
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    private void registerUserOnFirebase(String email, String senha, User newUser) {
        if (mSelectedUri == null) {
            Toast.makeText(RegisterActivity.this, "Selecione uma imagem de perfil.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(RegisterActivity.this, "O endereço de e-mail é inválido.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (senha.length() < 6) {
            Toast.makeText(RegisterActivity.this, "A senha deve ter pelo menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String userID = firebaseUser.getUid();
                        Log.d("", "ID " + userID);
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                        DatabaseReference userRef = usersRef.child(userID);

                        userRef.child("email").setValue(email);
                        userRef.child("nome").setValue(newUser.getNome());
                        userRef.child("cpf").setValue(newUser.getCpf());

                        DatabaseReference tableWatchlistRef = userRef.child("Watchlists");
                        DatabaseReference tableSavelistRef = userRef.child("Savelists");
                        DatabaseReference tableRemindersRef = userRef.child("Reminders");
                        DatabaseReference tableFilmsRef = userRef.child("Films");

                        tableWatchlistRef.child("Watchlist").setValue("Default");
                        tableSavelistRef.child("Savelist").setValue("Default");
                        tableRemindersRef.child("Reminders").setValue("Default");
                        tableFilmsRef.child("Films").setValue("Default");

                        uploadProfileImage(userID);

                        if (firebaseUser != null) {
                            saveNameInSharedPreferences(email, senha, newUser.getNome(), newUser.getCpf());
                            userDAO = new UserDao(getApplicationContext(), newUser);
                            if (userDAO.insertNewUser(newUser)) {
                                Toast.makeText(RegisterActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(it);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Falha ao cadastrar o usuário localmente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterActivity.this, "O endereço de e-mail já está em uso por outra conta.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Falha ao cadastrar o usuário no Firebase", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadProfileImage(String userID) {
        // Configura o nome do arquivo no Firebase Storage
        String imageFileName = "profile_images/" + userID + ".jpg";

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child(imageFileName);

        imageRef.putFile(mSelectedUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                        usersRef.child(userID).child("profile_image_url").setValue(uri.toString());
                        Log.d("IMAGE_URL", "URL da imagem: " + uri.toString());
                        saveImageUrlInSharedPreferences(uri.toString());


                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Falha ao enviar a imagem de perfil para o Firebase Storage.", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveImageUrlInSharedPreferences(String imageUrl) {
        SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("profile_image_url", imageUrl);
        editor.apply();
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

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

