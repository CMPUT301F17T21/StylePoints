package com.stylepoints.habittracker.repository.remote;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by mchauck on 11/23/17.
 * https://stackoverflow.com/questions/23070298/get-nested-json-object-with-gson-using-retrofit
 */

public class ElasticResponseDeserializer<T extends Id> implements JsonDeserializer<T> {
    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement source = json.getAsJsonObject().get("_source");
        T deserialized = new Gson().fromJson(source, typeOfT);
        // deserialized.setId(json.getAsJsonObject().get("_id").getAsString());
        return deserialized;
    }
}
