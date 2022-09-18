package me.maploop.mapdungeons.listener;

import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.session.DungeonSession;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class WorldListener implements Listener
{
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        Entity entity = e.getEntity();
        if (!entity.getPersistentDataContainer().has(new NamespacedKey(MapDungeons.getPlugin(), "specMob"), PersistentDataType.STRING)) return;
        String owner = entity.getPersistentDataContainer().get(new NamespacedKey(MapDungeons.getPlugin(), "specMob"), PersistentDataType.STRING);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getUniqueId().equals(UUID.fromString(owner))) continue;

            // Send a packet to despawn the entity for that player
            ((CraftPlayer) player).getHandle().connection.send(new ClientboundRemoveEntitiesPacket(entity.getEntityId()));
        }
    }

    // Not letting the entity target anyone except the owner
    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent e) {
        Entity entity = e.getEntity();
        if (!entity.getPersistentDataContainer().has(new NamespacedKey(MapDungeons.getPlugin(), "specMob"), PersistentDataType.STRING)) return;
        String owner = entity.getPersistentDataContainer().get(new NamespacedKey(MapDungeons.getPlugin(), "specMob"), PersistentDataType.STRING);
        if (!e.getTarget().getUniqueId().equals(UUID.fromString(owner))) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        if (DungeonSession.ACTIVE_SESSIONS.containsKey(e.getEntity().getKiller().getUniqueId())) {
            DungeonSession.ACTIVE_SESSIONS.get(e.getEntity().getKiller().getUniqueId()).handleKill(e.getEntity(), e);
        }
    }
}
