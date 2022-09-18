package me.maploop.mapdungeons.util;

import lombok.Getter;
import me.maploop.mapdungeons.listener.GUIListener;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/*
 *  Utility class taken from previous code from discord.gg/atlasmc
 */
@Getter
public class ChatQuery
{
    private final Player player;
    private final Consumer<String> onFinish;

    public ChatQuery(Player player, Consumer<String> onFinish) {
        this.player = player;
        this.onFinish = onFinish;
    }

    public void show() {
        this.player.sendMessage("§e§lEnter your query in chat:");
        this.player.closeInventory();
        GUIListener.ALT_QUERY_MAP.put(player.getUniqueId(), this);
    }
}
