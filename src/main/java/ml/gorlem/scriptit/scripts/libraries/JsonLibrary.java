package ml.gorlem.scriptit.scripts.libraries;

import com.google.gson.Gson;
import ml.gorlem.scriptit.api.libraries.LibraryInitializer;
import ml.gorlem.scriptit.api.libraries.LibraryRegistry;
import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Tickable;

import java.util.Map;

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
        return gson.fromJson((String)arguments[0], Object.class);
    }
}
