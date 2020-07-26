package com.ddoerr.scriptit.extension.lua;

import com.ddoerr.scriptit.ScriptItMod;
import net.fabricmc.loader.api.FabricLoader;
import org.luaj.vm2.lib.ResourceFinder;

import java.io.*;
import java.nio.file.Path;

public class ScriptResourceFinder implements ResourceFinder {
    Path scriptsPath;

    public ScriptResourceFinder() {
        scriptsPath = getScriptsPath();
    }

    private Path getScriptsPath() {
        return FabricLoader.getInstance().getConfigDirectory().toPath().resolve(ScriptItMod.MOD_NAME).resolve("scripts");
    }

    @Override
    public InputStream findResource(String resourceName) {
        try {
            File file = new File(scriptsPath.toFile(), resourceName);
            return new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
