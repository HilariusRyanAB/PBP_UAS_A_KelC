package com.kelompokc.tubes.ui.peminjaman;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompokc.tubes.Buku;
import com.kelompokc.tubes.QRBarcodeActivity;
import com.kelompokc.tubes.RecyclerViewAdapter;
import com.kelompokc.tubes.R;

import java.util.ArrayList;
import java.util.List;

public class PeminjamanFragment extends Fragment
{
    RecyclerView recyclerView;
    RecyclerViewAdapter pModel;
    FloatingActionButton add;
    ImageView scan;
    ArrayList<Buku> listBuku = new ArrayList<>();
    ArrayList<Buku> tempPinjam = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_peminjaman, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_peminjaman);
        listBuku = new ListPinjam().listPinjam;
        pModel = new RecyclerViewAdapter(getContext(), listBuku);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pModel);
        add = root.findViewById(R.id.button_pinjam);
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tempPinjam = pModel.getDataBuku();
                if(tempPinjam.isEmpty())
                {
                    Toast.makeText(getContext(),"Silahkan Pilih Buku Yang Akan Dipinjam", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CharSequence [] title = getStringArray(tempPinjam);
                    showDialog(title, tempPinjam.size());
                }
            }
        });

        scan = root.findViewById(R.id.button_qr);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), QRBarcodeActivity.class));
            }
        });
        return root;
    }

    private void showDialog(CharSequence[] a, int size)
    {
        String temp = "";
        AlertDialog alert = new AlertDialog.Builder(getContext()).create();
        alert.setTitle("Buku Yang Akan Dipinjam");
        for(int i = 0; i<size;i++)
        {
            if(i<size-1)
            {
                temp =temp + a[i] + "\n\n";
            }
            else
            {
                temp =temp + a[i];
            }
        }
        alert.setMessage(temp);
        alert.show();
    }

    public static String[] getStringArray(List<Buku> input)
    {
        String[] strings = new String[input.size()];
        for (int j = 0; j < input.size(); j++)
        {
            strings[j] = "Judul Buku: " +  input.get(j).getJudul();
        }
        return strings;
    }
}