package com.example.cineapp.Activities.Fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cineapp.Activities.Adapter.Adapter;
import com.example.cineapp.Activities.Broadcast.BroadCast;
import com.example.cineapp.Activities.Broadcast.MyWorker;
import com.example.cineapp.Activities.Decoration.ItemDecoration;
import com.example.cineapp.Activities.Helpers.MyAsyncTask;
import com.example.cineapp.Activities.Service.NotificationService;
import com.example.cineapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment implements MyAsyncTask.AsyncTaskListener {
    Button createWatchList;
    Button perfil;
    ConstraintLayout constraintLayout;
    TextView textViewTeste;
    private RecyclerView recyclerViewTopRated, recyclerViewPopular, recyclerViewUpComing;
    private List<String> cardList;
    private String reference;
    View view;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Context context = getContext();
        constraintLayout = view.findViewById(R.id.constraintLayout);
        createWatchList = view.findViewById(R.id.createWatchList);
        perfil = view.findViewById(R.id.perfil);
        textViewTeste = view.findViewById(R.id.textViewTeste);
        recyclerViewTopRated = view.findViewById(R.id.recyclerViewTopRated);
        recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular);
        recyclerViewUpComing = view.findViewById(R.id.recyclerViewUpComing);

        BroadCast broadCast = new BroadCast();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(broadCast, filter);
//        WorkManager workManager = WorkManager.getInstance(context);
//        workManager.enqueueUniquePeriodicWork("my_work", ExistingPeriodicWorkPolicy.KEEP,
//                new PeriodicWorkRequest.Builder(MyWorker.class, 1, TimeUnit.MINUTES)
//                        .build());
        return view;
    }

    @Override
    public void onTaskComplete(JSONObject result, String reference) {
        if (isAdded() && getContext() != null) {
            try {
                JSONArray results = result.getJSONArray("results");
                if (results.length() > 0) {

                    Adapter adapter = new Adapter(requireContext(), results);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                    if (reference.equals("Popular")) {
                        recyclerViewPopular.setLayoutManager(layoutManager);
                        recyclerViewPopular.addItemDecoration(new ItemDecoration(10));
                        recyclerViewPopular.setAdapter(adapter);
                    } else if (reference.equals("UpComing")) {
                        recyclerViewUpComing.setLayoutManager(layoutManager);
                        recyclerViewUpComing.addItemDecoration(new ItemDecoration(10));
                        recyclerViewUpComing.setAdapter(adapter);
                    } else if (reference.equals("TopRated")) {
                        recyclerViewTopRated.setLayoutManager(layoutManager);
                        recyclerViewTopRated.addItemDecoration(new ItemDecoration(10));  // Ajuste o espaçamento
                        recyclerViewTopRated.setAdapter(adapter);
                    }
                } else {
                    textViewTeste.setText("Nenhum resultado encontrado.");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onTaskError(String error) {

    }

    private void loadMoviesInBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlPopular = "https://api.themoviedb.org/3/movie/popular?language=pt_BR";
                reference = "Popular";
                MyAsyncTask myAsyncTaskPopular = new MyAsyncTask(HomeFragment.this, urlPopular, reference);
                myAsyncTaskPopular.execute();

                String urlUpComing = "https://api.themoviedb.org/3/movie/upcoming?language=pt_BR";
                reference = "UpComing";
                MyAsyncTask myAsyncTaskUpComing = new MyAsyncTask(HomeFragment.this, urlUpComing, reference);
                myAsyncTaskUpComing.execute();

                String urlTopRated = "https://api.themoviedb.org/3/movie/top_rated?language=pt_BR";
                reference = "TopRated";
                MyAsyncTask myAsyncTaskTopRated = new MyAsyncTask(HomeFragment.this, urlTopRated, reference);
                myAsyncTaskTopRated.execute();
            }
        }).start();
    }
}