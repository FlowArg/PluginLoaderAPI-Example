package fr.flowarg.anotherplugintest;

import fr.flowarg.pluginloaderapi.plugin.Plugin;
import fr.flowarg.sampleimplentation.APIImplementation;

public class AnotherPluginTest extends Plugin
{
    @Override
    public void onStart()
    {
        this.getLogger().info("Starting Plugin " + this.getPluginName() + " !");
        // API SAMPLE
        this.getLogger().info(this.getPluginName() + " using API " + this.getApi().getAPIName() + "...");
        if(this.getApi() instanceof APIImplementation)
            this.getLogger().info("API UUID: " + this.getApi().subAPI().get());

        this.getLogger().info("Another plugin test json: " + this.toJson());
    }

    @Override
    public void onStop()
    {
        this.getLogger().info("Stopping Plugin " + this.getPluginName() + " !");
    }
}
