package ml.gorlem.scriptit;

import ml.gorlem.scriptit.api.languages.LanguageImplementation;
import ml.gorlem.scriptit.api.libraries.NamespaceRegistry;
import ml.gorlem.scriptit.config.ConfigHandler;
import ml.gorlem.scriptit.dependencies.Loadable;
import ml.gorlem.scriptit.dependencies.Resolver;
import ml.gorlem.scriptit.elements.HudElementManager;
import ml.gorlem.scriptit.callbacks.RenderEntryListBackgroundCallback;
import ml.gorlem.scriptit.callbacks.RenderHotbarCallback;
import ml.gorlem.scriptit.callbacks.RenderInGameHudCallback;
import ml.gorlem.scriptit.events.EventManager;
import ml.gorlem.scriptit.loader.*;
import ml.gorlem.scriptit.screens.BindingScreen;
import ml.gorlem.scriptit.screens.WidgetDesignerScreen;
import ml.gorlem.scriptit.scripts.ScriptBindings;
import ml.gorlem.scriptit.scripts.ThreadLifetimeManager;
import ml.gorlem.scriptit.widgets.EventBindingsListWidget;
import ml.gorlem.scriptit.widgets.KeyBindingsListWidget;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import org.lwjgl.glfw.GLFW;

import java.util.Collection;

public class ScriptItMod implements ClientModInitializer {
	public static final String MOD_NAME = "scriptit";

	@Override
	public void onInitializeClient() {
		Resolver resolver = Resolver.getInstance();

		resolver.add(new ScriptBindings());
		resolver.add(new ThreadLifetimeManager());
		resolver.add(new HudElementManager());
		resolver.add(new LibraryLoader());
		resolver.add(new LanguageLoader());
		resolver.add(new HudElementLoader());
		resolver.add(new EventLoader());
		resolver.add(new EventManager());

		resolver.add(new ConfigHandler());

		MinecraftClient minecraft = MinecraftClient.getInstance();

		FabricKeyBinding openGuiKeyBinding = FabricKeyBinding.Builder.create(
				new Identifier(MOD_NAME, "open"),
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_K,
				"ScriptIt"
		).build();
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

		ClientTickCallback.EVENT.register(mc -> {
			if (openGuiKeyBinding.wasPressed()) {
				mc.openScreen(new BindingScreen());
			}

			if (mc.player != null) {
				for (Tickable tickable : tickables) {
					tickable.tick();
				}
			}
		});

		RenderHotbarCallback.SHOULD_RENDER.register(() -> minecraft.currentScreen instanceof WidgetDesignerScreen ? ActionResult.FAIL : ActionResult.PASS);
		RenderEntryListBackgroundCallback.SHOULD_RENDER.register((widget) -> widget instanceof KeyBindingsListWidget || widget instanceof EventBindingsListWidget ? ActionResult.FAIL : ActionResult.PASS);

		HudElementManager hudElementManager = Resolver.getInstance().resolve(HudElementManager.class);
		RenderInGameHudCallback.EVENT.register(hudElementManager::renderAll);
	}
}
