package com.stylepoints.habittracker.repository.remote;

/**
 * Objects that implement this class are able to be uploaded into Elastic Search.
 * Needed because the id used in elastic search is outside of the data we can pass
 * in the body, it must be set in the URL.
 */
public interface Id {
    public void setElasticId(String id);
}
