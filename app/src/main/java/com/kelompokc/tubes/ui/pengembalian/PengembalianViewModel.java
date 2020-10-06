package com.kelompokc.tubes.ui.pengembalian;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PengembalianViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PengembalianViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Pengembalian fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}