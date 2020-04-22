package com.ddoerr.scriptit.api.registry;

import com.ddoerr.scriptit.api.util.Named;

import java.util.List;

public interface ExtensionManager {
    <T> T findByName(Class<T> registryType, String name);
    <T> List<Named<T>> getAll(Class<T> registryType);
}
