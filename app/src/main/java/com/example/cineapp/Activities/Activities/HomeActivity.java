package com.example.cineapp.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
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

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements MyAsyncTask.AsyncTaskListener {

    Button createWatchList;
    Button perfil;
    TextView textViewTeste;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        constraintLayout = findViewById(R.id.constraintLayout);
        createWatchList = findViewById(R.id.createWatchList);
        perfil = findViewById(R.id.perfil);
        textViewTeste = findViewById(R.id.textViewTeste);

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
                        ImageView imageView = new ImageView(this);
                        imageView.setId(Integer.parseInt(filmId));
                        String backdropPath = "https://image.tmdb.org/t/p/original/" + currentResult.getString("backdrop_path");
                        Glide.with(this)
                                .load(backdropPath)
                                .into(imageView);
                        //imageView.setImageResource(backdropPath);
                        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                                500,
                                3000
                        );
                        if (i == 0) {
                            // Para a primeira imagem, alinhe à esquerda do ConstraintLayout
                            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                        } else {
                            // Para as imagens subsequentes, alinhe à direita da imagem anterior com a margem
                            layoutParams.startToEnd = constraintLayout.getChildAt(i - 1).getId();
                            layoutParams.leftMargin = 16;
                        }

                        imageView.setLayoutParams(layoutParams);
                        if (imageView.getParent() != null) {
                            ((ViewGroup) imageView.getParent()).removeView(imageView);
                        }
                        constraintLayout.addView(imageView);

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