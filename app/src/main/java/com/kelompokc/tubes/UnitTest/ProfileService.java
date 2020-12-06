package com.kelompokc.tubes.UnitTest;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kelompokc.tubes.API.UserAPI;
import com.kelompokc.tubes.model.User;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.PUT;

public class ProfileService
{
    public void editData(final ProfileView view, String nama, String fakultas, String jenisKelamin, int idUser, final ProfileCallback callback)
    {
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        StringRequest stringRequest = new StringRequest(PUT, UserAPI.URL_EDIT + idUser, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("message").equalsIgnoreCase("User Berhasil Diubah"))
                    {
                        callback.onSuccess(true);
                    }
                    else
                    {
                        callback.onError();
                        view.showEditError(obj.getString("message"));
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
                NetworkResponse networkResponse = error.networkResponse;

                if (networkResponse != null && networkResponse.data != null)
                {
                    String jsonError = new String(networkResponse.data);
                    FancyToast.makeText(view.getContext(), jsonError, FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR, false).show();
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

    public Boolean getValid(final ProfileView view, String nama, String fakultas, String jenisKelamin, int idUser)
    {
        final Boolean[] bool = new Boolean[1];
        editData(view, nama, fakultas, jenisKelamin, idUser, new ProfileCallback()
        {
            @Override
            public void onSuccess(boolean value)
            {
                bool[0] = true;
            }

            @Override
            public void onError()
            {
                bool[0] = false;
            }
        });
        return bool[0];
    }
}
