package com.kelompokc.tubes.ui.peminjaman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompokc.tubes.R;

import java.util.ArrayList;

public class PeminjamanFragment extends Fragment
{
    RecyclerView recyclerView;
    PeminjamanRecyclerView pModel;
    Button check;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_peminjaman, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_peminjaman);
        pModel = new PeminjamanRecyclerView(getContext(), getArrayData());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pModel);
        return root;
    }

    private ArrayList<Peminjaman> getArrayData()
    {
        ArrayList<Peminjaman> arrayP = new ArrayList<>();
        Peminjaman peminjaman1 = new Peminjaman("The Unspoken Name", "Fantasy", "A-0001", "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1576631770l/45046552.jpg");
        arrayP.add(peminjaman1);
        Peminjaman peminjaman2 = new Peminjaman("The Last Emperox", "science fiction", "B-0002", "https://images-na.ssl-images-amazon.com/images/I/81624tcjRxL.jpg");
        arrayP.add(peminjaman2);
        Peminjaman
        return arrayP;
    }
}