package me.maploop.mapdungeons.listener;

import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.listener.events.PlayerJoinDungeonEvent;
import me.maploop.mapdungeons.listener.events.PlayerLeaveDungeonEvent;
import me.maploop.mapdungeons.session.DungeonSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerListener implements Listener
{
    public static Map<UUID, ItemStack[]> INVENTORY_MAP = new HashMap<>();
    public static Map<UUID, ItemStack[]> ARMOUR_MAP = new HashMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (INVENTORY_MAP.containsKey(e.getPlayer().getUniqueId())) {
            e.getPlayer().getInventory().setContents(INVENTORY_MAP.get(e.getPlayer().getUniqueId()));
        }
        if (ARMOUR_MAP.containsKey(e.getPlayer().getUniqueId())) {
            e.getPlayer().getInventory().setArmorContents(ARMOUR_MAP.get(e.getPlayer().getUniqueId()));
        }

        if (DungeonSession.ACTIVE_SESSIONS.containsKey(e.getPlayer().getUniqueId())) {
            DungeonSession.ACTIVE_SESSIONS.get(e.getPlayer().getUniqueId()).end();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        MapDungeons.getPlugin().sql.insertPlayer(e.getPlayer());
        for (UUID player : DungeonSession.ACTIVE_SESSIONS.keySet()) {
            Player p = Bukkit.getPlayer(player);
            p.hidePlayer(MapDungeons.getPlugin(), e.getPlayer());
            e.getPlayer().hidePlayer(MapDungeons.getPlugin(), p);
        }
    }

    @EventHandler
    public void onPlayerJoinDungeon(PlayerJoinDungeonEvent e) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            e.getPlayer().hidePlayer(MapDungeons.getPlugin(), player);
            player.hidePlayer(MapDungeons.getPlugin(), e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerLeaveDungeon(PlayerLeaveDungeonEvent e) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!DungeonSession.ACTIVE_SESSIONS.containsKey(player.getUniqueId())) {
                e.getPlayer().showPlayer(MapDungeons.getPlugin(), player);
                player.showPlayer(MapDungeons.getPlugin(), e.getPlayer());
            }
        }
    }
}
