package com.ddoerr.scriptit.api.triggers.tabs;

import spinnery.widget.WTabHolder;

import java.util.Map;

public interface TriggerTabFactory {
    WTabHolder.WTab createTriggerTab(WTabHolder tabHolder, Map<String, String> triggerData);
}
