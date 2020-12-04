package com.kelompokc.tubes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kelompokc.tubes.API.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.Request.Method.GET;

public class ProfileActivity extends AppCompatActivity {
    private EditText etnama,etfakultas;
    private TextView etnpm,etemail,edgender;
    public static final String SHARE_PREFS = "SharedPrefUser";
    public static final String SAVE_ID = "idUser";
    private Integer iduser;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences  = getSharedPreferences("SharedPrefUser", Context.MODE_PRIVATE);

        iduser = sharedPreferences.getInt("idUser", 0);

        etnama = findViewById(R.id.etNama);
        etfakultas = findViewById(R.id.edfakultas);
        etnpm = findViewById(R.id.etNpm);
        etemail = findViewById(R.id.etEmail);
        edgender = findViewById(R.id.edgender);

        getUser();

    }
    public void getUser(){
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setTitle("Mengambil data mahasiswa");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        JsonObjectRequest stringRequest = new JsonObjectRequest(GET, UserAPI.URL_GET + iduser,null, new
                Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = response.getJSONObject("user");

                            String id               = (obj.optString("id"));
                            String nama             = obj.optString("nama");
                            String npm              = obj.optString("npm");
                            String jenisKelamin     = (obj.optString("jenisKelamin"));
                            String Fakultas         = obj.optString("Fakultas");
                            String email            = (obj.optString("email"));

                            etnama.setText(nama);
                            etfakultas.setText(Fakultas);
                            etnpm.setText(npm);
                            edgender.setText(jenisKelamin);
                            etemail.setText(email);
                            Toast.makeText(ProfileActivity.this, obj.getString("Data User Berhasil Diambil"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);

    }


}