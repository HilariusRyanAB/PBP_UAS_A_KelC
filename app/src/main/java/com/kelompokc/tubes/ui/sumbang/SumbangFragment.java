package com.kelompokc.tubes.ui.sumbang;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompokc.tubes.API.BukuAPI;
import com.kelompokc.tubes.API.SumbangAPI;
import com.kelompokc.tubes.R;
import com.kelompokc.tubes.adapter.AdapterSumbang;
import com.kelompokc.tubes.model.Buku;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class SumbangFragment extends Fragment, AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterSumbang pModel;
    private FloatingActionButton addSumbang;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private List<Buku> tempSumbang = new ArrayList<>();
    private int idUser;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_peminjaman, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_sumbang);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addSumbang = root.findViewById(R.id.button_sumbang);
        pModel = new AdapterSumbang(getContext(), tempSumbang);

        sharedPreferences  = getContext().getSharedPreferences("SharedPrefUser", Context.MODE_PRIVATE);

        idUser = sharedPreferences.getInt("idUser", 0);

        recyclerView.setAdapter(pModel);

        getBuku();

        addSumbang.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadFragment(new TambahEditSumbang());
            }
        });

        return root;
    }

    private void loadFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .replace(R.id.frame_fragment_sumbang,fragment)
                .commit();
    }


    public static String[] getStringArray(List<Buku> input)
    {
        String[] strings = new String[input.size()];

        for (int j = 0; j < input.size(); j++)
        {
            strings[j] = "Judul Buku: " + input.get(j).getJudul();
        }

        return strings;
    }

    public void getBuku()
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data buku");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, SumbangAPI.URL_GET,
                , null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                progressDialog.dismiss();
                try
                {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!tempSumbang.isEmpty())
                        tempSumbang.clear();

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        int id                  = Integer.parseInt(jsonObject.optString("id"));
                        String judul            = jsonObject.optString("judul");
                        String genre            = jsonObject.optString("genre");
                        String noSeri           = jsonObject.optString("noSeri");
                        String gambar           = jsonObject.optString("img");
                        String status           = jsonObject.optString("status");

                        gambar.replace("\\", "");

                        if(status.equalsIgnoreCase("Tersedia"))
                        {
                            Buku buku = new Buku(id, judul, genre, noSeri, gambar, status);
                            tempSumbang.add(buku);
                        }
                    }
                    pModel.notifyDataSetChanged();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), response.optString("message"), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}
