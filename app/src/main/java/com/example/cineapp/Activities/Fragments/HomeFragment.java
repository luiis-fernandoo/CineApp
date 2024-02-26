package com.example.cineapp.Activities.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cineapp.Activities.Adapter.Adapter;
import com.example.cineapp.Activities.Decoration.ItemDecoration;
import com.example.cineapp.Activities.Helpers.MyAsyncTask;
import com.example.cineapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HomeFragment extends Fragment implements MyAsyncTask.AsyncTaskListener {
    Button createWatchList;
    Button perfil;
    ConstraintLayout constraintLayout;
    TextView textViewTeste;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<String> cardList;
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

        constraintLayout = view.findViewById(R.id.constraintLayout);
        createWatchList = view.findViewById(R.id.createWatchList);
        perfil = view.findViewById(R.id.perfil);
        textViewTeste = view.findViewById(R.id.textViewTeste);
        recyclerView = view.findViewById(R.id.recyclerView);
        MyAsyncTask myAsyncTask = new MyAsyncTask(this);
        myAsyncTask.execute();

        return view;
    }

    @Override
    public void onTaskComplete(JSONObject result) {
        try {
            JSONArray results = result.getJSONArray("results");
            if (results.length() > 0) {

                adapter = new Adapter(requireContext(),  results);
                LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
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