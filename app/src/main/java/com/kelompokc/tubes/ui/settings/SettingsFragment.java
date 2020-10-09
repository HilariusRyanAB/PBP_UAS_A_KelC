package com.kelompokc.tubes.ui.settings;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.kelompokc.tubes.LoginActivity;
import com.kelompokc.tubes.R;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private String CHANNEL_ID="Channel 1";
    FloatingActionButton logOutBtn;
    private Switch myswitch;

    public static final String SHARE_PREFS = "SharedPrefs";
    public static final String SWITCH1 = "switch1";

    private boolean switchOnOff;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        logOutBtn = root.findViewById(R.id.button_logOut);
        logOutBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new AlertDialog.Builder(getContext()).setTitle("Log Out").setMessage("Are You Sure?")
                        .setPositiveButton("Log Out", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                FirebaseAuth.getInstance().signOut();
                                createNotificationChannel();
                                addNotification();
                                startActivity(new Intent(getContext(), LoginActivity.class));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {

                            }
                        })
                .create().show();
            }
        });
        myswitch=(Switch)root.findViewById(R.id.myswitch);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            myswitch.setChecked(true);
        }
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                saveData();
                if(b == true)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    getActivity().setTheme(R.style.darkTheme);
                    loadData(true);
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    getActivity().setTheme(R.style.AppTheme);
                    loadData(false);
                }
            }
        });
        updateView();
        return root;
    }

    public void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "Channel 1";
            String description = "This Is Channel 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void addNotification()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID).setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Goodbye").setContentText("Comeback Again...").setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent notificationIntent = new Intent(getContext(), LoginActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void saveData()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SWITCH1, myswitch.isChecked());
        editor.apply();
    }

    public void loadData(boolean b)
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
        switchOnOff = sharedPreferences.getBoolean(SWITCH1, b);
    }

    public void updateView()
    {
        myswitch.setChecked(switchOnOff);
    }
}