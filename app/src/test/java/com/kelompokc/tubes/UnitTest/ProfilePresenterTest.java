package com.kelompokc.tubes.UnitTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProfilePresenterTest {
    @Mock
    private ProfileView view;
    @Mock
    private ProfileService service;
    private ProfilePresenter presenter;
    @Before
    public void setUp() throws Exception {
        presenter = new ProfilePresenter(view, service);
    }
    @Test
    public void shouldShowErrorMessageWhenNamaIsEmpty() throws Exception {
        when(view.getNama()).thenReturn("");
        System.out.println("nama : "+view.getNama());
        presenter.onEditClicked();
        verify(view).showNamaError("Nama tidak boleh kosong");
    }

    @Test
    public void shouldStartProfileActivityWhenNamaIsCorrect() throws Exception {
        when(view.getNama()).thenReturn(view.getNama());
        System.out.println("nama : "+view.getNama());
        presenter.onEditClicked();
    }
}