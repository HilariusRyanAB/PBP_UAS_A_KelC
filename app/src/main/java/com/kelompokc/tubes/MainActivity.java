package com.kelompokc.tubes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kelompokc.tubes.ui.peminjaman.PeminjamanFragment;
import com.kelompokc.tubes.ui.pengembalian.PengembalianFragment;
import com.kelompokc.tubes.ui.settings.SettingsFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity
{
    private Fragment peminjamanF = new PeminjamanFragment();
    private Fragment pengembalianF = new PengembalianFragment();
    private Fragment settingsF = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView botNav = findViewById(R.id.nav_view);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, peminjamanF).commit();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE)
        {
            String CHANNEL_ID = "Channel 1";
            CharSequence name = "Channel 1";
            String description = "This is Channel 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.navigation_peminjaman :
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, peminjamanF).commit();
                    break;
                    case R.id.navigation_pengembalian :
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, pengembalianF).commit();
                    break;
                    case R.id.navigation_settings :
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, settingsF).commit();
                    break;
                }
                return true;
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("news").addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
            }
        });
    }
}