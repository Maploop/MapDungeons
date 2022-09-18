package me.maploop.mapdungeons;

import lombok.Getter;
import me.maploop.mapdungeons.command.CommandLoader;
import me.maploop.mapdungeons.command.DCommand;
import me.maploop.mapdungeons.data.SQLData;
import me.maploop.mapdungeons.session.DungeonSpawner;
import me.maploop.mapdungeons.util.Config;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.logging.Logger;

public final class MapDungeons extends JavaPlugin
{
    public static final String PLUGIN_VERSION = "1.0-SNAPSHOT";

    public static MapDungeons plugin;

    public CommandMap commandMap;
    public CommandLoader commandLoader;
    public SQLData sql;
    @Getter
    public Config serverData;
    public Config messages;
    public Config config;

    @Override
    public void onEnable() {
        plugin = this;

        commandLoader = new CommandLoader();
        loadCommands();
        loadListeners();

        serverData = new Config("serverdata.yml");
        messages = new Config("messages.yml");
        config = new Config("config.yml");

        sql = new SQLData();
    }

    private void loadCommands() {
        DCommand.register();

        Reflections reflection = new Reflections("me.maploop.mapdungeons.command.sub");
        for (Class<? extends DCommand> l : reflection.getSubTypesOf(DCommand.class)) {
            try {
                DCommand command = l.newInstance();
                commandLoader.register(command);
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadListeners() {
        Reflections reflection = new Reflections("me.maploop.mapdungeons.listener");
        for (Class<? extends Listener> l : reflection.getSubTypesOf(Listener.class)) {
            try {
                Listener listener = l.newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static MapDungeons getPlugin() {
        return plugin;
    }

    public static Logger logger() {
        return plugin.getServer().getLogger();
    }

    static {
        ConfigurationSerialization.registerClass(DungeonSpawner.class, "Spawner");
    }
}
