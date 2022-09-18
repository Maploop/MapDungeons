package me.maploop.mapdungeons.session;

import com.google.common.base.Joiner;
import lombok.Getter;
import me.maploop.mapdungeons.MapDungeons;
import me.maploop.mapdungeons.data.ServerData;
import me.maploop.mapdungeons.listener.PlayerListener;
import me.maploop.mapdungeons.listener.events.PlayerJoinDungeonEvent;
import me.maploop.mapdungeons.listener.events.PlayerLeaveDungeonEvent;
import me.maploop.mapdungeons.util.Messages;
import me.maploop.mapdungeons.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class DungeonSession
{
    public static final List<Entity> OVERALL_ENTITIES = new ArrayList<>();
    public static final Map<UUID, DungeonSession> ACTIVE_SESSIONS = new HashMap<>();

    private final String id;
    private final UUID owner;
    private final String map;
    private final List<Long> killTimestamps;
    private final int killsObjective;
    private int kills;

    private final List<Entity> entities;
    private BukkitTask task;

    public DungeonSession(String id, UUID owner, String map, List<Long> killTimestamps, int kills) {
        this.id = id;
        this.owner = owner;
        this.map = map;
        this.killTimestamps = killTimestamps;
        this.kills = kills;
        this.entities = new ArrayList<>();
        this.killsObjective = Dungeon.get(map).getKillsObjective();
    }

    public String getSerializedKillTimestamps() {
        StringBuilder b = new StringBuilder();
        killTimestamps.forEach(s -> b.append(s).append(";"));
        return Joiner.on(";").join(Arrays.copyOfRange(b.toString().split(";"), 1, b.toString().split(";").length - 1));
    }

    public void handleKill(Entity entity, EntityDeathEvent event) {
        if (!entities.contains(entity)) return;
        kills++;
        killTimestamps.add(System.currentTimeMillis());
        event.getDrops().clear();
        if (kills >= killsObjective) {
            Bukkit.getPlayer(owner).sendMessage(Messages.get("game.dungeon-completed", Map.of("{dungeon}", map)));
            end();
        }
    }

    public long getStartTime() {
        return killTimestamps.get(0);
    }

    public static DungeonSession New(String map, UUID owner) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        return new DungeonSession(id, owner, map, new ArrayList<>(), 0);
    }

    public void start() {
        ACTIVE_SESSIONS.put(owner, this);
        killTimestamps.add(System.currentTimeMillis());

        // Handle player inventory kit etc.
        Dungeon dungeon = Dungeon.get(map);
        PlayerListener.INVENTORY_MAP.put(owner, Bukkit.getPlayer(owner).getInventory().getContents());
        PlayerListener.ARMOUR_MAP.put(owner, Bukkit.getPlayer(owner).getInventory().getArmorContents());
        Bukkit.getPlayer(owner).getInventory().clear();
        Bukkit.getPlayer(owner).getInventory().setArmorContents(new ItemStack[0]);
        // Give player the kit
        Bukkit.getPlayer(owner).getInventory().setContents(dungeon.getKit());

        // Call start event
        Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinDungeonEvent(Bukkit.getPlayer(owner), dungeon, this));

        AtomicLong loop = new AtomicLong(0);
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (loop.get() > dungeon.getDelayBetweenSpawners()) {
                    for (DungeonSpawner spawner : dungeon.getMobSpawns()) {
                        spawner.execute(DungeonSession.this);
                    }
                    loop.set(0);
                }

                loop.getAndIncrement();
            }
        }.runTaskTimer(MapDungeons.getPlugin(), 20, 20);

        Bukkit.getPlayer(owner).sendMessage(Messages.get("game.dungeon-started", Map.of("{dungeon}", dungeon.getName(), "{objective}", killsObjective)));
    }

    public void end() {
        task.cancel();
        ACTIVE_SESSIONS.remove(owner);
        OVERALL_ENTITIES.removeAll(entities);
        entities.forEach(Entity::remove);

        // Give inventory back
        Player player = Bukkit.getPlayer(owner);
        player.getInventory().setContents(PlayerListener.INVENTORY_MAP.get(player.getUniqueId()));
        player.getInventory().setArmorContents(PlayerListener.ARMOUR_MAP.get(player.getUniqueId()));
        PlayerListener.ARMOUR_MAP.remove(player.getUniqueId());
        PlayerListener.INVENTORY_MAP.remove(player.getUniqueId());

        Dungeon dungeon = Dungeon.get(map);
        Bukkit.getServer().getPluginManager().callEvent(new PlayerLeaveDungeonEvent(Bukkit.getPlayer(owner), dungeon, this));
        MapDungeons.getPlugin().sql.saveSession(this);
        SUtil.runAsync(() -> MapDungeons.getPlugin().sql.setMobKills(owner, MapDungeons.getPlugin().sql.getMobKills(owner) + kills));

        Bukkit.getPlayer(owner).performCommand("md lobby");
    }
}
