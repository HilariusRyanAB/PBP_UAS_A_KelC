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

import com.kelompokc.tubes.Peminjaman;
import com.kelompokc.tubes.R;

import java.util.ArrayList;

public class PeminjamanFragment extends Fragment
{
    RecyclerView recyclerView;
    PeminjamanRecyclerView pModel;
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
        Peminjaman peminjaman2 = new Peminjaman("The Last Emperox", "Science Fiction", "B-0002", "https://images-na.ssl-images-amazon.com/images/I/81624tcjRxL.jpg");
        arrayP.add(peminjaman2);
        Peminjaman peminjaman3 = new Peminjaman("A Beginning at the End", "Dystopian", "C-0003", "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1563448171l/45152976.jpg");
        arrayP.add(peminjaman3);
        Peminjaman peminjaman4 = new Peminjaman("Rhythm of War", "Adventure", "D-0004", "https://images-na.ssl-images-amazon.com/images/I/71U9vIOiyxL.jpg");
        arrayP.add(peminjaman4);
        Peminjaman peminjaman5 = new Peminjaman("The Worst Best Man", "Romance", "E-0005", "https://images-na.ssl-images-amazon.com/images/I/81sZoRkH+BL.jpg");
        arrayP.add(peminjaman5);
        Peminjaman peminjaman6 = new Peminjaman("Troubled Blood", "Detective & Mystery", "F-0006", "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1594301752l/51807232._SY475_.jpg");
        arrayP.add(peminjaman6);
        Peminjaman peminjaman7 = new Peminjaman("Survivor Song", "Horror", "G-0007", "https://dyn.media.titanbooks.com/uYOLZDiCI5sHw8Fs13zwz2oNjkI=/fit-in/600x600/https://media.titanbooks.com/catalog/products/SurvivorSong.jpg");
        arrayP.add(peminjaman7);
        Peminjaman peminjaman8 = new Peminjaman("The Guest List", "Thriller", "H-0008", "https://images-na.ssl-images-amazon.com/images/I/81jHvrCW9yL.jpg");
        arrayP.add(peminjaman8);
        return arrayP;
    }
}