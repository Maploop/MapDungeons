package me.maploop.mapdungeons.command.sub;

import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.command.CommandParameters;
import me.maploop.mapdungeons.command.CommandSource;
import me.maploop.mapdungeons.command.DCommand;
import me.maploop.mapdungeons.gui.guis.StatsGUI;
import me.maploop.mapdungeons.util.Messages;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

@CommandParameters(usage = "md stats <player>", description = "View the statistics of a player")
public class StatsCommand extends DCommand
{
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length > 2) {
            sender.getPlayer().performCommand("md help");
            return;
        }
        SUtil.runAsync(() -> {
            if (MapDungeons.plugin.sql.getUUID(args[1]) == null) {
                send(Messages.get("commands.player-not-found", Map.of("{username}", args[1])));
                return;
            }
            SUtil.runSync(() -> new StatsGUI(args[1]).open(sender.getPlayer()));
        });
    }

    @Override
    public List<String> tabCompleters(CommandSender sender, String alias, String[] args) {
        return null;
    }
}
