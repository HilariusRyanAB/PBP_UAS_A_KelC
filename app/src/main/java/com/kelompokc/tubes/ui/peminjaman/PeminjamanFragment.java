package com.kelompokc.tubes.ui.peminjaman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompokc.tubes.Buku;
import com.kelompokc.tubes.RecyclerViewAdapter;
import com.kelompokc.tubes.R;

import java.util.ArrayList;

public class PeminjamanFragment extends Fragment
{
    RecyclerView recyclerView;
    RecyclerViewAdapter pModel;
    FloatingActionButton add;
    ArrayList<Buku> listPinjam = new ListPinjam().listPinjam;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_peminjaman, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_peminjaman);
        pModel = new RecyclerViewAdapter(getContext(), listPinjam);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pModel);
        add = root.findViewById(R.id.button_pinjam);
        return root;
    }
}