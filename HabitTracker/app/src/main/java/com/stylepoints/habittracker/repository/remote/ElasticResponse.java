package com.stylepoints.habittracker.repository.remote;


/**
 * The response from Elastic Search when doing one of the following:
 * create, update, delete.
 *
 * The fields are named this way so that gson can automatically deserialize the json
 * resposne.
 * @author Mackenzie Hauck
 * @param <T> The object the request relates to. E.g., deleting a Habit, use Habit
 */
public class ElasticResponse<T extends Id> {
    private String _index;
    private String _type;
    private String _id;
    private long _version;
    private boolean found;

    // T is our object type like Habit, or HabitEvent, Tweet, etc.
    private T _source;

    // TODO: custom error
    public T getSource() {
        _source.setElasticId(_id);
        return _source;
    }

    @Override
    public String toString() {
        return "ElasticResponse{" +
                "_index='" + _index + '\'' +
                ", _type='" + _type + '\'' +
                ", _id='" + _id + '\'' +
                ", _version=" + _version +
                ", found=" + found +
                ", _source=" + _source +
                '}';
    }

    public boolean wasFound() {
        return this.found;
    }
}
