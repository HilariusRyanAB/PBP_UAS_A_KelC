package com.kelompokc.tubes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kelompokc.tubes.API.UserAPI;
import com.kelompokc.tubes.ui.settings.SettingsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.PUT;

public class ProfileActivity extends AppCompatActivity {
    private TextInputEditText etnama;
    private TextView etnpm, etemail;
    public static final String SHARE_PREFS = "SharedPrefUser";
    public static final String SAVE_ID = "idUser";
    private SharedPreferences sharedPreferences;
    private AutoCompleteTextView exposedDropdownFakultas;
    private int iduser;
    private MaterialButton btnEdt;
    private RadioGroup rgGender;
    private MaterialRadioButton pria, wanita;
    private ImageButton ibBack;
    private String[] saFakultas = new String[] {"FTI", "FBE", "FISIP", "FH", "FT"};
    private String sFakultas = "", sGender = "Pria";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

                sharedPreferences  = getSharedPreferences("SharedPrefUser", Context.MODE_PRIVATE);

        iduser = sharedPreferences.getInt("idUser", 0);

        etnama = findViewById(R.id.txt_nama);
        etnpm = findViewById(R.id.txt_npm);
        etemail = findViewById(R.id.txt_email);
        pria = findViewById(R.id.rbPria);
        wanita = findViewById(R.id.rbWanita);

        getUser();

        rgGender = findViewById(R.id.rgJenisKelamin);
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch(i)
                {
                    case R.id.rbPria:
                        sGender = "Pria";
                        break;
                    case R.id.rbWanita:
                        sGender = "Wanita";
                        break;
                }
            }
        });

        exposedDropdownFakultas = findViewById(R.id.edtFakultas);
        ArrayAdapter<String> adapterFakultas = new ArrayAdapter<>(Objects.requireNonNull(this), R.layout.list_item, R.id.item_list, saFakultas);
        exposedDropdownFakultas.setAdapter(adapterFakultas);
        exposedDropdownFakultas.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                sFakultas = saFakultas[i];
            }
        });

        ibBack = findViewById(R.id.imageBack);
        ibBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        btnEdt = findViewById(R.id.edtBtn);
        btnEdt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(etnama.getText().toString().isEmpty())
                {
                    etnama.setError("Nama Tidak Boleh Kosong");
                    etnama.requestFocus();
                }
                else
                {
                    editUser(etnama.getText().toString(), sFakultas, sGender, iduser);
                    startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                    finish();
                }
            }
        });
    }
    public void getUser(){
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setTitle("Mengambil Data User");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        JsonObjectRequest stringRequest = new JsonObjectRequest(GET, UserAPI.URL_GET + iduser,null, new
                Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        progressDialog.dismiss();
                        try
                        {
                            JSONObject obj = response.getJSONObject("user");

                            String nama             = obj.optString("nama");
                            String npm              = obj.optString("npm");
                            String jenisKelamin     = (obj.optString("jenisKelamin"));
                            String Fakultas         = obj.optString("fakultas");
                            String email            = (obj.optString("email"));

                            etnama.setText(nama);
                            etnpm.setText(npm);
                            etemail.setText(email);
                            exposedDropdownFakultas.setText(Fakultas, false);
                            if(jenisKelamin.equalsIgnoreCase("Pria"))
                            {
                                pria.setChecked(true);
                                wanita.setChecked(false);
                            }
                            else if(jenisKelamin.equalsIgnoreCase("Wanita"))
                            {
                                pria.setChecked(false);
                                wanita.setChecked(true);
                            }
                            else
                            {
                                pria.setChecked(false);
                                wanita.setChecked(false);
                            }

                            Toast.makeText(ProfileActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        catch (JSONException e)
                        {
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

    public void editUser(String nama, String fakultas, String jenisKelamin, int idUser)
    {
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);

        StringRequest stringRequest = new StringRequest(PUT, UserAPI.URL_EDIT + idUser, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject obj = new JSONObject(response);
                    Toast.makeText(ProfileActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nama", nama);
                params.put("fakultas", fakultas);
                params.put("jenisKelamin", jenisKelamin);

                return params;
            }
        };

        queue.add(stringRequest);
    }
}