package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.annotations.Callable;
import com.ddoerr.scriptit.api.annotations.Getter;
import com.ddoerr.scriptit.api.languages.ContainedValue;
import com.ddoerr.scriptit.api.exceptions.ConversionException;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.libraries.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.*;
import net.minecraft.client.tutorial.TutorialStep;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.util.Arm;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OptionsLibrary extends AnnotationBasedModel {
    private List<Setting> settings = new ArrayList<>();
    private MinecraftClient minecraft;

    public OptionsLibrary(MinecraftClient minecraft) {
        this.minecraft = minecraft;
        addSettings();
    }

    private void addSettings() {
        // TODO: ResourcePacks

        settings.add(IntegerSetting.fromOption(Option.FOV));
        settings.add(IntegerSetting.fromOption(Option.MIPMAP_LEVELS));
        settings.add(IntegerSetting.fromOption(Option.RENDER_DISTANCE));

        settings.add(DoubleSetting.fromOption(Option.CHAT_WIDTH, "chatWidth"));
        settings.add(DoubleSetting.fromOption(Option.CHAT_HEIGHT_FOCUSED, "chatHeightFocused"));
        settings.add(DoubleSetting.fromOption(Option.SATURATION, "chatHeightUnfocused"));
        settings.add(DoubleSetting.fromOption(Option.CHAT_OPACITY, "chatOpacity"));
        settings.add(DoubleSetting.fromOption(Option.CHAT_SCALE, "chatScale"));
        settings.add(DoubleSetting.fromOption(Option.FRAMERATE_LIMIT));
        settings.add(DoubleSetting.fromOption(Option.MOUSE_WHEEL_SENSITIVITY));
        settings.add(DoubleSetting.fromOption(Option.SENSITIVITY));
        settings.add(DoubleSetting.fromOption(Option.TEXT_BACKGROUND_OPACITY, "textBackgroundOpacity"));
        settings.add(DoubleSetting.fromOption(Option.GAMMA));

        settings.add(BooleanSetting.fromOption(Option.AUTO_JUMP));
        settings.add(BooleanSetting.fromOption(Option.AUTO_SUGGESTIONS));
        settings.add(BooleanSetting.fromOption(Option.CHAT_COLOR, "chatColor"));
        settings.add(BooleanSetting.fromOption(Option.CHAT_LINKS, "chatLinks"));
        settings.add(BooleanSetting.fromOption(Option.CHAT_LINKS_PROMPT, "chatLinksPrompt"));
        settings.add(BooleanSetting.fromOption(Option.DISCRETE_MOUSE_SCROLL, "discreteMouseScroll"));
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

        settings.add(new EnumSetting<>("ao", AoOption.class, () -> minecraft.options.ao, ao -> {
            minecraft.options.ao = ao;
            minecraft.worldRenderer.reload();
        }));
        settings.add(new EnumSetting<>("attackIndicator", AttackIndicator.class, () -> minecraft.options.attackIndicator, attackIndicator -> minecraft.options.attackIndicator = attackIndicator));
        settings.add(new EnumSetting<>("chatVisibility", ChatVisibility.class, () -> minecraft.options.chatVisibility, chatVisibility -> minecraft.options.chatVisibility = chatVisibility));
        settings.add(new EnumSetting<>("mainHand", Arm.class, () -> minecraft.options.mainArm, arm -> minecraft.options.mainArm = arm));
        settings.add(new EnumSetting<>("narrator", NarratorOption.class, () -> minecraft.options.narrator, narrator -> {
            if (NarratorManager.INSTANCE.isActive()) {
                minecraft.options.narrator = narrator;
            } else {
                minecraft.options.narrator = NarratorOption.OFF;
            }
            NarratorManager.INSTANCE.addToast(minecraft.options.narrator);
        }));
        settings.add(new EnumSetting<>("particles", ParticlesOption.class, () -> minecraft.options.particles, particles -> minecraft.options.particles = particles));
        settings.add(new EnumSetting<>("renderClouds", CloudRenderMode.class, () -> minecraft.options.cloudRenderMode, cloudRenderMode -> minecraft.options.cloudRenderMode = cloudRenderMode));
        settings.add(new EnumSetting<>("tutorialStep", TutorialStep.class, () -> minecraft.options.tutorialStep, tutorialStep -> minecraft.options.tutorialStep = tutorialStep));

        settings.add(new BooleanSetting("graphics", () -> minecraft.options.fancyGraphics, fancyGraphics -> {
            minecraft.options.fancyGraphics = fancyGraphics;
            minecraft.worldRenderer.reload();
        }));
        settings.add(new BooleanSetting("textBackground", () -> minecraft.options.backgroundForChatOnly, background -> minecraft.options.backgroundForChatOnly = background));
        settings.add(new BooleanSetting("toggleCrouch", () -> minecraft.options.sneakToggled, sneak -> minecraft.options.sneakToggled = sneak));
        settings.add(new BooleanSetting("toggleSprint", () -> minecraft.options.sprintToggled, sprint -> minecraft.options.sprintToggled = sprint));
        settings.add(new BooleanSetting("hideServerAddress", () -> minecraft.options.hideServerAddress, hide -> minecraft.options.hideServerAddress = hide));
        settings.add(new BooleanSetting("advancedItemTooltips", () -> minecraft.options.advancedItemTooltips, advanced -> minecraft.options.advancedItemTooltips = advanced));
        settings.add(new BooleanSetting("pauseOnLostFocus", () -> minecraft.options.pauseOnLostFocus, pause -> minecraft.options.pauseOnLostFocus = pause));
        settings.add(new BooleanSetting("heldItemTooltips", () -> minecraft.options.heldItemTooltips, tooltips -> minecraft.options.heldItemTooltips = tooltips));
        settings.add(new BooleanSetting("skipMultiplayerWarning", () -> minecraft.options.skipMultiplayerWarning, skip -> minecraft.options.skipMultiplayerWarning = skip));

        settings.add(new IntegerSetting("guiScale", () -> minecraft.options.guiScale, guiScale -> {
            minecraft.options.guiScale = guiScale;
            minecraft.onResolutionChanged();
        }, 0, 2));

        settings.add(new StringSetting("lastServer", () -> minecraft.options.lastServer, lastServer -> minecraft.options.lastServer = lastServer));
        settings.add(new StringSetting("lang", () -> minecraft.options.language, language -> {
            minecraft.options.language = language;
            minecraft.getLanguageManager().setLanguage(minecraft.getLanguageManager().getLanguage(language));
            minecraft.reloadResources();
        }));
    }

    private Optional<Setting> getSetting(String key) {
        return settings.stream().filter(s -> s.getName().equalsIgnoreCase(key)).findFirst();
    }

    @Getter
    public List<String> getKeys() {
        return settings.stream().map(Setting::getName).collect(Collectors.toList());
    }

    @Callable
    public String possibleValues(String key) {
        return getSetting(key).map(Setting::getPossibleValues).orElse(StringUtils.EMPTY);
    }

    @Callable
    public Object toggle(String key) {
        Object result = getSetting(key)
                .filter(ToggleableSetting.class::isInstance)
                .map(ToggleableSetting.class::cast)
                .map(ToggleableSetting::toggle)
                .orElse(null);
        minecraft.options.write();
        return result;
    }

    @Callable
    public Object get(String key) {
        return getSetting(key)
                .map(Setting::get)
                .orElse(null);
    }

    @Callable
    public void set(String key, ContainedValue value) {
        Optional<Setting> optional = getSetting(key);
        if (optional.isPresent()) {
            try {
                Setting setting = optional.get();
                setting.set(value.to(setting.getType()));
                minecraft.options.write();
            } catch (ConversionException e) {
                e.printStackTrace();
            }
        }
    }
}
