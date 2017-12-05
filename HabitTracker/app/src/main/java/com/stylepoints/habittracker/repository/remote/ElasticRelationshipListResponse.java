package com.stylepoints.habittracker.repository.remote;

import com.stylepoints.habittracker.model.Habit;
import com.stylepoints.habittracker.model.Relationship;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikosomos on 2017-12-03.
 */

public class ElasticRelationshipListResponse {
    private int took;
    private boolean timed_out;
    private ElasticRelationshipListResponse.Hits hits;

    class Hits {
        private int total;
        private float max_score;
        List<ElasticResponse<Relationship>> hits;

        @Override
        public String toString() {
            return "Hits{" +
                    "total=" + total +
                    ", max_score=" + max_score +
                    ", hits=" + hits +
                    '}';
        }
    }

    public List<Relationship> getList() {
        List<Relationship> list = new ArrayList<>();
        for (ElasticResponse<Relationship> hit : this.hits.hits) {
            list.add(hit.getSource());
        }
        return list;
    }

    public int getNumHits() {
        return hits.total;
    }

    @Override
    public String toString() {
        return "ElasticQueryResponse{" +
                "took=" + took +
                ", timed_out=" + timed_out +
                ", hits=" + hits +
                '}';
    }

    public Boolean wasFound(){
        return wasFound();
    }

}
