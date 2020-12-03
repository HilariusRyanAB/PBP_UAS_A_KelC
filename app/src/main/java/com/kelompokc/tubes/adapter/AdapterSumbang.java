package com.kelompokc.tubes.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.kelompokc.tubes.API.BukuAPI;
import com.kelompokc.tubes.R;
import com.kelompokc.tubes.databinding.AdapterRecyclerViewBinding;
import com.kelompokc.tubes.model.Buku;
import com.kelompokc.tubes.ui.sumbang.TambahEditSumbang;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.DELETE;

public class AdapterSumbang extends RecyclerView.Adapter<AdapterSumbang.MyViewHolder> {
    private Context context;
    private List<Buku> result;
    private int check = 0;
    private boolean b;
    private AdapterRecyclerViewBinding binding;
    private View view;
    private List<Buku> bukuList = new ArrayList<>();
    private AdapterSumbang.deleteItemListener mListener;

    public AdapterSumbang(Context context, List<Buku> result)
    {
        this.context = context;
        this.result = result;
    }

    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_sumbang, parent, false);
        view = layoutInflater.inflate(R.layout.adapter_sumbang parent, false);
        return new MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position)
    {
        Buku buku = result.get(position);
        binding.setBuku(buku);

        if(bukuList.size()!=0)
        {
            for (int i = 0; i< bukuList.size(); i++)
            {
                bukuList.remove(i);
            }
        }

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle data = new Bundle();
                data.putSerializable("buku", buku);
                data.putString("status", "edit");
                TambahEditSumbang tambahEditSumbang = new TambahEditSumbang();
                tambahEditSumbang.setArguments(data);
                loadFragment(tambahEditSumbang);
            }
        });

        holder.ivHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Anda yakin ingin menghapus buku ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBuku(Integer.toString(buku.getId()));
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return result.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final AdapterRecyclerViewBinding binding;
        private TextView ivEdit, ivHapus;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            ivEdit          = (TextView) itemView.findViewById(R.id.ivEdit);
            ivHapus         = (TextView) itemView.findViewById(R.id.ivHapus);
        }
        public void onClick(View view)
        {

        }
    }

    private void setAddOrRemove(int index, boolean b, List<Buku> result)
    {
        if(b)
        {
            bukuList.add(result.get(index));
        }
        else
        {
            bukuList.remove(result.get(index));
        }
    }

    public List<Buku> getDataBuku()
    {
        return bukuList;
    }

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_fragment_sumbang,fragment)
                .commit();
    }

    public void deleteBuku(String id){
        //Tambahkan hapus buku disini
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data buku");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(DELETE, BukuAPI.URL_DELETE + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    mListener.deleteItem(true);

                    Log.i("DELBUKU", "try onResponse: " + obj.getString("message"));
                } catch (JSONException e) {
                    Log.i("DELBUKU", "catch onResponse: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                Log.i("DELBUKU", "onError: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
}
