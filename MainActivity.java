package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.project.Fragments.AboutUsFragment;
import com.example.project.Fragments.HomeFragment;
import com.example.project.Fragments.SettingsFragment;
import com.example.project.LoginFunctionality.LogInPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    Button start_game;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        openFragment(new HomeFragment());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);



/*
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int itemId = item.getItemId();
                if (itemId==R.id.bottom_home)
                {
                    openFragment(new HomeFragment());
                }
                else if (itemId==R.id.bottom_settings)
                {
                    openFragment(new SettingsFragment());
                }
                return true;
            }
        });

 */

        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int itemId = item.getItemId();
        if (itemId==R.id.nav_home)
        {
            openFragment(new HomeFragment());
        } else if (itemId==R.id.nav_settings) {
            openFragment(new SettingsFragment());
        }
        else if (itemId ==R.id.nav_about)
        {
            openFragment(new AboutUsFragment());
        }else if (itemId ==R.id.Leaderboard)
        {
            openFragment(new LeaderboardFragment());

        }
        else if (itemId ==R.id.nav_logout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(MainActivity.this, LogInPage.class);
            startActivity(i);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    public void openFragment(Fragment fragment)
    {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }


}