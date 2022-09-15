package me.maploop.mapdungeons.command.sub;

import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.command.CommandParameters;
import me.maploop.mapdungeons.command.CommandSource;
import me.maploop.mapdungeons.command.DCommand;
import me.maploop.mapdungeons.util.Config;
import me.maploop.mapdungeons.util.Messages;
import org.bukkit.command.CommandSender;

import java.util.List;

@CommandParameters(usage = "md reload", description = "Re-pull everything from the configuration files", permission = "dungeons.admin")
public class ReloadCommand extends DCommand
{
    @Override
    public void run(CommandSource sender, String[] args) {
        MapDungeons.getPlugin().messages = new Config("messages.yml");
        send(Messages.get("config.config-reloaded"));
    }

    @Override
    public List<String> tabCompleters(CommandSender sender, String alias, String[] args) {
        return null;
    }
}
