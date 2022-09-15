package me.maploop.mapdungeons.command;

import lombok.Getter;
import me.maploop.mapdungeons.util.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
public class CommandSource {
    private final CommandSender sender;
    private final Player player;

    public CommandSource(CommandSender sender) {
        this.sender = sender;
        this.player = sender instanceof Player ? (Player) sender : null;
    }

    public void send(String message) {
        sender.sendMessage(message);
    }

    public void send(Message message) {
        message.send(player);
    }
}
