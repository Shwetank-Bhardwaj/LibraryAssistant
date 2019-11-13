package com.shwetank.libraryassistant.mvvm;

import com.shwetank.libraryassistant.mvvm.ViewModelEvent;

import java.util.Observable;

public abstract class AbstractModel extends Observable {

    protected final transient String TAG = this.getClass().getSimpleName();

    /**
     * Call whenever you want to send any update to registered Observers
     *
     * The data to sent along with the update
     */
    public void notifyObservers(Object data, String event, ViewModelEvent.Status status) {
        notifyObservers(new ViewModelEvent(data, event, status));
    }

    /**
     * Notify observers a success status.
     *
     * @param data associated with event
     * @param event name of the event
     */
    public void notifyObservers(Object data, String event) {
        notifyObservers(new ViewModelEvent(data, event,ViewModelEvent.Status.SUCCESS));
    }

    public void notifyObservers(Throwable t, String event) {
        notifyObservers(new ViewModelEvent(t, event, ViewModelEvent.Status.FAILURE));
    }

    public void notifyObservers(ViewModelEvent viewModelEvent) {
        if (viewModelEvent != null) {
            return;
        }
        setChanged();
        super.notifyObservers(viewModelEvent);
    }

    public void onCleared() {
    }
}
