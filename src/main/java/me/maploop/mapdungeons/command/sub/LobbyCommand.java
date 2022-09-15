package me.maploop.mapdungeons.command.sub;

import me.maploop.mapdungeons.command.CommandParameters;
import me.maploop.mapdungeons.command.CommandSource;
import me.maploop.mapdungeons.command.DCommand;
import me.maploop.mapdungeons.data.ServerData;
import me.maploop.mapdungeons.util.Messages;
import org.bukkit.command.CommandSender;

import java.util.List;

@CommandParameters(usage = "md lobby", description = "Teleport to the lobby of the server", aliases = "spawn")
public class LobbyCommand extends DCommand
{
    @Override
    public void run(CommandSource sender, String[] args) {
        try {
            sender.getPlayer().teleport(ServerData.getLobby());
            send(Messages.get("commands.sent-lobby", sender.getPlayer()));
        } catch (Exception e) {
            send("&cThe server could not send you to the lobby! Are you sure you set it up correctly?");
        }
    }

    @Override
    public List<String> tabCompleters(CommandSender sender, String alias, String[] args) {
        return null;
    }
}
