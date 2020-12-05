package com.kelompokc.tubes.ui.pengembalian;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kelompokc.tubes.API.PinjamAPI;
import com.kelompokc.tubes.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.PUT;

public class DialogKembali extends DialogFragment
{
    private TextInputEditText txtJudul, txtGenre, txtSeri, txtDate;
    private ImageView img;
    private MaterialButton edtBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_kembali, container, false);
        txtJudul = view.findViewById(R.id.judulDialog);
        txtGenre = view.findViewById(R.id.genreDialog);
        txtSeri = view.findViewById(R.id.seriDialog);
        txtDate = view.findViewById(R.id.tanggal_kembali);
        img = view.findViewById(R.id.imgDialog);

        int id = getArguments().getInt("idTransaksi", 0);
        String judul = getArguments().getString("judul", "");
        String genre = getArguments().getString("genre", "");
        String seri = getArguments().getString("seri", "");
        String date = getArguments().getString("tanggal", "");
        String gambar = getArguments().getString("gambar", "");

        txtJudul.setText(judul);
        txtGenre.setText(genre);
        txtSeri.setText(seri);
        txtDate.setText(date);
        Glide.with(view.getContext())
                .applyDefaultRequestOptions(new RequestOptions())
                .load(gambar)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(img);

        edtBtn = view.findViewById(R.id.edtBtn);
        edtBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(txtDate.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Tanggal Harus Diisi", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    editTanggalKembali(txtDate.getText().toString(), id);
                    ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, new PengembalianFragment()).commit();
                    onStop();
                }
            }
        });
        return view;
    }

    public void editTanggalKembali(String tanggalKembali, int id)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengubah data buku");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(PUT, PinjamAPI.URL_UPDATE + id,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        progressDialog.dismiss();
                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                System.out.println(error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("tgl_kembali", tanggalKembali);

                return params;
            }
        };

        queue.add(stringRequest);
    }
}
