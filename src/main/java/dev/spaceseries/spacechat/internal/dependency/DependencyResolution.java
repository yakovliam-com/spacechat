package dev.spaceseries.spacechat.internal.dependency;

import org.jetbrains.annotations.NotNull;

/**
 * A dependency resolution used for handling the types of dependencies MinecraftMediaLibrary uses.
 * It includes the base URLs of certain repositories as well.
 */
public enum DependencyResolution {
    MAVEN_DEPENDENCY("https://repo1.maven.org/maven2/"),
    JITPACK_DEPENDENCY("https://jitpack.io/"),
    YAKO_DEPENDENCY("https://repo.yakovliam.com/releases/");

    private final String baseUrl;

    /**
     * Declares a Dependency Resolution from the URL.
     *
     * @param url repository url
     */
    DependencyResolution(@NotNull final String url) {
        baseUrl = url;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}