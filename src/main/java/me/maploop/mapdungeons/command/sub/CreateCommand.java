package me.maploop.mapdungeons.command.sub;

import me.maploop.mapdungeons.command.CommandParameters;
import me.maploop.mapdungeons.command.CommandSource;
import me.maploop.mapdungeons.command.DCommand;
import me.maploop.mapdungeons.data.ServerData;
import me.maploop.mapdungeons.gui.guis.ConfigGUI;
import me.maploop.mapdungeons.session.Dungeon;
import me.maploop.mapdungeons.util.Messages;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

@CommandParameters(usage = "md create <dungeon>", description = "Create a new dungeon", permission = "dungeons.admin")
public class CreateCommand extends DCommand
{
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length < 2) {
            sender.getPlayer().performCommand("dungeons help");
            return;
        }
        if (args[1].contains("-")) {
            send("&cThe dungeon name cannot contain `-`!");
            return;
        }
        ServerData.createDungeon(args[1]);
        send(Messages.get("config.dungeon-created", Map.ofEntries(Map.entry("{dungeon}", args[1]))));
        new ConfigGUI(Dungeon.get(args[1])).open(sender.getPlayer());
    }

    @Override
    public List<String> tabCompleters(CommandSender sender, String alias, String[] args) {
        return null;
    }
}
