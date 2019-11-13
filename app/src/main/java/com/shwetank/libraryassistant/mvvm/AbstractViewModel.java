package com.shwetank.libraryassistant.mvvm;

import android.util.Log;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public abstract class AbstractViewModel<T extends AbstractModel> extends ViewModel implements Observer {

    private final String TAG = getClass().getSimpleName();

    protected final T model;
    private final HashMap<String, MutableLiveData<ViewModelEvent>> liveDataMap = new HashMap<>();

    public AbstractViewModel() {
        model = getModel();
        if (model != null) {
            model.addObserver(this);
        }
    }

    final LiveData<ViewModelEvent> registerEvent(String eventName) {
        MutableLiveData<ViewModelEvent> mutableLiveData = new MutableLiveData<>();
        liveDataMap.put(eventName, mutableLiveData);
        return mutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (model != null) {
            model.deleteObserver(this);
            model.onCleared();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        ViewModelEvent viewModelEvent = (ViewModelEvent) arg;
        try {
            String eventType = viewModelEvent.getEvent();
            MutableLiveData mutableLiveData = liveDataMap.get(eventType);
            if (mutableLiveData != null) {
                //noinspection unchecked
                mutableLiveData.postValue(viewModelEvent);
                Log.d(TAG, "update, eventType:" + eventType);
            }
        } catch (Exception e) {
            Log.e("Update VM", String.valueOf(e.getMessage()));
        }
    }

    /**
     * Called by the framework. Do not call it directly.
     *
     * @return the model class for this ViewModel
     */
    protected abstract T getModel();
}