package com.kelompokc.tubes.UnitTest;

import com.kelompokc.tubes.model.User;

public class ProfilePresenter
{
    private ProfileService service;
    private ProfileView view;

    public ProfilePresenter(ProfileView view, ProfileService service)
    {
        this.view = view;
        this.service = service;
    }

    public void onEditClicked()
    {
        if (view.getNama().isEmpty())
        {
            view.showNamaError("Nama tidak boleh kosong");
            return;
        }
        else
        {
            service.editData(view, view.getNama(), view.getFakultas(), view.getGender(), view.getId(), new ProfileCallback()
            {
                @Override
                public void onSuccess(boolean value)
                {
                    view.startProfileActivity();
                }

                @Override
                public void onError()
                {

                }
            });
            return;
        }
    }
}
