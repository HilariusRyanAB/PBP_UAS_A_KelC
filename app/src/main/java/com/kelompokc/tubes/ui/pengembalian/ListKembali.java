package com.kelompokc.tubes.ui.pengembalian;

import com.kelompokc.tubes.Buku;

import java.util.ArrayList;

public class ListKembali
{
    public ArrayList<Buku> listKembali;

    public ListKembali()
    {
        listKembali = new ArrayList();
        listKembali.add(buku1);
        listKembali.add(buku2);
        listKembali.add(buku3);
        listKembali.add(buku4);
        listKembali.add(buku5);
        listKembali.add(buku6);
        listKembali.add(buku7);
        listKembali.add(buku8);
    }

    public static final Buku buku1 = new Buku("The Yellow Bird Sings", "Historical Fiction", "I-0009", "https://images-na.ssl-images-amazon.com/images/I/81oNtU62gtL.jpg");
    public static final Buku buku2 = new Buku("Ottolenghi Flavour", "Cooking", "J-0010", "https://m.media-amazon.com/images/I/713CXq2mtLL.jpg");
    public static final Buku buku3 = new Buku("Jordan Casteel: Within Reach", "Art & Photography", "K-0011", "https://images-na.ssl-images-amazon.com/images/I/71L4ptA84ZL.jpg");
    public static final Buku buku4 = new Buku("The Art of Resilience", "Health & Fitness", "L-0012", "https://images-na.ssl-images-amazon.com/images/I/81UrNrAKQYL.jpg");
    public static final Buku buku5 = new Buku("The Splendid and the Vile", "History", "M-0013", "https://images-na.ssl-images-amazon.com/images/I/8100fPwvpyL.jpg");
    public static final Buku buku6 = new Buku("Scotland Beyond the Bagpipes", "Travel", "N-0014", "https://images-na.ssl-images-amazon.com/images/I/81AeXxjk1TL.jpg");
    public static final Buku buku7 = new Buku("A Very Punchable Face", "Humor & Entertainment", "O-0015", "https://m.media-amazon.com/images/I/41oqit1aUPL.jpg");
    public static final Buku buku8 = new Buku("A Promised Land", "Biography", "P-0016", "https://m.media-amazon.com/images/I/41NwnkCbyoL.jpg");
}
