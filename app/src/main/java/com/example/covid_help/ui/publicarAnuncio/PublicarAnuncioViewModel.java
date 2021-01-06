package com.example.covid_help.ui.publicarAnuncio;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PublicarAnuncioViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public PublicarAnuncioViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
