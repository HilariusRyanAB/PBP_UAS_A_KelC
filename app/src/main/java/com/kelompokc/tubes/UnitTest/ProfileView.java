package com.kelompokc.tubes.UnitTest;

import android.content.Context;

public interface ProfileView
{
    String getNama();

    void showNamaError(String message);

    String getFakultas();

    String getGender();

    int getId();

    Context getContext();

    void showEditError(String messsage);

    void startProfileActivity();
}
