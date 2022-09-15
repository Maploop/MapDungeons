package me.maploop.mapdungeons;

import lombok.Getter;
import me.maploop.mapdungeons.command.CommandLoader;
import me.maploop.mapdungeons.command.DCommand;
import me.maploop.mapdungeons.data.SQLData;
import me.maploop.mapdungeons.util.Config;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

public final class MapDungeons extends JavaPlugin
{
    public static final String PLUGIN_VERSION = "1.0.0";

    public static MapDungeons plugin;

    public CommandMap commandMap;
    public CommandLoader cl;
    public SQLData sql;
    @Getter
    public Config serverData;
    @Getter
    public Config messages;

    @Override
    public void onEnable() {
        plugin = this;

        cl = new CommandLoader();
        loadCommands();
        loadListeners();

        sql = new SQLData();
        serverData = new Config("serverdata.yml");
        messages = new Config("messages.yml");
    }

    @Override
    public void onDisable() {

    }

    private void loadCommands() {
        DCommand.register();

        Reflections reflection = new Reflections("me.maploop.mapdungeons.command.sub");
        for (Class<? extends DCommand> l : reflection.getSubTypesOf(DCommand.class)) {
            try {
                DCommand command = l.newInstance();
                cl.register(command);
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
}
