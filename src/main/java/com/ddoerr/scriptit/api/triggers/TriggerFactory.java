package com.ddoerr.scriptit.api.triggers;

import com.ddoerr.scriptit.fields.Field;
import spinnery.widget.WTabHolder;

import java.util.Map;

public interface TriggerFactory {
    Trigger createTrigger();

    default Trigger createTrigger(Map<String, String> data) {
        Trigger trigger = createTrigger();

        for (Map.Entry<String, Field<?>> entry : trigger.getFields().entrySet()) {
            if (data.containsKey(entry.getKey())) {
                entry.getValue().deserialize(data.get(entry.getKey()));
            }
        }

        return trigger;
    }
}
