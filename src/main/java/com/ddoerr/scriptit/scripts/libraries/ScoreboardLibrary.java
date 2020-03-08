package com.ddoerr.scriptit.scripts.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.util.ObjectConverter;

import java.util.stream.Collectors;

public class ScoreboardLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry scoreboard = registry.registerLibrary("scoreboard");

        scoreboard.registerVariable("objectives", (name, mc) -> mc.world.getScoreboard().getObjectives().stream().map(ObjectConverter::convert).collect(Collectors.toList()));
        scoreboard.registerVariable("teams", (name, mc) -> mc.world.getScoreboard().getTeams().stream().map(ObjectConverter::convert).collect(Collectors.toList()));
    }
}
