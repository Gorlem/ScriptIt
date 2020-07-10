package com.ddoerr.scriptit.config;

import com.ddoerr.scriptit.ScriptItMod;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.hud.HudElementContainer;
import com.ddoerr.scriptit.api.scripts.ScriptContainer;
import com.ddoerr.scriptit.api.triggers.Trigger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Config {
    public static final String CONFIG_FILE = "config.json";

    private Gson gson;

    public Config(Resolver resolver) {
        GsonBuilder gsonBuilder = new GsonBuilder();

        try {
            gsonBuilder.registerTypeAdapter(ScriptContainer.class, resolver.create(ScriptContainerAdapter.class));
            gsonBuilder.registerTypeAdapter(HudElementContainer.class, resolver.create(HudElementAdapter.class));
            gsonBuilder.registerTypeAdapter(Trigger.class, resolver.create(TriggerAdapter.class));
        } catch (DependencyException e) {
            e.printStackTrace();
        }

        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }

    public void write(ConfigContainer configContainer) {
        File file = getConfigFilePath().toFile();

        try {
            FileWriter writer = new FileWriter(file, false);
            gson.toJson(configContainer, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigContainer read() {
        File file = getConfigFilePath().toFile();
        ConfigContainer container = null;

        try {
            FileReader reader = new FileReader(file);
            container = gson.fromJson(reader, ConfigContainer.class);
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return container;
    }

    private Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDirectory().toPath().resolve(ScriptItMod.MOD_NAME);
    }

    private Path getConfigFilePath() {
        return getConfigPath().resolve(CONFIG_FILE);
    }

    public void ensureConfigExists() {
        getConfigPath().toFile().mkdirs();

        if (!getConfigFilePath().toFile().exists()) {
            write(new ConfigContainer());
        }
    }
}
