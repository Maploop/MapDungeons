package me.maploop.mapdungeons.command;

import me.maploop.mapdungeons.MapDungeons;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CommandLoader {
    public final List<DCommand> commands;

    public CommandLoader() {
        this.commands = new ArrayList<>();

        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            MapDungeons.getPlugin().commandMap = (CommandMap) f.get(Bukkit.getServer());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void register(DCommand command) {
        commands.add(command);
    }

    public int getCommandAmount() {
        return commands.size();
    }
}
