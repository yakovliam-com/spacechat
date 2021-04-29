package dev.spaceseries.spacechat.internal.dependency.relocation;

import org.jetbrains.annotations.NotNull;

/**
 * A special class used to relocate the dependencies of MinecraftMediaLibrary. After installation of
 * the artifacts, MinecraftMediaLibrary will automatically relocate the jars to their respective
 * replacements. This relocation is to ensure that no dependency conflicts will exist with the
 * classpath.
 */
public enum JarRelocationConvention {

  /** ASM Jar Relocation Convention */
  ASM("org{}ow2{}asm", "dev{}spaceseries{}spacechat{}lib{}asm"),

  /** ASM Commons Jar Relocation Convention */
  ASM_COMMONS("org{}ow2{}asm{}commons", "dev{}spaceseries{}spacechat{}lib{}asm{}commons"),

  /** SQLITE JDBC Jar Relocation Convention */
  SQLITE_JDBC("org{}sqlite", "dev{}spaceseries{}spacechat{}lib{}sqlite"),

  /** SPACEAPI Jar Relocation Convention */
  SPACE_API("dev{}spaceseries{}spaceapi", "dev{}spaceseries{}spacechat{}lib{}spaceapi");

  private final Relocation relocation;

  /**
   * Creates a JarRelocation convention based on arguments.
   *
   * @param before targets
   * @param after replacement
   */
  JarRelocationConvention(@NotNull final String before, @NotNull final String after) {
    relocation = new Relocation(before.replaceAll("\\{}", "."), after.replaceAll("\\{}", "."));
  }

  /**
   * Gets relocation.
   *
   * @return the relocation
   */
  public Relocation getRelocation() {
    return relocation;
  }
}