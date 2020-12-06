package com.kelompokc.tubes.UnitTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProfilePresenterTest
{
    @Mock
    private ProfileView view;
    @Mock
    private ProfileService service;
    private ProfilePresenter presenter;

    @Before
    public void setUp() throws Exception
    {
        presenter = new ProfilePresenter(view, service);
    }

    @Test
    public void whenNameisEmpty() throws Exception
    {
        System.out.println("Test 1 - Name is Empty");

        when(view.getNama()).thenReturn("");
        System.out.println("Nama    = "+view.getNama());

        presenter.onEditClicked();
        verify(view).showNamaError("Nama tidak boleh kosong");
    }

    @Test
    public void editDataUserSuccess() throws Exception
    {
        System.out.println("Test 2 - Edit Data Success");

        when(view.getId()).thenReturn(1);
        System.out.println("Id          = "+view.getId());

        when(view.getNama()).thenReturn("Test123");
        System.out.println("Nama        = "+view.getNama());

        when(view.getFakultas()).thenReturn("FTI");
        System.out.println("Fakultas    = "+view.getFakultas());

        when(view.getGender()).thenReturn("Pria");
        System.out.println("Gender      = "+view.getGender());

        when(service.getValid(view, view.getNama(), view.getFakultas(), view.getGender(), view.getId())).thenReturn(true);
        System.out.println("Hasil       = "+service.getValid(view, view.getNama(), view.getFakultas(), view.getGender(), view.getId()));
        presenter.onEditClicked();
    }
}