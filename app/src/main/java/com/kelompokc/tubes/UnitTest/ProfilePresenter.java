package com.kelompokc.tubes.UnitTest;

public class ProfilePresenter {
    private ProfileService service;
    private ProfileView view;
    public ProfilePresenter(ProfileView view, ProfileService service){
        this.view = view;
        this.service = service;
    }

    public void onEditClicked() {
        if (view.getNama().isEmpty()) {
            view.showNamaError("Nama tidak boleh kosong");
            return;
        } else {
            service.edit(view.getNama(), view.getFakultas(), view.getGender(), view.getId());
            return;
        }
    }
}
