package me.maploop.mapdungeons.command.sub;

import me.maploop.mapdungeons.command.CommandParameters;
import me.maploop.mapdungeons.command.CommandSource;
import me.maploop.mapdungeons.command.DCommand;
import me.maploop.mapdungeons.data.ServerData;
import me.maploop.mapdungeons.gui.guis.ConfigGUI;
import me.maploop.mapdungeons.session.Dungeon;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@CommandParameters(usage = "md config <dungeon>", aliases = "edit", description = "Edit the configuration of a dungeon", permission = "dungeons.admin")
public class ConfigCommand extends DCommand
{
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length < 2) {
            sender.getPlayer().performCommand("dungeons help");
            return;
        }
        new ConfigGUI(Dungeon.get(args[1])).open(sender.getPlayer());
    }

    @Override
    public List<String> tabCompleters(CommandSender sender, String alias, String[] args) {
        return new ArrayList<>(ServerData.getDungeons());
    }
}
