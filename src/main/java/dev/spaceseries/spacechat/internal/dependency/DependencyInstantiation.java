package dev.spaceseries.spacechat.internal.dependency;

import dev.spaceseries.spacechat.SpaceChat;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * A special dependency instantiation class used to run dependency tasks asynchronously.
 */
public final class DependencyInstantiation {

    /**
     * Starts dependency tasks.
     */
    public void startTasks() {
        // load dependencies
        SpaceChat.getInstance().getLogger().info(
                "Starting Dependency Tasks... This may take a while depending on your environment!");
        assignClassLoader();
        try {
            CompletableFuture.allOf(
                    CompletableFuture.runAsync(this::loadDependencies))
                    .get();
        } catch (final InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Assigns ClassLoader for classpath loading.
     */
    private void assignClassLoader() {
        DependencyUtilities.setClassloader(
                (URLClassLoader) SpaceChat.getInstance().getClass().getClassLoader());
    }

    /**
     * Downloads/Loads Jitpack/Maven dependencies.
     */
    private void loadDependencies() {
        final DependencyManagement dependencyManagement = new DependencyManagement(new File(SpaceChat.getInstance().getDataFolder(), "libs"));
        dependencyManagement.install();
        dependencyManagement.relocate();
        dependencyManagement.load();
        deleteDependencies(dependencyManagement);
    }

    /**
     * Deletes the dependencies after finished loading.
     *
     * @param management the dependency management
     */
    private void deleteDependencies(@NotNull final DependencyManagement management) {
        final Set<File> files = management.getFiles();
        for (final File file : files) {
            if (file.delete()) {
                SpaceChat.getInstance().getLogger().info(String.format("Finished Initializing Dependency (%s)", file.getAbsolutePath()));
            }
        }
        files.clear();
    }
}