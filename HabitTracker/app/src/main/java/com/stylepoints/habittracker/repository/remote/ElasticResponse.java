package com.stylepoints.habittracker.repository.remote;

import java.util.Map;

/**
 * Created by mchauck on 11/23/17.
 */

public class ElasticResponse<T extends Id> {
    private String _index;
    private String _type;
    private String _id;
    //private long _version;
    private boolean found;

    // T is our object type like Habit, or HabitEvent, Tweet, etc.
    private T _source;

    // TODO: custom error
    public T getSource() throws NoSuchFieldError {
        /*
        if (!found) {
            throw new NoSuchFieldError();
        }*/
        //_source.setId(_id);
        return _source;
    }

    @Override
    public String toString() {
        return "ElasticResponse{" +
                "_index='" + _index + '\'' +
                ", _type='" + _type + '\'' +
                ", _id='" + _id + '\'' +
                //", _version=" + _version +
                ", found=" + found +
                ", _source=" + _source +
                '}';
    }
}
