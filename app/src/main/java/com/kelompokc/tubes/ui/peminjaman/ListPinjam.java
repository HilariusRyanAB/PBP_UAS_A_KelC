package com.kelompokc.tubes.ui.peminjaman;

import com.kelompokc.tubes.model.Buku;

import java.util.ArrayList;

public class ListPinjam
{
    public ArrayList<Buku> listPinjam;

    public ListPinjam()
    {
        listPinjam = new ArrayList();
        listPinjam.add(buku1);
        listPinjam.add(buku2);
        listPinjam.add(buku3);
        listPinjam.add(buku4);
        listPinjam.add(buku5);
        listPinjam.add(buku6);
        listPinjam.add(buku7);
        listPinjam.add(buku8);
    }

    public static final Buku buku1 = new Buku("The Unspoken Name", "Fantasy", "A-0001", "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1576631770l/45046552.jpg");
    public static final Buku buku2 = new Buku("The Last Emperox", "Science Fiction", "B-0002", "https://images-na.ssl-images-amazon.com/images/I/81624tcjRxL.jpg");
    public static final Buku buku3 = new Buku("A Beginning at the End", "Dystopian", "C-0003", "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1563448171l/45152976.jpg");
    public static final Buku buku4 = new Buku("Rhythm of War", "Adventure", "D-0004", "https://images-na.ssl-images-amazon.com/images/I/71U9vIOiyxL.jpg");
    public static final Buku buku5 = new Buku("The Worst Best Man", "Romance", "E-0005", "https://images-na.ssl-images-amazon.com/images/I/81sZoRkH+BL.jpg");
    public static final Buku buku6 = new Buku("Troubled Blood", "Detective & Mystery", "F-0006", "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1594301752l/51807232._SY475_.jpg");
    public static final Buku buku7 = new Buku("Survivor Song", "Horror", "G-0007", "https://dyn.media.titanbooks.com/uYOLZDiCI5sHw8Fs13zwz2oNjkI=/fit-in/600x600/https://media.titanbooks.com/catalog/products/SurvivorSong.jpg");
    public static final Buku buku8 = new Buku("The Guest List", "Thriller", "H-0008", "https://images-na.ssl-images-amazon.com/images/I/81jHvrCW9yL.jpg");
}
