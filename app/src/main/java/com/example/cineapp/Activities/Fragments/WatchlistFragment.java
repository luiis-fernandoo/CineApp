package com.example.cineapp.Activities.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.DAO.WatchlistDao;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.Activities.Models.WatchList;
import com.example.cineapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchlistFragment extends Fragment {
    Button btnWatchList;
    TextView watchListName;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public WatchlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WatchlistFragment.
     */
    public static WatchlistFragment newInstance(String param1, String param2) {
        WatchlistFragment fragment = new WatchlistFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_watchlist, container, false);

        btnWatchList = rootView.findViewById(R.id.btnWatchList);
        watchListName = rootView.findViewById(R.id.watchListName);

        btnWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameWatchList = watchListName.getText().toString();
                SharedPreferences sp = requireActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
                String savedEmail = sp.getString("email", "");

                User user = new User();
                UserDao userDao = new UserDao(requireContext(), user);
                user = userDao.getUserNameID(savedEmail);
                int userId = user.getId();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference tableWatchList = database.getReference("WatchList");
                DatabaseReference newItem = tableWatchList.push();

                newItem.child("name").setValue(nameWatchList);
                newItem.child("user_id").setValue(userId);
                newItem.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Verifique se os dados foram salvos com sucesso
                        Log.d("Firebase", "Dados salvos com sucesso!");

                        WatchList watchList = new WatchList(nameWatchList, userId);
                        WatchlistDao watchlistDao = new WatchlistDao(requireContext(), watchList);

                        if (watchlistDao.insertNewWatchList()) {
                            // Sucesso ao salvar no Firebase e no banco de dados local
                            Toast.makeText(requireActivity(), "WatchList criada com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(requireActivity(), HomeFragment.class);
                            startActivity(it);
                        } else {
                            // Erro ao salvar no banco de dados local
                            Toast.makeText(requireActivity(), "Erro ao criar WatchList.", Toast.LENGTH_SHORT).show();
                        }
                        Intent it = new Intent(requireActivity(), HomeFragment.class);
                        startActivity(it);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Ocorreu um erro ao salvar no Firebase
                        Log.d("Firebase", "Erro ao salvar dados: " + error.getMessage());
                        Toast.makeText(requireActivity(), "Erro ao salvar dados no Firebase.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return rootView;
    }
}