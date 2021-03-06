package com.ddoerr.scriptit;

import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.registry.ScriptItRegistry;
import com.ddoerr.scriptit.api.scripts.ScriptManager;
import com.ddoerr.scriptit.api.util.Color;
import com.ddoerr.scriptit.callbacks.LateInitCallback;
import com.ddoerr.scriptit.config.Config;
import com.ddoerr.scriptit.config.ConfigHandler;
import com.ddoerr.scriptit.elements.HudElementManagerImpl;
import com.ddoerr.scriptit.extensions.ExtensionLoader;
import com.ddoerr.scriptit.fields.FieldAssembler;
import com.ddoerr.scriptit.languages.LanguageManagerImpl;
import com.ddoerr.scriptit.screens.ScreenHistory;
import com.ddoerr.scriptit.screens.ScriptOverviewScreen;
import com.ddoerr.scriptit.scripts.ScriptContainerManagerImpl;
import com.ddoerr.scriptit.scripts.ScriptManagerImpl;
import com.ddoerr.scriptit.triggers.KeyBindingManagerImpl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;

public class ScriptItMod implements ClientModInitializer, LateInitCallback {
	public static final String MOD_NAME = "scriptit";
	private Resolver resolver;

	@Override
	public void onInitializeClient() {
		LateInitCallback.EVENT.register(this);

		resolver = Resolver.getInstance();

		try {
			resolver.add(resolver);
			resolver.add(MinecraftClient.getInstance());

			resolver.add(FieldAssembler.class);
			resolver.add(ScreenHistory.class);
			resolver.add(KeyBindingManagerImpl.class);
			resolver.add(ScriptItRegistry.class);
			resolver.add(ExtensionLoader.class);
			resolver.add(ScriptManagerImpl.class);
			resolver.add(ScriptContainerManagerImpl.class);
			resolver.add(LanguageManagerImpl.class);
			resolver.add(HudElementManagerImpl.class);

			resolver.add(Config.class);
			resolver.add(ConfigHandler.class);
		} catch (DependencyException e) {
			e.printStackTrace();
		}

		FabricKeyBinding openGuiKeyBinding = FabricKeyBinding.Builder.create(
				new Identifier(MOD_NAME, "open"),
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_K,
				"ScriptIt"
		).build();
		KeyBindingRegistry.INSTANCE.addCategory("ScriptIt");
		KeyBindingRegistry.INSTANCE.register(openGuiKeyBinding);

		try {
			Color.setScriptManager(resolver.resolve(ScriptManager.class));

			Collection<Tickable> tickables = resolver.resolveAll(Tickable.class);
			ScreenHistory history = resolver.resolve(ScreenHistory.class);

			ClientTickCallback.EVENT.register(mc -> {
				if (openGuiKeyBinding.wasPressed()) {
					history.open(ScriptOverviewScreen.class);
				}

				if (mc.player != null) {
					for (Tickable tickable : tickables) {
						tickable.tick();
					}
				}
			});
		} catch (DependencyException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLateInitialize(MinecraftClient minecraft) {
		Collection<Loadable> loadables = resolver.resolveAll(Loadable.class);
		for (Loadable loadable : loadables) {
			loadable.load();
		}
	}
}
