package com.example;

import com.example.Command.CommandEditWings;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "wingsmod", version = "1.0.0")
public class WingsMod {
    private WingSettings settings;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
        settings = new WingSettings(new Configuration(event.getSuggestedConfigurationFile()));
        settings.loadConfig(); // Load all settings.
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new RenderWings(settings)); // Register wing renderer.
        ClientCommandHandler.instance.registerCommand(new CommandEditWings(this)); // Register command.
    }

    public WingSettings getSettings()
    {
        return settings;
    }
}
