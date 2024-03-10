package com.example.cineapp.Activities.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class EditActivity extends AppCompatActivity {

    private EditText edit_email, edit_senha, edit_nome, edit_CPF;
    private UserDao userDAO;
    private SharedPreferences sp;
    private ImageView imgUser;
    private Uri mSelectedUri;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        edit_nome = findViewById(R.id.watchListName);
        edit_CPF = findViewById(R.id.edit_CPF);

        Button bt_salvar = findViewById(R.id.btnWatchList);
        imgUser = findViewById(R.id.imgUser);

        sp = getSharedPreferences("app", MODE_PRIVATE);

        String savedNome = sp.getString("nome", "");
        String savedCPF = sp.getString("cpf", "");
        String savedEmail = sp.getString("email", "");
        String savedSenha = sp.getString("senha", "");
        userID = sp.getString("user_id", "");

        edit_nome.setText(savedNome);
        edit_CPF.setText(savedCPF);
        edit_email.setText(savedEmail);
        edit_senha.setText(savedSenha);

        String imageUrl = sp.getString("profile_image_url", "");
        if (!imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl)
                    .transform(new CircleTransform())
                    .into(imgUser);
        }

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
                    return;
                }

                User user = new User(email, nome, senha, cpf);
                userDAO = new UserDao(getApplicationContext(), user);
                if (userDAO.UserUpdate(user)) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("nome", nome);
                    editor.putString("cpf", cpf);
                    editor.putString("senha", senha);
                    editor.apply();

                    uploadProfileImage(userID);

                    Toast.makeText(EditActivity.this, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditActivity.this, "Falha ao atualizar o usuário", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgUser.setOnClickListener(v -> {
            selectPhoto();
        });

    }

    private void uploadProfileImage(String userID) {
        String imageFileName = "profile_images/" + userID + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child(imageFileName);

        imageRef.putFile(mSelectedUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                        usersRef.child(userID).child("profile_image_url").setValue(uri.toString());
                        saveImageUrlInSharedPreferences(uri.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditActivity.this, "Falha ao enviar a imagem de perfil para o Firebase Storage.", Toast.LENGTH_SHORT).show();
                });
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            mSelectedUri = data.getData();
            Picasso.get().load(mSelectedUri)
                    .transform(new CircleTransform())
                    .into(imgUser, new Callback() {

                        public void onSuccess() {
                            // Imagem carregada com sucesso
                        }

                        public void onError(Exception e) {
                            // Erro ao carregar a imagem
                            Log.e("Picasso", "Erro ao carregar a imagem: " + e.getMessage());
                        }
                    });
        }
    }

    private void saveImageUrlInSharedPreferences(String imageUrl) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("profile_image_url", imageUrl);
        editor.apply();
    }
}
