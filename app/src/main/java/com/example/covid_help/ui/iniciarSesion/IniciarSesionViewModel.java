package com.example.covid_help.ui.iniciarSesion;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class IniciarSesionViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public IniciarSesionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
