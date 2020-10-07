package com.kelompokc.tubes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompokc.tubes.databinding.AdapterRecyclerViewBinding;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{
    private Context context;
    private List<Peminjaman> result;
    AdapterRecyclerViewBinding binding;
    private FloatingActionButton add;
    private FloatingActionButton remove;
    private List<Peminjaman> peminjamanList = new ArrayList<>();

    public RecyclerViewAdapter(){}

    public RecyclerViewAdapter(Context context, List<Peminjaman> result)
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
        return new MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position)
    {
        final Peminjaman peminjaman = result.get(position);
        binding.setPeminjaman(peminjaman);
        if(peminjamanList.size()!=0)
        {
            for (int i = 0; i<peminjamanList.size();i++)
            {
                peminjamanList.remove(i);
            }
        }
        holder.itemCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                holder.itemCard.setChecked(!holder.itemCard.isChecked());
                if(holder.itemCard.isChecked()==true)
                {
                }
                else
                {
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final AdapterRecyclerViewBinding binding;
        private MaterialCardView itemCard;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemCard = itemView.findViewById(R.id.item_pinjam);
        }
        public void onClick(View view)
        {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }
    }
}