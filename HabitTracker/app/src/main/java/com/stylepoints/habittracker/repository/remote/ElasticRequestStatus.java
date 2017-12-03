package com.stylepoints.habittracker.repository.remote;

/**
 * A class to hold the response of posting something to elastic search.
 * Fields are named weirdly so that they serialize properly with gson to
 * match exactly the JSON returned from elastic search.
 *
 * @author Mackenzie Hauck
 */
public class ElasticRequestStatus implements Id {
    private String _id;
    private String _type;
    private String _index;
    private long _version;
    private boolean created;

    @Override
    public void setElasticId(String id) {
        this._id = id;
    }

    public String getId() {
        return _id;
    }

    public String getType() {
        return _type;
    }

    public String getIndex() {
        return _index;
    }

    public long getVersion() {
        return _version;
    }

    public boolean wasCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "ElasticRequestStatus{" +
                "_id='" + _id + '\'' +
                ", _type='" + _type + '\'' +
                ", _index='" + _index + '\'' +
                ", _version=" + _version +
                ", created=" + created +
                '}';
    }
}
