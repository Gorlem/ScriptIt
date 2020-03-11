package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.util.ObjectConverter;
import com.google.gson.Gson;
import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import net.minecraft.client.MinecraftClient;

public class JsonLibrary implements LibraryInitializer {
    private Gson gson = new Gson();

    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry library = registry.registerLibrary("json");

        library.registerFunction("encode", this::encode);
        library.registerFunction("decode", this::decode);
    }

    private Object encode(String name, MinecraftClient minecraft, Object... arguments) {
        return gson.toJson(arguments[0]);
    }

    private Object decode(String name, MinecraftClient minecraft, Object... arguments) {
        return gson.fromJson(ObjectConverter.toString(arguments[0]), Object.class);
    }
}
