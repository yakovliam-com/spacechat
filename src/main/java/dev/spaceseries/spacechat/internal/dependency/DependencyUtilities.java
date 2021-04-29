package dev.spaceseries.spacechat.internal.dependency;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import dev.spaceseries.spacechat.SpaceChat;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.LongConsumer;

/**
 * Special dependency utilities used throughout the library and also open to users. Used for easier
 * dependency management.
 */
public final class DependencyUtilities {

  private static Method ADD_URL_METHOD;
  private static URLClassLoader CLASSLOADER;

  static {
    SpaceChat.getInstance().getLogger().info("Attempting to Open Reflection Module...");
    try {
      final Class<?> moduleClass = Class.forName("java.lang.Module");
      final Method getModuleMethod = Class.class.getMethod("getModule");
      final Method addOpensMethod = moduleClass.getMethod("addOpens", String.class, moduleClass);
      final Object urlClassLoaderModule = getModuleMethod.invoke(URLClassLoader.class);
      final Object thisModule = getModuleMethod.invoke(DependencyUtilities.class);
      addOpensMethod.invoke(
          urlClassLoaderModule, URLClassLoader.class.getPackage().getName(), thisModule);
      SpaceChat.getInstance().getLogger().info(
          "User is using Java 9+, meaning Reflection Module does have to be opened. You may safely ignore this.");
    } catch (final ClassNotFoundException
        | NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException ignored) {
      SpaceChat.getInstance().getLogger().info(
          "User is using Java 8, meaning Reflection Module does NOT have to be opened. You may safely ignore this.");
    }
    try {
      ADD_URL_METHOD = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
      ADD_URL_METHOD.setAccessible(true);
    } catch (final NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  private DependencyUtilities() {}

  /**
   * Download Maven Dependency.
   *
   * @param dependency the dependency
   * @param parent the parent
   * @return the file
   * @throws IOException the io exception
   */
  @NotNull
  public static File downloadMavenDependency(
      @NotNull final RepositoryDependency dependency, @NotNull final String parent)
      throws IOException {
    return downloadFile(dependency, getRepoUrl(dependency), parent);
  }

  /**
   * Download Jitpack Dependency.
   *
   * @param dependency the dependency
   * @param parent the parent
   * @return the file
   * @throws IOException the io exception
   */
  @NotNull
  public static File downloadJitpackDependency(
      @NotNull final RepositoryDependency dependency, @NotNull final String parent)
      throws IOException {
    return downloadFile(dependency, getRepoUrl(dependency), parent);
  }

  /**
   * Download Yako Dependency.
   *
   * @param dependency the dependency
   * @param parent the parent
   * @return the file
   * @throws IOException the io exception
   */
  @NotNull
  public static File downloadYakoDependency(
          @NotNull final RepositoryDependency dependency, @NotNull final String parent)
          throws IOException {
    return downloadFile(dependency, getRepoUrl(dependency), parent);
  }

  /**
   * Loads and downloads a dependency based from arguments.
   *
   * @param groupId group id
   * @param artifactId artifact id
   * @param version version
   * @param directory directory
   * @param resolution resolution
   * @return jar file
   * @throws IOException if the url constructed cannot be found
   */
  @NotNull
  public static File downloadAndLoadDependency(
      @NotNull final String groupId,
      @NotNull final String artifactId,
      @NotNull final String version,
      @NotNull final String directory,
      @NotNull final DependencyResolution resolution)
      throws IOException {
    final File f = downloadFile(groupId, artifactId, version, directory, resolution);
    loadDependency(f);
    return f;
  }

  /**
   * Loads and downloads a dependency based from arguments with consumer.
   *
   * @param groupId group id
   * @param artifactId artifact id
   * @param version version
   * @param directory directory
   * @param resolution resolution
   * @param consumer consumer
   * @return jar file
   * @throws IOException if the url constructed cannot be found
   */
  @NotNull
  public static File downloadAndLoadDependency(
      @NotNull final String groupId,
      @NotNull final String artifactId,
      @NotNull final String version,
      @NotNull final String directory,
      @NotNull final DependencyResolution resolution,
      @NotNull final LongConsumer consumer)
      throws IOException {
    final File f = downloadFile(groupId, artifactId, version, directory, resolution, consumer);
    loadDependency(f);
    return f;
  }

  /**
   * Gets Maven Central URL of Maven Dependency.
   *
   * @param dependency the dependency
   * @return the maven central url
   * @deprecated See {@link #getRepoUrl(RepositoryDependency)}
   */
  @Deprecated
  @NotNull
  public static String getMavenCentralUrl(@NotNull final RepositoryDependency dependency) {
    return getDependencyUrl(dependency);
  }

  /**
   * Gets Jitpack URL of Jitpack Dependency.
   *
   * @param dependency the dependency
   * @return the jitpack url
   * @deprecated See {@link #getRepoUrl(RepositoryDependency)}
   */
  @Deprecated
  @NotNull
  public static String getJitpackUrl(@NotNull final RepositoryDependency dependency) {
    return getDependencyUrl(dependency);
  }

  /**
   * Gets Dependency Repository URL of Maven/Jitpack Dependency.
   *
   * @param dependency the dependency
   * @return the jitpack url
   */
  @NotNull
  public static String getRepoUrl(@NotNull final RepositoryDependency dependency) {
    return getDependencyUrl(dependency);
  }

  /**
   * Constructs dependency URL of MavenDependency.
   *
   * @param dependency the dependency
   * @return the dependency url
   */
  @NotNull
  public static String getDependencyUrl(@NotNull final RepositoryDependency dependency) {
    return String.format(
        "%s%s/%s/%s/",
        dependency.getResolution().getBaseUrl(),
        dependency.getGroup().replaceAll("\\.", "/"),
        dependency.getArtifact(),
        dependency.getVersion());
  }

  /**
   * Constructs dependency URL directly based on parameters.
   *
   * @param groupId the group id
   * @param artifactId the artifact id
   * @param version the version
   * @param base the base
   * @return the dependency url
   */
  @NotNull
  public static String getDependencyUrl(
      @NotNull final String groupId,
      @NotNull final String artifactId,
      @NotNull final String version,
      @NotNull final String base) {
    Preconditions.checkArgument(!groupId.isEmpty(), "Group ID cannot be empty!");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(groupId), "Group ID cannot be empty!");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(artifactId), "Artifact ID cannot be empty!");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(version), "Version cannot be empty!");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(base), "Base URL cannot be empty or null!");
    return String.format("%s%s/%s/%s/", base, groupId.replaceAll("\\.", "/"), artifactId, version);
  }

  /**
   * Download dependency file.
   *
   * @param dependency the dependency
   * @param link the link
   * @param parent the parent
   * @return the file
   * @throws IOException the io exception
   */
  @NotNull
  public static File downloadFile(
      @NotNull final RepositoryDependency dependency,
      @NotNull final String link,
      @NotNull final String parent)
      throws IOException {
    final String file =
        String.format("%s-%s.jar", dependency.getArtifact(), dependency.getVersion());
    final String url = link + file;
    return downloadFile(Paths.get(String.format("%s/%s", parent, file)), url);
  }

  /**
   * Download dependency file with consumer.
   *
   * @param dependency the dependency
   * @param link the link
   * @param parent the parent
   * @param consumer the consumer
   * @return the file
   * @throws IOException the io exception
   */
  @NotNull
  public static File downloadFile(
      @NotNull final RepositoryDependency dependency,
      @NotNull final String link,
      @NotNull final String parent,
      @NotNull final LongConsumer consumer)
      throws IOException {
    final String file =
        String.format("%s-%s.jar", dependency.getArtifact(), dependency.getVersion());
    final String url = link + file;
    return downloadFile(Paths.get(String.format("%s/%s", parent, file)), url, consumer);
  }

  /**
   * Download dependency file.
   *
   * @param groupId the group id
   * @param artifactId the artifact id
   * @param version the version
   * @param parent the parent
   * @param resolution the resolution
   * @return the file
   * @throws IOException the io exception
   */
  @NotNull
  public static File downloadFile(
      @NotNull final String groupId,
      @NotNull final String artifactId,
      @NotNull final String version,
      @NotNull final String parent,
      @NotNull final DependencyResolution resolution)
      throws IOException {
    final String file = String.format("%s-%s.jar", artifactId, version);
    final String url =
        getDependencyUrl(groupId, artifactId, version, resolution.getBaseUrl()) + file;
    return downloadFile(Paths.get(parent, file), url);
  }

  /**
   * Download dependency file with consumer.
   *
   * @param groupId the group id
   * @param artifactId the artifact id
   * @param version the version
   * @param parent the parent
   * @param consumer the consumer
   * @param resolution the resolution
   * @return the file
   * @throws IOException the io exception
   */
  @NotNull
  public static File downloadFile(
      @NotNull final String groupId,
      @NotNull final String artifactId,
      @NotNull final String version,
      @NotNull final String parent,
      @NotNull final DependencyResolution resolution,
      @NotNull final LongConsumer consumer)
      throws IOException {
    final String file = String.format("%s-%s.jar", artifactId, version);
    final String url =
        getDependencyUrl(groupId, artifactId, version, resolution.getBaseUrl()) + file;
    return downloadFile(Paths.get(String.format("%s/%s", parent, file)), url, consumer);
  }

  /**
   * Download dependency file.
   *
   * @param p the p
   * @param url the url
   * @return the file
   * @throws IOException the io exception
   */
  @NotNull
  public static File downloadFile(@NotNull final Path p, @NotNull final String url)
      throws IOException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "URL cannot be empty or null!");
    SpaceChat.getInstance().getLogger().info(String.format("Downloading Dependency at %s into folder %s", url, p));
    final File file = p.toFile();
    try (final InputStream inputStream = new URL(url).openStream();
        final ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
        final FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    }
    return file;
  }

  /**
   * Download dependency file with hook included.
   *
   * @param p the p
   * @param url the url
   * @param progress the consumer
   * @return the file
   * @throws IOException the io exception
   */
  @NotNull
  public static File downloadFile(
      @NotNull final Path p, @NotNull final String url, @NotNull final LongConsumer progress)
      throws IOException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "URL cannot be empty or null!");
    SpaceChat.getInstance().getLogger().info(String.format("Downloading Dependency at %s into folder %s", url, p));
    try (final BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
        final FileOutputStream fileOutputStream = new FileOutputStream(String.valueOf(p))) {
      final byte[] dataBuffer = new byte[8192];
      int bytesRead;
      while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
        progress.accept(bytesRead);
        fileOutputStream.write(dataBuffer, 0, bytesRead);
      }
    }
    return new File(p.toString());
  }

  /**
   * Gets file size from link.
   *
   * @param url the link
   * @return long size
   * @throws IOException if url is invalid
   */
  public static long getFileSize(@NotNull final String url) throws IOException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "URL cannot be empty or null!");
    final URL download = new URL(url);
    final HttpURLConnection conn = (HttpURLConnection) download.openConnection();
    try (final AutoCloseable conc = conn::disconnect) {
      conn.setRequestMethod("HEAD");
      return conn.getContentLengthLong();
    } catch (final Exception e) {
      e.printStackTrace();
    }
    return -1L;
  }

  /**
   * Load JAR file.
   *
   * @param file the file
   * @throws IOException the io exception
   */
  public static void loadDependency(@NotNull final File file) throws IOException {
    Preconditions.checkArgument(
        file.exists(), String.format("Dependency File %s doesn't exist!", file.getAbsolutePath()));
    SpaceChat.getInstance().getLogger().info(String.format("Loading JAR Dependency at: %s", file.getAbsolutePath()));
    try {
      ADD_URL_METHOD.invoke(CLASSLOADER, file.toURI().toURL());
    } catch (final IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    SpaceChat.getInstance().getLogger().info(String.format("Finished Loading Dependency %s", file.getName()));
  }

  /**
   * Sets the classloader used for dependency loading.
   *
   * @param CLASSLOADER the classloader
   */
  public static void setClassloader(final URLClassLoader CLASSLOADER) {
    DependencyUtilities.CLASSLOADER = CLASSLOADER;
  }
}