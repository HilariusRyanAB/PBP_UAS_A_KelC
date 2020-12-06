package com.kelompokc.tubes.UnitTest;

import android.content.Context;
import android.content.Intent;

public class ProfileUtil
{
    private Context context;

    public ProfileUtil(Context context)
    {
        this.context = context;
    }

    public void startProfileActivity()
    {
        context.startActivity(new Intent(context, ProfileActivity.class));
    }
}
