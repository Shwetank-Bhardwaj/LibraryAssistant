package com.shwetank.libraryassistant.mvvm;

class ViewModelEvent {

    private String event;
    private Object data;
    private Status success;

    public ViewModelEvent(Object data, String event, Status success) {

        this.data = data;
        this.event = event;
        this.success = success;
    }

    public String getEvent() {
        return event;
    }

    public Object getData() {
        return data;
    }

    public Status getSuccess() {
        return success;
    }

    public enum Status {
        SUCCESS,
        FAILURE
    }

}
