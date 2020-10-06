package com.kelompokc.tubes.ui.peminjaman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompokc.tubes.R;
import com.kelompokc.tubes.databinding.AdapterRecyclerViewBinding;

import java.util.List;

public class PeminjamanRecyclerView extends RecyclerView.Adapter<PeminjamanRecyclerView.MyViewHolder>
{
    private Context context;
    private List<Peminjaman> result;
    AdapterRecyclerViewBinding binding;

    public PeminjamanRecyclerView(){}

    public PeminjamanRecyclerView(Context context, List<Peminjaman> result)
    {
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_recycler_view, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {
        final Peminjaman peminjaman = result.get(position);
        binding.setPeminjam(peminjaman);
    }

    @Override
    public int getItemCount()
    {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final AdapterRecyclerViewBinding binding;

        public MyViewHolder(@NonNull AdapterRecyclerViewBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void onClick(View view)
        {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }
}