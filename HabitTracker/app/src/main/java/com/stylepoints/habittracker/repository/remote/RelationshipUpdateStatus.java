package com.stylepoints.habittracker.repository.remote;

/**
 * Created by nikosomos on 2017-12-04.
 */

public class RelationshipUpdateStatus {

    private Document doc;

    public RelationshipUpdateStatus(int status) {
        this.doc = new Document(status);
    }

    class Document {
        private int status;
        public Document(int status) {
            this.status = status;
        }
    }

}
