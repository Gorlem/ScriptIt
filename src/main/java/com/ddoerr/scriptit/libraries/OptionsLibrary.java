package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.libraries.LibraryInitializer;
import com.ddoerr.scriptit.api.libraries.LibraryRegistry;
import com.ddoerr.scriptit.api.libraries.NamespaceRegistry;
import com.ddoerr.scriptit.api.util.ObjectConverter;
import com.ddoerr.scriptit.libraries.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.*;
import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.tutorial.TutorialStep;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.util.Arm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OptionsLibrary implements LibraryInitializer {
    private List<Setting> settings = new ArrayList<>();
    private Setting noopSetting = new NoopSetting();
    private GameOptions options;
    private MinecraftClient minecraft = MinecraftClient.getInstance();

    private StringSetting languageSetting;

    @Override
    public void onInitialize(LibraryRegistry registry) {
        addSettings();

        NamespaceRegistry options = registry.registerLibrary("options");

        options.registerFunction("set", this::setOption);
        options.registerFunction("get", this::getOption);
        options.registerFunction("toggle", this::toggleOption);
        options.registerFunction("possible_values", this::getPossibleValues);

        options.registerVariable("keys", (name, mc) -> this.settings.stream().map(Setting::getName).collect(Collectors.toList()));
    }

    private void ensureOptionsNotNull() {
        if (options == null) {
            options = minecraft.options;
            languageSetting.setWhitelist(minecraft.getLanguageManager().getAllLanguages().stream().map(LanguageDefinition::getCode).collect(Collectors.toList()));
        }
    }

    private Object setOption(String name, MinecraftClient minecraft, Object... arguments) {
        ensureOptionsNotNull();
        findOption(ObjectConverter.toString(arguments[0])).set(arguments[1]);
        minecraft.options.write();
        return null;
    }

    private Object getOption(String name, MinecraftClient minecraft, Object... arguments) {
        ensureOptionsNotNull();
        return findOption(ObjectConverter.toString(arguments[0])).get();
    }

    private Object toggleOption(String name, MinecraftClient minecraft, Object... arguments) {
        ensureOptionsNotNull();
        Setting option = findOption(ObjectConverter.toString(arguments[0]));

        if (option instanceof ToggleableSetting) {
            Object newValue = ((ToggleableSetting) option).toggle();
            minecraft.options.write();
            return newValue;
        }

        return null;
    }

    private Object getPossibleValues(String name, MinecraftClient minecraft, Object... arguments) {
        ensureOptionsNotNull();
        Setting option = findOption(ObjectConverter.toString(arguments[0]));

        return option.getPossibleValues();
    }

    private Setting findOption(String name) {
        return settings.stream()
                .filter(o -> o.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(noopSetting);
    }

    private void addSettings() {
        // TODO: ResourcePacks

        settings.add(IntegerSetting.fromOption(Option.FOV));
        settings.add(IntegerSetting.fromOption(Option.MIPMAP_LEVELS));
        settings.add(IntegerSetting.fromOption(Option.RENDER_DISTANCE));

        settings.add(DoubleSetting.fromOption(Option.CHAT_WIDTH));
        settings.add(DoubleSetting.fromOption(Option.CHAT_HEIGHT_FOCUSED));
        settings.add(DoubleSetting.fromOption(Option.SATURATION));
        settings.add(DoubleSetting.fromOption(Option.CHAT_OPACITY));
        settings.add(DoubleSetting.fromOption(Option.CHAT_SCALE));
        settings.add(DoubleSetting.fromOption(Option.FRAMERATE_LIMIT));
        settings.add(DoubleSetting.fromOption(Option.MOUSE_WHEEL_SENSITIVITY));
        settings.add(DoubleSetting.fromOption(Option.SENSITIVITY));
        settings.add(DoubleSetting.fromOption(Option.TEXT_BACKGROUND_OPACITY));
        settings.add(DoubleSetting.fromOption(Option.GAMMA));

        settings.add(BooleanSetting.fromOption(Option.AUTO_JUMP));
        settings.add(BooleanSetting.fromOption(Option.AUTO_SUGGESTIONS));
        settings.add(BooleanSetting.fromOption(Option.CHAT_COLOR));
        settings.add(BooleanSetting.fromOption(Option.CHAT_LINKS));
        settings.add(BooleanSetting.fromOption(Option.CHAT_LINKS_PROMPT));
        settings.add(BooleanSetting.fromOption(Option.DISCRETE_MOUSE_SCROLL));
        settings.add(BooleanSetting.fromOption(Option.VSYNC));
        settings.add(BooleanSetting.fromOption(Option.ENTITY_SHADOWS));
        settings.add(BooleanSetting.fromOption(Option.FORCE_UNICODE_FONT));
        settings.add(BooleanSetting.fromOption(Option.REALMS_NOTIFICATIONS));
        settings.add(BooleanSetting.fromOption(Option.REDUCED_DEBUG_INFO));
        settings.add(BooleanSetting.fromOption(Option.SUBTITLES));
        settings.add(BooleanSetting.fromOption(Option.SNOOPER));
        settings.add(BooleanSetting.fromOption(Option.TOUCHSCREEN));
        settings.add(BooleanSetting.fromOption(Option.FULLSCREEN));
        settings.add(BooleanSetting.fromOption(Option.VIEW_BOBBING));
        settings.add(BooleanSetting.fromOption(Option.RAW_MOUSE_INPUT));
        settings.add(BooleanSetting.fromOption(Option.INVERT_MOUSE));

        settings.add(new EnumSetting<>("ao", AoOption.class, () -> options.ao, ao -> {
            options.ao = ao;
            minecraft.worldRenderer.reload();
        }));
        settings.add(new EnumSetting<>("attackIndicator", AttackIndicator.class, () -> options.attackIndicator, attackIndicator -> options.attackIndicator = attackIndicator));
        settings.add(new EnumSetting<>("chat.visibility", ChatVisibility.class, () -> options.chatVisibility, chatVisibility -> options.chatVisibility = chatVisibility));
        settings.add(new EnumSetting<>("mainHand", Arm.class, () -> options.mainArm, arm -> options.mainArm = arm));
        settings.add(new EnumSetting<>("narrator", NarratorOption.class, () -> options.narrator, narrator -> {
            if (NarratorManager.INSTANCE.isActive()) {
                options.narrator = narrator;
            } else {
                options.narrator = NarratorOption.OFF;
            }
            NarratorManager.INSTANCE.addToast(options.narrator);
        }));
        settings.add(new EnumSetting<>("particles", ParticlesOption.class, () -> options.particles, particles -> options.particles = particles));
        settings.add(new EnumSetting<>("renderClouds", CloudRenderMode.class, () -> options.cloudRenderMode, cloudRenderMode -> options.cloudRenderMode = cloudRenderMode));
        settings.add(new EnumSetting<>("tutorialStep", TutorialStep.class, () -> options.tutorialStep, tutorialStep -> options.tutorialStep = tutorialStep));

        settings.add(new BooleanSetting("graphics", () -> options.fancyGraphics, fancyGraphics -> {
            options.fancyGraphics = fancyGraphics;
            minecraft.worldRenderer.reload();
        }));
        settings.add(new BooleanSetting("accessibility.text_background", () -> options.backgroundForChatOnly, background -> options.backgroundForChatOnly = background));
        settings.add(new BooleanSetting("toggleCrouch", () -> options.sneakToggled, sneak -> options.sneakToggled = sneak));
        settings.add(new BooleanSetting("toggleSprint", () -> options.sprintToggled, sprint -> options.sprintToggled = sprint));
        settings.add(new BooleanSetting("hideServerAddress", () -> options.hideServerAddress, hide -> options.hideServerAddress = hide));
        settings.add(new BooleanSetting("advancedItemTooltips", () -> options.advancedItemTooltips, advanced -> options.advancedItemTooltips = advanced));
        settings.add(new BooleanSetting("pauseOnLostFocus", () -> options.pauseOnLostFocus, pause -> options.pauseOnLostFocus = pause));
        settings.add(new BooleanSetting("heldItemTooltips", () -> options.heldItemTooltips, tooltips -> options.heldItemTooltips = tooltips));
        settings.add(new BooleanSetting("skipMultiplayerWarning", () -> options.skipMultiplayerWarning, skip -> options.skipMultiplayerWarning = skip));

        settings.add(new IntegerSetting("guiScale", () -> options.guiScale, guiScale -> {
            options.guiScale = guiScale;
            minecraft.onResolutionChanged();
        }, 0, 2));

        settings.add(new StringSetting("lastServer", () -> options.lastServer, lastServer -> options.lastServer = lastServer));
        settings.add(languageSetting = new StringSetting("lang", () -> options.language, language -> {
            options.language = language;
            minecraft.getLanguageManager().setLanguage(minecraft.getLanguageManager().getLanguage(language));
            minecraft.reloadResources();
        }));
    }
}
