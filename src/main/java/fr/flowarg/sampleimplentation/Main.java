package fr.flowarg.sampleimplentation;

import fr.flowarg.flowlogger.Logger;
import fr.flowarg.pluginloaderapi.PluginLoaderAPI;
import fr.flowarg.pluginloaderapi.api.Task;
import fr.flowarg.pluginloaderapi.plugin.PluginLoader;

import java.io.File;

public class Main
{
    public static void main(String[] args)
    {
        // Set custom logger BEFORE OPERATIONS
        PluginLoaderAPI.setLogger(new Logger("[SampleImplementation]", new File(".", "plugins/logs.log")));

        final PluginLoader testPluginLoader = new PluginLoader("Test", new File(".", "plugins"), Main.class);
        final PluginLoader anotherPluginLoader = new PluginLoader("Another", new File(".", "plugins/another"), Main.class, APIImplementation::new);

        PluginLoaderAPI.registerPluginLoader(testPluginLoader).complete();
        PluginLoaderAPI.registerPluginLoader(anotherPluginLoader).complete();
        // Disabling "stop" in console
        PluginLoaderAPI.removeDefaultShutdownTrigger().complete();
        PluginLoaderAPI.addShutdownTrigger(pluginLoaders -> {
            PluginLoaderAPI.getLogger().debug("Custom shutdown trigger: check if a plugin loader is loaded.");
            if(pluginLoaders.get(0).getName().equalsIgnoreCase("Test"))
            {
                PluginLoaderAPI.getLogger().info("Test is loaded, the API can shutting down !");
                return true;
            }
            return false;
        }).complete();
        // Replace "stop" by "exit"
        PluginLoaderAPI.addShutdownTrigger(pluginLoaders -> PluginLoaderAPI.SCANNER.nextLine().equalsIgnoreCase("exit")).complete();

        final Task<Void> readyTask = PluginLoaderAPI.ready(Main.class);
        PluginLoaderAPI.getLogger().debug("Before the task: " + readyTask.toJson());
        readyTask.complete();

        // Wait while the api is not completely loaded !
        while(!PluginLoaderAPI.finishedLoading()) ;

        PluginLoaderAPI.getLogger().debug("After the task: " + readyTask.toJson());

        // Wait some time doesn't make any problem.
        // Thread.sleep(5000L);
        PluginLoaderAPI.getLogger().info("TestPluginLoader json: " + testPluginLoader.toJson());
        PluginLoaderAPI.getLogger().info("AnotherPluginLoader json: " + anotherPluginLoader.toJson());

        // Reloading plugins
        PluginLoaderAPI.getLogger().info("Reloading plugin loaders:");
        PluginLoaderAPI.reloadPluginLoader(testPluginLoader).complete();
        PluginLoaderAPI.reloadPluginLoader(anotherPluginLoader).complete();
    }
}
