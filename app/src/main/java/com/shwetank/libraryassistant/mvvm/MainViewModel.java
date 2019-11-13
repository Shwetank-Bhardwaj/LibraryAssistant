package com.shwetank.libraryassistant.mvvm;

public class MainViewModel extends AbstractViewModel<MainModel> {

    @Override
    protected MainModel getModel() {
        return new MainModel();
    }
}
