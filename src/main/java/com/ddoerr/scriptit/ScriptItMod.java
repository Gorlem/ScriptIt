package com.ddoerr.scriptit;

import com.ddoerr.scriptit.api.bus.EventBus;
import com.ddoerr.scriptit.api.bus.KeyBindingBusExtension;
import com.ddoerr.scriptit.api.dependencies.LanguageLoader;
import com.ddoerr.scriptit.api.dependencies.LibraryLoader;
import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.api.exceptions.DependencyException;
import com.ddoerr.scriptit.api.hud.HudElementManager;
import com.ddoerr.scriptit.api.languages.Language;
import com.ddoerr.scriptit.api.libraries.Library;
import com.ddoerr.scriptit.config.ConfigHandler;
import com.ddoerr.scriptit.elements.HudElementManagerImpl;
import com.ddoerr.scriptit.loader.EventLoaderImpl;
import com.ddoerr.scriptit.loader.HudElementLoaderImpl;
import com.ddoerr.scriptit.loader.LanguageLoaderImpl;
import com.ddoerr.scriptit.loader.LibraryLoaderImpl;
import com.ddoerr.scriptit.screens.ScreenHistory;
import com.ddoerr.scriptit.screens.ScriptOverviewScreen;
import com.ddoerr.scriptit.scripts.ScriptManagerImpl;
import com.ddoerr.scriptit.scripts.ThreadLifetimeManagerImpl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;

public class ScriptItMod implements ClientModInitializer {
	public static final String MOD_NAME = "scriptit";

	@Override
	public void onInitializeClient() {
		Resolver resolver = Resolver.getInstance();

		try {
			resolver.add(resolver);
			resolver.add(MinecraftClient.getInstance());
			resolver.add(ScreenHistory.class);
			resolver.add(new EventBus());
			resolver.add(KeyBindingBusExtension.class);
			resolver.add(new ThreadLifetimeManagerImpl());
			resolver.add(new LibraryLoaderImpl());
			resolver.add(new LanguageLoaderImpl());
			resolver.add(new HudElementLoaderImpl());
			resolver.add(new HudElementManagerImpl());
			resolver.add(new EventLoaderImpl());
			resolver.add(new ScriptManagerImpl());

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

		Collection<Loadable> loadables = resolver.resolveAll(Loadable.class);
		for (Loadable loadable : loadables) {
			loadable.load();
		}

		try {
			LanguageLoader languageLoader = resolver.resolve(LanguageLoader.class);
			LibraryLoader libraryLoader = resolver.resolve(LibraryLoader.class);

			for (Language language : languageLoader.getLanguages()) {
				for (Library library : libraryLoader.getLibraries()) {
					language.loadLibrary(library);
				}
			}
		} catch (DependencyException e) {
			e.printStackTrace();
		}


		try {
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

		try {
			HudElementManager hudElementManager = resolver.resolve(HudElementManager.class);
			HudRenderCallback.EVENT.register(delta -> hudElementManager.renderAll());
		} catch (DependencyException e) {
			e.printStackTrace();
		}
	}
}
