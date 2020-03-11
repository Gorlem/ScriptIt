package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.util.ObjectConverter;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScoreboardLibrary implements LibraryInitializer {
    @Override
    public void onInitialize(LibraryRegistry registry) {
        NamespaceRegistry scoreboard = registry.registerLibrary("scoreboard");

        scoreboard.registerVariable("objectives", (name, mc) -> Optional.ofNullable(mc.world)
                .map(w -> w.getScoreboard().getObjectives().stream().map(ObjectConverter::convert).collect(Collectors.toList()))
                .orElse(Collections.emptyList()));
        scoreboard.registerVariable("teams", (name, mc) -> Optional.ofNullable(mc.world)
                .map(w -> w.getScoreboard().getTeams().stream().map(ObjectConverter::convert).collect(Collectors.toList()))
                .orElse(Collections.emptyList()));
    }
}
