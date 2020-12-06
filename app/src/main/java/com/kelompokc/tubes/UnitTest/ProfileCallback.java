package com.kelompokc.tubes.UnitTest;

import com.kelompokc.tubes.model.User;

public interface ProfileCallback {
    void onSuccess(boolean value, User user);
    void onError();
}
