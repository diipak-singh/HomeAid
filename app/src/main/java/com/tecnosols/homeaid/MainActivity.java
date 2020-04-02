package com.tecnosols.homeaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Services");
        getSupportActionBar().setElevation(3);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bot_home);
    }

    HomeFragment homeFragment = new HomeFragment();
    BookingsFragment bookingsFragment = new BookingsFragment();
    SupportFragment supportFragment = new SupportFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bot_home:
                getSupportActionBar().setTitle("Services");
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                return true;

            case R.id.bot_myBookings:
                getSupportActionBar().setTitle("My Bookings");
                getSupportFragmentManager().beginTransaction().replace(R.id.container, bookingsFragment).commit();
                return true;

            case R.id.bot_support:
                getSupportActionBar().setTitle("Support");
                getSupportFragmentManager().beginTransaction().replace(R.id.container, supportFragment).commit();
                return true;

            case R.id.bot_profile:
                getSupportActionBar().setTitle("Profile");
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();


        if (count == 0) {
            //super.onBackPressed();
            //additional code

            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_settings_power_black_24dp)
                    .setTitle("Closing App")
                    .setMessage("Are you sure, you want to close this app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }
}
