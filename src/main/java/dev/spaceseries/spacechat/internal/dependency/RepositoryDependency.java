package dev.spaceseries.spacechat.internal.dependency;

import org.jetbrains.annotations.NotNull;

/**
 * This is an enum used to store the dependencies MinecraftMediaLibrary will use during the runtime.
 * Dependencies are specified using a group id, an artifact id, a version, and a specific resolution
 * to be chosen. Such a resolution would include examples such as Maven or Jitpack and other
 * solutions.
 */
public enum RepositoryDependency {

    /**
     * ASM Maven Dependency
     */
    ASM("org{}ow2{}asm", "asm", "9{}1", DependencyResolution.MAVEN_DEPENDENCY, false),

    /**
     * ASM Commons Maven Dependency
     */
    ASM_COMMONS("org{}ow2{}asm", "asm-commons", "9{}1", DependencyResolution.MAVEN_DEPENDENCY, false),

    /**
     * SQLITE JDBC Maven Dependency
     */
    SQLITE_JDBC("org{}xerial", "sqlite-jdbc", "3{}34{}0", DependencyResolution.MAVEN_DEPENDENCY, true),

    /**
     * SPACEAPI Yako Dependency
     */
    SPACE_API("dev{}spaceseries", "spaceapi", "1{}0{}12", DependencyResolution.YAKO_DEPENDENCY, false),

    /**
     * Adventure api
     */
    KYORI_ADVENTURE_API("net{}kyori", "adventure-api", "4{}8{}1", DependencyResolution.MAVEN_DEPENDENCY, false),

    /**
     * Adventure bukkit
     */
    KYORI_ADVENTURE_PLATFORM_BUKKIT("net{}kyori", "adventure-platform-bukkit", "4{}0{}0-SNAPSHOT", DependencyResolution.MAVEN_DEPENDENCY, false),

    /**
     * Adventure minimessage
     */
    KYORI_ADVENTURE_TEXT_MINIMESSAGE("net{}kyori", "adventure-text-minimessage", "4{}1{}0-SNAPSHOT", DependencyResolution.MAVEN_DEPENDENCY, false),

    /**
     * CAFFEINE Maven Dependency
     */
    CAFFEINE("com{}github{}ben-manes{}caffeine", "caffeine", "2{}9{}1", DependencyResolution.MAVEN_DEPENDENCY, false);


    private final String group;
    private final String artifact;
    private final String version;
    private final DependencyResolution resolution;
    private final boolean isolated;

    /**
     * Instantiates a RepositoryDependency
     *
     * @param group      dependency group
     * @param artifact   dependency artifact
     * @param version    dependency version
     * @param resolution dependency resolution
     */
    RepositoryDependency(
            @NotNull final String group,
            @NotNull final String artifact,
            @NotNull final String version,
            @NotNull final DependencyResolution resolution,
            @NotNull final boolean isolated) {
        this.group = group.replaceAll("\\{}", ".");
        this.artifact = artifact.replaceAll("\\{}", ".");
        this.version = version.replaceAll("\\{}", ".");
        this.resolution = resolution;
        this.isolated = isolated;
    }

    /**
     * Gets group.
     *
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * Gets artifact.
     *
     * @return the artifact
     */
    public String getArtifact() {
        return artifact;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets dependency resolution.
     *
     * @return the resolution
     */
    public DependencyResolution getResolution() {
        return resolution;
    }

    /**
     * Gets Is isolated?
     *
     * @return is isolated?
     */
    public boolean isIsolated() {
        return isolated;
    }
}