package com.kelompokc.tubes.ui.donasi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DonasiViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DonasiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Donasi fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}