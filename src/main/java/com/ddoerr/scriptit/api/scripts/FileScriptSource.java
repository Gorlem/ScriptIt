package com.ddoerr.scriptit.api.scripts;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileScriptSource implements ScriptSource {
    File file;

    public FileScriptSource(File file) {
        this.file = file;
    }

    public String getFilePath() {
        return file.getPath();
    }

    @Override
    public String getContent() {
        try {
            return FileUtils.readFileToString(file, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
