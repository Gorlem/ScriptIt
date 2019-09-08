package ml.gorlem.scriptit.api.languages;

public interface LanguageRegistry {
    void registerLanguage(String name, LanguageImplementation languageImplementation);
}
