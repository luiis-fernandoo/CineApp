package com.example.cineapp.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.cineapp.Activities.Fragments.HomeFragment;
import com.example.cineapp.Activities.Fragments.NotificationFragment;
import com.example.cineapp.Activities.Fragments.PerfilFragment;
import com.example.cineapp.Activities.Fragments.WatchlistFragment;
import com.example.cineapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityMenu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);

        replaceFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId()== R.id.home){
                replaceFragment(new HomeFragment());
            } else if (item.getItemId()==R.id.watch){
                replaceFragment(new WatchlistFragment());
            } else if (item.getItemId()==R.id.perfil){
                replaceFragment(new PerfilFragment());
            }   else if (item.getItemId()==R.id.lembrete){
                replaceFragment(new NotificationFragment());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}