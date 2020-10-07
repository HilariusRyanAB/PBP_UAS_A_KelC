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
import com.kelompokc.tubes.Peminjaman;
import com.kelompokc.tubes.ui.peminjaman.PeminjamanRecyclerView;

import java.util.ArrayList;

public class PengembalianFragment extends Fragment
{
    RecyclerView recyclerView;
    PeminjamanRecyclerView pModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_pengembalian, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_pengembalian);
        pModel = new PeminjamanRecyclerView(getContext(), getArrayData());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pModel);
        return root;
    }

    private ArrayList<Peminjaman> getArrayData()
    {
        ArrayList<Peminjaman> arrayP = new ArrayList<>();
        Peminjaman peminjaman1 = new Peminjaman("The Yellow Bird Sings", "Historical Fiction", "I-0009", "https://images-na.ssl-images-amazon.com/images/I/81oNtU62gtL.jpg");
        arrayP.add(peminjaman1);
        Peminjaman peminjaman2 = new Peminjaman("Ottolenghi Flavour", "Cooking", "J-0010", "https://m.media-amazon.com/images/I/713CXq2mtLL.jpg");
        arrayP.add(peminjaman2);
        Peminjaman peminjaman3 = new Peminjaman("Jordan Casteel: Within Reach", "Art & Photography", "K-0011", "https://images-na.ssl-images-amazon.com/images/I/71L4ptA84ZL.jpg");
        arrayP.add(peminjaman3);
        Peminjaman peminjaman4 = new Peminjaman("The Art of Resilience", "Health & Fitness", "L-0012", "https://images-na.ssl-images-amazon.com/images/I/81UrNrAKQYL.jpg");
        arrayP.add(peminjaman4);
        Peminjaman peminjaman5 = new Peminjaman("The Splendid and the Vile", "History", "M-0013", "https://images-na.ssl-images-amazon.com/images/I/8100fPwvpyL.jpg");
        arrayP.add(peminjaman5);
        Peminjaman peminjaman6 = new Peminjaman("A Very Punchable Face", "Humor & Entertainment", "N-0014", "https://m.media-amazon.com/images/I/41oqit1aUPL.jpg");
        arrayP.add(peminjaman6);
        Peminjaman peminjaman7 = new Peminjaman("A Promised Land", "Biography", "O-0015", "https://m.media-amazon.com/images/I/41NwnkCbyoL.jpg");
        arrayP.add(peminjaman7);
        Peminjaman peminjaman8 = new Peminjaman("Scotland Beyond the Bagpipes", "Travel", "P-0016", "https://images-na.ssl-images-amazon.com/images/I/81AeXxjk1TL.jpg");
        arrayP.add(peminjaman8);
        return arrayP;
    }
}