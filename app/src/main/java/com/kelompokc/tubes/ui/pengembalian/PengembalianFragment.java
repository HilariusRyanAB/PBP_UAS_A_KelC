package com.kelompokc.tubes.ui.pengembalian;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompokc.tubes.R;
import com.kelompokc.tubes.Buku;
import com.kelompokc.tubes.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class PengembalianFragment extends Fragment
{
    RecyclerView recyclerView;
    RecyclerViewAdapter pModel;
    ArrayList<Buku> listKembali = new ListKembali().listKembali;
    ArrayList<Buku> tempKembali;
    FloatingActionButton remove;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_pengembalian, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_pengembalian);
        pModel = new RecyclerViewAdapter(getContext(), listKembali);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pModel);
        remove = root.findViewById(R.id.kembali_button);
        remove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tempKembali = pModel.getDataBuku();
                if(tempKembali.isEmpty())
                {
                    Toast.makeText(getContext(),"Silahkan Pilih Buku Yang Akan Dikembalikan", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CharSequence [] title = getStringArray(tempKembali);
                    showDialog(title, tempKembali.size());
                }
            }
        });
        return root;
    }

    private void showDialog(CharSequence[] a, int size)
    {
        String temp = "";
        AlertDialog alert = new AlertDialog.Builder(getContext()).create();
        alert.setTitle("Buku Yang Akan Dikembalikan");
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