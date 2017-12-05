package com.stylepoints.habittracker.repository.remote;

/**
 * Created by nikosomos on 2017-12-04.
 */

public class RelationshipUpdateStatus {

    private Document doc;

    public RelationshipUpdateStatus(String status) {
        this.doc = new Document(status);
    }

    class Document {
        private String status;
        public Document(String status) {
            this.status = status;
        }
    }

}
