package com.ddoerr.scriptit.api.registry;

import java.util.List;
import java.util.function.Supplier;

public interface Registry {
    <T> void register(RegistrableType<T> type, String name, Class<? extends T> object);
    <T> void register(RegistrableType<T> type, String name, Supplier<? extends T> object);

    <T> T findByName(RegistrableType<T> type, String name);

    <T> List<T> getAll(RegistrableType<T> type);
}
