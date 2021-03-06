package com.example.roshan.ncab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.example.roshan.ncab.BuildConfig.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        {

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new MapFragment())
                    .commit();}

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
        toggle.syncState();
        //drawer.openDrawer(Gravity.RIGHT);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name);
        SharedPreferences prefs=getSharedPreferences("LONGITUDE",Context.MODE_PRIVATE);
        String email=prefs.getString("username",null);
        txtProfileName.setText(email);

        if (BuildConfig.BUILD_TYPE.equals("debug")) {
            return;
            // do something for a debug build
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item != null && item.getItemId() == android.R.id.home) {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                drawer.openDrawer(Gravity.RIGHT);
            }
        }
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
        }
        return false;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            FragmentManager fragmentManager = getSupportFragmentManager();
            {

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                , new MapFragment())
                        .commit();}
        } else if (id == R.id.nav_gallery) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                , new DriverList())
                        .commit();}
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(getApplicationContext(),Profile.class));

        } else if (id == R.id.nav_share) {
            auth.signOut();
            SharedPreferences sharedpreferences =getSharedPreferences("LONGITUDE", Context.MODE_PRIVATE);
            sharedpreferences.edit().clear().commit();
            SharedPreferences sharedPreferences =getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
            sharedpreferences.edit().clear().commit();

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
