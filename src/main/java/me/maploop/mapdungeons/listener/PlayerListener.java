package me.maploop.mapdungeons.listener;

import me.maploop.mapdungeons.MapDungeons;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        MapDungeons.getPlugin().sql.insertPlayer(e.getPlayer().getUniqueId());
    }
}
