package me.maploop.mapdungeons.command.sub;

import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.command.CommandParameters;
import me.maploop.mapdungeons.command.CommandSource;
import me.maploop.mapdungeons.command.DCommand;
import me.maploop.mapdungeons.data.ServerData;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@CommandParameters(permission = "dungeons.admin", usage = "md delete <dungeon>", description = "Delete a dungeon, also deletes all existing sessions for the dungeon")
public class DeleteCommand extends DCommand
{
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length < 2) {
            send("&cNo dungeon found!");
            return;
        }
        if (!ServerData.getDungeons().contains(args[1])) {
            send("&cNo dungeon found with name " + args[1] + "!");
            return;
        }
        ServerData.delete(args[1]);
        MapDungeons.plugin.sql.deleteSessions(args[1]);
        send("&aSuccessfully deleted dungeon \"" + args[1] + "\"! And wiped all of it's existing data.");
    }

    @Override
    public List<String> tabCompleters(CommandSender sender, String alias, String[] args) {
        return new ArrayList<>(ServerData.getDungeons());
    }
}
