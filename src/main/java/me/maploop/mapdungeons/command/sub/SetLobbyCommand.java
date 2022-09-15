package me.maploop.mapdungeons.command.sub;

import me.maploop.mapdungeons.command.CommandParameters;
import me.maploop.mapdungeons.command.CommandSource;
import me.maploop.mapdungeons.command.DCommand;
import me.maploop.mapdungeons.data.ServerData;
import org.bukkit.command.CommandSender;

import java.util.List;

@CommandParameters(usage = "md setlobby", description = "Set the lobby position of the server", permission = "dungeons.admin")
public class SetLobbyCommand extends DCommand
{
    @Override
    public void run(CommandSource sender, String[] args) {
        ServerData.setLobby(sender.getPlayer().getLocation());
        send("&aSuccessfully set the location of the lobby! Use &b/dungeons lobby &ato teleport to it!");
    }

    @Override
    public List<String> tabCompleters(CommandSender sender, String alias, String[] args) {
        return null;
    }
}
