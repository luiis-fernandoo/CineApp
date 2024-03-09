package com.example.cineapp.Activities.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.BackoffPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cineapp.Activities.Adapter.Adapter;
import com.example.cineapp.Activities.Broadcast.MyWorker;
import com.example.cineapp.Activities.Decoration.ItemDecoration;
import com.example.cineapp.Activities.Helpers.MyAsyncTask;
import com.example.cineapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment implements MyAsyncTask.AsyncTaskListener {
    Button createWatchList, perfil;
    ConstraintLayout constraintLayout;
    TextView textViewTeste;

    ImageButton textSearch;
    private RecyclerView recyclerViewTopRated, recyclerViewPopular, recyclerViewUpComing,recyclerViewTerror ,recyclerViewComedia, recyclerViewAnimacao, recyclerViewFiccao, recyclerViewRomance;
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
        recyclerViewComedia = view.findViewById(R.id.recyclerViewComedia);
        recyclerViewTerror = view.findViewById(R.id.recyclerViewTerror);
        recyclerViewRomance = view.findViewById(R.id.recyclerViewRomance);
        recyclerViewFiccao = view.findViewById(R.id.recyclerViewFiccao);
        recyclerViewAnimacao = view.findViewById(R.id.recyclerViewAnimacao);
        textSearch = view.findViewById(R.id.textSearch);

        textSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new SearchFragment());
            }
        });

        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueueUniquePeriodicWork("workNotificationService", ExistingPeriodicWorkPolicy.KEEP,
                new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES).setBackoffCriteria(BackoffPolicy.LINEAR, 60000, TimeUnit.MILLISECONDS)
                .build());

        loadMoviesInBackground();
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
                        recyclerViewTopRated.addItemDecoration(new ItemDecoration(10));
                        recyclerViewTopRated.setAdapter(adapter);
                    }else if (reference.equals("Terror")) {
                        recyclerViewTerror.setLayoutManager(layoutManager);
                        recyclerViewTerror.addItemDecoration(new ItemDecoration(10));
                        recyclerViewTerror.setAdapter(adapter);
                    }else if (reference.equals("Comedia")) {
                        recyclerViewComedia.setLayoutManager(layoutManager);
                        recyclerViewComedia.addItemDecoration(new ItemDecoration(10));
                        recyclerViewComedia.setAdapter(adapter);
                    }else if (reference.equals("Romance")) {
                        recyclerViewRomance.setLayoutManager(layoutManager);
                        recyclerViewRomance.addItemDecoration(new ItemDecoration(10));
                        recyclerViewRomance.setAdapter(adapter);
                    }else if (reference.equals("Ficcao")) {
                        recyclerViewFiccao.setLayoutManager(layoutManager);
                        recyclerViewFiccao.addItemDecoration(new ItemDecoration(10));
                        recyclerViewFiccao.setAdapter(adapter);
                    }else if (reference.equals("Animacao")) {
                        recyclerViewAnimacao.setLayoutManager(layoutManager);
                        recyclerViewAnimacao.addItemDecoration(new ItemDecoration(10));
                        recyclerViewAnimacao.setAdapter(adapter);
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

                String urlTerror = "https://api.themoviedb.org/3/discover/movie?with_genres=27&language=pt-BR";
                reference = "Terror";
                MyAsyncTask myAsyncTaskTerror = new MyAsyncTask(HomeFragment.this, urlTerror, reference);
                myAsyncTaskTerror.execute();

                String urlComedia = "https://api.themoviedb.org/3/discover/movie?with_genres=35&language=pt-BR";
                reference = "Comedia";
                MyAsyncTask myAsyncTaskComedia = new MyAsyncTask(HomeFragment.this, urlComedia, reference);
                myAsyncTaskComedia.execute();

                String urlRomance = "https://api.themoviedb.org/3/discover/movie?with_genres=10749&language=pt-BR";
                reference = "Romance";
                MyAsyncTask myAsyncTaskRomance = new MyAsyncTask(HomeFragment.this, urlRomance, reference);
                myAsyncTaskRomance.execute();

                String urlFiccao = "https://api.themoviedb.org/3/discover/movie?with_genres=878&language=pt-BR";
                reference = "Ficcao";
                MyAsyncTask myAsyncTaskFiccao = new MyAsyncTask(HomeFragment.this, urlFiccao, reference);
                myAsyncTaskFiccao.execute();

                String urlAnimacao = "https://api.themoviedb.org/3/discover/movie?with_genres=16&language=pt-BR";
                reference = "Animacao";
                MyAsyncTask myAsyncTaskAnimacao = new MyAsyncTask(HomeFragment.this, urlAnimacao, reference);
                myAsyncTaskAnimacao.execute();

            }
        }).start();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}