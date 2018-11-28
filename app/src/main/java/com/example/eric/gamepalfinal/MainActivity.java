package com.example.eric.gamepalfinal;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    Spinner spinner;
    private DrawerLayout mDrawerLayout;
    Context context;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDrawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.ic_mymenu5);

        try {
            NavigationView navigationView = findViewById(R.id.nav_view);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            LogRegPage logRegPage = new LogRegPage();
            fragmentTransaction.replace(R.id.content_frame, logRegPage);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {

                            menuItem.setChecked(true);

                            mDrawerLayout.closeDrawers();

                            Log.d("Selected", menuItem.getTitle().toString());

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            if (menuItem.getTitle().equals("Home")) {

                            }

                            if (menuItem.getTitle().equals("Matches")) {

                            }

                            if (menuItem.getTitle().equals("Chat")) {

                            }
                            if (menuItem.getTitle().equals("Settings")) {

                            }
                            if (menuItem.getTitle().equals("Logout")) {
                                mAuth.signOut();
                                mAuth = null;
                                LogRegPage fragment = new LogRegPage();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.content_frame, fragment);
                                transaction.commit();
                            }

                            return true;
                        }
                    });
            navigationView.getMenu().getItem(0).setChecked(true);
        }
        catch(Exception e) {
            Log.d("ERROR ANT ERROR", e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}