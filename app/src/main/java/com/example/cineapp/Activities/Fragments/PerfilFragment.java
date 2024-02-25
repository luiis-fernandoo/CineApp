package com.example.cineapp.Activities.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cineapp.Activities.Activities.EditActivity;
import com.example.cineapp.Activities.Activities.LoginActivity;
import com.example.cineapp.Activities.Activities.MainUserActivity;
import com.example.cineapp.Activities.DAO.UserDao;
import com.example.cineapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {
    private TextView textNomeUser, textNomeEmail;
    private Button bt_deslogar, bt_apagar, bt_Editar;
    private SharedPreferences sp;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);

        textNomeUser = rootView.findViewById(R.id.textNomeUser);
        textNomeEmail = rootView.findViewById(R.id.textNomeEmail);
        bt_deslogar = rootView.findViewById(R.id.bt_deslogar);
        bt_apagar = rootView.findViewById(R.id.bt_apagar);
        bt_Editar = rootView.findViewById(R.id.bt_Editar);

        sp = requireActivity().getSharedPreferences("app", MODE_PRIVATE);
        String savedEmail = sp.getString("email", "");
        String savedNome = sp.getString("nome", "");

        Log.d("MainUserActivity", "Email recuperado: " + savedEmail);
        Log.d("MainUserActivity", "Nome recuperado: " + savedNome);

        textNomeUser.setText("Nome: "+savedNome);
        textNomeEmail.setText("Email: "+savedEmail);

        bt_deslogar.setOnClickListener(v -> {
            logoutUser();
        });

        bt_apagar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmação");
            builder.setMessage("Deseja realmente apagar o usuário?");

            builder.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Ação a ser realizada ao clicar em "Apagar"
                    String emailToBeDeleted = sp.getString("email", "");

                    UserDao userDAO = new UserDao(requireContext(), null); // Passe o objeto User, se necessário

                    if (userDAO.DeleteUser(emailToBeDeleted)) {
                        logoutUser();
                    } else {
                        Toast.makeText(requireContext(), "Falha ao apagar o usuário", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Ação a ser realizada ao clicar em "Cancelar"
                    dialog.dismiss(); // Fecha o diálogo
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });


        bt_deslogar.setOnClickListener(v -> {
            logoutUser();
        });

        bt_Editar.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), EditActivity.class);
            startActivity(intent);
        });


        return rootView;
    }
    private void logoutUser() {
        SharedPreferences.Editor editor = sp.edit();

        // Remove os dados do SharedPreferences
        editor.remove("email");
        editor.remove("nome");
        // Adicione outras chaves para remover se necessário

        editor.apply();

        // Após deslogar, redireciona para a tela de login
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sp = requireActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
        String savedNome = sp.getString("nome", "");
        String savedEmail = sp.getString("email", "");

        textNomeUser.setText("Nome: " + savedNome);
        textNomeEmail.setText("Email: " + savedEmail);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Atualiza os dados na tela principal antes de destruir a atividade
        SharedPreferences sp = requireActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
        String savedNome = sp.getString("nome", "");
        String savedEmail = sp.getString("email", "");

        textNomeUser.setText("Nome: " + savedNome);
        textNomeEmail.setText("Email: " + savedEmail);
    }
}