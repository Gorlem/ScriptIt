package com.ddoerr.scriptit.extension.libraries;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class JsonLibrary extends AnnotationBasedModel {
    private Gson gson = new Gson();

    @Callable
    public String encode(Map<?, ?> map) {
        return gson.toJson(map);
    }

    @Callable
    public String encode(List<?> list) {
        return gson.toJson(list);
    }

    @Callable
    public Object decode(String json) {
        return gson.fromJson(json, Object.class);
    }
}
