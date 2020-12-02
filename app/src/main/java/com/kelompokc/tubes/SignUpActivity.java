package com.kelompokc.tubes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kelompokc.tubes.API.BukuAPI;
import com.kelompokc.tubes.API.UserAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.android.volley.Request.Method.POST;

public class SignUpActivity extends AppCompatActivity
{
    private AutoCompleteTextView exposedDropdownFakultas;
    private String[] saFakultas = new String[] {"FTI", "FBE", "FISIP", "FH", "FT"};
    private String sFakultas = "", sGender = "Pria";
    private ImageButton btnBack;
    private RadioGroup rgGender;
    private MaterialButton btnRegister;
    private EditText txtNama, txtNPM, txtEmail, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtNama = findViewById(R.id.etNama);
        txtNPM = findViewById(R.id.etNpm);
        txtEmail = findViewById(R.id.etEmail);
        txtPassword = findViewById(R.id.etPassword);

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

        exposedDropdownFakultas = findViewById(R.id.edFakultas);
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

        btnBack = findViewById(R.id.ibBack);
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(txtNama.getText().toString().isEmpty())
                {
                    txtNama.setError("Nama Masih Kosong");
                    txtNama.requestFocus();
                }
                else if(txtNPM.getText().toString().isEmpty())
                {
                    txtNPM.setError("NPM Masih Kosong");
                    txtNPM.requestFocus();
                }
                else if(txtEmail.getText().toString().isEmpty())
                {
                    txtEmail.setError("Email Masih Kosong");
                    txtEmail.requestFocus();
                }
                else if(!txtEmail.getText().toString().contains("@") || !txtEmail.getText().toString().contains(".com"))
                {
                    txtEmail.setError("Format Email Tidak Sesuai");
                    txtEmail.requestFocus();
                }
                else if(sFakultas.isEmpty())
                {
                    exposedDropdownFakultas.setError("Fakultas Masih Kosong");
                    exposedDropdownFakultas.requestFocus();
                }
                else if(txtPassword.getText().toString().isEmpty())
                {
                    txtPassword.setError("Password Masih Kosong");
                    txtPassword.requestFocus();
                }
                else if(txtPassword.getText().toString().length()<6)
                {
                    txtPassword.setError("Password Minimal 6 Karakter");
                    txtPassword.requestFocus();
                }
                else
                {
                    registerUser(txtNama.getText().toString(), txtNPM.getText().toString(),
                            txtEmail.getText().toString(), sFakultas,
                            sGender, txtPassword.getText().toString());
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }

    public void registerUser(String nama, String npm, String email, String fakultas, String jenisKelamin, String password)
    {
        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Register Data User");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(POST, UserAPI.URL_REGISTER, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                try
                {
                    JSONObject obj = new JSONObject(response);

                    Toast.makeText(SignUpActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SignUpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("jenisKelamin", jenisKelamin);
                params.put("fakultas", fakultas);
                params.put("npm", npm);
                params.put("password", password);

                return params;
            }
        };

        queue.add(stringRequest);
    }
}