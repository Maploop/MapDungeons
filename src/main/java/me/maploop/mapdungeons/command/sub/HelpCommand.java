package me.maploop.mapdungeons.command.sub;

import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.command.*;
import me.maploop.mapdungeons.util.Messages;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@CommandParameters(usage = "md help", description = "Show this page")
public class HelpCommand extends DCommand implements CommandCooldown
{
    @Override
    public long cooldownSeconds() {
        return 1;
    }

    @Override
    public void run(CommandSource sender, String[] args) {
        send("&c&lMapDungeons - &ev" + MapDungeons.PLUGIN_VERSION);
        send("");
        for (DCommand cmd : MapDungeons.getPlugin().cl.commands) {
            if (sender.getPlayer().hasPermission(cmd.getPermission())) {
                send(Messages.get("commands.help-format",
                        Map.ofEntries(
                                Map.entry("{command}", "/" + cmd.getUsage()),
                                Map.entry("{aliases}", cmd.hasAliases() ? "&7 " + Arrays.toString(cmd.getAliases().toArray()) : ""),
                                Map.entry("{description}", cmd.getDescription())
                        )));
            }
        }
        send("");
    }

    @Override
    public List<String> tabCompleters(CommandSender sender, String alias, String[] args) {
        return null;
    }
}
