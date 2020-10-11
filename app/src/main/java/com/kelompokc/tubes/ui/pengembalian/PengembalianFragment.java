package com.kelompokc.tubes.ui.pengembalian;

import android.content.DialogInterface;
import android.os.AsyncTask;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompokc.tubes.R;
import com.kelompokc.tubes.database.DatabaseClient;
import com.kelompokc.tubes.model.Buku;
import com.kelompokc.tubes.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class PengembalianFragment extends Fragment
{
    private RecyclerView recyclerView;
    private RecyclerViewAdapter pModel;
    private ArrayList<Buku> tempList = new ListKembali().listKembali;
    private ArrayList<Buku> tempKembali;
    private FloatingActionButton remove;
    private SwipeRefreshLayout refreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_pengembalian, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_pengembalian);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        refreshLayout = root.findViewById(R.id.swipe_refresh_pengembalian);
        remove = root.findViewById(R.id.kembali_button);

        //addBukuKembaliAll();
        //deleteBukuKembaliAll(tempList);
        getBukuKembali();

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
                    showDialog(title, tempKembali.size(), tempKembali);
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refreshLayout.setRefreshing(false);
                getBukuKembali();
            }
        });

        return root;
    }

    private void showDialog(CharSequence[] a, int size, final ArrayList<Buku> tempBuku)
    {
        String temp = "";
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
        new AlertDialog.Builder(getContext())
            .setTitle("Buku Yang Akan Dikembalikan")
            .setMessage(temp)
            .setPositiveButton("Confirm", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    updateStatusKembali(tempBuku);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {

                }
            })
            .create().show();
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

    public void addBukuKembaliAll()
    {
        class addBuku extends AsyncTask<Void, Void, Void>
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                for(int i = 0; i < tempList.size(); i++)
                {
                    DatabaseClient.getInstance(getContext()).getDatabase().bukuDAO().insert(tempList.get(i));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                Toast.makeText(getContext(),"Add All Buku Successful", Toast.LENGTH_SHORT).show();
            }
        }
        addBuku get = new addBuku();
        get.execute();
    }

    public void getBukuKembali()
    {
        class getBuku extends AsyncTask<Void, Void, List<Buku>>
        {
            @Override
            protected List<Buku> doInBackground(Void... voids)
            {
                List<Buku> bukuList = DatabaseClient
                        .getInstance(getContext())
                        .getDatabase()
                        .bukuDAO()
                        .getAll("dipinjam");
                return bukuList;
            }

            @Override
            protected void onPostExecute(List<Buku> tempBuku)
            {
                super.onPostExecute(tempBuku);
                pModel = new RecyclerViewAdapter(getContext(), tempBuku);
                recyclerView.setAdapter(pModel);
                if (tempBuku.isEmpty())
                {
                    Toast.makeText(getContext(), "Empty List", Toast.LENGTH_SHORT).show();
                }
            }
        }
        getBuku get = new getBuku();
        get.execute();
    }

    public void deleteBukuKembaliAll(final ArrayList<Buku> tempList)
    {
        class deleteBuku extends AsyncTask<Void, Void, Void>
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                for(int i = 0; i < tempList.size(); i++)
                {
                    DatabaseClient
                            .getInstance(getContext())
                            .getDatabase()
                            .bukuDAO()
                            .delete(tempList.get(i).getNoSeri());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                Toast.makeText(getContext(), "Buku Berhasil Dihapus Semua", Toast.LENGTH_SHORT).show();
            }
        }
        deleteBuku buku = new deleteBuku();
        buku.execute();
    }

    public void updateStatusKembali(final ArrayList<Buku> tempList)
    {
        class updateStatusKembali extends AsyncTask<Void, Void, Void>
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                for (int i = 0; i < tempList.size(); i++)
                {
                    DatabaseClient
                            .getInstance(getContext())
                            .getDatabase()
                            .bukuDAO()
                            .updateStatus("tersedia", tempList.get(i).getNoSeri());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                Toast.makeText(getContext(), "Buku Berhasil Dikembalikan", Toast.LENGTH_SHORT).show();
            }
        }
        updateStatusKembali status = new updateStatusKembali();
        status.execute();
    }
}