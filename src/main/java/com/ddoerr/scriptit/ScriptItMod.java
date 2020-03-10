package com.ddoerr.scriptit;

import com.ddoerr.scriptit.api.languages.LanguageImplementation;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.bus.EventBus;
import com.ddoerr.scriptit.api.bus.KeyBindingBusExtension;
import com.ddoerr.scriptit.config.ConfigHandler;
import com.ddoerr.scriptit.api.dependencies.Loadable;
import com.ddoerr.scriptit.api.dependencies.Resolver;
import com.ddoerr.scriptit.elements.HudElementManager;
import com.ddoerr.scriptit.loader.EventLoader;
import com.ddoerr.scriptit.loader.HudElementLoader;
import com.ddoerr.scriptit.loader.LanguageLoader;
import com.ddoerr.scriptit.loader.LibraryLoader;
import com.ddoerr.scriptit.screens.ScreenHistory;
import com.ddoerr.scriptit.screens.ScriptOverviewScreen;
import com.ddoerr.scriptit.scripts.Scripts;
import com.ddoerr.scriptit.scripts.ThreadLifetimeManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
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

		resolver.add(new ScreenHistory());
		resolver.add(new EventBus());
		resolver.add(new KeyBindingBusExtension());
		resolver.add(new ThreadLifetimeManager());
		resolver.add(new LibraryLoader());
		resolver.add(new LanguageLoader());
		resolver.add(new HudElementLoader());
		resolver.add(new HudElementManager());
		resolver.add(new EventLoader());
		resolver.add(new Scripts());

		resolver.add(new ConfigHandler());


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

		LanguageLoader languageLoader = resolver.resolve(LanguageLoader.class);
		LibraryLoader libraryLoader = resolver.resolve(LibraryLoader.class);

		for (LanguageImplementation language : languageLoader.getLanguages()) {
			for (NamespaceRegistry library : libraryLoader.getLibraries()) {
				language.loadRegistry(library);
			}
		}

		Collection<Tickable> tickables = resolver.resolveAll(Tickable.class);
		ScreenHistory history = resolver.resolve(ScreenHistory.class);

		ClientTickCallback.EVENT.register(mc -> {
			if (openGuiKeyBinding.wasPressed()) {
				history.open(ScriptOverviewScreen::new);
			}

			if (mc.player != null) {
				for (Tickable tickable : tickables) {
					tickable.tick();
				}
			}
		});

		HudElementManager hudElementManager = Resolver.getInstance().resolve(HudElementManager.class);
		HudRenderCallback.EVENT.register(delta -> hudElementManager.renderAll(0, 0, 0));
	}
}
