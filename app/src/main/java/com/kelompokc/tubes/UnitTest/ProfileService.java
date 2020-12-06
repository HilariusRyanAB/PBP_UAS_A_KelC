package com.kelompokc.tubes.UnitTest;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
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

import static com.android.volley.Request.Method.PUT;

public class ProfileService extends AppCompatActivity {
    public void edit(String nama, String fakultas, String jenisKelamin, int idUser)
    {
        RequestQueue queue = Volley.newRequestQueue(ProfileService.this);

        StringRequest stringRequest = new StringRequest(PUT, UserAPI.URL_EDIT + idUser, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject obj = new JSONObject(response);
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
                NetworkResponse networkResponse = error.networkResponse;

                if (networkResponse != null && networkResponse.data != null)
                {
                    String jsonError = new String(networkResponse.data);
                }
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
