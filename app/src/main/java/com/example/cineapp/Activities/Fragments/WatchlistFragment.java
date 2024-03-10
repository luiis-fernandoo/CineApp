package com.example.cineapp.Activities.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cineapp.Activities.Adapter.WatchListAdapter;
import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.Activities.DAO.WatchlistDao;
import com.example.cineapp.Activities.Models.User;
import com.example.cineapp.Activities.Models.WatchList;
import com.example.cineapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchlistFragment extends Fragment {
    ImageButton button_watchlist, btnWatchList, watchlist_edit, watchlist_delete;
    TextView watchListName;

    private LinearLayout buttonOk;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private WatchListAdapter adapter;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;

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

    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_watchlist, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
        String savedEmail = sp.getString("email", "");

        User user = new User();
        UserDao userDao = new UserDao(getContext(), user);
        User userID = userDao.getUserNameID(savedEmail);
        List<WatchList> watchlists = new ArrayList<>();
        WatchList watchList = new WatchList();
        WatchlistDao watchlistDao = new WatchlistDao(getContext(), watchList);
        watchlists = watchlistDao.getAllWatchList(userID);

        recyclerView = rootView.findViewById(R.id.recycler_view_watchlists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WatchListAdapter(getContext());
        recyclerView.setAdapter(adapter);

        adapter.setWatchlists(watchlists);

        watchListName = rootView.findViewById(R.id.watchListName);
        button_watchlist = rootView.findViewById(R.id.button_watchlist);
        watchlist_edit = rootView.findViewById(R.id.button_watchlist);
        watchlist_delete = rootView.findViewById(R.id.button_watchlist);

        firebaseAuth = FirebaseAuth.getInstance();

        button_watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_add(view);
            }
        });
        return rootView;
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void popup_add(View view){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View popupView = inflater.inflate(R.layout.popup_add_watchlist, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(popupView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        buttonOk = popupView.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextInput = popupView.findViewById(R.id.editText_input);
                String nameWatchList = editTextInput.getText().toString();

                SharedPreferences sp = requireActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
                String savedEmail = sp.getString("email", "");

                User user = new User();
                UserDao userDao = new UserDao(requireContext(), user);
                user = userDao.getUserNameID(savedEmail);

                createWatchlistFirebase(nameWatchList, user);

                alertDialog.dismiss();
            }
        });
    }

    private void createWatchlistFirebase(String watchlistName, User user){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userID = currentUser.getUid();
            // Crie uma referência para o nó da watchlist do usuário atual
            DatabaseReference watchlistRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("Watchlists");
            watchlistRef.orderByChild("name").equalTo(watchlistName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Toast.makeText(requireContext(), "Essa watchlist já existe!", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("", "Else: " + user.getId());
                        WatchList watchList = new WatchList(watchlistName, user.getId());
                        WatchlistDao watchlistDao = new WatchlistDao(requireContext(), watchList);
                        DatabaseReference newItem = watchlistRef.push();
                        newItem.child("name").setValue(watchlistName);
                        newItem.child("user_id").setValue(user.getId());

                        newItem.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (watchlistDao.insertNewWatchList()) {
                                    Toast.makeText(requireActivity(), "WatchList criada com sucesso!", Toast.LENGTH_SHORT).show();
                                    replaceFragment(new WatchlistFragment());
                                } else {
                                    Toast.makeText(requireActivity(), "Erro ao criar WatchList.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }

}
