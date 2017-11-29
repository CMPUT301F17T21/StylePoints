package com.stylepoints.habittracker.repository.remote;

import com.stylepoints.habittracker.model.Habit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mackenzie on 2017-11-23.
 */

public class ElasticHabitListResponse {
    private int took;
    private boolean timed_out;
    private Hits hits;

    class Hits {
        private int total;
        private float max_score;
        List<ElasticResponse<Habit>> hits;

        @Override
        public String toString() {
            return "Hits{" +
                    "total=" + total +
                    ", max_score=" + max_score +
                    ", hits=" + hits +
                    '}';
        }
    }

    public List<Habit> getList() {
        List<Habit> list = new ArrayList<>();
        for (ElasticResponse<Habit> hit : this.hits.hits) {
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
