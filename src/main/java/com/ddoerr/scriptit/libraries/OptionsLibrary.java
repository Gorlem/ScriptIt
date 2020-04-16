package com.ddoerr.scriptit.libraries;

import com.ddoerr.scriptit.api.languages.ContainedResultFactory;
import com.ddoerr.scriptit.api.libraries.AnnotationBasedModel;
import com.ddoerr.scriptit.libraries.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.*;
import net.minecraft.client.tutorial.TutorialStep;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.util.Arm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OptionsLibrary extends AnnotationBasedModel {
    private List<SettingModel<?>> settings = new ArrayList<>();
    private MinecraftClient minecraft;

    public OptionsLibrary(MinecraftClient minecraft) {
        this.minecraft = minecraft;
        addSettings();
    }

    private void addSettings() {
        // TODO: ResourcePacks

        settings.add(IntegerSettingModel.fromOption(Option.FOV));
        settings.add(IntegerSettingModel.fromOption(Option.MIPMAP_LEVELS));
        settings.add(IntegerSettingModel.fromOption(Option.RENDER_DISTANCE));

        settings.add(DoubleSettingModel.fromOption(Option.CHAT_WIDTH, "chatWidth"));
        settings.add(DoubleSettingModel.fromOption(Option.CHAT_HEIGHT_FOCUSED, "chatHeightFocused"));
        settings.add(DoubleSettingModel.fromOption(Option.SATURATION, "chatHeightUnfocused"));
        settings.add(DoubleSettingModel.fromOption(Option.CHAT_OPACITY, "chatOpacity"));
        settings.add(DoubleSettingModel.fromOption(Option.CHAT_SCALE, "chatScale"));
        settings.add(DoubleSettingModel.fromOption(Option.FRAMERATE_LIMIT));
        settings.add(DoubleSettingModel.fromOption(Option.MOUSE_WHEEL_SENSITIVITY));
        settings.add(DoubleSettingModel.fromOption(Option.SENSITIVITY));
        settings.add(DoubleSettingModel.fromOption(Option.TEXT_BACKGROUND_OPACITY, "textBackgroundOpacity"));
        settings.add(DoubleSettingModel.fromOption(Option.GAMMA));

        settings.add(BooleanSettingModel.fromOption(Option.AUTO_JUMP));
        settings.add(BooleanSettingModel.fromOption(Option.AUTO_SUGGESTIONS));
        settings.add(BooleanSettingModel.fromOption(Option.CHAT_COLOR, "chatColor"));
        settings.add(BooleanSettingModel.fromOption(Option.CHAT_LINKS, "chatLinks"));
        settings.add(BooleanSettingModel.fromOption(Option.CHAT_LINKS_PROMPT, "chatLinksPrompt"));
        settings.add(BooleanSettingModel.fromOption(Option.DISCRETE_MOUSE_SCROLL, "discreteMouseScroll"));
        settings.add(BooleanSettingModel.fromOption(Option.VSYNC));
        settings.add(BooleanSettingModel.fromOption(Option.ENTITY_SHADOWS));
        settings.add(BooleanSettingModel.fromOption(Option.FORCE_UNICODE_FONT));
        settings.add(BooleanSettingModel.fromOption(Option.REALMS_NOTIFICATIONS));
        settings.add(BooleanSettingModel.fromOption(Option.REDUCED_DEBUG_INFO));
        settings.add(BooleanSettingModel.fromOption(Option.SUBTITLES));
        settings.add(BooleanSettingModel.fromOption(Option.SNOOPER));
        settings.add(BooleanSettingModel.fromOption(Option.TOUCHSCREEN));
        settings.add(BooleanSettingModel.fromOption(Option.FULLSCREEN));
        settings.add(BooleanSettingModel.fromOption(Option.VIEW_BOBBING));
        settings.add(BooleanSettingModel.fromOption(Option.RAW_MOUSE_INPUT));
        settings.add(BooleanSettingModel.fromOption(Option.INVERT_MOUSE));

        settings.add(new EnumSettingModel<>("ao", AoOption.class, () -> minecraft.options.ao, ao -> {
            minecraft.options.ao = ao;
            minecraft.worldRenderer.reload();
        }));
        settings.add(new EnumSettingModel<>("attackIndicator", AttackIndicator.class, () -> minecraft.options.attackIndicator, attackIndicator -> minecraft.options.attackIndicator = attackIndicator));
        settings.add(new EnumSettingModel<>("chatVisibility", ChatVisibility.class, () -> minecraft.options.chatVisibility, chatVisibility -> minecraft.options.chatVisibility = chatVisibility));
        settings.add(new EnumSettingModel<>("mainHand", Arm.class, () -> minecraft.options.mainArm, arm -> minecraft.options.mainArm = arm));
        settings.add(new EnumSettingModel<>("narrator", NarratorOption.class, () -> minecraft.options.narrator, narrator -> {
            if (NarratorManager.INSTANCE.isActive()) {
                minecraft.options.narrator = narrator;
            } else {
                minecraft.options.narrator = NarratorOption.OFF;
            }
            NarratorManager.INSTANCE.addToast(minecraft.options.narrator);
        }));
        settings.add(new EnumSettingModel<>("particles", ParticlesOption.class, () -> minecraft.options.particles, particles -> minecraft.options.particles = particles));
        settings.add(new EnumSettingModel<>("renderClouds", CloudRenderMode.class, () -> minecraft.options.cloudRenderMode, cloudRenderMode -> minecraft.options.cloudRenderMode = cloudRenderMode));
        settings.add(new EnumSettingModel<>("tutorialStep", TutorialStep.class, () -> minecraft.options.tutorialStep, tutorialStep -> minecraft.options.tutorialStep = tutorialStep));

        settings.add(new BooleanSettingModel("graphics", () -> minecraft.options.fancyGraphics, fancyGraphics -> {
            minecraft.options.fancyGraphics = fancyGraphics;
            minecraft.worldRenderer.reload();
        }));
        settings.add(new BooleanSettingModel("textBackground", () -> minecraft.options.backgroundForChatOnly, background -> minecraft.options.backgroundForChatOnly = background));
        settings.add(new BooleanSettingModel("toggleCrouch", () -> minecraft.options.sneakToggled, sneak -> minecraft.options.sneakToggled = sneak));
        settings.add(new BooleanSettingModel("toggleSprint", () -> minecraft.options.sprintToggled, sprint -> minecraft.options.sprintToggled = sprint));
        settings.add(new BooleanSettingModel("hideServerAddress", () -> minecraft.options.hideServerAddress, hide -> minecraft.options.hideServerAddress = hide));
        settings.add(new BooleanSettingModel("advancedItemTooltips", () -> minecraft.options.advancedItemTooltips, advanced -> minecraft.options.advancedItemTooltips = advanced));
        settings.add(new BooleanSettingModel("pauseOnLostFocus", () -> minecraft.options.pauseOnLostFocus, pause -> minecraft.options.pauseOnLostFocus = pause));
        settings.add(new BooleanSettingModel("heldItemTooltips", () -> minecraft.options.heldItemTooltips, tooltips -> minecraft.options.heldItemTooltips = tooltips));
        settings.add(new BooleanSettingModel("skipMultiplayerWarning", () -> minecraft.options.skipMultiplayerWarning, skip -> minecraft.options.skipMultiplayerWarning = skip));

        settings.add(new IntegerSettingModel("guiScale", () -> minecraft.options.guiScale, guiScale -> {
            minecraft.options.guiScale = guiScale;
            minecraft.onResolutionChanged();
        }, 0, 2));

        settings.add(new StringSettingModel("lastServer", () -> minecraft.options.lastServer, lastServer -> minecraft.options.lastServer = lastServer));
        settings.add(new StringSettingModel("lang", () -> minecraft.options.language, language -> {
            minecraft.options.language = language;
            minecraft.getLanguageManager().setLanguage(minecraft.getLanguageManager().getLanguage(language));
            minecraft.reloadResources();
        }));
    }

    @Override
    public boolean hasGetter(String key) {
        return super.hasGetter(key) || settings.stream().anyMatch(setting -> setting.getName().equals(key));
    }

    @Override
    public <T> T runGetter(String key, ContainedResultFactory<T> factory) {
        Optional<SettingModel<?>> optionalSetting = settings.stream().filter(setting -> setting.getName().equals(key)).findFirst();
        if (optionalSetting.isPresent()) {
            return factory.fromModel(optionalSetting.get());
        }

        return super.runGetter(key, factory);
    }
}
