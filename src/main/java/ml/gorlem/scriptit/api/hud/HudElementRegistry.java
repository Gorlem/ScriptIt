package ml.gorlem.scriptit.api.hud;

public interface HudElementRegistry {
    void registerFactory(Class<? extends HudElement> hudElementClass, HudElementFactory factory);
}
