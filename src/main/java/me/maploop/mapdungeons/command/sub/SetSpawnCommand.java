package me.maploop.mapdungeons.command.sub;

import me.maploop.mapdungeons.command.CommandParameters;
import me.maploop.mapdungeons.command.CommandSource;
import me.maploop.mapdungeons.command.DCommand;
import me.maploop.mapdungeons.data.ServerData;
import me.maploop.mapdungeons.util.Messages;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CommandParameters(usage = "md setspawn <dungeon>", description = "Set a spawn for a specific dungeon", permission = "dungeons.admin")
public class SetSpawnCommand extends DCommand
{
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length < 2) {
            sender.getPlayer().performCommand("dungeons help");
            return;
        }
        ServerData.setSpawnFor(args[1], sender.getPlayer().getLocation());
        send(Messages.get("config.dungeon-spawn-set", Map.ofEntries(Map.entry("{dungeon}", args[1]), Map.entry("{location}", SUtil.prettify(sender.getPlayer().getLocation())))));
    }

    @Override
    public List<String> tabCompleters(CommandSender sender, String alias, String[] args) {
        return new ArrayList<>(ServerData.getActiveDungeons());
    }
}
