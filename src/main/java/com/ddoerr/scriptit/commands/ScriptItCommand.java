package com.ddoerr.scriptit.commands;

import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.api.scripts.Script;
import com.ddoerr.scriptit.api.scripts.ScriptBuilder;
import com.ddoerr.scriptit.api.scripts.ScriptManager;
import com.mojang.brigadier.CommandDispatcher;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static io.github.cottonmc.clientcommands.ArgumentBuilders.argument;
import static io.github.cottonmc.clientcommands.ArgumentBuilders.literal;
import static net.minecraft.command.arguments.IdentifierArgumentType.identifier;

public class ScriptItCommand implements ClientCommandPlugin {
    private ScriptManager scriptManager;
    private ScriptItRegistry registry;

    public ScriptItCommand() {
        try {
            Resolver resolver = Resolver.getInstance();
            scriptManager = resolver.resolve(ScriptManager.class);
            registry = resolver.resolve(ScriptItRegistry.class);
        } catch (DependencyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        List<String> languageNames = registry.languages.getIds().stream().map(Identifier::toString).collect(Collectors.toList());

        dispatcher.register(literal("scriptit")
                .then(literal("run")
                        .then(argument("language", identifier())
                                .suggests((ctx, builder) -> CommandSource.suggestMatching(languageNames, builder))
                                .then(argument("script", greedyString())
                                        .executes(ctx -> execute(ctx.getSource(),
                                                ctx.getArgument("language", Identifier.class),
                                                getString(ctx, "script")))
                                )
                        )
                )
                .then(literal("start")
                        .then(argument("file", word())
                            .executes(ctx -> execute(ctx.getSource(), getString(ctx, "file")))
                        )
                )
        );
    }

    private int execute(CottonClientCommandSource ctx, Identifier language, String content) {
        if (content.startsWith("\"") && content.endsWith("\"")) {
            content = content.substring(1, content.length() - 1);
        }
        try {
            Script script = new ScriptBuilder()
                    .language(language.toString())
                    .fromString(content);
            scriptManager.runScript(script);
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText(e.getMessage()));
            e.printStackTrace();
        }

        return 1;
    }

    private int execute(CottonClientCommandSource ctx, String file) {
        try {
            Script script = new ScriptBuilder()
                    .fromFile(file);
            scriptManager.runScript(script);
        } catch (Exception e) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText(e.getMessage()));
            e.printStackTrace();
        }
        return 1;
    }
}
