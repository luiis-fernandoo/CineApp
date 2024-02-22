package com.example.cineapp.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cineapp.Activities.Helpers.MyAsyncTask;
import com.example.cineapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity implements MyAsyncTask.AsyncTaskListener {

    Button createWatchList;
    Button perfil;
    TextView textViewTeste;
    ConstraintLayout constraintLayout;
    ImageView img1, img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        constraintLayout = findViewById(R.id.constraintLayout);
        createWatchList = findViewById(R.id.createWatchList);
        perfil = findViewById(R.id.perfil);
        textViewTeste = findViewById(R.id.textViewTeste);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);

        MyAsyncTask myAsyncTask = new MyAsyncTask(this);
        myAsyncTask.execute();

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(HomeActivity.this, MainUserActivity.class);
                startActivity(it);
            }
        });
        createWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(HomeActivity.this, CreateWatchList.class);
                startActivity(it);

            }
        });
    }
    @Override
    public void onTaskComplete(JSONObject result) {
        try {
            JSONArray results = result.getJSONArray("results");

            if (results.length() > 0) {
                for (int i = 0; i < 3; i++) {
                    try {
                        JSONObject currentResult = results.getJSONObject(i);
                        String filmId = currentResult.getString("id");
                        img1.setTag(filmId);

                        String poster_path = "https://image.tmdb.org/t/p/original/" + currentResult.getString("poster_path");
                        Glide.with(this)
                                .load(poster_path)
                                .into(img1);
                        //imageView.setImageResource(backdropPath);

                        if (img1.getParent() != null) {
                            ((ViewGroup) img1.getParent()).removeView(img1);
                        }
                        constraintLayout.addView(img1);
                        img1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent it = new Intent(HomeActivity.this, detailsFilmActivity.class);
                                it.putExtra("tag", (String)img1.getTag());
                                startActivity(it);
                            }
                        });
                        i++;
                        JSONObject currentResult2 = results.getJSONObject(i);
                        String filmId2 = currentResult2.getString("id");
                        img2.setTag(filmId2);

                        String poster_path2 = "https://image.tmdb.org/t/p/original/" + currentResult2.getString("poster_path");
                        Glide.with(this)
                                .load(poster_path2)
                                .into(img2);
                        //imageView.setImageResource(backdropPath);

                        if (img2.getParent() != null) {
                            ((ViewGroup) img2.getParent()).removeView(img2);
                        }
                        constraintLayout.addView(img2);
                        img2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent it = new Intent(HomeActivity.this, detailsFilmActivity.class);
                                it.putExtra("tag", (String)img2.getTag());
                                startActivity(it);
                            }
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else {
                textViewTeste.setText("Nenhum resultado encontrado.");
            }
            } catch(JSONException e){
                // Lidar com erros de análise JSON, se necessário
                e.printStackTrace();
            }
        }

        @Override
        public void onTaskError(String error) {

        }
}