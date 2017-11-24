package com.stylepoints.habittracker.repository.remote;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Generics apparently erase type when run so we have to make separate classes
 * for every thing that can be searched. A great application for generics but
 * java is dumb.
 */
public class ElasticTweetResponse {
    private int took;
    private boolean timed_out;
    private Hits hits;

    class Hits {
        private int total;
        private float max_score;
        List<ElasticResponse<Tweet>> hits;

        @Override
        public String toString() {
            return "Hits{" +
                    "total=" + total +
                    ", max_score=" + max_score +
                    ", hits=" + hits +
                    '}';
        }
    }

    public List<Tweet> getList() {
        List<Tweet> list = new ArrayList<>();
        for (ElasticResponse<Tweet> hit : this.hits.hits) {
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
}
