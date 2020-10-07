package com.kelompokc.tubes.ui.pengembalian;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompokc.tubes.R;
import com.kelompokc.tubes.Buku;
import com.kelompokc.tubes.RecyclerViewAdapter;
import com.kelompokc.tubes.ui.peminjaman.ListPinjam;

import java.util.ArrayList;

public class PengembalianFragment extends Fragment
{
    RecyclerView recyclerView;
    RecyclerViewAdapter pModel;
    ArrayList<Buku> listKembali = new ListKembali().listKembali;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_pengembalian, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_pengembalian);
        pModel = new RecyclerViewAdapter(getContext(), listKembali);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pModel);
        return root;
    }
}