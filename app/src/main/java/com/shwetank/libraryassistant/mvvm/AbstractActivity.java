package com.shwetank.libraryassistant.mvvm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public abstract class AbstractActivity<T extends AbstractViewModel> extends AppCompatActivity {

    protected T viewModel;
    private Class<T> aClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aClass = getViewModel();
        registerEvents();
        doOnCreate(savedInstanceState);
    }

    protected abstract void registerEvents();

    protected abstract void doOnCreate(@Nullable Bundle savedInstanceState);

    protected abstract @Nullable
    Class<T> getViewModel();

    protected void onDataChanged(ViewModelEvent viewModelEvent) {
    }

    protected final void registerEvent(String eventName) {
        if (aClass == null) {
            throw new IllegalStateException("ViewModel not provided in getViewModel()");
        }

        viewModel = ViewModelProviders.of(AbstractActivity.this).get(aClass);
        //noinspection unchecked
        viewModel.registerEvent(eventName).observe(this, new Observer<ViewModelEvent>() {
            @Override
            public void onChanged(@Nullable ViewModelEvent viewModelEvent) {
                onDataChanged(viewModelEvent);
            }
        });
    }
}