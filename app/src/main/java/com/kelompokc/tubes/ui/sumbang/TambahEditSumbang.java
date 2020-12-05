package com.kelompokc.tubes.ui.sumbang;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.kelompokc.tubes.API.BukuAPI;
import com.kelompokc.tubes.API.SumbangAPI;
import com.kelompokc.tubes.LoginActivity;
import com.kelompokc.tubes.MainActivity;
import com.kelompokc.tubes.R;
import com.kelompokc.tubes.adapter.AdapterSumbang;
import com.kelompokc.tubes.model.Buku;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class TambahEditSumbang extends Fragment {
    private TextInputEditText txtJudulBuku, txtGenre, txtNoSeri;
    private ImageView ivGambar;
    private Button btnSimpan, btnBatal, btnUnggah;
    private String status, selected;
    private int idUser, idBuku;
    private View view;
    private Bitmap bitmap;
    private Uri selectedImage = null;
    private Buku buku;
    private static final int PERMISSION_CODE = 1000;
    private SharedPreferences sharedPreferences, sharedPreference;
    public static final String SHARE_PREFS = "SharedPrefIdBuku";
    public static final String SAVE_IDBuku = "idBuku";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_tambah_edit_sumbang, container, false);
        init();
        setAttribut();

        return view;
    }

    public void loadFragment(Fragment fragment)
    {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_fragment_sumbang, fragment)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public TambahEditSumbang(String status)
    {
        this.status = status;
    }

    public TambahEditSumbang(String status, Buku buku)
    {
        this.status = status;
        this.buku = buku;
    }

    public void init()
    {
        txtJudulBuku        = view.findViewById(R.id.txtJudulBuku);
        txtGenre            = view.findViewById(R.id.txtGenre);
        txtNoSeri           = view.findViewById(R.id.txtNoSeri);
        btnSimpan           = view.findViewById(R.id.btnSimpan);
        btnBatal            = view.findViewById(R.id.btnBatal);
        btnUnggah           = view.findViewById(R.id.btnUnggah);
        ivGambar            = view.findViewById(R.id.ivGambar);
        sharedPreferences  = getContext().getSharedPreferences("SharedPrefUser", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getInt("idUser", 0);
        sharedPreference = getContext().getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
        if(status.equalsIgnoreCase("edit"))
        {
            txtJudulBuku.setText(buku.getJudul());
            txtGenre.setText(buku.getGenre());
            txtNoSeri.setText(buku.getNoSeri());
            Glide.with(view.getContext())
                    .applyDefaultRequestOptions(new RequestOptions())
                    .load(buku.getImgURL())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(ivGambar);
        }
    }

    private void setAttribut()
    {
        btnUnggah.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View view = layoutInflater.inflate(R.layout.pilih_media, null);

                final AlertDialog alertD = new AlertDialog.Builder(view.getContext()).create();

                Button btnKamera = (Button) view.findViewById(R.id.btnKamera);
                Button btnGaleri = (Button) view.findViewById(R.id.btnGaleri);

                btnKamera.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        selected="kamera";
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)==
                                    PackageManager.PERMISSION_DENIED ||
                                    getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                            PackageManager.PERMISSION_DENIED)
                            {
                                String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permission,PERMISSION_CODE);
                            }
                            else
                                {
                                openCamera();
                            }
                        }
                        else
                        {
                            openCamera();
                        }
                        alertD.dismiss();
                    }
                });

                btnGaleri.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        selected="galeri";
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                    PackageManager.PERMISSION_DENIED){
                                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permission,PERMISSION_CODE);
                            }
                            else{
                                openGallery();
                            }
                        }
                        else{
                            openGallery();
                        }
                        alertD.dismiss();
                    }
                });

                alertD.setView(view);
                alertD.show();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String judulBuku  = txtJudulBuku.getText().toString();
                String genre      = txtGenre.getText().toString();
                String noSeri     = txtNoSeri.getText().toString();

                if(judulBuku.isEmpty() || genre.isEmpty() || noSeri.isEmpty())
                    Toast.makeText(getContext(), "Data Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
                else
                {
                    String gambar = "";
                    if (bitmap != null)
                    {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        gambar = Base64.encodeToString(bytes, Base64.DEFAULT);
                    }
                    if(status.equals("tambah"))
                    {
                        tambahBuku(judulBuku, genre, noSeri);
                    }
                    else
                    {
                        editBuku(buku.getId(), judulBuku, genre, noSeri);
                    }
                    loadFragment(new SumbangFragment());
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadFragment(new SumbangFragment());
            }
        });
    }

    private void openGallery(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    if(selected.equals("kamera"))
                        openCamera();
                    else
                        openGallery();
                }else{
                    Toast.makeText(getContext() ,"Permision denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1)
        {
            selectedImage = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            ivGambar.setImageBitmap(bitmap);
            bitmap = getResizedBitmap(bitmap, 512);
        }
        else if(resultCode == RESULT_OK && requestCode == 2)
        {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            ivGambar.setImageBitmap(bitmap);
            bitmap = getResizedBitmap(bitmap, 512);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void tambahBuku(final String judulBuku, final String genre, final String noSeri)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data buku");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(POST, BukuAPI.URL_ADD, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                try
                {
                    JSONObject obj = new JSONObject(response);
                    JSONObject bookObj = obj.getJSONObject("data");
                    saveIdBuku(bookObj.getInt("id"));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("judul", judulBuku);
                params.put("genre", genre);
                params.put("noSeri", noSeri);
                params.put("img", "https://cdn-5ec40373c1ac18016c052912.closte.com/wp-content/uploads/2020/02/no-image-found-360x260-2.png");
                params.put("status", "Tersedia");

                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void editBuku(final int id, final String judulBuku, final String genre, final String noSeri) {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengubah data buku");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(PUT,BukuAPI.URL_UPDATE + id, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("judul", judulBuku);
                params.put("genre", genre);
                params.put("noSeri", noSeri);

                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void saveIdBuku(int idBuku)
    {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putInt(SAVE_IDBuku, idBuku);
        editor.commit();
    }
}