package com.kelompokc.tubes.UnitTest;

public class ProfilePresenter {
    private ProfileActivity activity;
    private ProfileView view;
    public ProfilePresenter(ProfileView view){
        this.view = view;
    }

    public void onEditClicked() {
        if (view.getNama().isEmpty()) {
            view.showNamaError("Nama tidak boleh kosong");
            return;
        } else {
            activity.editUser(view.getNama(), view.getFakultas(), view.getGender(), view.getId());
            return;
        }
    }
}
