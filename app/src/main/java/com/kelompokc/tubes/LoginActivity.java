package com.kelompokc.tubes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kelompokc.tubes.API.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class LoginActivity extends AppCompatActivity
{
    public static final String SHARE_PREFS = "SharedPrefUser";
    public static final String SAVE_ID = "idUser";
    private String CHANNEL_ID="Channel 1";
    private EditText emailInput, passInput;
    private Button btnSignUp, btnSignIn;
    private int idUser;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        sharedPreferences  = getSharedPreferences("SharedPrefUser", Context.MODE_PRIVATE);

        idUser = sharedPreferences.getInt("idUser", 0);

        if(idUser!=0)
        {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailInput = findViewById(R.id.input_email);
        passInput = findViewById(R.id.input_password);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = emailInput.getText().toString();
                String pass = passInput.getText().toString();
                loginAccount(email, pass);
            }
        });
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
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void addNotification()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Hello").setContentText("Welcome to Atma Library, Please Enjoy Your Stay...").setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void loginAccount(String email, String password)
    {
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Login");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(POST, UserAPI.URL_LOGIN, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                try
                {
                    JSONObject obj = new JSONObject(response);
                    Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    if(obj.getString("message").equals("Login Success"))
                    {
                        JSONObject userObj = obj.getJSONObject("user");
                        createNotificationChannel();
                        addNotification();
                        saveID(userObj.getInt("id"));
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void saveID(int idUser)
    {
        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(SHARE_PREFS, LoginActivity.this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SAVE_ID, idUser);
        editor.commit();
    }
}