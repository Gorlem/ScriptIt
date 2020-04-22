package com.ddoerr.scriptit.commands;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.registry.ExtensionManager;
import com.ddoerr.scriptit.api.scripts.LifeCycle;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.util.Named;
import com.mojang.brigadier.CommandDispatcher;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandSource;
import net.minecraft.text.LiteralText;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static io.github.cottonmc.clientcommands.ArgumentBuilders.argument;
import static io.github.cottonmc.clientcommands.ArgumentBuilders.literal;

public class ScriptItCommand implements ClientCommandPlugin {
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        try {
            ExtensionManager extensionManager = Resolver.getInstance().resolve(ExtensionManager.class);

            List<String> languageNames = extensionManager.getAll(Language.class).stream().map(Named::getName).collect(Collectors.toList());
            List<String> lifeCycles = Arrays.stream(LifeCycle.values()).map(Enum::name).collect(Collectors.toList());

            dispatcher.register(literal("scriptit")
                    .then(literal("run")
                            .then(argument("language", word())
                                    .suggests((ctx, builder) -> CommandSource.suggestMatching(languageNames, builder))
                                    .then(argument("script", greedyString())
                                            .executes(ctx -> execute(ctx.getSource(),
                                                    getString(ctx, "language"),
                                                    "Instant",
                                                    getString(ctx, "script")))
                                    )
                                    .then(argument("lifeCycle", word())
                                            .suggests((ctx, builder) -> CommandSource.suggestMatching(lifeCycles, builder))
                                            .then(argument("script", greedyString())
                                                    .executes(ctx -> execute(ctx.getSource(),
                                                            getString(ctx, "language"),
                                                            getString(ctx, "lifeCycle"),
                                                            getString(ctx, "script")))
                                            )
                                    )
                            )
                    )
                    .then(literal("start")
                            .then(argument("file", word())
                                .executes(ctx -> execute(ctx.getSource(), getString(ctx, "file")))
                            )
                    )
            );
        } catch (DependencyException e) {
            e.printStackTrace();
        }
    }

    private int execute(CottonClientCommandSource ctx, String language, String lifeCycle, String script) {
        if (script.startsWith("\"") && script.endsWith("\"")) {
            script = script.substring(1, script.length() - 1);
        }
        try {
            new ScriptBuilder()
                    .language(language)
                    .fromString(script)
                    .lifeCycle(LifeCycle.valueOf(lifeCycle))
                    .run();
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText(e.getMessage()));
            e.printStackTrace();
        }

        return 1;
    }

    private int execute(CottonClientCommandSource ctx, String file) {
        try {
            new ScriptBuilder()
                    .fromFile(file)
                    .lifeCycle(LifeCycle.Threaded)
                    .run();
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText(e.getMessage()));
            e.printStackTrace();
        }
        return 1;
    }
}
