package com.example.cineapp.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.cineapp.Activities.Adapter.Adapter;
import com.example.cineapp.Activities.Decoration.ItemDecoration;
import com.example.cineapp.Activities.Helpers.MyAsyncTask;
import com.example.cineapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements MyAsyncTask.AsyncTaskListener {

    Button perfil, notify, createWatchList;
    TextView textViewTeste;
    ConstraintLayout constraintLayout;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<String> cardList;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        constraintLayout = findViewById(R.id.constraintLayout);
        createWatchList = findViewById(R.id.createWatchList);
        perfil = findViewById(R.id.perfil);
        textViewTeste = findViewById(R.id.textViewTeste);
        notify = findViewById(R.id.notify);

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
                recyclerView = findViewById(R.id.recyclerView);

                adapter = new Adapter((Context) this,  results);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new ItemDecoration(10));  // Ajuste o espaçamento
                recyclerView.setAdapter(adapter);
            }else {
                textViewTeste.setText("Nenhum resultado encontrado.");
            }
            } catch(JSONException e){
                e.printStackTrace();
            }
    }
    @Override
    public void onTaskError(String error) {

    }
}