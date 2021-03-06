package com.kelompokc.tubes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.kelompokc.tubes.R;
import com.kelompokc.tubes.databinding.AdapterRecyclerViewBinding;
import com.kelompokc.tubes.model.Buku;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class AdapterPinjam extends RecyclerView.Adapter<AdapterPinjam.MyViewHolder>
{
    private Context context;
    private List<Buku> result;
    private int check = 0;
    private boolean b;
    private AdapterRecyclerViewBinding binding;
    private List<Buku> bukuList = new ArrayList<>();

    public AdapterPinjam(Context context, List<Buku> result)
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
        Buku buku = result.get(position);
        binding.setBuku(buku);

        if(bukuList.size()!=0)
        {
            for (int i = 0; i< bukuList.size(); i++)
            {
                bukuList.remove(i);
            }
        }

        holder.itemCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                holder.itemCard.setChecked(!holder.itemCard.isChecked());
                if(holder.itemCard.isChecked())
                {
                    check++;
                    b = true;
                }
                else
                {
                    check--;
                    b = false;
                }
                if(check<=2)
                {
                    setAddOrRemove(position, b, result);
                }
                else
                {
                    FancyToast.makeText(context, "Maksimal Pinjam 2", FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR, false).show();
                    holder.itemCard.setChecked(false);
                    check--;
                }
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
        private MaterialCardView itemCard;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemCard = itemView.findViewById(R.id.item_buku);
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
}