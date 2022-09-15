package me.maploop.mapdungeons.command.sub;

import me.maploop.mapdungeons.command.CommandParameters;
import me.maploop.mapdungeons.command.CommandSource;
import me.maploop.mapdungeons.command.DCommand;
import me.maploop.mapdungeons.data.ServerData;
import me.maploop.mapdungeons.gui.guis.StartGUI;
import me.maploop.mapdungeons.util.Messages;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CommandParameters(usage = "md start [map]", description = "Start playing a dungeon!")
public class StartCommand extends DCommand
{
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length < 2) {
            new StartGUI().open(sender.getPlayer());
            return;
        }

        try {
            sender.getPlayer().teleport(ServerData.getSpawnFor(args[1]));
            send(Messages.get("game.starting", Map.ofEntries(Map.entry("{seconds}", "0"), Map.entry("{dungeon}", args[1]))));
        } catch (Exception e) {
            send(Messages.get("game.dungeon-not-found", Map.ofEntries(Map.entry("{dungeon}", args[1]))));
        }
    }

    @Override
    public List<String> tabCompleters(CommandSender sender, String alias, String[] args) {
        return new ArrayList<>(ServerData.getActiveDungeons());
    }
}
