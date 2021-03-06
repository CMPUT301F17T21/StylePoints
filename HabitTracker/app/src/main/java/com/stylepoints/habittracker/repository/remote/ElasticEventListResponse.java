package com.stylepoints.habittracker.repository.remote;

import com.stylepoints.habittracker.model.HabitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper class to hold the server response when we request a list of HabitEvents.
 * The fields, and class hierarchy are organized so that gson can deserialize the
 * json response directly.
 *
 * @author Mackenzie Hauck
 */
public class ElasticEventListResponse {
    private int took;
    private boolean timed_out;
    private Hits hits;

    class Hits {
        private int total;
        private float max_score;
        List<ElasticResponse<HabitEvent>> hits;

        @Override
        public String toString() {
            return "Hits{" +
                    "total=" + total +
                    ", max_score=" + max_score +
                    ", hits=" + hits +
                    '}';
        }
    }

    public List<HabitEvent> getList() {
        List<HabitEvent> list = new ArrayList<>();
        for (ElasticResponse<HabitEvent> hit : this.hits.hits) {
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